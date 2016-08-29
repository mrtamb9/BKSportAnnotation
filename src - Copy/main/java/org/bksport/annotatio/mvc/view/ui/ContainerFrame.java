package org.bksport.annotation.mvc.view.ui;

import com.ontotext.kim.client.corpora.KIMAnnotation;
import com.ontotext.kim.client.corpora.KIMAnnotationSet;
import com.ontotext.kim.client.corpora.KIMCorporaException;
import com.ontotext.kim.client.corpora.KIMDocument;
import com.ontotext.kim.client.model.FeatureConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.model.ColorProxy;

/**
 * 
 * @author congnh
 * 
 */
public class ContainerFrame extends JFrame {

  private static final long serialVersionUID = -2739749346336366674L;
  private static final int STATUS_LENGTH = 64;
  private ArrayList<KIMDocument> documents = new ArrayList<KIMDocument>();
  private ArrayList<JTextArea> textAreas = new ArrayList<JTextArea>();
  private PopupMenu popupMenu;

  private JMenuItem aboutMenuItem;
  private JButton annotateBtn;
  private JMenuItem annotateMenuItem;
  private JPanel componentPanel;
  private JButton connectBtn;
  private JButton documentBtn;
  private JMenuItem documentMenuItem;
  private JMenuItem exitMenuItem;
  private JButton saveBtn;
  private JMenu fileMenu;
  private JMenu helpMenu;
  private JMenuBar menuBar;
  private JButton openBtn;
  private JMenuItem openMenuItem;
  private JMenuItem optionMenuItem;
  private JButton uploadBtn;
  private JMenu runMenu;
  private JButton statisticBtn;
  private JMenuItem statisticMenuItem;
  private JTabbedPane tabbedPane;
  private JToolBar toolBar;
  private JMenu toolMenu;
  private JLabel statusLb;
  private JProgressBar statusProgressBar;
  private JButton statusBtn;

  /** Creates new form MainFrame */
  public ContainerFrame() {
    initComponents();
    initPopupMenu();
    statusProgressBar.setVisible(false);
    tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
  }

