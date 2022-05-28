package tool;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@SuppressWarnings("serial")
public class ImageCanvas implements Serializable {

	transient Image image;

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Object readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
		int[] pixels = (int[]) (s.readObject());
		return pixels;
	}

	public void writeObject(ObjectOutputStream s) throws IOException {
		int[] pixels = image != null ? new int[50 * 50] : null;

		if (image != null) {
			try {
				PixelGrabber pg = new PixelGrabber(image, 0, 0, 50,  50, pixels, 0, 50);
				pg.grabPixels();
				if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
					throw new IOException("failed to load image contents");
				}
			} catch (InterruptedException e) {
				throw new IOException("image load interrupted");
			}
		}
		s.writeObject(pixels);
	}



}
