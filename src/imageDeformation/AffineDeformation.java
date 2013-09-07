package imageDeformation;
import geometry.Matrix22;
import geometry.Point;


public class AffineDeformation implements ImageDeformation {

	@Override
	public Point query(Point v, Point[] p, Point[] q, double alpha) {
		assert( p.length == q.length );
		
		int n = p.length;
		
		if ( null == w || w.length < n ) w = new double[n];
		if ( null == pRelative || pRelative.length < n ) pRelative = new Point[n];
		if ( null == qRelative || qRelative.length < n ) qRelative = new Point[n];
		if ( null == A || A.length < n ) A = new double[n];
		
		for ( int i = 0; i < n; ++i ) {
			Point t = p[i].subtract( v );
			w[i] = Math.pow( t.x * t.x + t.y * t.y , -alpha );
		}
		Point pAverage = Point.average( p, w );
		Point qAverage = Point.average( q, w );
		
		for ( int i = 0; i < n; ++i ) {
			pRelative[i] = p[i].subtract( pAverage );
			qRelative[i] = q[i].subtract( qAverage );
		}
		
		Matrix22 B = Matrix22.ZERO;
		for ( int i = 0; i < n; ++i ) {
			B = B.add( pRelative[i].ThisTransposeMultiplyOtherMultiplyThis(w[i]) );
		}
		
		B = B.inverse();
		
		for ( int j = 0; j < n; ++j ) {
			A[j] = v.subtract(pAverage).multiply(B).multiplyOtherTranspose(pRelative[j]) * w[j];
		}
		
		Point r = qAverage;
		for ( int j = 0; j < n; ++j ) {
			r = r.add( qRelative[j].multiply(A[j]) );
		}
		
		return r;
	}
	
	double []w = null;
	Point[] pRelative = null;
	Point[] qRelative = null;
	double []A = null;
}
