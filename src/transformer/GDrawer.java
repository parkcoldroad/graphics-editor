package transformer;

import java.awt.Graphics2D;

public class GDrawer extends GTransformer{

	@Override
	public void initTransforming(int x, int y) {
		this.getgShape().setInitPoint(x, y);
	}

	@Override
	public void keepTransforming(Graphics2D graphics2d, int x, int y) {
		this.getgShape().movePoint(x, y);	
	}

	@Override
	public void finishTransforming(Graphics2D graphics2d, int x, int y) {
	}
	
	public void continueTransforming(Graphics2D graphics2d, int x, int y) {
		this.getgShape().addPoint(x, y);
	}
}
