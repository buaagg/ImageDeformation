


public class AffineDeformation implements Deformation {

	@Override
	public Point query(Point v, Point[] p, Point[] q, double alpha) {
		assert( p.length == q.length );
		
		int n = p.length;
		double []w = new double[n];
		Point []pRelative = new Point[n];
		Point []qRelative = new Point[n];
		double []A = new double[n];
		
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
		
//		assert( B.Inverse().multiply( B ).equals( Matrix22.E ) );
		
		B = B.Inverse();
		
		/*
		do {
			Matrix22 C = Matrix22.ZERO;
			for ( int j = 0; j < n; ++j ) {
				C = C.add( pRelative[j].ThisTransposeMultiplyOther(qRelative[j]).multiply(w[j]) );
			}
			Matrix22 M = B.multiply(C);
			assert( M.equals(M.E) );
//			M.print();
		} while (false);*/
		
		for ( int j = 0; j < n; ++j ) {
			A[j] = v.subtract(pAverage).multiply(B).multiplyOtherTranspose(pRelative[j]) * w[j];
		}
		
		Point r = qAverage;
		for ( int j = 0; j < n; ++j ) {
			r = r.add( qRelative[j].multiply(A[j]) );
		}
		
		return r;
	}
}
