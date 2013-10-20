package javaHelper;

import imageDeformation.AffineDeformation;
import imageDeformation.ImageDeformation;
import geometry.Lattice;
import geometry.Point;

public class DeformationHelper {	
	static ImageDeformation imageDeformationHandler = new AffineDeformation();
	static double alpha = 1;
	
	public static void setDeformationHandler( ImageDeformation handler ) {
		imageDeformationHandler = handler;
	}
	
	public static void setAlpha(double alph) {
		alpha = alph;
	}
			
	public static Lattice transform(Lattice targetLattice, final Lattice sourceLattice, final Point []originalKeyPoints, final Point []currentKeyPoints) {
		if ( targetLattice == null || targetLattice == sourceLattice || 
				targetLattice.getXCount() != sourceLattice.getXCount() || targetLattice.getYCount() != sourceLattice.getYCount() ) {
			targetLattice = new Lattice(sourceLattice);
		} 
		long startTime = System.nanoTime();
		for ( int k = 0; k < sourceLattice.getSize(); ++k ) {
			targetLattice.data[k] = imageDeformationHandler.query( sourceLattice.data[k], originalKeyPoints, currentKeyPoints, alpha);
		}
		long finishTime = System.nanoTime();		
		System.out.printf( "Time of Deformation = %.2f ms%n", (finishTime - startTime) * 1e-6 );
		return targetLattice;
	}
}
