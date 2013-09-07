import geometry.Grid;
import geometry.Point;
import geometry.Util;
import imageDeformation.AffineDeformation;
import imageDeformation.ImageDeformation;
import imageDeformation.RigidDeformation;
import imageDeformation.SimilarityDeformation;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

class GMouseAdapter extends MouseAdapter {
	public void mousePressed(MouseEvent e) {		
		Point []q = parent.q;
		
		Point r = new Point( e.getX(), e.getY() );
		for ( int i = 0; i < q.length; ++i ) {
			if ( q[i].InfintyNormDistanceTo(r) <= 20 ) {
				currentPointIndex = i;
				parent.draw( parent.img, parent.q, null, true, parent.gTrans );
				return;
			}
		}
	}
		
	void work(Point q) {		
		parent.q[currentPointIndex] = q;
		
		Grid []newG = new Grid[ parent.g.length ];
		for ( int i = 0; i < parent.g.length; ++i ) {
			newG[i] = new Grid(
					Config.imageDeformation.query( parent.g[i].p[0] , parent.p, parent.q, Config.alpha),
					Config.imageDeformation.query( parent.g[i].p[1] , parent.p, parent.q, Config.alpha),
					Config.imageDeformation.query( parent.g[i].p[2] , parent.p, parent.q, Config.alpha),
					Config.imageDeformation.query( parent.g[i].p[3] , parent.p, parent.q, Config.alpha)
			);
		}
		
		parent.gTrans = newG;
		
		BufferedImage newImg = Config.binlinearInterpolation.generate(parent.img, parent.oriImg, parent.g, parent.gTrans);		
		
		parent.draw( newImg, parent.q, q, true, parent.gTrans );		
	}
	public void mouseDragged(MouseEvent e) {
		Point r = new Point( e.getX(), e.getY() );
		work(r);
	}
	public void mouseReleased(MouseEvent e) {
		if ( this.currentPointIndex != -1 ) {
			Point r = new Point( e.getX(), e.getY() );
			work(r);			
		}
		this.currentPointIndex = -1;
	}

	GMouseAdapter(Main parent) {
		this.parent = parent;
	}
	
	private Main parent;
	private int currentPointIndex = -1;
	
}
