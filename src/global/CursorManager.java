package global;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

import global.Constants.ECursor;

public class CursorManager {
	public static final Toolkit tk = Toolkit.getDefaultToolkit();
	public static final String rotatecursoranme = ECursor.eCursorImage.getRotateImage();
	public static final String defaultcursorname = ECursor.eCursorImage.getCursorImage();
	
	public static final Cursor DEFAULT_CURSOR = tk.createCustomCursor(tk.getImage(defaultcursorname), new Point(0, 0), "");
	public static final Cursor CROSSHAIR_CURSOR = new Cursor(Cursor.CROSSHAIR_CURSOR);
	public static final Cursor MOVE_CURSOR = new Cursor(Cursor.MOVE_CURSOR);
	public static final Cursor NW_CURSOR = new Cursor(Cursor.NW_RESIZE_CURSOR);
	public static final Cursor NN_CURSOR = new Cursor(Cursor.N_RESIZE_CURSOR);
	public static final Cursor NE_CURSOR = new Cursor(Cursor.NE_RESIZE_CURSOR);
	public static final Cursor EE_CURSOR = new Cursor(Cursor.E_RESIZE_CURSOR);
	public static final Cursor SE_CURSOR = new Cursor(Cursor.SE_RESIZE_CURSOR);
	public static final Cursor SS_CURSOR = new Cursor(Cursor.S_RESIZE_CURSOR);
	public static final Cursor SW_CURSOR = new Cursor(Cursor.SW_RESIZE_CURSOR);
	public static final Cursor WW_CURSOR = new Cursor(Cursor.W_RESIZE_CURSOR);
	public static final Cursor RR_CURSOR = tk.createCustomCursor(tk.getImage(rotatecursoranme), new Point(25, 25), "");
}
