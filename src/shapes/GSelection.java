package shapes;

import java.util.Vector;

public class GSelection extends GRectangle {
	private static final long serialVersionUID = 1L;
	private Vector<GShape> containedShapes;
	
	public GSelection() {
		this.containedShapes = new Vector<GShape>();
	}

	@Override
	public GShape clone() {
		return new GSelection();
	}
	
	public void contains(Vector<GShape> shapes) {
		for(GShape shape : shapes) {
			if(this.getShape().contains(shape.getShape().getBounds())) {
				this.containedShapes.add(shape);
				shape.setSelected(true);
			}
		}
	}
}
