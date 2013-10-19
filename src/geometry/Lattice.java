package geometry;

public class Lattice {
	int xCount, yCount;
	Point []data;
	
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
	
	public int []getSize() {
		return new int[]{xCount, yCount};
	}
	
	public Lattice(int []size) {
		if ( size.length != 2 ) System.out.println( "Invalid Lattice Size" );
		xCount = size[0];
		yCount = size[1];
		data = new Point[xCount * yCount];
	}
}
