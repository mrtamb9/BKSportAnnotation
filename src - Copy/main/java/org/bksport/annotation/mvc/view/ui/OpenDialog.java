package org.bksport.annotation.mvc.view.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.WindowConstants;

import org.bksport.annotation.util.FileUtil;

/**
 * 
 * @author congnh
 * 
 */
public class OpenDialog extends JDialog {

  private static final long serialVersionUID = 1L;

  private File currentDirectory;
  private JFileChooser fileChooser;

  public OpenDialog(Frame parent, boolean modal, String curDir) {
    super(parent, modal);
    currentDirectory = new File(curDir);
    initComponents();
    initOpenDialog();
  }

  private void initOpenDialog() {
    fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
    fileChooser.setMultiSelectionEnabled(true);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    fileChooser.setCurrentDirectory(currentDirectory);
    fileChooser.addChoosableFileFilter(FileUtil.htmlFilter);
    fileChooser.addChoosableFileFilter(FileUtil.documentFilter);
  }

  private void initComponents() {

    fileChooser = new JFileChooser();

    fileChooser.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        fileChooserActionPerformed(evt);
      }
    });
    add(fileChooser, BorderLayout.CENTER);

    pack();
    setLocationRelativeTo(getParent());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  private void fileChooserActionPerformed(ActionEvent evt) {
    dispose();
  }

  public File[] getSelectedFiles() {
    return fileChooser.getSelectedFiles();
  }
}