  private void initComponents() {

    toolBar = new JToolBar();
    openBtn = new JButton();
    connectBtn = new JButton();
    documentBtn = new JButton();
    uploadBtn = new JButton();
    annotateBtn = new JButton();
    statisticBtn = new JButton();
    saveBtn = new JButton();
    componentPanel = new JPanel();
    tabbedPane = new JTabbedPane();
    statusProgressBar = new JProgressBar();
    statusLb = new JLabel();
    menuBar = new JMenuBar();
    fileMenu = new JMenu();
    openMenuItem = new JMenuItem();
    exitMenuItem = new JMenuItem();
    runMenu = new JMenu();
    annotateMenuItem = new JMenuItem();
    toolMenu = new JMenu();
    documentMenuItem = new JMenuItem();
    statisticMenuItem = new JMenuItem();
    optionMenuItem = new JMenuItem();
    helpMenu = new JMenu();
    aboutMenuItem = new JMenuItem();

    statusBtn = new JButton();

    toolBar.setFloatable(false);

    openBtn.setIcon(new ImageIcon(getClass().getResource("/icon/open.png"))); // NOI18N
    openBtn.setToolTipText("Open Files/Folders");
    openBtn.setFocusable(false);
    openBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    openBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    toolBar.add(openBtn);

    connectBtn.setIcon(new ImageIcon(getClass()
        .getResource("/icon/connect.png")));
    connectBtn.setFocusable(false);
    connectBtn.setToolTipText("Connect to KIM Server");
    toolBar.add(connectBtn);

    documentBtn.setIcon(new ImageIcon(getClass().getResource(
        "/icon/document.png"))); // NOI18N
    documentBtn.setToolTipText("View Documents Information");
    documentBtn.setFocusable(false);
    documentBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    documentBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    toolBar.add(documentBtn);

    annotateBtn.setIcon(new ImageIcon(getClass().getResource(
        "/icon/annotate.png"))); // NOI18N
    annotateBtn.setToolTipText("Annotate Documents");
    annotateBtn.setFocusable(false);
    annotateBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    annotateBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    annotateBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        annotateBtnActionPerformed(evt);
      }
    });
    toolBar.add(annotateBtn);

    statisticBtn.setIcon(new ImageIcon(getClass().getResource(
        "/icon/statistic.png"))); // NOI18N
    statisticBtn.setToolTipText("Statistic Annotations");
    statisticBtn.setFocusable(false);
    statisticBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    statisticBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    toolBar.add(statisticBtn);

    saveBtn.setIcon(new ImageIcon(getClass().getResource("/icon/save.png")));
    saveBtn.setToolTipText("Export Semmantic Information to File");
    saveBtn.setFocusable(false);
    saveBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    saveBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    toolBar.add(saveBtn);

    uploadBtn
        .setIcon(new ImageIcon(getClass().getResource("/icon/upload.png")));
    uploadBtn.setToolTipText("Upload Semantic Information To Server");
    uploadBtn.setFocusable(false);
    uploadBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    uploadBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    toolBar.add(uploadBtn);

    componentPanel.setLayout(new java.awt.GridLayout(1, 0));
    componentPanel.add(tabbedPane);

    statusProgressBar.setMinimum(1);
    statusBtn.setIcon(new ImageIcon(getClass().getResource(
        "/icon/progressbar.png")));
    statusBtn.setFocusable(false);
    statusBtn.setVisible(false);
    statusBtn.setToolTipText("Show background tasks");
    statusBtn.setPreferredSize(new Dimension(24, 14));

    fileMenu.setMnemonic('F');
    fileMenu.setText("File");

    openMenuItem
        .setIcon(new ImageIcon(getClass().getResource("/icon/open.png"))); // NOI18N
    openMenuItem.setMnemonic('O');
    openMenuItem.setText("Open Files/Folders");
    fileMenu.add(openMenuItem);

    exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
        InputEvent.CTRL_MASK));
    exitMenuItem.setMnemonic('X');
    exitMenuItem.setText("Exit");
    fileMenu.add(exitMenuItem);

    menuBar.add(fileMenu);

    runMenu.setMnemonic('R');
    runMenu.setText("Run");

    annotateMenuItem.setIcon(new ImageIcon(getClass().getResource(
        "/icon/annotate.png"))); // NOI18N
    annotateMenuItem.setText("Annotate");
    runMenu.add(annotateMenuItem);

    menuBar.add(runMenu);

    toolMenu.setMnemonic('T');
    toolMenu.setText("Tools");

    documentMenuItem.setIcon(new ImageIcon(getClass().getResource(
        "/icon/document.png"))); // NOI18N
    documentMenuItem.setText("View Documents");
    toolMenu.add(documentMenuItem);

    statisticMenuItem.setIcon(new ImageIcon(getClass().getResource(
        "/icon/statistic.png"))); // NOI18N
    statisticMenuItem.setText("Statistic Annotations");
    toolMenu.add(statisticMenuItem);

    optionMenuItem.setMnemonic('O');
    optionMenuItem.setText("Option");
    toolMenu.add(optionMenuItem);

    menuBar.add(toolMenu);

    helpMenu.setMnemonic('H');
    helpMenu.setText("Help");

    aboutMenuItem.setMnemonic('A');
    aboutMenuItem.setText("About");
    aboutMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        aboutMenuItemActionPerformed(evt);
      }
    });
    helpMenu.add(aboutMenuItem);

    menuBar.add(helpMenu);

    setJMenuBar(menuBar);

    add(toolBar, BorderLayout.PAGE_START);
    add(componentPanel, BorderLayout.CENTER);
    JPanel statusPanel = new JPanel();
    statusPanel.setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = new Insets(0, 5, 0, 5);
    constraints.gridx = 0;
    constraints.gridy = 0;
    statusPanel.add(statusLb, constraints);
    constraints = new GridBagConstraints();
    constraints.insets = new Insets(0, 5, 0, 5);
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.weightx = 1.0;
    constraints.gridx = 2;
    constraints.gridy = 0;
    statusPanel.add(Box.createHorizontalGlue(), constraints);
    constraints = new GridBagConstraints();
    constraints.insets = new Insets(0, 5, 0, 5);
    constraints.anchor = GridBagConstraints.EAST;
    constraints.gridx = 2;
    constraints.gridy = 0;
    statusPanel.add(statusProgressBar, constraints);
    constraints.insets = new Insets(0, 5, 0, 5);
    constraints.gridx = 3;
    constraints.gridy = 0;
    statusPanel.add(statusBtn, constraints);
    add(statusPanel, BorderLayout.PAGE_END);
    pack();
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  public void addExitListener(ActionListener listener) {
    exitMenuItem.addActionListener(listener);
  }

  public void addBrowseListener(ActionListener listener) {
    openBtn.addActionListener(listener);
    openMenuItem.addActionListener(listener);
  }

  public void addConnectListener(ActionListener listener) {
    connectBtn.addActionListener(listener);
  }

  public void addExportListener(ActionListener listener) {
    saveBtn.addActionListener(listener);
  }

  public void addUploadListener(ActionListener listener) {
    uploadBtn.addActionListener(listener);
  }

  public void addDocumentListener(ActionListener listener) {
    documentBtn.addActionListener(listener);
    documentMenuItem.addActionListener(listener);
  }

  public void addStatisticListener(ActionListener listener) {
    statisticBtn.addActionListener(listener);
    statisticMenuItem.addActionListener(listener);
  }

  private void aboutMenuItemActionPerformed(ActionEvent evt) {
    AboutDialog aboutBox = new AboutDialog(this, true);
    aboutBox.setVisible(true);
  }

  private void annotateBtnActionPerformed(ActionEvent evt) {
    int selectedIndex = tabbedPane.getSelectedIndex();
    if (selectedIndex >= 0) {
      long id = documents.get(selectedIndex).getDocumentId();
      ArrayList<Long> ids = new ArrayList<Long>();
      ids.add(id);
      AppFacade.getInstance()
          .sendNotification(AppFacade.ANNOTATE_DOCS_CMD, ids);
    }
  }

  private void initPopupMenu() {
    popupMenu = new PopupMenu("Option");
    MenuItem viewKIM = new MenuItem("View KIM annotations");
    viewKIM.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        showKIMAnnotations(tabbedPane.getSelectedIndex());
      }
    });
    MenuItem viewBKSport = new MenuItem("View BKSport annotations");
    viewBKSport.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        showBKSportAnnotations(tabbedPane.getSelectedIndex());
      }
    });
    popupMenu.add(viewKIM);
    popupMenu.add(viewBKSport);
    add(popupMenu);
  }

  public void updateStatus(String status) {
    if (status != null) {
      if (!status.isEmpty()) {
        if (status.length() > STATUS_LENGTH) {
          statusLb.setText(status.substring(0, STATUS_LENGTH) + "...");
          statusLb.setToolTipText(status);
        } else {
          statusLb.setText(status);
          statusLb.setToolTipText(status);
        }
      } else {
        statusLb.setText("");
        statusLb.setToolTipText("");
      }
    } else {
      statusLb.setText("");
      statusLb.setToolTipText("");
    }
  }

  public void updateStatusProgress(int percent) {
    statusBtn.setVisible(true);
    if (percent == -1) {
      statusProgressBar.setIndeterminate(true);
      statusProgressBar.setVisible(true);
    }
    if (percent > 0) {
      statusProgressBar.setIndeterminate(false);
      statusProgressBar.setValue(percent > 100 ? 100 : percent);
      statusProgressBar.setVisible(true);
    }
    if (percent == 0) {
      statusProgressBar.setIndeterminate(false);
      statusProgressBar.setVisible(false);
      statusBtn.setVisible(false);
    }
  }

  public void viewDocument(final KIMDocument document) {
    for (int i = 0; i < documents.size(); i++) {
      // TODO: check whether add document exist or not
      // if exist, update it
      if (documents.get(i) == document) {
        documents.set(i, document);// update
        tabbedPane.setSelectedIndex(i);
        showKIMAnnotations(i);
      }
    }
    final JTextArea textArea = new JTextArea();
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textAreas.add(textArea);
    JScrollPane scrollPane = new JScrollPane(textArea);
    try {
      scrollPane.setName(document.getName());
    } catch (KIMCorporaException ex) {
      scrollPane.setName("Document");
      ex.printStackTrace();
    }
    textArea.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent evt) {
        if (!SwingUtilities.isLeftMouseButton(evt))
          return;
        if (textArea.getText().isEmpty())
          return;
        int offset = textArea.getCaretPosition();
        if (offset >= 0) {
          try {
            AppFacade.getInstance().sendNotification(AppFacade.LOAD_ANN_CMD,
                new Object[] { offset, document.getSourceUrl() });
          } catch (KIMCorporaException e) {
            Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
          }
        }
      }

      @Override
      public void mouseReleased(MouseEvent evt) {
        if (evt.isPopupTrigger()) {
          popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
      }

      @Override
      public void mousePressed(MouseEvent evt) {
        if (evt.isPopupTrigger()) {
          popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
      }
    });
    addTab(scrollPane);
    documents.add(document);
    showKIMAnnotations(documents.size() - 1);
  }

  private void showKIMAnnotations(int index) {
    if (index >= 0 && index < documents.size()) {
      tabbedPane.setSelectedIndex(index);
      KIMDocument doc = documents.get(index);
      JTextArea currentTextArea = textAreas.get(index);
      Highlighter highlighter = currentTextArea.getHighlighter();
      highlighter.removeAllHighlights();
      System.gc();
      try {
        currentTextArea.setText(doc.getContent());
        KIMAnnotationSet setAnno = doc.getAnnotations();
        Iterator<?> iterator = setAnno.iterator();

        KIMAnnotationSet sentenceAnnSet = setAnno.get("Sentence");
        Iterator<?> sentenceIterator = sentenceAnnSet.iterator();
        ArrayList<int[]> sentenceOffset = new ArrayList<int[]>();
        HashMap<Integer, Integer> senOffsetAnn = new HashMap<Integer, Integer>();
        while (sentenceIterator.hasNext()) {
          KIMAnnotation anno = (KIMAnnotation) sentenceIterator.next();
          sentenceOffset.add(new int[] { anno.getStartOffset(),
              anno.getEndOffset() });
          senOffsetAnn.put(anno.getStartOffset(), 0);
        }

        while (iterator.hasNext()) {
          KIMAnnotation anno = (KIMAnnotation) iterator.next();
          Object clas = anno.getFeatures().get(FeatureConstants.CLASS);
          if (clas == null || !clas.toString().contains("http://bk.sport.owl#")) {
            for (int j = 0; j < sentenceOffset.size(); j++) {
              if (sentenceOffset.get(j)[0] <= anno.getStartOffset()
                  && sentenceOffset.get(j)[1] >= anno.getEndOffset()) {
                // numAnnInSen[j] = numAnnInSen[j]+1;
                senOffsetAnn.put(sentenceOffset.get(j)[0],
                    senOffsetAnn.get(sentenceOffset.get(j)[0]) + 1);
              }
            }
            if (!anno.getType().equals("Sentence")
                && !anno.getType().equals("Token")) {
              highlighter.addHighlight(
                  anno.getStartOffset(),
                  anno.getEndOffset(),
                  new DefaultHighlighter.DefaultHighlightPainter(ColorProxy
                      .getColor(anno.getType())));
            }
          }
        }
        // iterator = setAnno.iterator();
        // while(iterator.hasNext()){
        // KIMAnnotation anno = (KIMAnnotation)iterator.next();
        // if(anno.getType().equals("Sentence")){
        // highlighter.addHighlight(anno.getStartOffset(), anno.getEndOffset()
        // ,new DefaultHighlighter.DefaultHighlightPainter(
        // new Color(255,165,0,100)));
        // }
        // }
        sentenceIterator = sentenceAnnSet.iterator();
        while (sentenceIterator.hasNext()) {
          KIMAnnotation anno = (KIMAnnotation) sentenceIterator.next();
          if (senOffsetAnn.get(anno.getStartOffset()) >= 2)
            highlighter.addHighlight(anno.getStartOffset(),
                anno.getEndOffset(),
                new DefaultHighlighter.DefaultHighlightPainter(new Color(255,
                    165, 0, 100)));
        }
        System.gc();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    // repaint();
  }

  private void showBKSportAnnotations(int docIndex) {
    if (docIndex >= 0 && docIndex < documents.size()) {
      tabbedPane.setSelectedIndex(docIndex);
      KIMDocument doc = documents.get(docIndex);
      JTextArea currentTextArea = textAreas.get(docIndex);
      Highlighter highlighter = currentTextArea.getHighlighter();
      highlighter.removeAllHighlights();
      System.gc();
      try {
        currentTextArea.setText(doc.getContent());
        KIMAnnotationSet setAnno = doc.getAnnotations();

        KIMAnnotationSet sentenceAnnSet = setAnno.get("Sentence");
        Iterator<?> sentenceIterator = sentenceAnnSet.iterator();
        ArrayList<int[]> sentenceOffset = new ArrayList<int[]>();
        HashMap<Integer, Integer> senOffsetAnn = new HashMap<Integer, Integer>();
        // int[] numAnnInSen= new int[sentenceOffset.size()];
        // int i = -1;
        while (sentenceIterator.hasNext()) {
          // i++;
          KIMAnnotation anno = (KIMAnnotation) sentenceIterator.next();
          sentenceOffset.add(new int[] { anno.getStartOffset(),
              anno.getEndOffset() });
          // numAnnInSen[i]=0;
          senOffsetAnn.put(anno.getStartOffset(), 0);
        }

        Iterator<?> iterator = setAnno.iterator();
        iterator = setAnno.iterator();
        while (iterator.hasNext()) {
          KIMAnnotation anno = (KIMAnnotation) iterator.next();
          Object clas = anno.getFeatures().get(FeatureConstants.CLASS);
          if (clas == null)
            continue;
          else if (clas.toString().contains("http://bk.sport.owl#")) {
            for (int j = 0; j < sentenceOffset.size(); j++) {
              if (sentenceOffset.get(j)[0] <= anno.getStartOffset()
                  && sentenceOffset.get(j)[1] >= anno.getEndOffset()) {
                // numAnnInSen[j] = numAnnInSen[j]+1;
                senOffsetAnn.put(sentenceOffset.get(j)[0],
                    senOffsetAnn.get(sentenceOffset.get(j)[0]) + 1);
              }
            }
            highlighter.addHighlight(
                anno.getStartOffset(),
                anno.getEndOffset(),
                new DefaultHighlighter.DefaultHighlightPainter(ColorProxy
                    .getColor(anno.getType())));
          }
        }
        sentenceIterator = sentenceAnnSet.iterator();
        // i = -1;
        while (sentenceIterator.hasNext()) {
          KIMAnnotation anno = (KIMAnnotation) sentenceIterator.next();
          if (senOffsetAnn.get(anno.getStartOffset()) >= 2)
            highlighter.addHighlight(anno.getStartOffset(),
                anno.getEndOffset(),
                new DefaultHighlighter.DefaultHighlightPainter(new Color(255,
                    165, 0, 100)));
        }
        System.gc();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  private void addTab(Component component) {
    // tabbedPane.insertTab(component.getName(),null,
    // component,component.getName()
    // ,tabbedPane.getTabCount()-1);
    // tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-2
    // ,createBar(component));
    tabbedPane.addTab(component.getName(), component);
    tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1,
        createTabBar(component));
  }

  private Component createTabBar(final Component component) {
    JPanel panel = new JPanel();
    panel.setPreferredSize(new java.awt.Dimension(80, 20));
    int maxLength = 7;
    JLabel label = new JLabel();
    String name = component.getName();
    if (name.length() > maxLength) {
      label.setText(name.substring(0, maxLength));
      panel.setToolTipText(name);
    } else {
      label.setText(name);
      panel.setToolTipText(name);
    }

    JButton closeBtn = new JButton(new ImageIcon(getClass().getResource(
        "/icon/close.png")));
    // JButton closeBtn = new JButton(
    // new ImageIcon(getClass().getResource(
    // "/icon/close.png")));
    closeBtn.setBorder(null);
    closeBtn.setContentAreaFilled(false);
    closeBtn.setToolTipText("Close");

    panel.add(label);
    panel.add(closeBtn);

    closeBtn.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        int componentIndex = tabbedPane.indexOfComponent(component);
        tabbedPane.remove(component);
        documents.remove(componentIndex);
        textAreas.remove(componentIndex);
        if (tabbedPane.getSelectedIndex() != tabbedPane.getTabCount() - 1) {
          showKIMAnnotations(tabbedPane.getSelectedIndex());
        }
      }
    });

    panel.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent evt) {
        tabbedPane.setSelectedComponent(component);
        if (tabbedPane.indexOfComponent(component) != tabbedPane.getTabCount() - 1) {
          showKIMAnnotations(tabbedPane.indexOfComponent(component));
        }
      }
    });

    return panel;
  }
}
