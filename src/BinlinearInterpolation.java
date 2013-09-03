
public class BinlinearInterpolation {
	public static void work(Picture target, final Picture src, 
			final Grid[] gridFrom, final Grid[] grid2) {
		assert( gridFrom.length == grid2.length );
		
		int n = grid2.length;
		for (int k = 0; k < n; ++k) {
			final Grid grid = grid2[k];
			int x0 = (int)Math.floor(Math.min( 
					Math.min(grid.p[0].x, grid.p[1].x), 
					Math.min(grid.p[2].x, grid.p[3].x) 
			));
			int x1 =  (int)Math.ceil(Math.max( 
					Math.max(grid.p[0].x, grid.p[1].x), 
					Math.max(grid.p[2].x, grid.p[3].x) 
			));
			int y0 = (int)Math.floor(Math.min( 
					Math.min(grid.p[0].y, grid.p[1].y), 
					Math.min(grid.p[2].y, grid.p[3].y) 
			));
			int y1 = (int)Math.ceil(Math.max( 
					Math.max(grid.p[0].y, grid.p[1].y), 
					Math.max(grid.p[2].y, grid.p[3].y) 
			));
			for ( int i = x0; i <= x1; ++i ) {
				for ( int j = y0; j < y1; ++j ) {
					if ( grid.contains(i, j) ) {
						int a, r, g, b;
//						a = f( i, j,  )
//						target.a[ i * h + j ] = 
					}
				}
			}
		}
		
	}
	static double f(int x, int y, int Q11, int Q12, int Q21, int Q22, double x1, double x2, double y1, double y2) {
		double r = 0;
		r += Q11 * (x2 - x) * (y2 - y);
		r += Q21 * (x - x1) * (y2 - y);
		r += Q12 * (x2 - x) * (y - y1);
		r += Q22 * (x - x1) * (y - y1);
		r /= (x2 - x1) * (y2 - y1);
		return r;
	}
}
