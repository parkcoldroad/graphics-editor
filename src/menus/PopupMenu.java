package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import frames.DrawingPanel;
import global.Constants.EPopupMenu;

public class PopupMenu extends JPopupMenu{
	private static final long serialVersionUID = 1L;
	private MousePopupHandler mousePopupHandler;
	DrawingPanel drawingPanel;

	public PopupMenu(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		this.mousePopupHandler = new MousePopupHandler();

		for(EPopupMenu eMenu : EPopupMenu.values()) {
			JMenuItem menuItem = new JMenuItem(eMenu.getTitle());
			menuItem.setActionCommand(eMenu.getActionCommand());
			menuItem.addActionListener(this.mousePopupHandler);
			this.add(menuItem);
		}
		this.addPopupMenuListener(new PopupPrintListener());
	}
	public void copy() {
		this.drawingPanel.copy();
	}
	public void cut() {
		this.drawingPanel.cut();
	}
	public void paste() {
		this.drawingPanel.paste();
	}
	public void shapeGoFront() {
		this.drawingPanel.setShapeFrontBack(true);
	}
	public void shapeGoBack() {
		this.drawingPanel.setShapeFrontBack(false);
	}
	private void invokeMethod(String methodName) {
		try {
			this.getClass().getMethod(methodName).invoke(this);
			//
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
	}
	class MousePopupHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			String methodName = event.getActionCommand();
			invokeMethod(methodName);
		}
	}
	class PopupPrintListener implements PopupMenuListener {
	    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	    }

	    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
	    }

	    public void popupMenuCanceled(PopupMenuEvent e) {
	    }
	  }
}
