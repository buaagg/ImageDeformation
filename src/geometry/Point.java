package geometry;



public class Point {
	public final double x, y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;		
	}
	
	public Point add( final Point o ) {
		return new Point( x + o.x, y + o.y );
	}
	
	public Point subtract( final Point o ) {
		return new Point( x - o.x, y - o.y );
	}
	
	public static double cross(final Point a, final Point b, final Point c) {
		return b.subtract(a).cross(c.subtract(a));
	}
	
	double cross(final Point o) {
		return x * o.y - y * o.x;
	}
	
	public Point negate() {
		return new Point(-x, -y);
	}
	
	public double length() {
		return Math.hypot(x, y);
	}
	
	public Matrix22 ThisTransposeMultiplyOtherMultiplyThis(double w) {
		return new Matrix22(
			x * x * w, x * y * w,
			y * x * w, y * y * w 
		);
	}

	Matrix22 ThisTransposeMultiplyOther( final Point o ) {
		return new Matrix22(
			x * o.x, x * o.y,
			y * o.x, y * o.y 
		);
	}

	
	public double multiplyOtherTranspose( final Point o ) {
		return x * o.x + y * o.y;
	}
	
	
	public Point multiply( final Matrix22 o ) {
		return new Point(
			x * o.M11 + y * o.M21, x * o.M12 + y * o.M22
		);
	}
	
	public Point multiply( double o ) {
		return new Point(
			x * o, y * o
		);
	}
	
	static Point average( Point p[] ) {
		double x = 0, y = 0;
		for ( int i = 0; i < p.length; ++i ) {
			x += p[i].x;
			y += p[i].y;
		}
		x /= p.length;
		y /= p.length;
		return new Point( x, y );
	}
	
	public static Point average( Point []p, double []w ) {
/*		for ( int i = 0; i < p.length; ++i ) {
			if ( w[i] > 1e10 ) return p[i];
		}*/
		double sx = 0, sy = 0, sw = 0;
		for ( int i = 0; i < p.length; ++i ) {
			sx += p[i].x * w[i];
			sy += p[i].y * w[i];
			sw += w[i];
		}
		return new Point( sx / sw, sy / sw );
//		return null;
	}
	
	public static double[] TriangleContainsPoint( final Point a, final Point b, final Point c, final Point o) {
		double xa = a.x - o.x, ya = a.y - o.y;
		double xb = b.x - o.x, yb = b.y - o.y;
		double xc = c.x - o.x, yc = c.y - o.y;
		
		double d = xb * yc -    xc * yb;
		double d1 = -xa * yc -  xc * -ya;
		double d2 = xb * -ya - -xa * yb;
		
		return new double[]{1, d1/d, d2/d};
		
//		return null;
	}
	
	/*
	double DistanceTo( final Point o ) {
		return Math.hypot( x - o.x , y - o.y);
	}*/
	
	public double InfintyNormDistanceTo( final Point o ) {
		return Math.max( Math.abs(x - o.x), Math.abs(y - o.y) );
	}
	
	boolean equals( final Point o ) {
		return x == o.x && y == o.y;
	}

	public Point orthogonal() {
		return new Point(-y, x);
	}

	public Point divide(double mus) {
		return this.multiply( 1.0 / mus );
	}
	
	public static Point ZERO = new Point(0, 0);
}

