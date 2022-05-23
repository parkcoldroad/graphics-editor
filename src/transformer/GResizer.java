package transformer;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import shapes.GAnchor.EAnchors;

public class GResizer extends GTransformer {
	private EAnchors anchor;

	private double getDX(double x, double width) {
		return (x - this.oldPoint.getX()) / width;
	}

	private double getDY(double y, double height) {
		return (y - this.oldPoint.getY()) / height;
	}

	@Override
	public void initTransforming(int x, int y) {
		this.setOldPoint(x, y);
		this.anchor = this.gShape.getAnchor();
	}
	
	@Override
	public void keepTransforming(Graphics2D graphics2d, int x, int y) {
		Rectangle bound = this.gShape.getShape().getBounds();
		if (bound.getWidth() > 0 && bound.getHeight() > 0) {
			double dx = this.getDX(x, bound.getWidth());
			double dy = this.getDY(y, bound.getHeight());
			GResizerDto dto = null;

			switch (this.anchor) {
			case NW:
				dto = new GResizerDto(bound.getMinX() + bound.getWidth(), bound.getMinY() + bound.getHeight(), 1 - dx,
						1 - dy);
				break;
			case NN:
				dto = new GResizerDto(0, bound.getMinY() + bound.getHeight(), 1, 1 - dy);
				break;
			case NE:
				dto = new GResizerDto(bound.getMinX(), bound.getMinY() + bound.getHeight(), 1 + dx, 1 - dy);
				break;
			case WW:
				dto = new GResizerDto(bound.getMinX() + bound.getWidth(), 0, 1 - dx, 1);
				break;
			case EE:
				dto = new GResizerDto(bound.getMinX(), 0, 1 + dx, 1);
				break;
			case SW:
				dto = new GResizerDto(bound.getMinX() + bound.getWidth(), bound.getMinY(), 1 - dx, 1 + dy);
				break;
			case SS:
				dto = new GResizerDto(0, bound.getMinY(), 1, 1 + dy);
				break;
			case SE:
				dto = new GResizerDto(bound.getMinX(), bound.getMinY(), 1 + dx, 1 + dy);
				break;
			default:
				break;
			}
			this.gShape.resize(dto);
			this.setOldPoint(x, y);
		}
	}

	@Override
	public void finishTransforming(Graphics2D graphics2d, int x, int y) {
		// TODO Auto-generated method stub
	}

}
