package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.util.Vector;

import javax.swing.JPanel;

import global.CursorManager;
import shapes.GAnchor;
import shapes.GAnchor.EAnchors;
import shapes.GGroup;
import shapes.GShape;
import shapes.GShape.EDrawingStyle;
import shapes.GShape.EOnState;
import transformer.GDrawer;
import transformer.GMover;
import transformer.GResizer;
import transformer.GRotator;
import transformer.GTransformer;

@SuppressWarnings("serial")
public class DrawingPanel extends JPanel implements java.awt.print.Printable {
	private boolean isUpdated;

	private BorderLayout borderlayout = new BorderLayout();
	private PreviewPanel previewPanel;

	private Color lineColor;
	private Color fillColor = null;

	private Image openimage;
	private Image pixelimage;
	private int[] imagePixel;

	private Vector<GShape> shapes;

	private GShape selectedShape;
	private GShape shapeTool;
	private GTransformer transformer;
	private GAnchor gAnchor;

	private EDrawingState eDrawingState;
	private ECurrentState eCurrentState;
	private EAnchors eAnchor;

	private enum EDrawingState {
		eIdle, // no draw state
		eTransforming;
	}

	public enum ECurrentState {
		eDrawing, // only draw shape(draw)
		eGrouping; // only transform shape(move,resize,rotate)
	}

	public DrawingPanel() {
		// attributes
		this.lineColor = Color.BLACK;
		this.fillColor = null;
		this.isUpdated = false;

		this.setLayout(borderlayout);
		this.setForeground(Color.BLACK);
		this.setBackground(Color.WHITE);
		this.eDrawingState = EDrawingState.eIdle;
		this.eCurrentState = ECurrentState.eGrouping;

		// components
		shapes = new Vector<GShape>();
		this.transformer = null;

		MouseHandler mouseHandler = new MouseHandler();
		// button
		this.addMouseListener(mouseHandler);
		// position
		this.addMouseMotionListener(mouseHandler);
		// wheel
		this.addMouseWheelListener(mouseHandler);

	}

	public void initiatePanel() {
		this.shapes.clear();
		this.isUpdated = false;
		this.fillColor = null;
		this.lineColor = Color.BLACK;
		this.selectedShape = null;
		this.openimage = null;
		this.pixelimage = null;
		previewPanel.setFillColor(Color.WHITE);
		previewPanel.setLineColor(Color.BLACK);
		this.repaint();
	}

	// get/set methods

	public void associatePreviewPanel(PreviewPanel previewPanel) {
		this.previewPanel = previewPanel;
	}

	public boolean isUpdated() {
		return isUpdated;
	}

	public Object getShapes() {
		return this.shapes;
	}

	public EAnchors geteAnchor() {
		return eAnchor;
	}

