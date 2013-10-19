package viewer;

import imageDeformation.AffineDeformation;
import imageDeformation.BinlinearInterpolation;
import imageDeformation.ImageDeformation;

public class Config {
	public static final double alpha = 1;
	public static final int gridLength = 20;	
	public static ImageDeformation imageDeformation = new AffineDeformation();
	public static BinlinearInterpolation binlinearInterpolation = new BinlinearInterpolation();
}
