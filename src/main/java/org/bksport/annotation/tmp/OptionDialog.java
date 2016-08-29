package org.bksport.annotation.tmp;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

/**
 * 
 * @author congnh
 * 
 */
public class OptionDialog extends JDialog {

  private static final long serialVersionUID = -2311332224252698403L;
  private JLabel portLb;
  private JScrollPane scrollPane;
  private JLabel svHostLb;
  private JTextField svHostTxtField;
  private JTextField svPortTxtField;
  private JToolBar toolBar;

  public OptionDialog() {
    initComponents();
  }

  private void initComponents() {

    toolBar = new JToolBar();
    scrollPane = new JScrollPane();
    svHostLb = new JLabel();
    svHostTxtField = new JTextField();
    portLb = new JLabel();
    svPortTxtField = new JTextField();

    svHostLb.setText("Service host:");

    portLb.setText("Port:");

    svPortTxtField.setText("10035");

    add(toolBar, BorderLayout.PAGE_START);

    JPanel panel = new JPanel();
    panel.add(svHostLb);
    panel.add(svHostTxtField);
    panel.add(portLb);
    panel.add(svPortTxtField);
    scrollPane.setViewportView(panel);
    add(scrollPane, BorderLayout.CENTER);
    pack();

    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  public static void main(String args[]) {
    OptionDialog frame = new OptionDialog();
    frame.setVisible(true);
    frame.setSize(600, 400);
  }
}
