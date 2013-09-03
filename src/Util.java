
public class Util {
	static double min( double x, double y, double z ) {
		return Math.min( Math.min(x, y), z);
	}
	static double max( double x, double y, double z ) {
		return Math.max( Math.max(x, y), z);
	}
	static double minX( Point a, Point b, Point c ) {
		return min( a.x, b.x, c.x );
	}
	static double maxX( Point a, Point b, Point c ) {
		return max( a.x, b.x, c.x );
	}
	static double minY( Point a, Point b, Point c ) {
		return min( a.y, b.y, c.y );
	}
	static double maxY( Point a, Point b, Point c ) {
		return max( a.y, b.y, c.y );
	}
}
