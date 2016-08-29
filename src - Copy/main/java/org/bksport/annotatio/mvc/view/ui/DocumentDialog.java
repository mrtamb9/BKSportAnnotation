package org.bksport.annotation.mvc.view.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

import org.bksport.annotation.mvc.model.data.Document;

/**
 * 
 * @author congnh
 * 
 */
public class DocumentDialog extends JDialog {

  private static final long serialVersionUID = -497940828095165153L;
  private JButton annotateBtn;
  private JButton closeBtn;
  private JTable documentTable;
  private JCheckBox selectCheckBox;
  private JButton viewBtn;

  public DocumentDialog(Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
  }

  private void initComponents() {

    documentTable = new JTable();
    annotateBtn = new JButton();
    viewBtn = new JButton();
    closeBtn = new JButton();
    selectCheckBox = new JCheckBox();

    documentTable.setModel(new DefaultTableModel(new String[] { "URL",
        "Content", "Select" }, 0) {
      private static final long serialVersionUID = -8055891223726726097L;
      Class<?>[] types = new Class[] { Object.class, String.class,
          Boolean.class };

      public Class<?> getColumnClass(int columnIndex) {
        return types[columnIndex];
      }
    });
    documentTable.setColumnSelectionAllowed(true);
    documentTable.getColumnModel().getSelectionModel()
        .setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

    annotateBtn.setText("Annotate");
    annotateBtn.setPreferredSize(new Dimension(90, 23));

    viewBtn.setText("View");
    viewBtn.setToolTipText("View document");
    viewBtn.setPreferredSize(new Dimension(90, 23));

    closeBtn.setText("Close");
    closeBtn.setPreferredSize(new Dimension(90, 23));
    closeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        closeBtnActionPerformed(evt);
      }
    });

    selectCheckBox.setText("Select/Unselect all");
    selectCheckBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent evt) {
        selectCheckBoxItemStateChanged(evt);
      }
    });

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setViewportView(documentTable);
    add(scrollPane, BorderLayout.CENTER);
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(annotateBtn);
    buttonPanel.add(viewBtn);
    buttonPanel.add(closeBtn);
    buttonPanel.add(selectCheckBox);
    add(buttonPanel, BorderLayout.PAGE_END);

    pack();
    setLocationRelativeTo(getParent());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  public void addAnnotateListener(ActionListener listener) {
    annotateBtn.addActionListener(listener);
  }

  public void addViewListener(ActionListener listener) {
    viewBtn.addActionListener(listener);
  }

  public void enableAnnotate(boolean b) {
    annotateBtn.setEnabled(b);
    documentTable.setEnabled(b);
  }

  private void closeBtnActionPerformed(ActionEvent evt) {
    setVisible(false);
    dispose();
  }

  private void selectCheckBoxItemStateChanged(ItemEvent evt) {
    DefaultTableModel tableModel = (DefaultTableModel) documentTable.getModel();
    if (selectCheckBox.isSelected()) {
      for (int i = 0; i < tableModel.getRowCount(); i++) {
        tableModel.setValueAt(true, i, 2);
      }
    } else {
      for (int i = 0; i < tableModel.getRowCount(); i++) {
        tableModel.setValueAt(false, i, 2);
      }
    }
  }

  public void setDocuments(Collection<Document> docs) {
    DefaultTableModel tableModel = (DefaultTableModel) documentTable.getModel();
    for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
      tableModel.removeRow(i);
    }
    tableModel.setRowCount(0);
    Iterator<Document> iterator = docs.iterator();
    while (iterator.hasNext()) {
      Document doc = iterator.next();
      tableModel.addRow(new Object[] { doc.getURL(), doc.getContent(),
          selectCheckBox.isSelected(), new JProgressBar() });
    }
  }

  public ArrayList<String> getSelectedDocuments() {
    ArrayList<String> urls = new ArrayList<String>();
    DefaultTableModel tableModel = (DefaultTableModel) documentTable.getModel();
    for (int row = 0; row < tableModel.getRowCount(); row++) {
      if ((Boolean) tableModel.getValueAt(row, 2))
        urls.add((String) tableModel.getValueAt(row, 0));
    }
    return urls;
  }

  public static void main(String args[]) {
    DocumentDialog dialog = new DocumentDialog(null, true);
    dialog.setVisible(true);
  }
}
