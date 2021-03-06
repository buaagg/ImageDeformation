package viewer;

import geometry.Lattice;
import geometry.Point;
import imageDeformation.AffineDeformation;
import imageDeformation.RigidDeformation;
import imageDeformation.SimilarityDeformation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class ConvertKeyListener implements KeyListener {
	private Main parent;
	ConvertKeyListener( Main parent ) {
		this.parent = parent;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		if ( e.getKeyChar() == KeyEvent.VK_ENTER && parent.stage == 1 ) {
			parent.stage = 2;
			System.out.println("Convert from stage 1 to stage 2");
			parent.label.removeMouseListener( parent.stage1MouseAdapter );
			parent.label.removeMouseMotionListener( parent.stage1MouseAdapter );
			
			parent.label.addMouseListener( parent.stage2MouseAdapter );
			parent.label.addMouseMotionListener( parent.stage2MouseAdapter );
			
			do {
				parent.originalKeyPoints = parent.currentKeyPoints;
				parent.currentKeyPoints = new Point[ parent.originalKeyPoints.length ];
				for ( int i = 0; i < parent.originalKeyPoints.length; ++i )
					parent.currentKeyPoints[i] = parent.originalKeyPoints[i];
			} while (false);
			
			parent.originalLattice = new Lattice( parent.currentLattice );
			parent.originalImage = parent.currentImage;
		}

		if ( e.getKeyChar() == KeyEvent.VK_SPACE ) {
			parent.isGridVisible = !parent.isGridVisible;
			parent.draw( ); 
		}
		
		boolean isKey1 = ( e.getKeyChar() == KeyEvent.VK_NUMPAD1 || e.getKeyChar() == KeyEvent.VK_1 );
		boolean isKey2 = ( e.getKeyChar() == KeyEvent.VK_NUMPAD2 || e.getKeyChar() == KeyEvent.VK_2 );
		boolean isKey3 = ( e.getKeyChar() == KeyEvent.VK_NUMPAD3 || e.getKeyChar() == KeyEvent.VK_3 );
		
		if ( isKey1 || isKey2 || isKey3 ) {
			if ( isKey1 ) {
				javaHelper.DeformationHelper.setDeformationHandler(new AffineDeformation());
				parent.setTitle("AffineDeformation");				
			}
			if ( isKey2 ) {
				javaHelper.DeformationHelper.setDeformationHandler(new SimilarityDeformation());
				parent.setTitle("SimilarityDeformation");				
			}
			if ( isKey3 ) {
				javaHelper.DeformationHelper.setDeformationHandler(new RigidDeformation());
				parent.setTitle("RigidDeformation");				
			}
			if ( parent.stage == 2 ) {
				parent.currentLattice = javaHelper.DeformationHelper.transform(parent.currentLattice, parent.originalLattice, parent.originalKeyPoints, parent.currentKeyPoints);				
				parent.currentImage = Config.binlinearInterpolation.generate(parent.currentImage, parent.originalImage, parent.originalLattice, parent.currentLattice);		

				parent.draw( );		
			}

		}		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
}

