package shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;

@SuppressWarnings("serial")
public class GLine extends GShape {

	public GLine() {
		seteDrawingStyle(EDrawingStyle.e2PointDrawing);
		this.shape = new Line2D.Double();
	}

	public GShape clone() {
		return new GLine();
	}

	@Override
	public void setInitPoint(int x1, int y1) {
		Line2D line = (Line2D) this.shape;
		line.setLine(x1, y1, x1, y1);
	}

	@Override
	public void setFinalPoint(int x2, int y2) {
	}

	@Override
	public void movePoint(int x2, int y2) {
		Line2D line = (Line2D) this.shape;
		setNullFillColor();
		line.setLine(line.getX1(), line.getY1(), x2, y2);
	}

//	@Override
//	public boolean contains(int x, int y) { 
//		//Line2D.Float line = (Line2D.Float) this.shape;
//		boolean bContains = false;
//		if (eAnchors == null) {
//			if (this.shape.getBounds().contains(new Point(x, y))) {
//				bContains = true;
//			}
//		} else
//			bContains = true;
//		return bContains;
//	}
	
	@Override
	public void finishMoving(Graphics2D graphics2d, int x, int y) {
	}

	@Override
	public void addPoint(int x, int y) {
	}
}