package menus;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import frames.DrawingPanel;
import frames.StrokeFrame;
import global.Constants.EStrokeMenuItem;

public class StrokeMenu extends JMenu {

	private static final long serialVersionUID = 1L;
	private DrawingPanel drawingPanel;
	private StrokeFrame strokeFrame;
	
	public StrokeMenu() {
		super("stroke");

		actionListener = new StrokeHandler();
		strokeFrame = new StrokeFrame();

		for (EStrokeMenuItem eStrokemenu : EStrokeMenuItem.values()) {
			JMenuItem jmenu = new JMenuItem(eStrokemenu.getText());
			jmenu.setBackground(Color.WHITE);
			jmenu.setActionCommand(eStrokemenu.name()); // name() = toString()
			jmenu.addActionListener(actionListener);
			this.add(jmenu);
		}
	}

	public void associate(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		strokeFrame.associate(drawingPanel);
	}
	
	private class StrokeHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			EStrokeMenuItem eStrokeMenuItem = EStrokeMenuItem.valueOf(e.getActionCommand());
			if (eStrokeMenuItem == EStrokeMenuItem.eStroke) {
				strokeFrame.setVisible(true);
			}
		}
	}
}
