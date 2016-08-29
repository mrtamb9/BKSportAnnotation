package org.bksport.annotation.mvc.view.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

/**
 * 
 * @author congnh
 * 
 */
public class SaveDialog extends Dialog {

  private static final long serialVersionUID = -8783838916155736644L;
  private JFileChooser fileChooser;
  private static File recentDirectory = new File(".");
  private String content = null;

  public SaveDialog(Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    initSaveDialog();
    setLocationRelativeTo(parent);
  }

  private void initComponents() {

    fileChooser = new JFileChooser();

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent evt) {
        closeDialog(evt);
      }
    });

    fileChooser.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        fileChooserActionPerformed(evt);
      }
    });
    add(fileChooser, BorderLayout.CENTER);

    pack();
  }

  /** Closes the dialog */
  private void closeDialog(WindowEvent evt) {
    setVisible(false);
    dispose();
  }

  private void fileChooserActionPerformed(ActionEvent evt) {
    recentDirectory = fileChooser.getCurrentDirectory();
    if (evt.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
      File saveFile = fileChooser.getSelectedFile();
      try {
        FileWriter writer = new FileWriter(saveFile);
        writer.write(content);
        writer.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      content = null;
      setVisible(false);
      dispose();
    } else if (evt.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
      content = null;
      setVisible(false);
      dispose();
    }
  }

  private void initSaveDialog() {
    fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
    fileChooser.setMultiSelectionEnabled(true);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setCurrentDirectory(recentDirectory);
  }

  public void showSaveDialog(String content) {
    if (content == null)
      return;
    this.content = content;
    setVisible(true);
    setSize(600, 400);
    setLocationRelativeTo(getParent());
  }
}
