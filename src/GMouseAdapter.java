import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

class GMouseAdapter extends MouseAdapter {
	Deformation def = new AffineDeformation();

	
	public void mouseClicked(MouseEvent e) {
//		parent.draw( parent.img, parent.q, null, true, parent.gTrans );
	}
	
	int currentPointIndex = -1;
	
	public void mousePressed(MouseEvent e) {
		
		Point []q = parent.q;
//		System.out.printf( "Silly Pressed on (%d, %d)%n", e.getX(), e.getY() );	
		
		Point r = new Point( e.getX(), e.getY() );
		for ( int i = 0; i < q.length; ++i ) {
			if ( q[i].InfintyNormDistanceTo(r) <= 20 ) {
				currentPointIndex = i;
				parent.draw( parent.img, parent.q, null, true, parent.gTrans );
				return;
			}
		}
	}
	
	void fill( Point a, Point b, Point c, Point a0, Point b0, Point c0 ) {
		int x0 = (int)Math.floor( Util.minX(a, b, c) ), x1 = (int)Math.ceil( Util.maxX(a, b, c) );
		int y0 = (int)Math.floor( Util.minY(a, b, c) ), y1 = (int)Math.ceil( Util.maxY(a, b, c) );
		x0 = Math.max(x0, 0); x1 = Math.min(x1,  parent.img.getWidth()-1 );
		y0 = Math.max(y0, 0); y1 = Math.min(y1,  parent.img.getHeight()-1 );
		for ( int i = x0; i <= x1; ++i ) {
			for ( int j = y0; j <= y1; ++j ) {
				double []t = Point.TriangleContainsPoint(a, b, c, new Point(i, j) );
				if ( t[1] >= 0 && t[2] >= 0 ) {
					double srcX = (a0.x * t[0] + b0.x * t[1] + c0.x * t[2]) / (t[0] + t[1] + t[2]);
					double srcY = (a0.y * t[0] + b0.y * t[1] + c0.y * t[2]) / (t[0] + t[1] + t[2]);
//					if ( debug ) System.out.printf( "%d %d -> %.2f %.2f (t = %.2f %.2f %.2f)%n", i, j, srcX, srcY, t[0], t[1], t[2] );
					if ( 0 <= srcX && srcX <= parent.img.getWidth() - 1) {
						if ( 0 <= srcY && srcY <= parent.img.getHeight() - 1 ) {
							int rgb = query(srcX, srcY);
//							int rgb = parent.oriImg.getRGB((int)srcX, (int)srcY);
							parent.img.setRGB(i, j, rgb);
						}
					}
				}
				
			}
		}
	}
	
//	boolean debug = false;
	
	int query(double x, double y) {
		int x1 = (int)Math.floor(x), x2 = (int)Math.ceil(x);
		int y1 = (int)Math.floor(y), y2 = (int)Math.ceil(y);
		
		int rgb11 = parent.oriImg.getRGB(x1, y1); 
		int rgb21 = parent.oriImg.getRGB(x2, y1); 
		int rgb12 = parent.oriImg.getRGB(x1, y2); 
		int rgb22 = parent.oriImg.getRGB(x2, y2); 
		
		int r11 = ColorModel.getRGBdefault().getRed(rgb11);
		int r12 = ColorModel.getRGBdefault().getRed(rgb12);
		int r21 = ColorModel.getRGBdefault().getRed(rgb21);
		int r22 = ColorModel.getRGBdefault().getRed(rgb22);

		int g11 = ColorModel.getRGBdefault().getGreen(rgb11);
		int g12 = ColorModel.getRGBdefault().getGreen(rgb12);
		int g21 = ColorModel.getRGBdefault().getGreen(rgb21);
		int g22 = ColorModel.getRGBdefault().getGreen(rgb22);

		int b11 = ColorModel.getRGBdefault().getBlue(rgb11);
		int b12 = ColorModel.getRGBdefault().getBlue(rgb12);
		int b21 = ColorModel.getRGBdefault().getBlue(rgb21);
		int b22 = ColorModel.getRGBdefault().getBlue(rgb22);

		int a11 = ColorModel.getRGBdefault().getAlpha(rgb11);
		int a12 = ColorModel.getRGBdefault().getAlpha(rgb12);
		int a21 = ColorModel.getRGBdefault().getAlpha(rgb21);
		int a22 = ColorModel.getRGBdefault().getAlpha(rgb22);
		
		double r = r11 * (x2 - x) * (y2 - y) + r21 * (x - x1) * (y2 - y) + r12 * (x2 - x) * (y - y1) + r22 * (x - x1) * (y - y1);
		double g = g11 * (x2 - x) * (y2 - y) + g21 * (x - x1) * (y2 - y) + g12 * (x2 - x) * (y - y1) + g22 * (x - x1) * (y - y1);
		double b = b11 * (x2 - x) * (y2 - y) + b21 * (x - x1) * (y2 - y) + b12 * (x2 - x) * (y - y1) + b22 * (x - x1) * (y - y1);
		double a = a11 * (x2 - x) * (y2 - y) + a21 * (x - x1) * (y2 - y) + a12 * (x2 - x) * (y - y1) + a22 * (x - x1) * (y - y1);
		
//		System.out.println( r + "  " + g + "  " + b +"  " +a );
		
		r /= ( x2 - x1 ) * (y2 - y1);
		g /= ( x2 - x1 ) * (y2 - y1);
		b /= ( x2 - x1 ) * (y2 - y1);
		a /= ( x2 - x1 ) * (y2 - y1);
		
		int c = (new Color((int)r, (int)g, (int)b, (int)a)).getRGB();	
		
		return c;
		
	}
	
