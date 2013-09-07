import geometry.Grid;
import geometry.Point;
import imageDeformation.AffineDeformation;
import imageDeformation.RigidDeformation;
import imageDeformation.SimilarityDeformation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;


public class Main extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5773907634885296879L;
	public boolean fDrawGrid = true;
	public GMouseAdapter gMouseAdapter = new GMouseAdapter(this);
	public SillyMouseAdapter sillyMouseAdapter = new SillyMouseAdapter(this);
	public ConvertKeyListener convertKeyListener = new ConvertKeyListener(this);
	BufferedImage img;
	BufferedImage oriImg;
	public JLabel label;
	public int status = 0;
	Point []p = new Point[0];
	Point []q;
	Grid []g, gTrans, lastGrid = null;
	
	public Main() {
		String osName = System.getProperty("os.name");
		System.out.println( osName );
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setTitle("AffineDeformation");
		
		JFileChooser fileChooser = new JFileChooser( "." );
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter (
			new FileFilter() {
				String []acceptExtensions = {
					"jpg",
					"png",
				};
				
				private boolean judge(String fileName) {
					for ( String s : acceptExtensions ) {
						if ( fileName.endsWith(s) ) return true;
					}
					return false;
				}
				
				public boolean accept(File file) {					
					if ( judge(file.getName()) || file.isDirectory() ) {
//						System.out.println( file.getName() );
						return true;
					} else {
						return false;						
					}
				}
				public String getDescription() {
					return "jpg|png files";
				}
			}
		);
		String FileName = "";
		if ( fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			FileName = fileChooser.getSelectedFile().getAbsolutePath();
		} else {
			System.exit(HIDE_ON_CLOSE);
		}
		
		System.out.println( FileName );
		
		BufferedImage src = null;
		try {			
			src = ImageIO.read( new File(FileName) );
		} catch (IOException e) {
			;
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();		
		double scale = Math.min( 
				(double)screenSize.width / src.getWidth(),
				(double)screenSize.height / src.getHeight()
		);
		
		scale = Math.min( 1.0 ,  scale * 0.9 );
		
//		scale *= 0.5; //TODO
		
		System.out.println( "scale = " + scale );
		
		
		img = new BufferedImage( 
				(int)(src.getWidth() * scale), 
				(int)(src.getHeight() * scale), 
				BufferedImage.TYPE_INT_ARGB );
		
		Graphics2D graph = (Graphics2D) img.getGraphics();
		graph.scale(scale, scale);
		graph.drawImage( src, 0, 0, null );
		graph.dispose();
		
		label = new JLabel( new ImageIcon(img) );

		JPanel panel = new JPanel();
		panel.setSize( img.getWidth() ,  img.getHeight() );
		panel.add( label );
		
		this.getContentPane().add( panel );
		this.setSize( img.getWidth() + 30, img.getHeight() + 50  );  
		this.setVisible(true);  		
		
//		g = new Grid[ (img.getWidth() / 10) * ( img.getHeight() / 10) ];
		Vector<Grid> vg = new Vector<Grid>();
		for ( int i = 0; i < img.getWidth(); i += Config.gridLength ) {
			for ( int j = 0; j < img.getHeight(); j += Config.gridLength ) {
				vg.add(
						new Grid( 
								new Point(i, j), 
								new Point(i + Config.gridLength, j),
								new Point(i + Config.gridLength, j + Config.gridLength), 
								new Point(i, j + Config.gridLength)
						)
				);
			}
		}
		g = vg.toArray( new Grid[ vg.size() ] );
		System.out.println(g.length);
		
//		label.addMouseListener( gMouseAdapter );
//		label.addMouseMotionListener(gMouseAdapter);	
		
		this.draw( this.img, this.g );
		
		label.addMouseListener( sillyMouseAdapter );
		label.addMouseMotionListener( sillyMouseAdapter );
		this.addKeyListener( convertKeyListener );
	}
	
	public void draw(BufferedImage img, Grid []grid) {
		draw( img, this.p, null, false, grid );
	}
	
	public void draw(BufferedImage img, Point p[], Point q, boolean qOK, Grid []grid) {
		this.img = img;
		
		img = new BufferedImage(img.getWidth(), img.getHeight(), 
				BufferedImage.TYPE_INT_ARGB );

		Graphics2D graph = (Graphics2D) img.getGraphics();
		
		graph.drawImage( this.img,  0, 0, null );
		graph.setStroke( new BasicStroke((2.0f) ));
		graph.setColor( Color.RED );
//		System.out.println( p.length );
		for ( int i = 0; i < p.length; ++i ) {
			graph.drawLine( (int)(p[i].x - 10), (int)(p[i].y - 10), 
							(int)(p[i].x + 10), (int)(p[i].y + 10));
			graph.drawLine( (int)(p[i].x + 10), (int)(p[i].y - 10), 
							(int)(p[i].x - 10), (int)(p[i].y + 10));
		}
		if ( q != null ) {
			if ( !qOK ) graph.setColor( Color.BLUE );
			graph.drawLine( (int)(q.x - 10), (int)(q.y - 10), 
					(int)(q.x + 10), (int)(q.y + 10));
			graph.drawLine( (int)(q.x + 10), (int)(q.y - 10), 
					(int)(q.x - 10), (int)(q.y + 10));
			
		}
		do {
			graph.setColor( Color.BLACK );
			graph.drawLine(0, 0, 0, img.getHeight() );			
			graph.drawLine(0, img.getHeight(), img.getWidth(), img.getHeight() );			
			graph.drawLine(img.getWidth(), img.getHeight(), img.getWidth(), 0 );			
			graph.drawLine(img.getWidth(), 0, 0, 0 );			
		} while (false);
		
		lastGrid = grid;
		if ( grid != null && fDrawGrid ) {
			graph.setStroke( new BasicStroke((1.0f) ));
			graph.setColor( Color.GRAY );
			for ( Grid g : grid ) {
				graph.drawLine(  (int)g.p[0].x , (int)g.p[0].y, (int)g.p[1].x, (int)g.p[1].y);
				graph.drawLine(  (int)g.p[1].x , (int)g.p[1].y, (int)g.p[2].x, (int)g.p[2].y);
				graph.drawLine(  (int)g.p[2].x , (int)g.p[2].y, (int)g.p[3].x, (int)g.p[3].y);
				graph.drawLine(  (int)g.p[3].x , (int)g.p[3].y, (int)g.p[0].x, (int)g.p[0].y);
			}
		}

		
		graph.dispose();
		
		this.label.setIcon( new ImageIcon(img) );		
		
	}
	
	public static void main(String args[]) {
		new Main();
	}
}

