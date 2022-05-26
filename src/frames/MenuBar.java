package frames;
import javax.swing.JMenuBar;

import menus.ColorMenu;
import menus.EditMenu;
import menus.FileMenu;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private DrawingPanel drawingPanel;
	private FileMenu fileMenu;
	private EditMenu editMenu;
	private ColorMenu colorMenu;

	public MenuBar() {
		this.fileMenu = new FileMenu();
		this.add(this.fileMenu);

		this.editMenu = new EditMenu("edit");
		this.add(this.editMenu);
		
		this.colorMenu = new ColorMenu("color");
		this.add(this.colorMenu);
	}

	public void associate(DrawingPanel drawingPanel, MainFrame mainFrame) {
		this.drawingPanel = drawingPanel;
		this.fileMenu.associate(this.drawingPanel,mainFrame);
		this.editMenu.associate(this.drawingPanel);
		this.colorMenu.associate(this.drawingPanel);
	}

	public void checkWindowSave() {
		this.fileMenu.quit();
	}

}
