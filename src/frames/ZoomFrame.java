package frames;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ZoomFrame extends JFrame {

  private static final long serialVersionUID = 1L;
  private DrawingPanel drawingPanel;

  public ZoomFrame() {
    this.setSize(230, 80);
    ZoomChooseHandler zoomChooseHandler = new ZoomChooseHandler();
    this.setLayout(new FlowLayout());

    JLabel zoomInlabel = new JLabel("ZoomIn");
    JLabel zoomOutlabel = new JLabel("ZoomOut");
    JButton zoomInBtn = new JButton("+");
    JButton zoomOutBtn = new JButton("-");
    zoomInBtn.setActionCommand("zoomIn");
    zoomInBtn.addActionListener(zoomChooseHandler);
    zoomOutBtn.setActionCommand("zoomOut");
    zoomOutBtn.addActionListener(zoomChooseHandler);
    this.add(zoomInlabel);
    this.add(zoomInBtn);
    this.add(zoomOutlabel);
    this.add(zoomOutBtn);
  }

  public void associate(DrawingPanel drawingPanel) {
    this.drawingPanel = drawingPanel;
  }

  private class ZoomChooseHandler implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      String command = e.getActionCommand();
      if (command.equals("zoomIn")) {
        drawingPanel.zoomIn();
      } else {
        drawingPanel.zoomOut();
      }
    }
  }
}
