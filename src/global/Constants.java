package global;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import shapes.GGroup;
import shapes.GLine;
import shapes.GOval;
import shapes.GPencil;
import shapes.GPolygon;
import shapes.GRectangle;
import shapes.GShape;
import shapes.GTextBox;

public class Constants {

	
	public enum EShapes {
		eSelection(new GGroup(), "images/drag.png", "images/drag-selected.png","selection",1),
		eRectangle(new GRectangle(), "images/rectangle.png", "images/rectangle-selected.png","rectangle",0),
		eOval(new GOval(), "images/oval.png", "images/oval-selected.png", "oval",0),
		eLine(new GLine(), "images/line.png", "images/line-selected.png", "line",0),
		ePolygon(new GPolygon(), "images/polygon.png", "images/polygon-selected.png", "polygon",0),
		eTextBox(new GTextBox(), "images/text.png", "images/text-selected.png", "text",0),
		ePencil(new GPencil(), "images/pencil.png", "images/pencil-selected.png","pencil",0);
		
		private GShape shapeTool;
		private String image;
		private String selectedimage;
		private String tooltipname;
		private int currentstate;

		private EShapes(GShape shapeTool, String image, String selectedimage,String tooltipname,int currentstate) {
			this.shapeTool = shapeTool;
			this.image = image;
			this.selectedimage = selectedimage;
			this.tooltipname = tooltipname;
			this.currentstate = currentstate;
		}

		public GShape newInstance() {
			return shapeTool;
		}

		public String getImage() {
			return image;
		}

		public String getSelectedImage() {
			return selectedimage;
		}

		public String getTooltipname() {
			return tooltipname;
		}
		
		public int getCurrentState() {
			return currentstate;
		}
	}
	
	public enum ECursor {
		eCursorImage("images/default-cursor.png","images/rotate-cursor.png");
		
		private String cursorimage;
		private String rotateimage;
		
		private ECursor(String cursorimage,String rotateimage) {
			this.cursorimage = cursorimage;
			this.rotateimage = rotateimage;
		}

		public String getCursorImage() {
			return cursorimage;
		}
		
		public String getRotateImage() {
			return rotateimage;
		}

	}
	
	public enum EMenu {
		eFile("file"), 
		eEdit("edit"), 
		eColor("color");

		private String text;

		private EMenu(String text) {
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

	}

	public enum EFileMenuItem {
		eNew("new", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)),
		eOpen("open", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)),
		eOpenImage("openImage", KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK)),
		eSave("save", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)),
		eSaveAs("saveAs", KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK)),
		ePrint("print", KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK)),
		eExit("exit", KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_DOWN_MASK));

		private String text;
		private KeyStroke keyStroke;

		private EFileMenuItem(String text, KeyStroke keyStroke) {
			this.text = text;
			this.keyStroke = keyStroke;
		}

		public String getText() {
			return this.text;
		}

		public KeyStroke getKeyStroke() {
			return this.keyStroke;
		}
	}

	public enum EEditMenuItem {
		eUndo("undo"), 
		eRedo("redo"),
		eCut("cut"),
		eCopy("copy"), 
		ePaste("paste"), 
		eDelete("delete");

		private String text;

		private EEditMenuItem(String text) {
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

	}

	public enum EColorMenuItem {
		eLineColor("linecolor"),
		eFillColor("fillcolor");

		private String text;

		private EColorMenuItem(String text) {
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

	}

}
