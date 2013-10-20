package geometry;

public class Lattice {
	int xCount, yCount;
	public final Point []data;
	
	public Lattice(int width, int height, int length) {
		java.util.Vector<Point> v = new java.util.Vector<>();
		xCount = 0;
		for ( int i = 0; i < width; i += length ) {
			++xCount;
			for ( int j = 0; j < height; j += length ) {
				v.add( new Point(i, j) );
			}
		}
		yCount = v.size() / xCount;
		data = v.toArray( new Point[v.size()] );
	}
	
	public Lattice(final Lattice other) {
		xCount = other.xCount;
		yCount = other.yCount;
		data = new Point[xCount * yCount];
		for ( int i = 0; i < data.length; ++i )
			data[i] = other.data[i];
	}
	
	public boolean isValid() {
		for ( int i = 1; i < xCount - 1; ++i ) {
			for ( int j = 0; j < yCount - 1; ++j ) {
				double x1 = Point.cross( get(i, j), get(i+1, j), get(i, j+1) );
				double x2 = Point.cross( get(i, j), get(i, j+1), get(i-1, j) );
				if ( Math.signum(x1) * Math.signum(x2) < 0 ) return false;
			}
		}
		for ( int i = 0; i < xCount - 1; ++i ) {
			for ( int j = 1; j < yCount - 1; ++j ) {
				double x1 = Point.cross( get(i, j), get(i, j+1), get(i+1, j) );
				double x2 = Point.cross( get(i, j), get(i+1, j), get(i, j-1) );
				if ( Math.signum(x1) * Math.signum(x2) < 0 ) return false;
			}
		}
		return true;
	}
	
	public Point get(int i, int j) {
		return data[ i * yCount + j ];
	}
	
	public Point []getRect(int i, int j) {
		return new Point[]{ data[(i-1) * yCount + (j-1)], data[(i-1) * yCount + (j)], data[(i) * yCount + (j)], data[(i) * yCount + (j-1)]};
	}
		
	public int getXCount() {
		return xCount;
	}
	
	public int getYCount() {
		return yCount;
	}
	
	public int getSize() {
		return xCount * yCount;
	}
	
}
