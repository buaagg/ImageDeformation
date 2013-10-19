package viewer;

import geometry.Grid;
import geometry.Point;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javaHelper.BufferedImageHelper;

public class DisplayImageFactory {
	static private BufferedImage displayImg = null;

	public static BufferedImage getDisplayImage(BufferedImage currentImg, Grid []currentGrid, Point []currentKeyPoints, 
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
		
		if ( currentGrid != null && isGridVisible ) {
			graph.setStroke( new BasicStroke((1.0f) ));
			graph.setColor( Color.GRAY );
			for ( Grid g : currentGrid ) {
				graph.drawLine(  (int)g.p[0].x , (int)g.p[0].y, (int)g.p[1].x, (int)g.p[1].y);
				graph.drawLine(  (int)g.p[1].x , (int)g.p[1].y, (int)g.p[2].x, (int)g.p[2].y);
				graph.drawLine(  (int)g.p[2].x , (int)g.p[2].y, (int)g.p[3].x, (int)g.p[3].y);
				graph.drawLine(  (int)g.p[3].x , (int)g.p[3].y, (int)g.p[0].x, (int)g.p[0].y);				
			}
		}
		
		graph.dispose();
		
		return displayImg;
	}	
}
