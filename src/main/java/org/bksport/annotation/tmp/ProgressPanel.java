package org.bksport.annotation.tmp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.bksport.annotation.mvc.model.data.Progress;

/**
 * 
 * @author congnh
 * 
 */
public class ProgressPanel extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = -8994572670802930564L;
  private HashMap<Long, Integer> idPosition;

  public ProgressPanel() {
    super();
    idPosition = new HashMap<Long, Integer>();
    initComponents();
  }

  private void initComponents() {
    setLayout(new GridBagLayout());
    revalidate();
  }

  protected void addProgressBar(Progress progress) {
    JPanel panel = new JPanel(new GridBagLayout());
    JProgressBar progressBar = new JProgressBar();
    if (progress.getPercent() < 0)
      progressBar.setIndeterminate(true);
    else
      progressBar.setValue(progress.getPercent());
    JButton stopBtn = new JButton();
    stopBtn.setIcon(new ImageIcon(getClass().getResource("/icon/stop16.png")));
    stopBtn.setBorder(BorderFactory.createEmptyBorder());
    stopBtn.setFocusable(false);
    // stopBtn.setFocusPainted(false);
    JLabel label = new JLabel();
    label.setText(progress.getAbstract());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = 0;
    panel.add(progressBar, constraints);
    constraints.gridx = 1;
    panel.add(stopBtn, constraints);
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.anchor = GridBagConstraints.LINE_START;
    panel.add(label, constraints);
    constraints = new GridBagConstraints();
    constraints.gridx = 0;
    add(panel, constraints);
    revalidate();
  }

  protected void removeProgressBar(Progress progress) {

  }

  public static void main(String args[]) {
    JFrame frame = new JFrame();
    ProgressPanel panel = new ProgressPanel();
    Progress progress = new Progress();
    progress.setAbstract("demo");
    panel.addProgressBar(progress);
    Progress progress1 = new Progress();
    progress1.setPercent(100);
    panel.addProgressBar(progress1);
    frame.add(panel);
    frame.pack();
    frame.setSize(300, 100);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

}
