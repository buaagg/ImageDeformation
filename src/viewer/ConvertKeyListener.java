package viewer;


import geometry.Grid;
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
			
			parent.orginalKeyPoints = parent.currentKeyPoints;
			parent.currentKeyPoints = new Point[ parent.orginalKeyPoints.length ];
			for ( int i = 0; i < parent.orginalKeyPoints.length; ++i )
				parent.currentKeyPoints[i] = parent.orginalKeyPoints[i];

			parent.originalGrid = parent.currentGrid;
			parent.orginalImg = parent.currentImg;
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
				Config.imageDeformation = new AffineDeformation();
				parent.setTitle("AffineDeformation");				
			}
			if ( isKey2 ) {
				Config.imageDeformation = new SimilarityDeformation();
				parent.setTitle("SimilarityDeformation");				
			}
			if ( isKey3 ) {
				Config.imageDeformation = new RigidDeformation();
				parent.setTitle("RigidDeformation");				
			}
			if ( parent.stage == 2 ) {
				Grid []newG = new Grid[ parent.originalGrid.length ];
				for ( int i = 0; i < parent.originalGrid.length; ++i ) {
					newG[i] = new Grid(
							Config.imageDeformation.query( parent.originalGrid[i].p[0] , parent.orginalKeyPoints, parent.currentKeyPoints, Config.alpha),
							Config.imageDeformation.query( parent.originalGrid[i].p[1] , parent.orginalKeyPoints, parent.currentKeyPoints, Config.alpha),
							Config.imageDeformation.query( parent.originalGrid[i].p[2] , parent.orginalKeyPoints, parent.currentKeyPoints, Config.alpha),
							Config.imageDeformation.query( parent.originalGrid[i].p[3] , parent.orginalKeyPoints, parent.currentKeyPoints, Config.alpha)
					);
				}
				
				parent.currentGrid = newG;
				
				parent.currentImg = Config.binlinearInterpolation.generate(parent.currentImg, parent.orginalImg, parent.originalGrid, parent.currentGrid);		

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

