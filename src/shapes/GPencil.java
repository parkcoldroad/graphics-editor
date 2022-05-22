package shapes;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

@SuppressWarnings("serial")
public class GPencil extends GShape{

	
	public GPencil() {
		seteDrawingStyle(EDrawingStyle.e2PointDrawing);		
		this.shape = new Path2D.Double();
	}
	
	public GShape clone() {
		return new GPencil();
	}
	
	@Override
	public void setInitPoint(int x1, int y1) {
		Path2D path = (Path2D)this.shape;
		path.moveTo(x1, y1); //initial move point add paths , 현재 좌표 path에 추가
	}

	@Override
	public void setFinalPoint(int x2, int y2) {
	}

	@Override
	public void movePoint(int x2, int y2) {
		Path2D path = (Path2D)this.shape;
		setNullFillColor();
		path.lineTo(x2, y2);// 현재 좌표부터 새롭게 지정된 좌표까지 직선 draw 
	}

	@Override
	public void finishMoving(Graphics2D graphics2d, int x, int y) {
	}

	@Override
	public void addPoint(int x, int y) {
	}

}
