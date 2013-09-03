
public class Worker {
	public static void go(Picture tar, Picture src, Point p[], Point q[]) {
		Deformation def = new AffineDeformation();
		for (int i = 0; i < src.w; ++i) {
			for (int j = 0; j < src.h; ++j) {
				def.query( new Point(i, j), p, q, 1.0);
//				tar.setPixel(i, j, src.getPixel(src.w - 1 - i, j) );
			}
		}
	}
}
