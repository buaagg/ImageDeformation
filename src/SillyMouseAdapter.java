import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class SillyMouseAdapter extends MouseAdapter {
	public void mouseClicked(MouseEvent e) {
		Point []p = parent.p;
//		System.out.printf( "Silly Clicked on (%d, %d)%n", e.getX(), e.getY() );	
		
		Point q = new Point( e.getX(), e.getY() );
		for ( int i = 0; i < p.length; ++i ) {
			if ( p[i].InfintyNormDistanceTo(q) <= 20 ) {
				return;
			}
		}
		parent.p = new Point[ p.length + 1 ];
		for ( int i = 0; i < p.length; ++i ) {
			parent.p[i] = p[i];
		}
		parent.p[ p.length ] = q;		
		
		parent.draw( parent.img, parent.g );
		
	}
	public void mousePressed(MouseEvent e) {
		Point []p = parent.p;
//		System.out.printf( "Silly Pressed on (%d, %d)%n", e.getX(), e.getY() );	
		
		Point q = new Point( e.getX(), e.getY() );
		for ( int i = 0; i < p.length; ++i ) {
			if ( p[i].InfintyNormDistanceTo(q) <= 20 ) {
				parent.p = new Point[ p.length - 1 ];
				for ( int j = 0, k = 0; j < p.length; ++j ) if ( i != j ) {
					parent.p[k++] = p[j];
				}
				parent.draw( parent.img, parent.p, q, true, parent.g );
				return;
			}
		}
	}
	public void mouseReleased(MouseEvent e) {
//		System.out.printf( "Silly Released on (%d, %d)%n", e.getX(), e.getY() );	
		
		Point q = new Point( e.getX(), e.getY() );
		if ( ok(q) ) {
			Point []p = parent.p;

			parent.p = new Point[ p.length + 1 ];
			for ( int i = 0; i < p.length; ++i ) {
				parent.p[i] = p[i];
			}
			parent.p[ p.length ] = q;		
			
		}
		parent.draw( parent.img, parent.g );			
	}
	
	private boolean ok( Point q ) {
		for ( int i = 0; i < parent.p.length; ++i ) {
			if ( parent.p[i].InfintyNormDistanceTo(q) <= 20 ) {
				return false;
			}
		}		
		return true;
	}
	
	public void mouseDragged(MouseEvent e) {
//		System.out.printf( "Silly Drag on (%d, %d)%n", e.getX(), e.getY() );
		Point q = new Point( e.getX(), e.getY() );
		
		parent.draw( parent.img, parent.p, q, ok(q), parent.g );
	}
	SillyMouseAdapter(Main parent) {
		this.parent = parent;
	}
	Main parent;
}
