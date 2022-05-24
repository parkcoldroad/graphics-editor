package frames;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private MenuBar menuBar;
	private ToolBar toolBar;
	private DrawingPanel drawingPanel;
	private ExitHandler exitHandler;

	@SuppressWarnings("static-access")
	public MainFrame() {
		//attribute
		this.setSize(560, 600);
		this.setTitle("No Title - GraphicsEditor");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		ImageIcon img = new ImageIcon("images/canvas.png");
		this.setIconImage(img.getImage());	
		
		BorderLayout borderlayout = new BorderLayout();
		this.setLayout(borderlayout); 
		this.exitHandler = new ExitHandler();
		this.addWindowListener(exitHandler);
		
		//components
		this.drawingPanel = new DrawingPanel();
		this.add(drawingPanel,borderlayout.CENTER);
		
		this.toolBar = new ToolBar();
		this.add(this.toolBar, borderlayout.NORTH);
		
		this.menuBar = new MenuBar();
		this.setJMenuBar(this.menuBar);

		//association
		this.toolBar.associate(drawingPanel);
		this.menuBar.associate(drawingPanel,this);
	
		
	}
	
	class ExitHandler extends WindowAdapter{
		public void windowClosing(WindowEvent e) {
			menuBar.checkWindowSave();
		}
	}
}
