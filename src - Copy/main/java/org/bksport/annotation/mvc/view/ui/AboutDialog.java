package org.bksport.annotation.mvc.view.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * 
 * Show information about the application
 * 
 * @author congnh
 * 
 */
public class AboutDialog extends JDialog {

  private static final long serialVersionUID = -4343200858776767270L;
  private JLabel authorLb;
  private JLabel iconLb;
  private JLabel titleLb;
  private JLabel versionLb;

  /** Creates new form AboutDialog */
  public AboutDialog(Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
  }

  private void initComponents() {

    iconLb = new JLabel();
    iconLb.setText("<html>BK SPORT</html>");
    iconLb.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
    iconLb.setForeground(Color.GREEN);
    titleLb = new JLabel();
    versionLb = new JLabel();
    JLabel descLb = new JLabel();
    authorLb = new JLabel();

    titleLb.setFont(titleLb.getFont().deriveFont(
        titleLb.getFont().getStyle() | Font.BOLD, 15));
    titleLb.setText("BK Sport Annotation");

    versionLb.setFont(versionLb.getFont().deriveFont(
        versionLb.getFont().getStyle() | Font.BOLD));
    versionLb.setText("Application Version:");

    descLb.setText("An application using KIM platform");
    descLb.setDoubleBuffered(true);
    descLb.setFocusable(false);

    authorLb.setFont(authorLb.getFont().deriveFont(
        authorLb.getFont().getStyle() | Font.BOLD));
    authorLb.setText("Author:");

    setLayout(new FlowLayout());
    add(iconLb);
    JPanel infoPanel = new JPanel();
    BoxLayout layout = new BoxLayout(infoPanel, BoxLayout.Y_AXIS);
    infoPanel.setLayout(layout);
    infoPanel.add(titleLb);
    infoPanel.add(descLb);
    infoPanel.add(versionLb);
    infoPanel.add(authorLb);
    add(infoPanel);

    pack();
    setTitle("About");
    setResizable(false);
    setLocationRelativeTo(getParent());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }
}
