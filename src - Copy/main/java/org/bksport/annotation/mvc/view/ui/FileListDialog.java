package org.bksport.annotation.mvc.view.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author congnh
 * 
 */
public class FileListDialog extends JDialog {

  private static final long serialVersionUID = 571734227955364659L;
  private ArrayList<File> acceptedFiles = new ArrayList<File>();
  private String[] columns = new String[] { "File", "Type", "Select" };

  private JButton addBtn;
  private JPanel buttonPanel;
  private JButton closeBtn;
  private JScrollPane scrollPane;
  private JTable uriTable;

  public FileListDialog(Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
  }

  private void initComponents() {

    scrollPane = new JScrollPane();
    uriTable = new JTable();
    buttonPanel = new JPanel();
    addBtn = new JButton();
    closeBtn = new JButton();

    uriTable.setModel(new DefaultTableModel(new Object[][] {
        { null, null, null }, { null, null, null }, { null, null, null },
        { null, null, null } }, columns) {
      private static final long serialVersionUID = 1L;
      Class<?>[] types = new Class[] { java.lang.Object.class,
          java.lang.String.class, java.lang.Boolean.class };

      public Class<?> getColumnClass(int columnIndex) {
        return types[columnIndex];
      }
    });
    uriTable.setColumnSelectionAllowed(true);
    scrollPane.setViewportView(uriTable);
    uriTable
        .getColumnModel()
        .getSelectionModel()
        .setSelectionMode(
            javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

    addBtn.setText("Add");
    addBtn.setToolTipText("Add URIs and create corresponding KIMDocuments");
    addBtn.setMaximumSize(new Dimension(80, 23));
    addBtn.setMinimumSize(new Dimension(80, 23));
    addBtn.setPreferredSize(new Dimension(80, 23));
    addBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        addBtnActionPerformed(evt);
      }
    });
    buttonPanel.add(addBtn);

    closeBtn.setText("Close");
    closeBtn.setMaximumSize(new Dimension(80, 23));
    closeBtn.setMinimumSize(new Dimension(80, 23));
    closeBtn.setPreferredSize(new Dimension(80, 23));
    closeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        closeBtnActionPerformed(evt);
      }
    });
    buttonPanel.add(closeBtn);

    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.PAGE_END);
    pack();
    setLocationRelativeTo(getParent());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  private void addBtnActionPerformed(ActionEvent evt) {
    acceptedFiles.clear();
    DefaultTableModel tableModel = (DefaultTableModel) uriTable.getModel();
    for (int row = 0; row < tableModel.getRowCount(); row++) {
      if ((Boolean) tableModel.getValueAt(row, 2)) {// 2 --> "select"
        acceptedFiles.add((File) tableModel.getValueAt(row, 0));// 0--> "file"
      }
    }
    dispose();
  }

  private void closeBtnActionPerformed(ActionEvent evt) {
    setVisible(false);
    dispose();
  }

  public void viewURIList(File[] files) {
    DefaultTableModel tableModel = (DefaultTableModel) uriTable.getModel();
    // remove old elements
    for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
      tableModel.removeRow(i);
    }
    tableModel.setRowCount(0);
    // add new elements
    for (File file : files) {
      if (file.isDirectory())
        tableModel.addRow(new Object[] { file, "Directory", true });
      else if (file.isFile())
        tableModel.addRow(new Object[] { file, "File", true });
      else
        tableModel.addRow(new Object[] { file, "Unidentify", false });
    }
  }

  public ArrayList<File> showURIListDialog() {
    setVisible(true);
    setSize(600, 400);
    setLocationRelativeTo(getParent());
    return acceptedFiles;
  }
}
