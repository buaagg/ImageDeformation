package imageDeformation;

import geometry.Matrix22;
import geometry.Point;

public class SimilarityDeformation implements ImageDeformation {

	@Override
	public Point query(Point v, Point[] p, Point[] q, double alpha) {
		assert( p.length == q.length );
		
		int n = p.length;
		if ( null == w || w.length < n ) w = new double[n];
		if ( null == pRelative || pRelative.length < n ) pRelative = new Point[n];
		if ( null == qRelative || qRelative.length < n ) qRelative = new Point[n];
		if ( null == A || A.length < n ) A = new Matrix22[n];
		
		for ( int i = 0; i < n; ++i ) {
			double tx = p[i].x - v.x;
			double ty = p[i].y - v.y;
			w[i] = Math.pow( tx * tx + ty * ty , -alpha );
		}
		Point pAverage = Point.average( p, w );
		Point qAverage = Point.average( q, w );
		
		for ( int i = 0; i < n; ++i ) {
			pRelative[i] = p[i].subtract( pAverage );
			qRelative[i] = q[i].subtract( qAverage );
		}
		
		double mus = 0;
		for ( int i = 0; i < n; ++i ) {
			mus += w[i] * (pRelative[i].x * pRelative[i].x + pRelative[i].y * pRelative[i].y);
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
		
		Point r = qAverage;
		for ( int i = 0; i < n; ++i ) {
			r = r.add( qRelative[i].multiply( A[i] ).divide(mus) );
		}
		
		return r;
	}

	double []w = null;
	Point[] pRelative = null;
	Point[] qRelative = null;
	Matrix22 []A = null;
}
