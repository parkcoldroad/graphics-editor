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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private MenuBar menuBar;
	private ToolBar toolBar;
	private DrawingPanel drawingPanel;
	private ExitHandler exitHandler;
	private JTabbedPane tapPane;
	private TabbedPaneHandler tabbedPaneHandler;
	private Vector<JPanel> drawingPanelList;
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
		this.exitHandler = new ExitHandler();
		this.addWindowListener(exitHandler);

		// components

		this.drawingPanel = new DrawingPanel();
		this.drawingPanelList.add(drawingPanel);
		this.add(drawingPanel, borderlayout.CENTER);

		this.toolBar = new ToolBar();
		this.add(this.toolBar, borderlayout.NORTH);

		this.menuBar = new MenuBar();
		this.setJMenuBar(this.menuBar);

		this.tapPane = new JTabbedPane();
		this.tabbedPaneHandler = new TabbedPaneHandler();
		tapPane.addTab("File", this.drawingPanel);
		this.add(tapPane);

		JButton addPanelBtn = new JButton("add DrawingPanel");
		addPanelBtn.addActionListener(tabbedPaneHandler);
		this.add(addPanelBtn, borderlayout.SOUTH);

		// association
		this.toolBar.associate(drawingPanel);
		this.menuBar.associate(drawingPanel);
	}

	private class ExitHandler extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			menuBar.checkWindowSave();
		}
	}

	private class TabbedPaneHandler implements ActionListener, ChangeListener {

		private TabbedPaneHandler() {
			tapPane.addChangeListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			DrawingPanel drawingPanel = new DrawingPanel();
			drawingPanelList.add(drawingPanel);
			tapPane.addTab("File" + count, drawingPanel);
			count++;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			tapPane = (JTabbedPane) e.getSource();
			drawingPanel = (DrawingPanel) drawingPanelList.get(tapPane.getSelectedIndex());
			toolBar.associate(drawingPanel);
			menuBar.associate(drawingPanel);
		}
	}
}
