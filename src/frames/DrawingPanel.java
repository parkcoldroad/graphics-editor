package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import menus.PopupMenu;
import shapes.GAnchor;
import shapes.GAnchor.EAnchors;
import shapes.GSelection;
import shapes.GShape;
import shapes.GShape.EDrawingStyle;
import shapes.GShape.EOnState;
import shapes.GTextBox;
import tool.Clipboard;
import tool.CursorManager;
import transformer.GDrawer;
import transformer.GMover;
import transformer.GResizer;
import transformer.GRotator;
import transformer.GTransformer;

public class DrawingPanel extends JPanel implements java.awt.print.Printable {

  private boolean isUpdated;

  private PreviewPanel previewPanel;

  private Color lineColor;
  private Color fillColor;
  private Color backgroundColor;
  private Color objectbackgroundColor;

  private Image openimage;
  private Image pixelimage;
  private int index;
  private double scale;

  private Vector<GShape> shapes;
  private GShape selectedShape;
  private GShape shapeTool;
  private final Clipboard clip;
  private GTransformer transformer;

  private final Vector<GShape> forFront;

  private EDrawingState eDrawingState;
  private ECurrentState eCurrentState;
  private EAnchors eAnchor;

  private enum EDrawingState {
    eIdle, // no draw state
    eTransforming
  }

  public enum ECurrentState {
    eDrawing, // only draw shape(draw)
    eSelecting // only transform shape(move,resize,rotate)
  }

  public DrawingPanel() {
    // attributes
    this.lineColor = Color.BLACK;
    this.fillColor = null;
    this.backgroundColor = null;
    this.objectbackgroundColor = null;
    this.isUpdated = false;

    BorderLayout borderlayout = new BorderLayout();
    this.setLayout(borderlayout);
    this.setForeground(Color.BLACK);
    this.setBackground(Color.WHITE);
    this.eDrawingState = EDrawingState.eIdle;
    this.eCurrentState = ECurrentState.eSelecting;

    // components
    this.forFront = new Vector<GShape>();
    this.shapes = new Vector<GShape>();
    this.clip = new Clipboard();
    this.transformer = null;
    this.openimage = null;
    this.pixelimage = null;
    this.index = 0;
    this.scale = 1.0;

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
    this.clip.tempshapes.clear();
    this.clip.clipshapes.clear();
    this.forFront.clear();
    this.isUpdated = false;
    this.fillColor = null;
    this.objectbackgroundColor = null;
    this.lineColor = Color.BLACK;
    this.backgroundColor = Color.WHITE;
    this.selectedShape = null;
    this.openimage = null;
    this.pixelimage = null;
    previewPanel.setFillColor(Color.WHITE);
    previewPanel.setLineColor(Color.BLACK);

    this.setBackgroundColor(backgroundColor);
    this.repaint();
  }

  // get/set methods

  public void associatePreviewPanel(PreviewPanel previewPanel) {
    this.previewPanel = previewPanel;
    this.previewPanel.setFillColor(fillColor);
    this.previewPanel.setLineColor(lineColor);
  }

  public boolean isUpdated() {
    return isUpdated;
  }

  public Object getShapes() {
    return this.shapes;
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
    int[] imagePixel = (int[]) imageobject;
    if (imagePixel != null) {
      Toolkit tk = Toolkit.getDefaultToolkit();
      ColorModel cm = ColorModel.getRGBdefault();
      pixelimage = tk.createImage(new MemoryImageSource(100, 100, cm, imagePixel, 0, 100));
    }
    isUpdated = false;
  }

  public void setUserFont(Font font) {
    if (this.selectedShape instanceof GTextBox) {
      ((GTextBox) this.selectedShape).setFont(font);
    }
    this.repaint();
  }

  public void seteCurrentState(int currentstate) {
    if (currentstate == 0) {
      this.eCurrentState = ECurrentState.eDrawing;
      this.clearSelected();
    } else {
      this.eCurrentState = ECurrentState.eSelecting;
    }
  }

