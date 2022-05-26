package transformer;

import java.awt.Graphics2D;

public class GMover extends GTransformer{

	private int tMoveX, tMoveY;
	
	@Override
	public void initTransforming(int x, int y) {
		this.tMoveX = x;
		this.tMoveY = y;
	}

	@Override
	public void keepTransforming(Graphics2D graphics2d, int x, int y) {
		int dw = x - tMoveX;
		int dh = y - tMoveY;
		getgShape().move(dw, dh);
		this.tMoveX = x;
		this.tMoveY = y;
	}

	@Override
	public void finishTransforming(Graphics2D graphics2d, int x, int y) {
		this.getgShape().finishMoving(graphics2d, x, y);
	}

}