	void work(Point q) {
		
		parent.q[currentPointIndex] = q;
		
		Grid []newG = new Grid[ parent.g.length ];
		for ( int i = 0; i < parent.g.length; ++i ) {
			newG[i] = new Grid(
					def.query( parent.g[i].p[0] , parent.p, parent.q, Config.alpha),
					def.query( parent.g[i].p[1] , parent.p, parent.q, Config.alpha),
					def.query( parent.g[i].p[2] , parent.p, parent.q, Config.alpha),
					def.query( parent.g[i].p[3] , parent.p, parent.q, Config.alpha)
			);
		}
		
		parent.gTrans = newG;
		
		
		int width = parent.oriImg.getWidth();
		int height = parent.oriImg.getHeight();
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB );
		
		parent.img = newImg;
		
		for ( int k = 0; k < newG.length; ++k ) {
			Grid g = parent.g[k];
			Grid ng = newG[k];
			fill( ng.p[0], ng.p[1], ng.p[2], g.p[0], g.p[1], g.p[2] ); 
			fill( ng.p[2], ng.p[3], ng.p[0], g.p[2], g.p[3], g.p[0] ); 
		}
		
/*		for (int i = 0; i < width; i += Config.gridLength) {
			for (int j = 0; j < height; j += Config.gridLength) {
				Point s = def.query( new Point(i, j), parent.q, parent.p, Config.alpha);
				if ( 0 <= s.x && s.x <= parent.oriImg.getWidth() - 1 
						&& 0 <= s.y && s.y <= parent.oriImg.getHeight() - 1 ) {
					
					double x = s.x, y = s.y;
					int x1 = (int)Math.floor(x), x2 = (int)Math.ceil(x);
					int y1 = (int)Math.floor(y), y2 = (int)Math.ceil(y);
					
					int rgb11 = parent.oriImg.getRGB(x1, y1); 
					int rgb21 = parent.oriImg.getRGB(x2, y1); 
					int rgb12 = parent.oriImg.getRGB(x1, y2); 
					int rgb22 = parent.oriImg.getRGB(x2, y2); 
					
					int r11 = ColorModel.getRGBdefault().getRed(rgb11);
					int r12 = ColorModel.getRGBdefault().getRed(rgb12);
					int r21 = ColorModel.getRGBdefault().getRed(rgb21);
					int r22 = ColorModel.getRGBdefault().getRed(rgb22);

					int g11 = ColorModel.getRGBdefault().getGreen(rgb11);
					int g12 = ColorModel.getRGBdefault().getGreen(rgb12);
					int g21 = ColorModel.getRGBdefault().getGreen(rgb21);
					int g22 = ColorModel.getRGBdefault().getGreen(rgb22);

					int b11 = ColorModel.getRGBdefault().getBlue(rgb11);
					int b12 = ColorModel.getRGBdefault().getBlue(rgb12);
					int b21 = ColorModel.getRGBdefault().getBlue(rgb21);
					int b22 = ColorModel.getRGBdefault().getBlue(rgb22);

					int a11 = ColorModel.getRGBdefault().getAlpha(rgb11);
					int a12 = ColorModel.getRGBdefault().getAlpha(rgb12);
					int a21 = ColorModel.getRGBdefault().getAlpha(rgb21);
					int a22 = ColorModel.getRGBdefault().getAlpha(rgb22);
					
					double r = r11 * (x2 - x) * (y2 - y) + r21 * (x - x1) * (y2 - y) + r12 * (x2 - x) * (y - y1) + r22 * (x - x1) * (y - y1);
					double g = g11 * (x2 - x) * (y2 - y) + g21 * (x - x1) * (y2 - y) + g12 * (x2 - x) * (y - y1) + g22 * (x - x1) * (y - y1);
					double b = b11 * (x2 - x) * (y2 - y) + b21 * (x - x1) * (y2 - y) + b12 * (x2 - x) * (y - y1) + b22 * (x - x1) * (y - y1);
					double a = a11 * (x2 - x) * (y2 - y) + a21 * (x - x1) * (y2 - y) + a12 * (x2 - x) * (y - y1) + a22 * (x - x1) * (y - y1);
					
//					System.out.println( r + "  " + g + "  " + b +"  " +a );
					
					r /= ( x2 - x1 ) * (y2 - y1);
					g /= ( x2 - x1 ) * (y2 - y1);
					b /= ( x2 - x1 ) * (y2 - y1);
					a /= ( x2 - x1 ) * (y2 - y1);
					
					int c = (new Color((int)r, (int)g, (int)b, (int)a)).getRGB();					
					
					for ( int ii = i; ii < i + Config.gridLength && ii < width; ++ii ) {
						for ( int jj = j; jj < j + Config.gridLength && jj < height; ++jj ) {
							newImg.setRGB(ii, jj, c);							
						}
					}
					
				}
//				tar.setPixel(i, j, src.getPixel(src.w - 1 - i, j) );
			}
		}*/
		
		parent.draw( newImg, parent.q, q, true, parent.gTrans );
		
	}
	public void mouseDragged(MouseEvent e) {
		System.out.printf( "Drag on (%d, %d)%n", e.getX(), e.getY() );
		Point r = new Point( e.getX(), e.getY() );
		work(r);
	}
	public void mouseReleased(MouseEvent e) {
		System.out.printf( "Drag on (%d, %d)%n", e.getX(), e.getY() );
		Point r = new Point( e.getX(), e.getY() );
		work(r);
	}

	GMouseAdapter(Main parent) {
		this.parent = parent;
	}
	Main parent;
}
