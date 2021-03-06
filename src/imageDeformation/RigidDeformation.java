package imageDeformation;

import geometry.Matrix22;
import geometry.Point;

public class RigidDeformation implements ImageDeformation {

	@Override
	public Point query(Point v, Point[] p, Point[] q, double alpha) {
		int n = p.length;
		if ( null == w || w.length < n ) w = new double[n];
		if ( null == pRelative || pRelative.length < n ) pRelative = new Point[n];
		if ( null == qRelative || qRelative.length < n ) qRelative = new Point[n];
		if ( null == A || A.length < n ) A = new Matrix22[n];
		
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
		
		Matrix22 Aright = (new Matrix22(
				v.subtract(pAverage),
				v.subtract(pAverage).orthogonal().negate()
		)).transpose();
		
		for ( int i = 0; i < n; ++i ) {
			Matrix22 Aleft = new Matrix22(
					pRelative[i],
					pRelative[i].orthogonal().negate()
			);		
			A[i] = Aleft.multiply( Aright ).multiply( w[i] );
		}
		
		Point fr = Point.ZERO;
		for ( int i = 0; i < n; ++i ) {
			fr = fr.add( qRelative[i].multiply(A[i]) );
		}
		
		Point r = fr.multiply( v.subtract(pAverage).length() / fr.length() ).add( qAverage );
		
		return r;
	}
	
	double []w = null;
	Point[] pRelative = null;
	Point[] qRelative = null;
	Matrix22 []A = null;
}
