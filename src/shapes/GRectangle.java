package shapes;

import java.awt.Graphics2D;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class GRectangle extends GShape {
	
	public GRectangle() {
		seteDrawingStyle(EDrawingStyle.e2PointDrawing);
		this.shape = new Rectangle();
	}

	public GShape clone() {
		return new GRectangle();
	}
	

	@Override
	public void setInitPoint(int x1, int y1) {
		Rectangle rectangle = (Rectangle) this.shape;
		rectangle.setLocation( x1, y1 );
		rectangle.setSize( 0, 0 );
	}

	@Override
	public void setFinalPoint(int x2, int y2) {
	}

	@Override
	public void movePoint(int x2, int y2) {
		Rectangle rectangle = (Rectangle) this.shape;
		rectangle.setSize( x2 - rectangle.x, y2 - rectangle.y );
	}

	@Override
	public void finishMoving(Graphics2D graphics2d, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPoint(int x, int y) {
	}


}
