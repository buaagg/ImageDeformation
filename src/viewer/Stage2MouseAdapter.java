package viewer;
import geometry.Grid;
import geometry.Lattice;
import geometry.Point;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Stage2MouseAdapter extends MouseAdapter {
	
	public void mouseClicked(MouseEvent e) {	
	}
	
	public void mousePressed(MouseEvent e) {		
		if ( javax.swing.SwingUtilities.isLeftMouseButton(e) ) {
			Point []q = parent.currentKeyPoints;
			
			Point r = new Point( e.getX(), e.getY() );
			for ( int i = 0; i < q.length; ++i ) {
				if ( q[i].InfintyNormDistanceTo(r) <= 20 ) {
					currentPointIndex = i;
					parent.draw(null, true );
					return;
				}
			}
		}
	}
	
	boolean gridValid(Grid[] g, int gridCountX, int gridCountY) {
		for (int i = 0; i < gridCountX; ++i) {
			for (int j = 0; j < gridCountY; ++j) {
				
			}
		}
		return true;
	}
		
	void work(Point q) {		
		parent.currentKeyPoints[currentPointIndex] = q;
		
		Grid []newG = new Grid[ parent.originalGrid.length ];
		for ( int i = 0; i < parent.originalGrid.length; ++i ) {
			newG[i] = new Grid(
					Config.imageDeformation.query( parent.originalGrid[i].p[0] , parent.orginalKeyPoints, parent.currentKeyPoints, Config.alpha),
					Config.imageDeformation.query( parent.originalGrid[i].p[1] , parent.orginalKeyPoints, parent.currentKeyPoints, Config.alpha),
					Config.imageDeformation.query( parent.originalGrid[i].p[2] , parent.orginalKeyPoints, parent.currentKeyPoints, Config.alpha),
					Config.imageDeformation.query( parent.originalGrid[i].p[3] , parent.orginalKeyPoints, parent.currentKeyPoints, Config.alpha)
			);
		}
		
		Lattice nextLattice = new Lattice( parent.currentLattice.getSize() );
		
		parent.currentGrid = newG;		
		parent.currentImg = Config.binlinearInterpolation.generate(parent.currentImg, parent.orginalImg, parent.originalGrid, parent.currentGrid);		
		
		parent.draw(q, true );		
	}
	public void mouseDragged(MouseEvent e) {
		if ( javax.swing.SwingUtilities.isLeftMouseButton(e) ) {
			if ( currentPointIndex != -1 ) {
				Point r = new Point( e.getX(), e.getY() );
				work(r);
			}
		}
	}
	public void mouseReleased(MouseEvent e) {
		if ( javax.swing.SwingUtilities.isLeftMouseButton(e) ) {
			if ( this.currentPointIndex != -1 ) {
				Point r = new Point( e.getX(), e.getY() );
				work(r);			
			}
			this.currentPointIndex = -1;
		}
	}

	Stage2MouseAdapter(Main parent) {
		this.parent = parent;
	}
	
	private Main parent;
	private int currentPointIndex = -1;
	
}
