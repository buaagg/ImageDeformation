package viewer;
//import geometry.Grid;
import geometry.Lattice;
import geometry.Point;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javaHelper.BufferedImageHelper;

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

	public Stage1MouseAdapter stage1MouseAdapter = new Stage1MouseAdapter(this);
	public Stage2MouseAdapter stage2MouseAdapter = new Stage2MouseAdapter(this);
	public ConvertKeyListener convertKeyListener = new ConvertKeyListener(this);
	
	public boolean isGridVisible = true;
	BufferedImage currentImage;
	BufferedImage originalImage;
	int width, height;
	public JLabel label;
	public int stage;
	Point []originalKeyPoints;
	Point []currentKeyPoints = new Point[0];

	int gridCountX, gridCountY;
	
	Lattice currentLattice = null;
	Lattice originalLattice = null;
	
	public Main() {
		
		String osName = System.getProperty("os.name");
		System.out.println( "You are running on " + osName );
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
		
		System.out.println( "The source file path is " + FileName );
		
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
		
		scale = Math.min( 1.0, scale * 0.85 );
		
		System.out.println( "scale = " + scale );
		
		this.width = (int)(src.getWidth() * scale);
		this.height = (int)(src.getHeight() * scale);
				
		currentImage = BufferedImageHelper.newBufferdImage(width, height);
		
		Graphics2D graph = (Graphics2D) currentImage.getGraphics();
		graph.scale(scale, scale);
		graph.drawImage( src, 0, 0, null );
		graph.dispose();
		
		label = new JLabel( new ImageIcon(currentImage) );

		JPanel panel = new JPanel();
		panel.setSize( currentImage.getWidth() ,  currentImage.getHeight() );
		panel.add( label );
		
		this.getContentPane().add( panel );
		this.setSize( currentImage.getWidth() + 30, currentImage.getHeight() + 50  );  
		this.setVisible(true);  		
		
		currentLattice = new Lattice( width, height, Config.gridLength );
		
		this.draw();
		
		this.stage = 1;
		label.addMouseListener( stage1MouseAdapter );
		label.addMouseMotionListener( stage1MouseAdapter );
		this.addKeyListener( convertKeyListener );
	}
	
	public void draw() {
		draw( null, false );
	}
	
	public void draw(Point activeKeyPoint, boolean isActiveKeyPointSpecial) { // draw currentImg, currentLattice, and currentKeyPoints
		
		BufferedImage displayImg = DisplayImageFactory.getDisplayImage(currentImage, currentLattice, currentKeyPoints, activeKeyPoint, isActiveKeyPointSpecial, isGridVisible);
		
		this.label.setIcon( new ImageIcon(displayImg) );		
		
	}
	
	public static void main(String args[]) {
		new Main();
	}
}

