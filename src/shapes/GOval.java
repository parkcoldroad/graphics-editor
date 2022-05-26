package shapes;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

@SuppressWarnings("serial")
public class GOval extends GShape {

	public GOval() {
		seteDrawingStyle(EDrawingStyle.e2PointDrawing);
		this.shape = new Ellipse2D.Float();
	}

	public GShape clone() {
		return new GOval();
	}

	@Override
	public void setInitPoint(int x1, int y1) {
		Ellipse2D ellipse = (Ellipse2D) this.shape;
		ellipse.setFrame(x1, y1, 0, 0);
	}

	@Override
	public void setFinalPoint(int x2, int y2) {
	}

	@Override
	public void movePoint(int x2, int y2) {
		Ellipse2D ellipse = (Ellipse2D) this.shape;
		ellipse.setFrame(ellipse.getX(), ellipse.getY(), x2 - ellipse.getX(), y2 - ellipse.getY());
	}

	@Override
	public void finishMoving(Graphics2D graphics2d, int x, int y) {
	}

	@Override
	public void addPoint(int x, int y) {
	}
}