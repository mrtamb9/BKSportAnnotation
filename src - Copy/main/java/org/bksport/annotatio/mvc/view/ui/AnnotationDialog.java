package org.bksport.annotation.mvc.view.ui;

import com.ontotext.kim.client.corpora.KIMAnnotation;
import com.ontotext.kim.client.corpora.KIMFeatureMap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author congnh
 * 
 */
public class AnnotationDialog extends JDialog {

  private static final long serialVersionUID = -8518481160422830390L;
  private ArrayList<KIMAnnotation> annotations = new ArrayList<KIMAnnotation>();
  private int currentIndex = -1;

  private JTable featureTable;
  private JLabel instLb;
  private JButton nextBtn;
  private JButton previousBtn;
  private JScrollPane scrollPane;
  private JToolBar toolBar;
  private JLabel typeLb;

  public AnnotationDialog(Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
  }

  private void initComponents() {

    scrollPane = new JScrollPane();
    featureTable = new JTable();
    toolBar = new JToolBar();
    previousBtn = new JButton();
    nextBtn = new JButton();
    typeLb = new JLabel();
    instLb = new JLabel();

    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    featureTable.setModel(new DefaultTableModel(new Object[][] {

    }, new String[] { "Feature", "Value" }) {
      private static final long serialVersionUID = 6792416695169156950L;
      Class<?>[] types = new Class[] { String.class, String.class };
      boolean[] canEdit = new boolean[] { false, false };

      public Class<?> getColumnClass(int columnIndex) {
        return types[columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit[columnIndex];
      }
    });
    scrollPane.setViewportView(featureTable);

    toolBar.setFloatable(false);
    toolBar.setRollover(true);

    previousBtn.setIcon(new ImageIcon(getClass().getResource(
        "/icon/previous.png"))); // NOI18N
    previousBtn.setToolTipText("Previous annotation");
    previousBtn.setFocusable(false);
    previousBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    previousBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    previousBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        previousBtnActionPerformed(evt);
      }
    });
    toolBar.add(previousBtn);

    nextBtn.setIcon(new ImageIcon(getClass().getResource("/icon/next.png"))); // NOI18N
    nextBtn.setToolTipText("Next annotation");
    nextBtn.setFocusable(false);
    nextBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    nextBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    nextBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        nextBtnActionPerformed(evt);
      }
    });
    toolBar.add(nextBtn);

    typeLb.setText("Type");
    typeLb.setToolTipText("");

    instLb.setText("Instance");
    instLb.setToolTipText("");

    add(toolBar, BorderLayout.PAGE_START);
    JPanel mainPanel = new JPanel();
    BoxLayout layout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
    mainPanel.setLayout(layout);
    typeLb.setHorizontalAlignment(SwingConstants.LEFT);
    mainPanel.add(typeLb);
    instLb.setAlignmentX(0f);
    mainPanel.add(instLb);
    mainPanel.add(scrollPane);
    add(mainPanel, BorderLayout.CENTER);
    JPanel buttonPanel = new JPanel();
    JButton closeBtn = new JButton("Close");
    closeBtn.setPreferredSize(new Dimension(90, 23));
    closeBtn.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    buttonPanel.add(closeBtn);
    add(buttonPanel, BorderLayout.PAGE_END);

    addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        annotations.clear();
      }
    });
    pack();
    setLocationRelativeTo(getParent());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  private void previousBtnActionPerformed(ActionEvent evt) {
    currentIndex--;
    if (currentIndex < 0) {
      currentIndex++;
    }
    showAnnotation(currentIndex);
  }

  private void nextBtnActionPerformed(ActionEvent evt) {
    currentIndex++;
    if (currentIndex >= annotations.size()) {
      currentIndex--;
    }
    showAnnotation(currentIndex);
  }

  private void showBtn() {
    int annSize = annotations.size();
    if (annSize <= 1) {
      previousBtn.setEnabled(false);
      nextBtn.setEnabled(false);
    } else {
      if (currentIndex <= 0)
        previousBtn.setEnabled(false);
      else
        previousBtn.setEnabled(true);
      if (currentIndex >= annSize - 1)
        nextBtn.setEnabled(false);
      else
        nextBtn.setEnabled(true);
    }
  }

  private void showAnnotation(int index) {
    if (index >= 0 && index < annotations.size()) {
      KIMAnnotation ann = annotations.get(index);
      DefaultTableModel tableModel = (DefaultTableModel) featureTable
          .getModel();
      while (tableModel.getRowCount() > 0) {
        tableModel.removeRow(0);
      }
      KIMFeatureMap featureMap = ann.getFeatures();
      Iterator<?> featureIterator = featureMap.keySet().iterator();
      typeLb.setText("Type: " + ann.getType());
      while (featureIterator.hasNext()) {
        String featureName = featureIterator.next().toString();
        Object value = featureMap.get(featureName);
        String featureValue = null;
        if (value != null)
          featureValue = featureMap.get(featureName).toString();
        else {
          featureValue = "null";
        }
        tableModel.addRow(new Object[] { featureName, featureValue });
      }
      featureTable.setModel(tableModel);
    }
    showBtn();
  }

  public void addAnnotation(KIMAnnotation ann) {
    if (ann != null) {
      for (int i = 0; i < annotations.size(); i++) {
        if (ann.equals(annotations.get(i))) {
          showAnnotation(i);
          return;
        }
      }
      // add new annotation to the end
      annotations.add(ann);
      // show recent added annotation
      currentIndex = annotations.size() - 1;
      showAnnotation(currentIndex);
    } else {

    }
  }
}
