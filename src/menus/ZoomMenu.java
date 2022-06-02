package menus;

import frames.ZoomFrame;
import global.Constants.EZoomMenuItem;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import frames.DrawingPanel;
import frames.StrokeFrame;
import global.Constants.EStrokeMenuItem;

public class ZoomMenu extends JMenu {

  private static final long serialVersionUID = 1L;
  private DrawingPanel drawingPanel;
  private ZoomFrame zoomFrame;

  public ZoomMenu() {
    super("zoom");

    actionListener = new ZoomHandler();
    zoomFrame = new ZoomFrame();

    for (EZoomMenuItem eZoomMenu : EZoomMenuItem.values()) {
      JMenuItem jmenu = new JMenuItem(eZoomMenu.getText());
      jmenu.setBackground(Color.WHITE);
      jmenu.setActionCommand(eZoomMenu.name()); // name() = toString()
      jmenu.addActionListener(actionListener);
      this.add(jmenu);
    }
  }

  public void associate(DrawingPanel drawingPanel) {
    this.drawingPanel = drawingPanel;
    zoomFrame.associate(drawingPanel);
  }

  private class ZoomHandler implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      EZoomMenuItem eZoomMenuItem = EZoomMenuItem.valueOf(e.getActionCommand());
      if (eZoomMenuItem == EZoomMenuItem.eZoom) {
        zoomFrame.setVisible(true);
      }
    }
  }
}
