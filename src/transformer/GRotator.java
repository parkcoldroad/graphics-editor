package transformer;

import java.awt.Graphics2D;
import java.awt.Point;

public class GRotator extends GTransformer {
	private Point rotatePoint;
	
    @Override
    public void initTransforming(int x, int y) {
    	this.setOldPoint(x,y);
    	rotatePoint = new Point((int)gShape.getShape().getBounds().getCenterX(),(int)gShape.getShape().getBounds().getCenterY());
    }
    
    private double computeRotateAngle(Point centerP, Point startP, Point endP) {
    	double startAngle = Math.toDegrees(Math.atan2(startP.getY() - centerP.getY(), startP.getX()-centerP.getX()));
    	double endAngle = Math.toDegrees(Math.atan2(endP.getY()-centerP.getY(), endP.getX()-centerP.getX()));
    	double angle = endAngle - startAngle;
    	
    	if(angle<0) {
    		angle += 360;
    	}
    	
    	return angle;
    }

    @Override
    public void keepTransforming(Graphics2D g2, int x2, int y2) {
    	double angle = this.computeRotateAngle(rotatePoint, oldPoint , new Point(x2,y2));
		this.gShape.rotate(angle,rotatePoint);
    	this.setOldPoint(x2,y2);
    }

    @Override
    public void finishTransforming(Graphics2D graphics2D, int x, int y) {

    }
}
	