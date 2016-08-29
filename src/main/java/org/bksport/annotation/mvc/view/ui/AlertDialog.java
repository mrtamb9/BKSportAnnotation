package org.bksport.annotation.mvc.view.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * 
 * Show importance message of the application
 * 
 * @author congnh
 * 
 */
public class AlertDialog extends JDialog {

  private static final long serialVersionUID = -8518481160422830390L;
  private JLabel label;
  private JButton button;

  public AlertDialog(Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
  }

  private void initComponents() {
    label = new JLabel();
    button = new JButton("OK");
    button.setPreferredSize(new Dimension(90, 23));
    button.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });

    JPanel mainPanel = new JPanel();
    BoxLayout layout = new BoxLayout(mainPanel, SwingConstants.HORIZONTAL);
    mainPanel.setLayout(layout);
    label.setAlignmentX(0.5f);
    mainPanel.add(label);
    add(mainPanel, BorderLayout.CENTER);
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(button);
    add(buttonPanel, BorderLayout.PAGE_END);
    pack();
    setTitle("Alert");
    setSize(300, 150);
    setLocationRelativeTo(getParent());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  public void addOkButtonListener(ActionListener listener) {
    button.addActionListener(listener);
  }

  public void removeOkButtonListener(ActionListener listener) {
    button.removeActionListener(listener);
  }

  public void setAlertMessage(String message) {
    label.setText(message);
    invalidate();
  }
}
