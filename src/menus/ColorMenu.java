package menus;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import frames.DrawingPanel;
import global.Constants.EColorMenuItem;

@SuppressWarnings("serial")
public class ColorMenu extends JMenu {
	private DrawingPanel drawingPanel;

	public ColorMenu(String s) {
		super(s);
	
		actionListener = new ColorHandler();

		for (EColorMenuItem eColormenu : EColorMenuItem.values()) {
			JMenuItem jmenu = new JMenuItem(eColormenu.getText());
			jmenu.setBackground(Color.WHITE);
			jmenu.setActionCommand(eColormenu.name()); // name() = toString()
			jmenu.addActionListener(actionListener);
			this.add(jmenu);
		}

	}

	public void associate(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	@SuppressWarnings({ "static-access" })
	private void setLineColor() {
		JColorChooser chooser = new JColorChooser();
		Color selectedColor = chooser.showDialog(null, "Color", Color.yellow);
		this.drawingPanel.setLineColor(selectedColor);
		this.drawingPanel.setSelectedLineColor();
	}

	@SuppressWarnings({ "static-access" })
	private void setFillColor() {
		JColorChooser chooser = new JColorChooser();
		Color selectedColor = chooser.showDialog(null, "Color", Color.yellow);
		this.drawingPanel.setFillColor(selectedColor);
		this.drawingPanel.setSelectedFillColor();

	}

	private class ColorHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			EColorMenuItem eColorMenuItem = EColorMenuItem.valueOf(e.getActionCommand());
			switch (eColorMenuItem) {
			case eLineColor:
				setLineColor();
				break;
			case eFillColor:
				setFillColor();
				break;
			default:
				break;
			}
		}
	}

}
