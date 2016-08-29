package org.bksport.annotation.mvc.view.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 * 
 * @author congnh
 * 
 */
public class ExportDialog extends JDialog {

  private static final long serialVersionUID = -8422086490610882799L;
  private JButton cancelBtn;
  private JLabel exportLabel;
  private JButton okBtn;
  private JTextArea textArea;

  public ExportDialog(Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
  }

  private void initComponents() {

    exportLabel = new JLabel();
    textArea = new JTextArea();
    okBtn = new JButton();
    cancelBtn = new JButton();

    exportLabel.setText("Exported content:");

    textArea.setColumns(20);
    textArea.setRows(5);

    okBtn.setText("Ok");
    okBtn.setPreferredSize(new Dimension(90, 23));

    cancelBtn.setText("Cancel");
    cancelBtn.setPreferredSize(new Dimension(90, 23));

    // layout label
    exportLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    add(exportLabel, BorderLayout.PAGE_START);
    ((BorderLayout) getLayout()).setHgap(5);
    // layout text area
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setViewportView(textArea);
    add(scrollPane, BorderLayout.CENTER);
    // layout panel of buttons
    JPanel btnPanel = new JPanel();
    btnPanel.add(okBtn);
    btnPanel.add(cancelBtn);
    add(btnPanel, BorderLayout.PAGE_END);
    pack();
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  public void addAcceptListener(ActionListener listener) {
    okBtn.addActionListener(listener);
  }

  public void removeAcceptListener(ActionListener listener) {
    okBtn.removeActionListener(listener);
  }

  public void addCancelListener(ActionListener listener) {
    cancelBtn.addActionListener(listener);
  }

  public void removeCancelListener(ActionListener listener) {
    cancelBtn.removeActionListener(listener);
  }

  public String getExportedContent() {
    return textArea.getText();
  }

  public void setExportedContent(String content) {
    textArea.setText(content);
  }
}
