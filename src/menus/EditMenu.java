package menus;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import frames.DrawingPanel;
import global.Constants.EEditMenuItem;

@SuppressWarnings("serial")
public class EditMenu extends JMenu {
	@SuppressWarnings("unused")
	private DrawingPanel drawingPanel;

	public EditMenu(String s) {
		super(s);
	
		actionListener = new EditMenuHandler();

		for (EEditMenuItem eEditItem : EEditMenuItem.values()) {
			JMenuItem jmenu = new JMenuItem(eEditItem.getText());
			jmenu.setBackground(Color.WHITE);
			jmenu.setActionCommand(eEditItem.name()); // name() = toString()
			jmenu.addActionListener(actionListener);
			this.add(jmenu);
		}

	}

	public void associate(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	private class EditMenuHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			EEditMenuItem eEditMenuItem = EEditMenuItem.valueOf(e.getActionCommand());
			switch (eEditMenuItem) {
			case eUndo:
//				undo();
				break;
			case eRedo:
//				redo();
				break;
			case eCut:
//				cut();
				break;
			case eCopy:
//				copy();
				break;
			case ePaste:
//				paste();
				break;
			case eDelete:
				break;
			default:
				break;
			}
		}
	}

}
