package frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame extends JFrame {

  private final MenuBar menuBar;
  private final ToolBar toolBar;
  private DrawingPanel drawingPanel;
  private JTabbedPane tabPane;
  private final Vector<JPanel> drawingPanelList;
  private int count = 1;

  @SuppressWarnings("static-access")
  public MainFrame() {
    // attribute
    this.setSize(590, 600);
    this.setTitle("GraphicsEditor");
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    ImageIcon img = new ImageIcon("images/canvas.png");
    this.setIconImage(img.getImage());

    drawingPanelList = new Vector<JPanel>();

    BorderLayout borderlayout = new BorderLayout();
    this.setLayout(borderlayout);
    ExitHandler exitHandler = new ExitHandler();
    this.addWindowListener(exitHandler);

    // components

    this.drawingPanel = new DrawingPanel();
    this.drawingPanelList.add(drawingPanel);
    this.add(drawingPanel, borderlayout.CENTER);

    this.toolBar = new ToolBar();
    this.add(this.toolBar, borderlayout.NORTH);

    this.menuBar = new MenuBar();
    this.setJMenuBar(this.menuBar);

    this.tabPane = new JTabbedPane();
    TabbedPaneHandler tabbedPaneHandler = new TabbedPaneHandler();
    tabPane.addTab("File", this.drawingPanel);
    this.add(tabPane);

    JPanel btnpanel = new JPanel();
    JButton addPanelBtn = new JButton("add DrawingPanel");
    JButton removePanelBtn = new JButton("remove SelectedPanel");
    btnpanel.add(addPanelBtn);
    btnpanel.add(removePanelBtn);

    addPanelBtn.addActionListener(tabbedPaneHandler);
    addPanelBtn.setActionCommand("addPanel");
    removePanelBtn.addActionListener(tabbedPaneHandler);
    removePanelBtn.setActionCommand("removePanel");
    this.add(btnpanel, borderlayout.SOUTH);

    // association
    this.toolBar.associate(drawingPanel);
    this.menuBar.associate(drawingPanel);
  }

  public static void setTabTitle(JPanel tab, String title) {
    JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class,
        tab);

    for (int tabIndex = 0; tabIndex < tabbedPane.getTabCount(); tabIndex++) {
      if (SwingUtilities.isDescendingFrom(tab, tabbedPane.getComponentAt(tabIndex))) {
        tabbedPane.setTitleAt(tabIndex, title);
        break;
      }
    }
  }

  private class ExitHandler extends WindowAdapter {

    public void windowClosing(WindowEvent e) {
      menuBar.checkWindowSave();
    }
  }

  private class TabbedPaneHandler implements ActionListener, ChangeListener {

    private TabbedPaneHandler() {
      tabPane.addChangeListener(this);
    }

    public void actionPerformed(ActionEvent e) {
      if (e.getActionCommand().equals("addPanel")) {
        DrawingPanel drawingPanel = new DrawingPanel();
        drawingPanelList.add(drawingPanel);
        tabPane.addTab("File" + count, drawingPanel);
        count++;
      } else {
        if (drawingPanelList.size()-1 > 0) {
          drawingPanelList.remove(tabPane.getSelectedIndex());
          tabPane.remove(tabPane.getSelectedIndex());
        }

      }

    }

    @Override
    public void stateChanged(ChangeEvent e) {
      tabPane = (JTabbedPane) e.getSource();
      drawingPanel = (DrawingPanel) drawingPanelList.get(tabPane.getSelectedIndex());
      toolBar.associate(drawingPanel);
      menuBar.associate(drawingPanel);

    }
  }
}
