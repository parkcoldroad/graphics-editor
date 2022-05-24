package shapes;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;

@SuppressWarnings("serial")
public class GTextBox extends GRectangle {

	private Font font;
	private String text = "hi";

	public GTextBox() {
		this.font = new Font("Helvetica", Font.PLAIN, 12);
	}
	
	@Override
	public GShape clone() {
		return new GTextBox();
	}

	public void setText(String text) {
		 this.text = text;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public String getText() {
		return text;
	}

	@Override
	public void draw(Graphics2D graphics2d) {
		graphics2d.setStroke(new BasicStroke(1));
		graphics2d.setFont(this.font);
		if (this.getBounds().getMaxX() - this.getBounds().getMinX() > 1) {
			graphics2d.drawString(this.text, (int) this.getBounds().getCenterX(), (int) this.getBounds().getCenterY());
			if (this.selected) {
				this.gAnchors.setBoundingRect(this.shape.getBounds());
				this.gAnchors.draw(graphics2d);
			}
		}
	}

}
