package imageDeformation;

import geometry.Point;

public interface ImageDeformation {
	Point query( Point v, Point []p, Point []q, double alpha );
}
