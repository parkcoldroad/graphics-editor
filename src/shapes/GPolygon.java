package shapes;

import java.awt.Graphics2D;
import java.awt.Polygon;

@SuppressWarnings("serial")
public class GPolygon extends GShape {

	Polygon toCloneP;

	public GPolygon() {
		seteDrawingStyle(EDrawingStyle.eNPointDrawing);
		this.shape = new Polygon();
		//폴리곤은 끝점과 원점을 이어서 만드는 도형이다.
	}

	public GShape clone() {
		return new GPolygon();
	}

	public void setInitPoint(int x, int y) {
		Polygon polygon = (Polygon) this.shape;
		polygon.addPoint(x, y);
		polygon.addPoint(x, y);
	}

	@Override
	public void setFinalPoint(int x2, int y2) {
	}

	@Override
	public void movePoint(int x, int y) {
		Polygon polygon = (Polygon) this.shape;
		polygon.xpoints[polygon.npoints - 1] = x;
		polygon.ypoints[polygon.npoints - 1] = y;
	}

	@Override
	public void addPoint(int x, int y) {
		Polygon polygon = (Polygon) this.shape;
		polygon.addPoint(x, y);		
	}
	
	@Override
	public void finishMoving(Graphics2D graphics2d, int x, int y) {
	}

	

}