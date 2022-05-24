package frames;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import global.Constants.EShapes;

@SuppressWarnings("serial")
public class ToolBar extends JToolBar {

	private ActionListener actionListener;

	private DrawingPanel drawingPanel;
	private PreviewPanel previewPanel;
	private JComboBox<Integer> strokeSizeCombo;
	private StrokeChooseHandler strokeChooseHandler;
	
	public ToolBar() {
		ButtonGroup buttonGroup = new ButtonGroup();

		this.actionListener = new ToolbarHandler();
		this.strokeChooseHandler = new StrokeChooseHandler();
		
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
		

		this.addSeparator();
		
		JLabel lStroke = new JLabel("Stroke");
		this.add(lStroke);
		
		this.addSeparator();
		
		this.strokeSizeCombo = new JComboBox<Integer>();
		for(int i=1;i<6; i++) {
			this.strokeSizeCombo.addItem(i);
		}
		this.strokeSizeCombo.addActionListener(strokeChooseHandler);
		this.add(this.strokeSizeCombo);
	}

	public void associate(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		JRadioButton defaultBtn = ((JRadioButton) this.getComponent(EShapes.eSelection.ordinal()));
		defaultBtn.doClick();
		
		this.addSeparator();
		
		this.previewPanel = new PreviewPanel();
		this.add(previewPanel);
		drawingPanel.associatePreviewPanel(previewPanel);
	}

	private class ToolbarHandler implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			EShapes eShapeTool = EShapes.valueOf(event.getActionCommand());
			drawingPanel.seteCurrentState(eShapeTool.getCurrentState());
			drawingPanel.setSelection(eShapeTool.newInstance());
		}
	}
	
	private class StrokeChooseHandler implements ActionListener{
		public void actionPerformed(ActionEvent event) {
				int index = (int) strokeSizeCombo.getSelectedItem();
				drawingPanel.setStroke(index);
		}
	}
}
