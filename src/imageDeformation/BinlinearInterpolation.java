package imageDeformation;

import geometry.Grid;
import geometry.Point;
import geometry.Util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

import JavaHelper.BufferedImageHelper;

public class BinlinearInterpolation {
	int width = 0;
	int height = 0;
	BufferedImage target = null, source = null;
	static final ColorModel cm = ColorModel.getRGBdefault();
	
	public BufferedImage generate( BufferedImage target, final BufferedImage source, final Grid []fromGrid, final Grid []toGrid) {
		assert( source != null );
		
		this.source = source;		
		width = source.getWidth();
		height = source.getHeight();

		if ( target == null || target == source || target.getWidth() != width || target.getHeight() != height ) {
			this.target = BufferedImageHelper.newBufferdImage(width, height);
		} else {
			this.target = target;
		}
		
		for ( int i = 0; i < width; ++i ) {
			for ( int j = 0; j < height; ++j ) {
				this.target.setRGB(i, j, 0);
			}
		}
		
		assert( fromGrid.length == toGrid.length );
		
		for ( int i = 0; i < toGrid.length; ++i ) {
			Grid fg = fromGrid[i];
			Grid tg = toGrid[i];
			fill( tg.p[0], tg.p[1], tg.p[2], fg.p[0], fg.p[1], fg.p[2] ); 
			fill( tg.p[2], tg.p[3], tg.p[0], fg.p[2], fg.p[3], fg.p[0] ); 			
		}
		
		
		return this.target;
	}

	void fill( Point a, Point b, Point c, Point a0, Point b0, Point c0 ) {
		int x0 = (int)Math.floor( Util.minX(a, b, c) ), x1 = (int)Math.ceil( Util.maxX(a, b, c) );
		int y0 = (int)Math.floor( Util.minY(a, b, c) ), y1 = (int)Math.ceil( Util.maxY(a, b, c) );
		x0 = Math.max(x0, 0); x1 = Math.min(x1,  width - 1 );
		y0 = Math.max(y0, 0); y1 = Math.min(y1,  height - 1 );
		for ( int i = x0; i <= x1; ++i ) {
			for ( int j = y0; j <= y1; ++j ) {
				double []t = Point.TriangleContainsPoint(a, b, c, new Point(i, j) );
				if ( t[1] >= 0 && t[2] >= 0 ) {
					double srcX = (a0.x * t[0] + b0.x * t[1] + c0.x * t[2]) / (t[0] + t[1] + t[2]);
					double srcY = (a0.y * t[0] + b0.y * t[1] + c0.y * t[2]) / (t[0] + t[1] + t[2]);
					
					if ( 0 <= srcX && srcX <= width - 1) {
						if ( 0 <= srcY && srcY <= height - 1 ) {
							int rgb = query(srcX, srcY);
							this.target.setRGB(i, j, rgb);
						}
					}
				}
				
			}
		}
	}
	
	int query(double x, double y) {
		int x1 = (int)Math.floor(x), x2 = (int)Math.ceil(x);
		int y1 = (int)Math.floor(y), y2 = (int)Math.ceil(y);
		
		int rgb11 = source.getRGB(x1, y1); 
		int rgb21 = source.getRGB(x2, y1); 
		int rgb12 = source.getRGB(x1, y2); 
		int rgb22 = source.getRGB(x2, y2); 
		
		int r11 = cm.getRed(rgb11);
		int r12 = cm.getRed(rgb12);
		int r21 = cm.getRed(rgb21);
		int r22 = cm.getRed(rgb22);

		int g11 = cm.getGreen(rgb11);
		int g12 = cm.getGreen(rgb12);
		int g21 = cm.getGreen(rgb21);
		int g22 = cm.getGreen(rgb22);

		int b11 = cm.getBlue(rgb11);
		int b12 = cm.getBlue(rgb12);
		int b21 = cm.getBlue(rgb21);
		int b22 = cm.getBlue(rgb22);

		int a11 = cm.getAlpha(rgb11);
		int a12 = cm.getAlpha(rgb12);
		int a21 = cm.getAlpha(rgb21);
		int a22 = cm.getAlpha(rgb22);
		
		double r = r11 * (x2 - x) * (y2 - y) + r21 * (x - x1) * (y2 - y) + r12 * (x2 - x) * (y - y1) + r22 * (x - x1) * (y - y1);
		double g = g11 * (x2 - x) * (y2 - y) + g21 * (x - x1) * (y2 - y) + g12 * (x2 - x) * (y - y1) + g22 * (x - x1) * (y - y1);
		double b = b11 * (x2 - x) * (y2 - y) + b21 * (x - x1) * (y2 - y) + b12 * (x2 - x) * (y - y1) + b22 * (x - x1) * (y - y1);
		double a = a11 * (x2 - x) * (y2 - y) + a21 * (x - x1) * (y2 - y) + a12 * (x2 - x) * (y - y1) + a22 * (x - x1) * (y - y1);
		
		r /= ( x2 - x1 ) * (y2 - y1);
		g /= ( x2 - x1 ) * (y2 - y1);
		b /= ( x2 - x1 ) * (y2 - y1);
		a /= ( x2 - x1 ) * (y2 - y1);
		
		int c = (new Color((int)r, (int)g, (int)b, (int)a)).getRGB();	
		
		return c;
		
	}

}
