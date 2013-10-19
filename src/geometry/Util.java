package geometry;


public class Util {
	static public final double eps = 1e-5;
	
	static double min( double x, double y, double z ) {
		return Math.min( Math.min(x, y), z);
	}
	static double max( double x, double y, double z ) {
		return Math.max( Math.max(x, y), z);
	}
	public static double minX( Point a, Point b, Point c ) {
		return min( a.x, b.x, c.x );
	}
	public static double maxX( Point a, Point b, Point c ) {
		return max( a.x, b.x, c.x );
	}
	public static double minY( Point a, Point b, Point c ) {
		return min( a.y, b.y, c.y );
	}
	public static double maxY( Point a, Point b, Point c ) {
		return max( a.y, b.y, c.y );
	}
	public static double maxX( Point []p ) {
		double r = Double.MIN_VALUE;
		for ( int i = 0; i < p.length; ++i ) r = Math.max(r, p[i].x);
		return r;
	}
	public static double maxY( Point []p ) {
		double r = Double.MIN_VALUE;
		for ( int i = 0; i < p.length; ++i ) r = Math.max(r, p[i].y);
		return r;
	}
	public static double minX( Point []p ) {
		double r = Double.MAX_VALUE;
		for ( int i = 0; i < p.length; ++i ) r = Math.min(r, p[i].x);
		return r;
	}
	public static double minY( Point []p ) {
		double r = Double.MAX_VALUE;
		for ( int i = 0; i < p.length; ++i ) r = Math.min(r, p[i].y);
		return r;
	}
}
