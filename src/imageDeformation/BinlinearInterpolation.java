package imageDeformation;

import geometry.Lattice;
import geometry.Point;
import geometry.Util;

import java.awt.image.BufferedImage;

import javaHelper.BufferedImageHelper;

public class BinlinearInterpolation {
	int width = 0;
	int height = 0;
	BufferedImage target = null, source = null;
	
	public BufferedImage generate( BufferedImage target, final BufferedImage source, final Lattice originalLattice, final Lattice currentLattice) {
		
		long startTime = System.nanoTime();
		
		if ( null == source ) System.err.println( "ERROR" );
		
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
		
		int xCount = originalLattice.getXCount();
		int yCount = originalLattice.getYCount();
		
		if ( currentLattice.getXCount() != xCount ) System.err.println( "ERROR" );
		if ( currentLattice.getYCount() != yCount ) System.err.println( "ERROR" );
		
		for ( int i = 1; i < xCount; ++i ) {
			for ( int j = 1; j < yCount; ++j ) {
				fillGrid( currentLattice.getRect(i, j), originalLattice.getRect(i, j) );
			}
		}
		
		long finishTime = System.nanoTime();
		
		System.out.printf( "Time of BinlinearInterpolation = %.0f ms%n", (finishTime - startTime) / 1e6 );
		
		return this.target;
	}
	
	void fillGrid( Point p[], Point p0[] ) {
		if ( p.length != 4 )  System.err.println("Error on p.length");
		if ( p0.length != 4 ) System.err.println("Error on p.length");
		int x0 = (int)Math.floor( Util.minX(p) ), x1 = (int)Math.ceil( Util.maxX(p) );
		int y0 = (int)Math.floor( Util.minY(p) ), y1 = (int)Math.ceil( Util.maxY(p) );
		x0 = Math.max(x0, 0); x1 = Math.min(x1,  width - 1 );
		y0 = Math.max(y0, 0); y1 = Math.min(y1,  height - 1 );		
		for ( int i = x0; i <= x1; ++i ) {
			for ( int j = y0; j <= y1; ++j ) {
				Point cur = new Point(i, j);
				double srcX = Double.NaN, srcY = Double.NaN;
				for ( int k = 0; k < 4; ++k ) {
					if ( p[k].equals(i, j) ) {
						srcX = p0[k].x;
						srcY = p0[k].y;
						break;
					}
				}
				if ( Double.isNaN(srcX) ) {
					double []t = Point.TriangleContainsPoint(p[0], p[1], p[2], cur);
					if ( t[1] >= 0 && t[2] >= 0 ) {
						srcX = (p0[0].x * t[0] + p0[1].x * t[1] + p0[2].x * t[2]) / (t[0] + t[1] + t[2]);
						srcY = (p0[0].y * t[0] + p0[1].y * t[1] + p0[2].y * t[2]) / (t[0] + t[1] + t[2]);
						
					} else {
						t = Point.TriangleContainsPoint(p[2], p[3], p[0], cur);
						if ( t[1] >= 0 && t[2] >= 0 ) {
							srcX = (p0[2].x * t[0] + p0[3].x * t[1] + p0[0].x * t[2]) / (t[0] + t[1] + t[2]);
							srcY = (p0[2].y * t[0] + p0[3].y * t[1] + p0[0].y * t[2]) / (t[0] + t[1] + t[2]);							
						} else {
							;
						}
					}					
				}
				if ( 0 <= srcX && srcX <= width - 1) {
					if ( 0 <= srcY && srcY <= height - 1 ) {
						int rgb = query(srcX, srcY);
						this.target.setRGB(i, j, rgb);
					}
				}
			}
		}
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
		
		return source.getRGB(x1, y1);
		/*
		if ( x1 == x2 && y1 == y2 ) {
			return source.getRGB(x1, y1);
		}
		
		if ( x1 == x2 ) {
			int rgb11 = source.getRGB(x1, y1); 
			int rgb12 = source.getRGB(x1, y2); 
			return javaHelper.RGBHelper.mergeRGB(rgb11, rgb12, y2 - y, y - y1 );
		}
		
		if ( y1 == y2 ) {
			int rgb11 = source.getRGB(x1, y1); 
			int rgb21 = source.getRGB(x2, y1); 
			return javaHelper.RGBHelper.mergeRGB(rgb11, rgb21, x2 - x, x - x1 );
		}
		
		do {
			int rgb11 = source.getRGB(x1, y1); 
			int rgb21 = source.getRGB(x2, y1); 
			int rgb12 = source.getRGB(x1, y2); 
			int rgb22 = source.getRGB(x2, y2);
			return javaHelper.RGBHelper.mergeRGB(
					javaHelper.RGBHelper.mergeRGB(rgb11, rgb12, y2 - y, y - y1),
					javaHelper.RGBHelper.mergeRGB(rgb21, rgb22, y2 - y, y - y1),
					x2 - x, x - x1
			);
		} while (false);	*/	
	}

}
