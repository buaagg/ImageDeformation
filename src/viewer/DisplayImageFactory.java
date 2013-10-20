package viewer;

import geometry.Lattice;
import geometry.Point;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javaHelper.BufferedImageHelper;

public class DisplayImageFactory {
	static private BufferedImage displayImg = null;

	public static BufferedImage getDisplayImage(BufferedImage currentImg, Lattice currentLattice, Point []currentKeyPoints, 
			Point activeKeyPoint, boolean isActiveKeyPointSpecial, boolean isGridVisible) 
	{		
		if ( displayImg == null || displayImg.getWidth() != currentImg.getWidth() || displayImg.getHeight() != currentImg.getHeight() ) {
			displayImg = BufferedImageHelper.newBufferdImage(currentImg.getWidth(), currentImg.getHeight() );
		} else {
			for ( int i = 0; i < displayImg.getWidth(); ++i ) {
				for ( int j = 0; j < displayImg.getHeight() ; ++j ) {
					displayImg.setRGB(i, j, 0);
				}
			}
		}

		Graphics2D graph = (Graphics2D) displayImg.getGraphics();
		
		graph.drawImage( currentImg,  0, 0, null );
		graph.setStroke( new BasicStroke((2.0f) ));
		graph.setColor( Color.RED );

		for ( int i = 0; i < currentKeyPoints.length; ++i ) {
			graph.drawLine( (int)(currentKeyPoints[i].x - 10), (int)(currentKeyPoints[i].y - 10), 
							(int)(currentKeyPoints[i].x + 10), (int)(currentKeyPoints[i].y + 10));
			graph.drawLine( (int)(currentKeyPoints[i].x + 10), (int)(currentKeyPoints[i].y - 10), 
							(int)(currentKeyPoints[i].x - 10), (int)(currentKeyPoints[i].y + 10));
		}
		if ( activeKeyPoint != null ) {
			if ( !isActiveKeyPointSpecial ) graph.setColor( Color.BLUE );
			graph.drawLine( (int)(activeKeyPoint.x - 10), (int)(activeKeyPoint.y - 10), 
					(int)(activeKeyPoint.x + 10), (int)(activeKeyPoint.y + 10));
			graph.drawLine( (int)(activeKeyPoint.x + 10), (int)(activeKeyPoint.y - 10), 
					(int)(activeKeyPoint.x - 10), (int)(activeKeyPoint.y + 10));
			
		}
		do {
			graph.setColor( Color.BLACK );
			graph.drawLine(0, 0, 0, displayImg.getHeight() );			
			graph.drawLine(0, displayImg.getHeight(), displayImg.getWidth(), displayImg.getHeight() );			
			graph.drawLine(displayImg.getWidth(), displayImg.getHeight(), displayImg.getWidth(), 0 );			
			graph.drawLine(displayImg.getWidth(), 0, 0, 0 );			
		} while (false);
		
		if ( currentLattice != null && isGridVisible ) {
			graph.setStroke( new BasicStroke((1.0f) ));
			graph.setColor( Color.gray );
			for ( int i = 0; i < currentLattice.getXCount(); ++i ) {
				for ( int j = 0; j < currentLattice.getYCount(); ++j ) {
					if ( i - 1 >= 0 ) graph.drawLine( 
							(int)currentLattice.data[(i-1) * currentLattice.getYCount() + j].x, (int)currentLattice.data[(i-1) * currentLattice.getYCount() + j].y, 
							(int)currentLattice.data[i * currentLattice.getYCount() + j].x, (int)currentLattice.data[i * currentLattice.getYCount() + j].y);
					if ( j - 1 >= 0 ) graph.drawLine( 
							(int)currentLattice.data[i * currentLattice.getYCount() + (j-1)].x, (int)currentLattice.data[i * currentLattice.getYCount() + (j-1)].y, 
							(int)currentLattice.data[i * currentLattice.getYCount() + j].x, (int)currentLattice.data[i * currentLattice.getYCount() + j].y);
				}
			}
		}
		
		graph.dispose();
		
		return displayImg;
	}	
}
