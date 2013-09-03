import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class Picture {
	public int []r, g, b, a;
	public int w, h;
	ColorModel cm = ColorModel.getRGBdefault();
	
	Picture( BufferedImage src ) {
		w = src.getWidth();
		h = src.getHeight();
		r = new int[w * h];
		g = new int[w * h];
		b = new int[w * h];
		a = new int[w * h];
		
		for (int i = 0, k = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				int rgba = src.getRGB(i, j);
				r[k] = cm.getRed(rgba);
				g[k] = cm.getGreen(rgba);
				b[k] = cm.getBlue(rgba);
				a[k] = cm.getAlpha(rgba);
				++k;
			}
		}
	}
	
	Picture(int w, int h) {
		this.w = w;
		this.h = h;
		r = new int[w * h];
		g = new int[w * h];
		b = new int[w * h];		
		a = new int[w * h];
	}
	
	int getPixel(int i, int j) {
		int k = i * h + j;
		int R = r[k], G = g[k], B = b[k], A = a[k];
		Color c = new Color(R, G, B, A);
//		System.out.println( R + "  " + G + "  " + B +"  " +A );
		return c.getRGB();
	}
	
	int getA(int i, int j) {		
		return a[i * h + j];
	}
	
	int getR(int i, int j) {		
		return r[i * h + j];
	}

	int getG(int i, int j) {		
		return g[i * h + j];
	}
	
	int getB(int i, int j) {		
		return b[i * h + j];
	}

	int getA(final Point o) {
		return getA( (int)o.x, (int)o.y );
	}

	int getR(final Point o) {
		return getR( (int)o.x, (int)o.y );
	}

	int getG(final Point o) {
		return getG( (int)o.x, (int)o.y );
	}

	int getB(final Point o) {
		return getB( (int)o.x, (int)o.y );
	}

	
	void setPixel(int i, int j, int rgba) {
		int k = i * h + j;
		r[k] = cm.getRed(rgba);
		g[k] = cm.getGreen(rgba);
		b[k] = cm.getBlue(rgba);
		a[k] = cm.getAlpha(rgba);		
	}
	
	BufferedImage getBufferedImage() {
		BufferedImage ret = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				ret.setRGB(i, j, getPixel(i, j) );
			}
		}
		return ret;
	}
}