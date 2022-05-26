package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import shapes.GAnchor.EAnchors;
import transformer.GResizerDto;

@SuppressWarnings("serial")
abstract public class GShape implements Serializable {
	// attributes
	protected int tMoveX, tMoveY;

	// components
	protected boolean selected;
	private Color lineColor;
	private Color fillColor;
	private int stroke;

	protected Shape shape;
	protected GAnchor gAnchors;
	protected EAnchors eAnchors;
	private EDrawingStyle eDrawingStyle;

	private AffineTransform af = null;

	public enum EOnState {
		eOnShape, eOnResize, eOnRotate;
	}

	public enum EDrawingStyle {
		e2PointDrawing, eNPointDrawing;
	}

	public GShape() {
		af = new AffineTransform();
		this.lineColor = Color.BLACK;
		this.fillColor = null;
		this.selected = false;
		this.stroke = 0;
		this.gAnchors = new GAnchor();
	}

	public void seteDrawingStyle(EDrawingStyle eDrawingStyle) {
		this.eDrawingStyle = eDrawingStyle;
	}

	public EDrawingStyle geteDrawingStyle() {
		return this.eDrawingStyle;
	}

	public EAnchors getAnchor() {
		return this.eAnchors;
	}

	public GAnchor getGAnchor() {
		return this.gAnchors;
	}

	public void verticalPaste() {
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.translate(10, 10);
		this.shape = affineTransform.createTransformedShape(this.shape);
	}
	
	public EOnState onShape(int x, int y) {
		if (this.selected) {
			EAnchors eAnchor = this.gAnchors.onShape(x, y);
			if (eAnchor == EAnchors.RR) {
				return EOnState.eOnRotate;
			}
			if (eAnchor == null) {
				if (this.shape.contains(x, y)) {
					return EOnState.eOnShape;
				} else if (this.linecontains(x, y)) {
					return EOnState.eOnShape;
				}
			} else {
				this.eAnchors = eAnchor;
				return EOnState.eOnResize;
			}
		} else {
			if (this.shape.contains(x, y)) {
				return EOnState.eOnShape;
			}
		}
		return null;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setStroke(int index) {
		this.stroke = index;
	}

	public Shape getShape() {
		return this.shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setLineColor(Color color) {
		this.lineColor = color;
	}

	public void setFillColor(Color color) {
		this.fillColor = color;
	}

	public void setNullFillColor() {
		fillColor = null;
	}

	public void draw(Graphics2D graphics2d) {
		if (this.stroke != 0) {
			graphics2d.setStroke(new BasicStroke(this.stroke));
		}

		if (this.fillColor != null) {
			graphics2d.setColor(fillColor);
			graphics2d.fill(this.shape);
		}

		graphics2d.setColor(this.lineColor);
		graphics2d.draw(this.shape);
		graphics2d.setStroke(new BasicStroke(1));
		
		if (this.selected) {
			this.gAnchors.setBoundingRect(this.shape.getBounds());
			this.gAnchors.draw(graphics2d);
		}
	}

	public void move(int dw, int dh) {
		af.setToTranslation(dw, dh);
		this.shape = af.createTransformedShape(this.shape);
	}

	public void resize(GResizerDto dto) {
		if (dto != null) {
			af.setToTranslation(dto.getTx(), dto.getTy());
			af.scale(dto.getSx(), dto.getSy());
			af.translate(-(dto.getTx()), -(dto.getTy()));
			this.shape = af.createTransformedShape(this.shape);
		}
	}

	public void rotate(double angle, Point rotatePoint) {
		// setToRation = radian, rotate = degree
		af.setToRotation(Math.toRadians(angle), rotatePoint.getX(), rotatePoint.getY());
		this.shape = af.createTransformedShape(this.shape);
	}

	public GShape cloneShapes() {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(this);

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			return (GShape) objectInputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Rectangle getBounds() {
		return this.shape.getBounds();
	}
	
	public abstract void finishMoving(Graphics2D graphics2d, int x, int y);

	public abstract void setInitPoint(int x1, int y1);

	public abstract void addPoint(int x, int y);

	public abstract void setFinalPoint(int x2, int y2);

	public abstract void movePoint(int x, int y);

	public abstract GShape clone();

	public boolean linecontains(int x, int y) {
			if (this.shape.getBounds().contains(new Point(x, y))) {return true;	}
			else {	return false;}
	}

}