class ConvertKeyListener implements KeyListener {
	private Main parent;
	ConvertKeyListener( Main parent ) {
		this.parent = parent;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		if ( e.getKeyChar() == KeyEvent.VK_ENTER && parent.status == 0 ) {
			parent.status = 1;
			System.out.println("MM");
//			parent.removeKeyListener( parent.convertKeyListener );
			parent.label.removeMouseListener( parent.sillyMouseAdapter );
			parent.label.removeMouseMotionListener( parent.sillyMouseAdapter );
			
			parent.label.addMouseListener( parent.gMouseAdapter );
			parent.label.addMouseMotionListener( parent.gMouseAdapter );
			
			parent.q = new Point[ parent.p.length ];
			for ( int i = 0; i < parent.p.length; ++i )
				parent.q[i] = parent.p[i];
			parent.gTrans = parent.g;
			parent.oriImg = parent.img;
		}
		if ( e.getKeyChar() == KeyEvent.VK_SPACE ) {
			parent.fDrawGrid = !parent.fDrawGrid;
			if ( parent.q != null ) {
				parent.draw( parent.img, parent.q, null, false, parent.lastGrid );				
			} else {
				parent.draw( parent.img, parent.lastGrid ); //TODO				
			}
		}
		if ( e.getKeyChar() == KeyEvent.VK_NUMPAD1 || e.getKeyChar() == KeyEvent.VK_1 ) {
			Config.imageDeformation = new AffineDeformation();
			parent.setTitle("AffineDeformation");

			if ( parent.q != null ) {
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
				parent.draw( newImg, parent.q, null, false, parent.gTrans );		
			}
		}
		if ( e.getKeyChar() == KeyEvent.VK_NUMPAD2 || e.getKeyChar() == KeyEvent.VK_2 ) {
			Config.imageDeformation = new SimilarityDeformation();
			parent.setTitle("SimilarityDeformation");

			if ( parent.q != null ) {
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
				parent.draw( newImg, parent.q, null, false, parent.gTrans );		
			}
}
		if ( e.getKeyChar() == KeyEvent.VK_NUMPAD3 || e.getKeyChar() == KeyEvent.VK_3 ) {
			Config.imageDeformation = new RigidDeformation();
			parent.setTitle("RigidDeformation");

			if ( parent.q != null ) {
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
				parent.draw( newImg, parent.q, null, false, parent.gTrans );		
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

