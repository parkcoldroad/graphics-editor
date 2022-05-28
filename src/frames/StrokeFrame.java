package frames;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class StrokeFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private StrokeHandler strokeChooseHandler;
	private JComboBox<Integer> strokeSizeCombo;
	private JComboBox<float[]> strokePatternCombo;
	private DrawingPanel drawingPanel;

	public StrokeFrame() {
		this.setSize(150, 130);
		this.strokeChooseHandler = new StrokeHandler();
		this.setLayout(new FlowLayout());

		JLabel strokesizelabel = new JLabel("StrokeSize");
		this.add(strokesizelabel);

		this.strokeSizeCombo = new JComboBox<Integer>();
		for (int i = 1; i < 6; i++) {
			this.strokeSizeCombo.addItem(i);
		}
		this.strokeSizeCombo.addActionListener(strokeChooseHandler);
		this.add(this.strokeSizeCombo);
		
		JLabel strokebashlabel = new JLabel("StrokePattern");
		this.add(strokebashlabel);

		this.strokePatternCombo = new JComboBox<>();
		this.strokePatternCombo.addItem(null);
		this.strokePatternCombo.addItem(new float[] { 2f, 2f });
		this.strokePatternCombo.addItem(new float[] { 10f, 4f });
		this.strokePatternCombo.addItem(new float[] { 10f, 10f, 1f, 10f });
		this.strokePatternCombo.addActionListener(strokeChooseHandler);
		this.add(this.strokePatternCombo);
	}

	public void associate(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	private class StrokeHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			int index = (int) strokeSizeCombo.getSelectedItem();
			float[] dash = (float[]) strokePatternCombo.getSelectedItem();
			drawingPanel.setStroke(index,dash);
		}
	}
}
