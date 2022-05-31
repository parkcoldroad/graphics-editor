package menus;

import frames.DrawingPanel;
import frames.MainFrame;
import global.Constants.EFileMenuItem;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import tool.ImageCanvas;

public class FileMenu extends JMenu {

  private JFileChooser fileChooser;
  private  String file = null;
  private String filename = "No Title";
  private final File directory;
  private final File imagedirectory;
  protected Image openimage;

  private DrawingPanel drawingPanel;
  private final ImageCanvas imageCanvas;

  private String checkfilename = null;
  private boolean checkfile;

  public FileMenu() {
    super("file");
    this.checkfile = false;
    this.actionListener = new FileMenuHandler();
    this.directory = new File("data");
    this.imagedirectory = new File("images");
    this.imageCanvas = new ImageCanvas();

    for (EFileMenuItem eFileMenuItem : EFileMenuItem.values()) {
      JMenuItem menuItem = new JMenuItem(eFileMenuItem.getText());
      menuItem.setBackground(Color.WHITE);
      menuItem.setActionCommand(eFileMenuItem.name());
      menuItem.addActionListener(actionListener);
      menuItem.setAccelerator(eFileMenuItem.getKeyStroke());
      this.add(menuItem);
    }
  }

  private void setCheckFile(String file) {
    this.file = file;
  }

  public String getCheckFile() {
    return file;
  }

  public void associate(DrawingPanel drawingPanel) {
    this.drawingPanel = drawingPanel;
    this.file = null;
  }

  private void newFile() {
    if (checkSave()) {
      this.drawingPanel.initiatePanel();
      this.file = null;
      this.openimage = null;
      this.filename = "File";
      this.checkfile = false;
      this.checkfilename = null;
      MainFrame.setTabTitle(drawingPanel, filename);
    }
  }

  private boolean checkSave() {
    boolean isChecked = true;
    if (this.drawingPanel.isUpdated()) {
      // save
      int reply = JOptionPane.showConfirmDialog(this.drawingPanel,
          "<html>" + "현재 선택된 파일에 저장되지 않은 변경 내용이 있습니다." + " <br> 작업을 저장하시겠습니까? </html>", "SaveFile",
          JOptionPane.YES_NO_CANCEL_OPTION);
      if (reply == JOptionPane.OK_OPTION) {
        this.saveAs();
      } else if (reply == JOptionPane.NO_OPTION) {
        this.drawingPanel.setUpdated(false);
      } else if (reply == JOptionPane.CANCEL_OPTION || reply == JOptionPane.CLOSED_OPTION) {
        isChecked = false;
      }
    }
    return isChecked;
  }

  private boolean checkSameFile() {
     checkfilename = getCheckFile();
    if (file == checkfilename) {
      return checkfile = true;
    } else {
      return checkfile = false;
    }
  }

  private void open() {
    if (checkSave()) {
      fileChooser = new JFileChooser(directory);
      fileChooser.setDialogTitle("open File");
      fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".GIL FILE", "gil"));
      int returnvalue = fileChooser.showOpenDialog(this.drawingPanel);

      if (returnvalue == JFileChooser.APPROVE_OPTION) {
        file = fileChooser.getSelectedFile().toString();
        this.drawingPanel.initiatePanel();
        this.setCheckFile(file);

        filename = fileChooser.getSelectedFile().getName();
        MainFrame.setTabTitle(drawingPanel, filename);

        load();
      }
    }
  }

  private void openImage() {
    JFileChooser fileChooser = new JFileChooser(imagedirectory);
    fileChooser.setDialogTitle("open Image");
    int returnValue = fileChooser.showOpenDialog(this.drawingPanel);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      try {
        File imagefile = fileChooser.getSelectedFile();
        openimage = ImageIO.read(imagefile);
        this.drawingPanel.openImage(openimage);
        imageCanvas.setImage(openimage);
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
  }

  private void load() {
    try {
      FileInputStream fileinputStream = new FileInputStream(file); // bytestream -> file
      ObjectInputStream objectinputStream = new ObjectInputStream(
          fileinputStream); // object->bytestream
      Object object = objectinputStream.readObject();
      Object imageobject = imageCanvas.readObject(objectinputStream);
      Object backcolorobject = objectinputStream.readObject();
      this.drawingPanel.setImagePixel(imageobject);
      this.drawingPanel.setShapes(object);
      this.drawingPanel.setObjectBackColor(backcolorobject);
      objectinputStream.close();
    } catch (IOException | ClassNotFoundException e) {
      JOptionPane.showMessageDialog(null, "열 수 없는 파일입니다.");
    }
  }

  private void save() {
    checkfile = checkSameFile();
    if (this.file == null) {
      saveAs();
    } else if (this.checkfile) {
      store();
    } else {
      saveAs();
    }
  }

  private void store() {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(file); // bytestream -> file
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(
          fileOutputStream);// object->bytestream
      objectOutputStream.writeObject(this.drawingPanel.getShapes());
      imageCanvas.writeObject(objectOutputStream);
      objectOutputStream.writeObject(this.drawingPanel.getBackground());
      objectOutputStream.close();
      this.drawingPanel.setUpdated(false);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void saveAs() {
    fileChooser = new JFileChooser(directory);
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".GIL FILE", "gil"));
    int returnVal = fileChooser.showSaveDialog(this.drawingPanel);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      this.file = fileChooser.getSelectedFile().toString();

      filename = fileChooser.getSelectedFile().getName();
      MainFrame.setTabTitle(drawingPanel, filename);

      if (!this.file.endsWith(".gil")) {
        file += ".gil";
      }
      store();
    }
  }

  public void quit() {
    if (checkSave()) {
      System.exit(0);
    }
  }

  private void print() {
    Paper paper = new Paper();
    final PrinterJob printerJob = PrinterJob.getPrinterJob();
    PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
    PageFormat pageFormat = new PageFormat();
    pageFormat.setPaper(paper);
    pageFormat.setOrientation(PageFormat.LANDSCAPE);
    printerJob.setPrintable(drawingPanel, pageFormat);
    boolean isOn = printerJob.printDialog(printRequestAttributeSet);
    if (isOn) {
      try {
        printerJob.print();
      } catch (PrinterException ex) {
        ex.getStackTrace();
      }
    }
  }

  private class FileMenuHandler implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      EFileMenuItem eMenuItem = EFileMenuItem.valueOf(e.getActionCommand());
      switch (eMenuItem) {
        case eNew:
          newFile();
          break;
        case eOpen:
          open();
          break;
        case eOpenImage:
          openImage();
          break;
        case eSave:
          save();
          break;
        case eSaveAs:
          saveAs();
          break;
        case ePrint:
          print();
          break;
        case eExit:
          quit();
          break;
      }
    }

  }

}
