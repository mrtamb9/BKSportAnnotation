package org.bksport.annotation.mvc.view.ui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

/**
 * 
 * @author congnh
 * 
 */
public class WaitDialog extends JDialog {

  private static final long serialVersionUID = -8518481160422830390L;
  private JProgressBar progressbar;
  private JLabel label;
  private JButton button;

  public WaitDialog(Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
  }

  private void initComponents() {
    label = new JLabel();
    progressbar = new JProgressBar();
    progressbar.setIndeterminate(true);
    button = new JButton("Cancel");

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints constraint = new GridBagConstraints();
    constraint.gridx = 1;
    constraint.gridy = 0;
    constraint.gridwidth = 3;
    constraint.anchor = GridBagConstraints.LINE_START;
    panel.add(label, constraint);
    constraint.gridx = 1;
    constraint.gridy = 1;
    constraint.weightx = 1;
    constraint.gridwidth = 3;
    constraint.fill = GridBagConstraints.HORIZONTAL;
    constraint.anchor = GridBagConstraints.CENTER;
    panel.add(progressbar, constraint);
    constraint = new GridBagConstraints();
    constraint.gridx = 1;
    constraint.gridy = 2;
    constraint.gridwidth = 3;
    constraint.anchor = GridBagConstraints.CENTER;
    panel.add(button, constraint);
    add(panel);
    pack();
    setLocationRelativeTo(getParent());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  public void addCancelButtonListener(ActionListener listener) {
    button.addActionListener(listener);
  }

  public void setWaitMessage(String message) {
    label.setText(message);
    invalidate();
  }
}
