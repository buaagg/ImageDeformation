package viewer;
import geometry.Point;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Stage1MouseAdapter extends MouseAdapter {
	public void mouseClicked(MouseEvent e) {
		if ( javax.swing.SwingUtilities.isLeftMouseButton(e) ) {
			Point []p = parent.currentKeyPoints;
	//		System.out.printf( "Silly Clicked on (%d, %d)%n", e.getX(), e.getY() );	
			
			Point q = new Point( e.getX(), e.getY() );
			for ( int i = 0; i < p.length; ++i ) {
				if ( p[i].InfintyNormDistanceTo(q) <= 20 ) {
					return;
				}
			}
			parent.currentKeyPoints = new Point[ p.length + 1 ];
			for ( int i = 0; i < p.length; ++i ) {
				parent.currentKeyPoints[i] = p[i];
			}
			parent.currentKeyPoints[ p.length ] = q;		
			
			parent.draw(  );
		}		
	}
	public void mousePressed(MouseEvent e) {
		if ( javax.swing.SwingUtilities.isLeftMouseButton(e) ) {

			Point []p = parent.currentKeyPoints;
	//		System.out.printf( "Silly Pressed on (%d, %d)%n", e.getX(), e.getY() );	
			
			Point q = new Point( e.getX(), e.getY() );
			for ( int i = 0; i < p.length; ++i ) {
				if ( p[i].InfintyNormDistanceTo(q) <= 20 ) {
					parent.currentKeyPoints = new Point[ p.length - 1 ];
					for ( int j = 0, k = 0; j < p.length; ++j ) if ( i != j ) {
						parent.currentKeyPoints[k++] = p[j];
					}
					parent.draw( q, true );
					return;
				}
			}
		}
	}
	public void mouseReleased(MouseEvent e) {
//		System.out.printf( "Silly Released on (%d, %d)%n", e.getX(), e.getY() );	
		if ( javax.swing.SwingUtilities.isLeftMouseButton(e) ) {		
			Point q = new Point( e.getX(), e.getY() );
			if ( ok(q) ) {
				Point []p = parent.currentKeyPoints;
	
				parent.currentKeyPoints = new Point[ p.length + 1 ];
				for ( int i = 0; i < p.length; ++i ) {
					parent.currentKeyPoints[i] = p[i];
				}
				parent.currentKeyPoints[ p.length ] = q;		
				
			}
			parent.draw(  );			
		}
	}
	
	private boolean ok( Point q ) {
		for ( int i = 0; i < parent.currentKeyPoints.length; ++i ) {
			if ( parent.currentKeyPoints[i].InfintyNormDistanceTo(q) <= 20 ) {
				return false;
			}
		}		
		return true;
	}
	
	public void mouseDragged(MouseEvent e) {
		if ( javax.swing.SwingUtilities.isLeftMouseButton(e) ) {
	//		System.out.printf( "Silly Drag on (%d, %d)%n", e.getX(), e.getY() );
			Point q = new Point( e.getX(), e.getY() );
			
			parent.draw( q, ok(q) );
		}
	}
	Stage1MouseAdapter(Main parent) {
		this.parent = parent;
	}
	Main parent;
}
