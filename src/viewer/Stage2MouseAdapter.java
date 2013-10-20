package viewer;

import geometry.Lattice;
import geometry.Point;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Stage2MouseAdapter extends MouseAdapter {
	
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
	
	boolean gridValid(Lattice nextLattice, int gridCountX, int gridCountY) {
		for (int i = 0; i < gridCountX; ++i) {
			for (int j = 0; j < gridCountY; ++j) {
				
			}
		}
		return true;
	}
		
	void work(Point q) {		
		parent.currentKeyPoints[currentPointIndex] = q;		

		Lattice futureLattice = javaHelper.DeformationHelper.transform(null, parent.originalLattice, parent.originalKeyPoints, parent.currentKeyPoints);
		if ( futureLattice.isValid() ) {
			parent.currentLattice = futureLattice;
			parent.currentImage = Config.binlinearInterpolation.generate(parent.currentImage, parent.originalImage, parent.originalLattice, parent.currentLattice);				
			parent.draw(q, true );		
		} else {
			parent.draw(q, false );					
		}
		
//		parent.currentLattice = javaHelper.DeformationHelper.transform(parent.currentLattice, parent.originalLattice, parent.originalKeyPoints, parent.currentKeyPoints);
//		parent.currentImage = Config.binlinearInterpolation.generate(parent.currentImage, parent.originalImage, parent.originalLattice, parent.currentLattice);				

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
