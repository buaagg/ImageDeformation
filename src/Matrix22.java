
public class Matrix22 {
	final double M11, M12;
	final double M21, M22;
	
	Matrix22( 
			double N11, double N12,
			double N21, double N22) {
		M11 = N11; M12 = N12;
		M21 = N21; M22 = N22;
	}
	
	Matrix22 Adjugate() {
		return new Matrix22(
			M22, -M12,
			-M21, M11
		);
	}	
	
	double Determinant() {
		return M11 * M22 - M12 * M21;
	}
	
	Matrix22 divide(double d) {
		double m = 1.0 / d;
		return multiply(m);
	}
	
	Matrix22 multiply(double m) {
		return new Matrix22(
			M11 * m, M12 * m,
			M21 * m, M22 * m
		);
	}
	
	Matrix22 multiply( final Matrix22 o ) {
		return new Matrix22(
				M11 * o.M11 + M12 * o.M21, M11 * o.M12 + M12 * o.M22,
				M21 * o.M11 + M22 * o.M21, M21 * o.M12 + M22 * o.M22 
		);
	}
	
	Matrix22 add(Matrix22 o) {
		return new Matrix22(
			M11 + o.M11, M12 + o.M12,
			M21 + o.M21, M22 + o.M22
		);
	}
	
	Matrix22 Inverse() {
		return this.Adjugate().divide( Determinant() );
	}
	
	boolean equals( final Matrix22 o ) {
		return Math.abs( M11 - o.M11 ) < Config.eps
				&& Math.abs( M12 - o.M12 ) < Config.eps
				&& Math.abs( M21 - o.M21 ) < Config.eps
				&& Math.abs( M22 - o.M22 ) < Config.eps;
	}
	
	static Matrix22 ZERO = new Matrix22(0, 0, 0, 0);
	static Matrix22 E = new Matrix22(1, 0, 0, 1);

	public void print() {
		System.out.printf("%.2f %.2f%n", M11, M12);
		System.out.printf("%.2f %.2f%n", M21, M22);
	}
}
