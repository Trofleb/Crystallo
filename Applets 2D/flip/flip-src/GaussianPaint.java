import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/* Charge Flip - GaussianPaint.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 18 janv. 07
 * 
 * nicolas.schoeni@epfl.ch
 */

public class GaussianPaint implements Paint {
	final int h, sigma;
	final boolean eraser;
	public GaussianPaint(int h, int sigma, boolean eraser) {
		this.h=h; this.sigma=sigma; this.eraser=eraser;
	}

	public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
		return new GaussianPaintContext(h, sigma, eraser, deviceBounds);
	}
	public int getTransparency() {
		return TRANSLUCENT;
	}

	public class GaussianPaintContext implements PaintContext {
		int cx, cy;
		int height, sigma2;
		boolean eraser;
		
		public GaussianPaintContext(int height, int sigma, boolean eraser, Rectangle b) {
			this.height = height; 
			this.sigma2 = 2*sigma*sigma;
			this.eraser=eraser;
			cx = b.x + b.width/2;
			cy = b.y + b.height/2;
		}

		public void dispose() {
		}
		public ColorModel getColorModel() {
			return ColorModel.getRGBdefault();
		}
		public Raster getRaster(int x, int y, int w, int h) {
			WritableRaster raster = getColorModel().createCompatibleWritableRaster(w, h);
			int t = Color.white.getAlpha();
			int[] data = new int[w * h * 4];
			int c = eraser?255:(255-height);
			for (int j = 0; j < h; j++) {
				for (int i = 0; i < w; i++) {
					int base = (j * w + i) * 4;

					int g = (int)Math.round(255*Math.exp(-((cx-x-i)*(cx-x-i)+(cy-y-j)*(cy-y-j))/(double)sigma2));
					if (g<0) g=0;
					if (g>255) g=255;
					data[base + 3] = g;
					data[base + 0] = c;
					data[base + 1] = c;
					data[base + 2] = c;
				}
			}
			raster.setPixels(0, 0, w, h, data);
			return raster;
		}
	}
}
