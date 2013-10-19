package geometry;

public class Grid {
	public Point []p;
	
	public Grid(Point p0, Point p1, Point p2, Point p3) {
		p = new Point[4];
		p[0] = p0; p[1] = p1; p[2] = p2; p[3] = p3;
	}
	
	public String toString() {
		String s = "{";
		for ( int i = 0; i < p.length; ++i ) {
			s += p[i].toString();
		}
		s += "}";
		return s;
	}
	
	public boolean contains(int i, int j) {
		Point q = new Point(i, j);
		double x1 = Point.cross(p[0], p[1], q);
		double x2 = Point.cross(p[1], p[2], q);
		double x3 = Point.cross(p[2], p[3], q);
		double x4 = Point.cross(p[3], p[0], q);
		
		if ( x1 * x2 < 0 ) return false;
		if ( x1 * x3 < 0 ) return false;
		if ( x1 * x4 < 0 ) return false;
		return true;
	}
}
