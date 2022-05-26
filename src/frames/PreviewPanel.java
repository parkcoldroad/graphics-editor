package frames;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PreviewPanel extends JPanel {

	private Color lineColor = Color.BLACK;
	private Color fillColor = Color.WHITE;

	public PreviewPanel() {

		this.setBackground(Color.WHITE);
		setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));

		JLabel previewlabel = new JLabel("Preview");
		this.add(previewlabel);
	}

	public void setLineColor(Color linecolor) {
		this.lineColor = linecolor;
	}

	public void setFillColor(Color fillcolor) {
		this.fillColor = fillcolor;
	}
	
	public void paint(Graphics g) {
		Graphics2D graphics2d = (Graphics2D) g;
		super.paint(graphics2d);
		
		graphics2d.setColor(fillColor);
		graphics2d.fillRect(10, 25, 30, 30);

		graphics2d.setColor(lineColor);

		graphics2d.drawRect(10, 25, 30, 30);
		
		repaint();
	}

}
