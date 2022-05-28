package frames;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import global.Constants.EShapes;

@SuppressWarnings("serial")
public class ToolBar extends JToolBar {

	private ActionListener actionListener;
	private DrawingPanel drawingPanel;
	private PreviewPanel previewPanel;
	

	public ToolBar() {
		ButtonGroup buttonGroup = new ButtonGroup();
		this.actionListener = new ToolbarHandler();
		
		for (EShapes eButton : EShapes.values()) {
			JRadioButton button = new JRadioButton();
			button.setBackground(Color.white); // toString() = name()
			button.setIcon(new ImageIcon(eButton.getImage()));
			button.setSelectedIcon(new ImageIcon(eButton.getSelectedImage()));
			button.setActionCommand(eButton.toString()); // toString is bring enum names. ex)eOval,eLine...
			button.setToolTipText(eButton.getTooltipname());
			button.addActionListener(actionListener);
			this.add(button);
			buttonGroup.add(button);
		}

		this.previewPanel = new PreviewPanel();
		this.addSeparator();
		this.add(previewPanel);
	}

	public void associate(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		JRadioButton defaultBtn = ((JRadioButton) this.getComponent(EShapes.eSelection.ordinal()));
		defaultBtn.doClick();
		drawingPanel.associatePreviewPanel(previewPanel);
	}


	private class ToolbarHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			EShapes eShapeTool = EShapes.valueOf(event.getActionCommand());
			drawingPanel.seteCurrentState(eShapeTool.getCurrentState());
			drawingPanel.setSelection(eShapeTool.newInstance());
		}
	}

}