	public ECurrentState geteCurrentState() {
		return eCurrentState;
	}

	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}

	@SuppressWarnings("unchecked")
	public void setShapes(Object shapes) {
		this.shapes = (Vector<GShape>) shapes;
	}

	public void openImage(Image image) {
		openimage = image;
		isUpdated = true;
	}

	public void setImagePixel(Object imageobject) {
		imagePixel = (int[]) imageobject;
		if (imagePixel != null) {
			Toolkit tk = Toolkit.getDefaultToolkit();
			ColorModel cm = ColorModel.getRGBdefault();
			pixelimage = tk.createImage(new MemoryImageSource(50, 50, cm, imagePixel, 0, 50));
		}
		isUpdated = false;
	}

	public void seteCurrentState(int currentstate) {
		if (currentstate == 0) {
			this.eCurrentState = ECurrentState.eDrawing;
			this.clearSelected();
		} else {
			this.eCurrentState = ECurrentState.eGrouping;
		}
	}

	public void seteAnchor(EAnchors eAnchor) {
		this.eAnchor = eAnchor;
	}

	public void setSelection(GShape shapeTool) {
		this.shapeTool = shapeTool;
	}

	public void setStroke(int index) {
		for (GShape shape : this.shapes) {
			if (shape.isSelected()) {
				shape.setStroke(index);
			}
		}
		this.repaint();
	}

	public void setSelectedLineColor() {
		for (GShape shape : this.shapes) {
			if (shape.isSelected()) {
				shape.setLineColor(lineColor);
			}
		}
		this.repaint();
	}
	
	public void setSelectedFillColor() {
		for (GShape shape : this.shapes) {
			if (shape.isSelected()) {
				shape.setFillColor(fillColor);
			}
		}
		this.repaint();
	}

	public void setLineColor(Color linecolor) {
		this.lineColor = linecolor;
		previewPanel.setLineColor(linecolor);
	}

	public void setFillColor(Color fillcolor) {
		this.fillColor = fillcolor;
		previewPanel.setFillColor(fillcolor);
	}

	public void setColorInfo() {
		this.selectedShape.setFillColor(this.fillColor);
		this.selectedShape.setLineColor(this.lineColor);
	}
	// methods
	private void defineActionState(int x, int y) {
		EOnState eOnState = onShape(x, y);
		if (eOnState == null) {
			this.clearSelected();
			this.transformer = new GDrawer();
		} else if (geteCurrentState() == ECurrentState.eGrouping) {
			if (!this.selectedShape.isSelected()) {
				this.clearSelected();
				this.selectedShape.setSelected(true);
			}
			switch (eOnState) {
			case eOnShape:
				this.transformer = new GMover();
				break;
			case eOnResize:
				this.transformer = new GResizer();
				break;
			case eOnRotate:
				this.transformer = new GRotator();
				break;
			default:
				this.eDrawingState = null;
				break;
			}
		}
	}

	private void clearSelected() {
		for (GShape shape : this.shapes) {
			shape.setSelected(false);
		}
	}

	public EOnState onShape(int x, int y) {
		for (GShape shape : this.shapes) {
			EOnState eOnState = shape.onShape(x, y);
			if (geteCurrentState() == ECurrentState.eDrawing) {
				eOnState = null;
				return eOnState;
			}
			if (eOnState != null) {
				this.selectedShape = shape;
				return eOnState;
			}
		}
		// this.clearSelected();
		return null;
	}

	private EAnchors confirmAnchorSelected(int x, int y) {
		if (selectedShape != null) {
			gAnchor = selectedShape.getGAnchor();
			eAnchor = gAnchor.getSelectedAnchor(x, y);
		}
		if (eAnchor == null) {
			eAnchor = GAnchor.EAnchors.MM;
			return eAnchor;
		} else {
			return eAnchor;
		}

	}

	public void changeCursor(int x, int y) {
		if (geteCurrentState() == ECurrentState.eDrawing) {
			this.setCursor(CursorManager.CROSSHAIR_CURSOR);
		} else {
			this.setCursor(CursorManager.DEFAULT_CURSOR);
			this.eAnchor = confirmAnchorSelected(x, y);
			if (this.selectedShape != null && selectedShape.isSelected()) {
				switch (eAnchor) {
				case NW:
					this.setCursor(CursorManager.NW_CURSOR);
					break;
				case NN:
					this.setCursor(CursorManager.NN_CURSOR);
					break;
				case NE:
					this.setCursor(CursorManager.NE_CURSOR);
					break;
				case EE:
					this.setCursor(CursorManager.EE_CURSOR);
					break;
				case SE:
					this.setCursor(CursorManager.SE_CURSOR);
					break;
				case SS:
					this.setCursor(CursorManager.SS_CURSOR);
					break;
				case SW:
					this.setCursor(CursorManager.SW_CURSOR);
					break;
				case WW:
					this.setCursor(CursorManager.WW_CURSOR);
					break;
				case RR:
					this.setCursor(CursorManager.RR_CURSOR);
					break;
				case MM:
					this.setCursor(CursorManager.DEFAULT_CURSOR);
					break;
				}
			}
		}

	}

	public void undo() {
		
	}

	public void redo() {
		
	}

	public void copy() {
		
	}

	public void cut() {
		
	}

	public void paste() {
		
	}

	public void group() {
		
	}
	
	// Paint Components
	public void paint(Graphics g) {
		Graphics2D graphics2d = (Graphics2D) g;
		super.paint(g);

		if (this.openimage != null) {
			graphics2d.drawImage(openimage, 0, 0, this);
		}

		if (this.pixelimage != null) {
			graphics2d.drawImage(pixelimage, 0, 0, this);
		}

		if (this.selectedShape != null) {
			this.selectedShape.draw(graphics2d);
		}

		for (GShape shape : shapes) {
			shape.draw(graphics2d);
		}
		repaint();
	}

	private void initTransforming(int x1, int y1) {
		if (this.transformer instanceof GDrawer) {
			this.selectedShape = this.shapeTool.clone();
			this.setColorInfo();
		}

		if (this.selectedShape instanceof GGroup) {
			this.selectedShape.setFillColor(Color.LIGHT_GRAY);
			this.selectedShape.setLineColor(Color.BLACK);
		}

		this.transformer.setgShape(this.selectedShape);
		this.transformer.initTransforming(x1, y1);
	}

	private void keepTransforming(int x2, int y2) {
		Graphics2D g2 = (Graphics2D) this.getGraphics();
		g2.setXORMode(this.getBackground());
		try {
			this.transformer.keepTransforming(g2, x2, y2);
		} catch (NullPointerException e) {
		}
	}

	private void finishTransforming(int x2, int y2) {
		this.transformer.finishTransforming((Graphics2D) this.getGraphics(), x2, y2);
		if (this.transformer instanceof GDrawer) {
			if (this.selectedShape instanceof GGroup) {
				((GGroup) this.selectedShape).contains(this.shapes);
				this.selectedShape = null;
				this.isUpdated = false;
				if (this.shapes.size() > 0) {
					this.isUpdated = true;
				}
			} else {
				this.shapes.add(this.selectedShape);
				this.isUpdated = true;
			}
		}
		this.repaint();
	}

	private void continueTransforming(int x2, int y2) {
		this.transformer.setgShape(this.selectedShape);
		Graphics2D g2 = (Graphics2D) this.getGraphics();
		this.transformer.continueTransforming(g2, x2, y2);
	}
	
	// EventHandler
	private class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				defineActionState(e.getX(), e.getY());
				if (eDrawingState == EDrawingState.eIdle) {
					if (shapeTool.geteDrawingStyle() == EDrawingStyle.e2PointDrawing) {
						initTransforming(e.getX(), e.getY());
						eDrawingState = EDrawingState.eTransforming;
					}
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) {

			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (eDrawingState == EDrawingState.eTransforming) {
				if (shapeTool.geteDrawingStyle() == EDrawingStyle.e2PointDrawing) {
					keepTransforming(e.getX(), e.getY());
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			if (eDrawingState == EDrawingState.eTransforming) {
				if (shapeTool.geteDrawingStyle() == EDrawingStyle.e2PointDrawing) {
					finishTransforming(e.getX(), e.getY());
					confirmAnchorSelected(x, y);
					eDrawingState = EDrawingState.eIdle;
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {// left click
				if (e.getClickCount() == 1) {
					mouse1Cliked(e);
				} else if (e.getClickCount() == 2) {
					mouse2Cliked(e);
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) {// right click

			}
		}

		private void mouse1Cliked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (shapeTool.geteDrawingStyle() == EDrawingStyle.eNPointDrawing) {
					if (eDrawingState == EDrawingState.eIdle) {
						initTransforming(e.getX(), e.getY());
						eDrawingState = EDrawingState.eTransforming;
					} else if (eDrawingState == EDrawingState.eTransforming) {
						continueTransforming(e.getX(), e.getY());
					}
				}
			}
		}

		private void mouse2Cliked(MouseEvent e) {
			if (shapeTool.geteDrawingStyle() == EDrawingStyle.eNPointDrawing
					&& eDrawingState == EDrawingState.eTransforming) {
				finishTransforming(e.getX(), e.getY());
				eDrawingState = EDrawingState.eIdle;
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (shapeTool.geteDrawingStyle() == EDrawingStyle.eNPointDrawing
					&& eDrawingState == EDrawingState.eTransforming) {
				keepTransforming(e.getX(), e.getY());
			} else if (eDrawingState == EDrawingState.eIdle) {
				changeCursor(e.getX(), e.getY());
			}

		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		Graphics2D grapchis2D;

		if (pageIndex == 0) {
			grapchis2D = (Graphics2D) graphics;
			this.paint(grapchis2D);

			return (PAGE_EXISTS);
		}

		return (NO_SUCH_PAGE);
	}
}
