package shapes;

import java.awt.Graphics2D;
import java.awt.Polygon;

@SuppressWarnings("serial")
public class GPolygon extends GShape {

	Polygon toCloneP;

	public GPolygon() {
		seteDrawingStyle(EDrawingStyle.eNPointDrawing);
		this.shape = new Polygon();
		//�뤃由ш낀�� �걹�젏怨� �썝�젏�쓣 �씠�뼱�꽌 留뚮뱶�뒗 �룄�삎�씠�떎.
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
	public void movePoint(int x, int y) {
		Polygon polygon = (Polygon) this.shape;
		polygon.xpoints[polygon.npoints - 1] = x;
		polygon.ypoints[polygon.npoints - 1] = y;
	}

	@Override
	public void setFinalPoint(int x2, int y2) {
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