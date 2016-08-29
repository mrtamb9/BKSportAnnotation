package org.bksport.annotation.mvc.view.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
public class StatisticDialog extends JDialog {

  private static final long serialVersionUID = -1652214797404984627L;
  private JButton closeBtn;
  private JComboBox docComboBox;
  private JTable statTable;
  HashMap<String, HashMap<String, Integer>> statistic;

  public StatisticDialog(Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
  }

  private void initComponents() {

    statTable = new JTable();
    closeBtn = new JButton();
    docComboBox = new JComboBox();

    statTable.setModel(new DefaultTableModel(new Object[][] { { null, null },
        { null, null }, { null, null }, { null, null } }, new String[] {
        "Class", "Number of instances" }) {
      private static final long serialVersionUID = 448004062672618157L;
      Class<?>[] types = new Class[] { java.lang.String.class,
          java.lang.Integer.class };

      public Class<?> getColumnClass(int columnIndex) {
        return types[columnIndex];
      }
    });

    closeBtn.setText("Close");
    closeBtn.setToolTipText("View document");
    closeBtn.setPreferredSize(new Dimension(90, 23));
    closeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        closeBtnActionPerformed(evt);
      }
    });

    docComboBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent evt) {
        docComboBoxItemStateChanged(evt);
      }
    });

    ((BorderLayout) getLayout()).setVgap(5);
    JPanel docPanel = new JPanel();
    docPanel.setLayout(new BoxLayout(docPanel, BoxLayout.LINE_AXIS));
    docPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    docPanel.add(docComboBox);
    add(docPanel, BorderLayout.PAGE_START);
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setViewportView(statTable);
    add(scrollPane, BorderLayout.CENTER);
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(closeBtn);
    add(buttonPanel, BorderLayout.PAGE_END);
    pack();
    setLocationRelativeTo(getParent());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  private void closeBtnActionPerformed(ActionEvent evt) {
    dispose();
  }

  private void docComboBoxItemStateChanged(ItemEvent evt) {
    if (evt.getStateChange() == ItemEvent.SELECTED) {
      viewDocumentStatistic((String) docComboBox.getSelectedItem());
    }
  }

  private void viewDocumentStatistic(String docURL) {
    HashMap<String, Integer> annSt = statistic.get(docURL);
    Set<String> keySet = annSt.keySet();
    DefaultTableModel tableModel = (DefaultTableModel) statTable.getModel();
    for (int i = 0; i < tableModel.getRowCount(); i++) {
      tableModel.removeRow(i);
    }
    tableModel.setRowCount(0);
    for (String key : keySet) {
      tableModel.addRow(new Object[] { key, annSt.get(key) });
    }
  }

  public void setStatistic(HashMap<String, HashMap<String, Integer>> statistic) {
    docComboBox.removeAllItems();
    this.statistic = statistic;
    Set<String> keySet = statistic.keySet();
    for (String key : keySet) {
      docComboBox.setSelectedItem(key);
      docComboBox.addItem(key);
    }
  }
}