  public void setSelection(GShape shapeTool) {
    this.shapeTool = shapeTool;
  }

  public void setStroke(int index, float[] dash) {
    for (GShape shape : this.shapes) {
      if (shape.isSelected()) {
        shape.setStroke(index);
        shape.setStrokeDash(dash);
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

  public void setBackgroundColor(Color backgroundcolor) {
    this.backgroundColor = backgroundcolor;
    this.setBackground(backgroundColor);
    this.isUpdated = true;
  }

  public void setObjectBackColor(Object backcolorobject) {
    this.objectbackgroundColor = (Color) backcolorobject;
  }

  public void setColorInfo() {
    this.selectedShape.setFillColor(this.fillColor);
    this.selectedShape.setLineColor(this.lineColor);
  }

  public void setShapeFrontBack(boolean front) {
    this.forFront.clear();

    if (front) {
      this.shapes.remove(this.selectedShape);
      this.forFront.addAll(this.shapes);
      this.forFront.add(this.selectedShape);
    } else {
      this.forFront.add(this.selectedShape);
      this.shapes.remove(this.selectedShape);
      this.forFront.addAll(this.shapes);
    }
    this.shapes.clear();
    this.shapes.addAll(this.forFront);
    this.repaint();
  }

  public void zoomIn() {
    this.scale = this.scale+0.5;
    repaint();
  }

  public void zoomOut() {
    this.scale = this.scale-0.5;
    repaint();
  }

  // Paint Components
  public void paint(Graphics g) {
    Graphics2D graphics2d = (Graphics2D) g;
    super.paint(g);

    if (this.scale != 1.0) {
      AffineTransform transform = new AffineTransform();
      transform.scale(this.scale, this.scale);
      graphics2d.setTransform(transform);
    }

    if (this.openimage != null) {
      graphics2d.drawImage(openimage, 0, 0, this);
    }

    if (this.pixelimage != null) {
      graphics2d.drawImage(pixelimage, 0, 0, this);
    }

    if (this.selectedShape != null) {
      this.selectedShape.draw(graphics2d);
    }

    if (this.selectedShape instanceof GTextBox) {
      (selectedShape).draw(graphics2d);
    }

    if (this.objectbackgroundColor != null) {
      this.setBackground(objectbackgroundColor);
      this.objectbackgroundColor = null;
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

    if (this.selectedShape instanceof GSelection) {
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
    } catch (NullPointerException ignored) {
    }
  }

  private void finishTransforming(int x2, int y2) {
    this.transformer.finishTransforming((Graphics2D) this.getGraphics(), x2, y2);
    if (this.transformer instanceof GDrawer) {
      if (this.selectedShape instanceof GSelection) {
        ((GSelection) this.selectedShape).contains(this.shapes);
        this.selectedShape = null;
        this.isUpdated = this.shapes.size() > 0;
      } else {
        this.shapes.add(this.selectedShape);
        this.clip.tempshapes.clear();
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

  // methods
  private void defineActionState(int x, int y) {
    EOnState eOnState = onShape(x, y);
    if (eOnState == null) {
      this.clearSelected();
      this.transformer = new GDrawer();
    } else if (geteCurrentState() == ECurrentState.eSelecting) {
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
    return null;
  }

  private EAnchors confirmAnchorSelected(int x, int y) {
    if (selectedShape != null) {
      GAnchor gAnchor = selectedShape.getGAnchor();
      eAnchor = gAnchor.getSelectedAnchor(x, y);
    } else {
      eAnchor = null;
    }
    return eAnchor;
  }

  private Cursor getResizeCursor(EAnchors eAnchor) {
    Cursor resizeCursor = null;
    switch (eAnchor) {
      case NW:
        resizeCursor = CursorManager.NW_CURSOR;
        break;
      case NN:
        resizeCursor = CursorManager.NN_CURSOR;
        break;
      case NE:
        resizeCursor = CursorManager.NE_CURSOR;
        break;
      case EE:
        resizeCursor = CursorManager.EE_CURSOR;
        break;
      case SE:
        resizeCursor = CursorManager.SE_CURSOR;
        break;
      case SS:
        resizeCursor = CursorManager.SS_CURSOR;
        break;
      case SW:
        resizeCursor = CursorManager.SW_CURSOR;
        break;
      case WW:
        resizeCursor = CursorManager.WW_CURSOR;
        break;
      case RR:
        resizeCursor = CursorManager.RR_CURSOR;
        break;
      default:
        this.setCursor(CursorManager.DEFAULT_CURSOR);
        break;
    }

    return resizeCursor;
  }

  public void changeCursor(int x, int y) {
    if (geteCurrentState() == ECurrentState.eDrawing) {
      this.setCursor(CursorManager.CROSSHAIR_CURSOR);
    } else {
      this.eAnchor = confirmAnchorSelected(x, y);
      this.setCursor(onShape(x, y) == EOnState.eOnShape ? CursorManager.MOVE_CURSOR
          : (this.eAnchor != null && selectedShape.isSelected()) ? getResizeCursor(eAnchor)
              : CursorManager.DEFAULT_CURSOR);
    }

  }

  public void undo() {
    if (this.clip.tempshapes.size() > 0) {
      this.clearSelected();
      index = clip.getTempShape().size() - 1;
      this.shapes.add(clip.getTempShape().get(index));
      this.shapes.lastElement().setSelected(true);
      clip.tempshapes.remove(index);
    }
    this.repaint();
  }

  public void redo() {
    if (this.shapes.size() > 0) {
      this.clip.setTempShape(shapes.lastElement());
      this.shapes.remove(this.shapes.lastElement());

      if (!this.shapes.isEmpty()) {
        this.shapes.lastElement().setSelected(true);
      }
      this.selectedShape = null;
    }
    this.repaint();
  }

  public void cut() {
    Vector<GShape> selectedShapes = new Vector<GShape>();
    for (int i = this.shapes.size() - 1; i >= 0; i--) {
      if (this.shapes.get(i).isSelected()) {
        selectedShapes.add(this.shapes.get(i));
        this.shapes.remove(i);
      }
    }
    this.clip.setContents(selectedShapes);
    this.selectedShape = null;
    this.repaint();
  }

  public void copy() {
    Vector<GShape> selectedShapes = new Vector<GShape>();
    for (GShape shape : this.shapes) {
      if (shape.isSelected()) {
        selectedShapes.add(shape);
      }
    }
    this.clip.setContents(selectedShapes);
    this.repaint();
  }

  public void paste() {
    Vector<GShape> clipshapes = this.clip.getContents();
    for (GShape shape : clipshapes) {
      this.clearSelected();
      shape.verticalPaste();
      shape.setSelected(true);
    }
    this.shapes.addAll(clipshapes);
    this.clip.setContents(clipshapes);

    repaint();
  }

  public void delete() {
    for (int i = this.shapes.size() - 1; i >= 0; i--) {
      if (this.shapes.get(i).isSelected()) {
        this.shapes.remove(i);
        this.selectedShape = null;
      }
    }
    this.repaint();
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
      if (eDrawingState == EDrawingState.eTransforming) {
        if (shapeTool.geteDrawingStyle() == EDrawingStyle.e2PointDrawing) {
          finishTransforming(e.getX(), e.getY());
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
        PopupMenu popup = new PopupMenu(DrawingPanel.this);
        popup.show(DrawingPanel.this, e.getX(), e.getY());
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
      if (selectedShape instanceof GTextBox && eCurrentState == ECurrentState.eSelecting) {
        String text = JOptionPane.showInputDialog("삽입하실 글자를 입력해주세요", "글자");
        if (text != null) {
          ((GTextBox) selectedShape).setText(text);
        }
      }
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
  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
      throws PrinterException {
    Graphics2D grapchis2D;

    if (pageIndex == 0) {
      grapchis2D = (Graphics2D) graphics;
      this.paint(grapchis2D);

      return (PAGE_EXISTS);
    }

    return (NO_SUCH_PAGE);
  }

}
