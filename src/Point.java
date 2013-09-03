public class Point {
	public final double x, y;
	
	Point(double x, double y) {
		this.x = x;
		this.y = y;		
	}
	
	Point add( final Point o ) {
		return new Point( x + o.x, y + o.y );
	}
	
	Point subtract( final Point o ) {
		return new Point( x - o.x, y - o.y );
	}
	
	static double cross(final Point a, final Point b, final Point c) {
		return b.subtract(a).cross(c.subtract(a));
	}
	
	double cross(final Point o) {
		return x * o.y - y * o.x;
	}
	
	Matrix22 ThisTransposeMultiplyOtherMultiplyThis(double w) {
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

	
	double multiplyOtherTranspose( final Point o ) {
		return x * o.x + y * o.y;
	}
	
	
	Point multiply( final Matrix22 o ) {
		return new Point(
			x * o.M11 + y * o.M21, x * o.M12 + y * o.M22
		);
	}
	
	Point multiply( double o ) {
		return new Point(
			x * o, y * o
		);
	}
	
	static Point average( Point []p, double []w ) {
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
	/*
	double DistanceTo( final Point o ) {
		return Math.hypot( x - o.x , y - o.y);
	}*/
	
	double InfintyNormDistanceTo( final Point o ) {
		return Math.max( Math.abs(x - o.x), Math.abs(y - o.y) );
	}
	
	boolean equals( final Point o ) {
		return x == o.x && y == o.y;
	}

}

