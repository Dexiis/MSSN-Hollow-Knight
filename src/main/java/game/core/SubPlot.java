package game.core;

public class SubPlot {

	private double[] window;
	private float[] viewport;
	private double mx;
	private double bx;
	private double my;
	private double by;
	private final double fullHeight;
    private final double fullWidth;

	public SubPlot(double[] window, float[] viewport, float fullWidth, float fullHeight) {
		this.window = window;
		this.viewport = viewport;
		this.fullWidth = fullWidth;
		this.fullHeight = fullHeight;
		mx = viewport[2] * fullWidth / (window[1] - window[0]);
		bx = viewport[0] * fullWidth;
		my = -viewport[3] * fullHeight / (window[3] - window[2]);
		by = (1 - viewport[1]) * fullHeight;
	}

	public float[] getPixelCoord(double x, double y) {
		float[] coord = new float[2];
		coord[0] = (float) (bx + mx * (x - window[0]));
		coord[1] = (float) (by + my * (y - window[2]));
		return coord;
	}

	public float[] getPixelCoord(double[] xy) {
		return getPixelCoord(xy[0], xy[1]);
	}

	public float[] getDimInPixel(double dimx, double dimy) {
		float[] d = new float[2];
		d[0] = (float) (dimx * mx);
		d[1] = (float) (-dimy * my);

		return d;
	}

	public double[] getWorldCoord(double xx, double yy) {
		double[] coord = new double[2];
		coord[0] = window[0] + (xx - bx) / mx;
		coord[1] = window[2] + (yy - by) / my;
		return coord;
	}

	public float[] getWorldCoord(float[] xy) {
		return getPixelCoord(xy[0], xy[1]);
	}

	public boolean isInside(float xx, float yy) {
		double[] c = getWorldCoord(xx, yy);
		return (c[0] >= window[0] && c[0] <= window[1] && c[1] >= window[2] && c[1] <= window[3]);
	}

	public boolean isInside(float[] xy) {
		return isInside(xy[0], xy[1]);
	}

	public float[] getBoundingBox() {
		float[] c1 = getPixelCoord(window[0], window[2]);
		float[] c2 = getPixelCoord(window[1], window[3]);
		return new float[] { c1[0], c2[1], c2[0] - c1[0], c1[1] - c2[1] };
	}

	public float[] getBox(double cx, double cy, double dimx, double dimy) {
		float[] c1 = getPixelCoord(cx, cy);
		float[] c2 = getPixelCoord(cx + dimx, cy + dimy);
		return new float[] { c1[0], c2[1], c2[0] - c1[0], c1[1] - c2[1] };
	}

	public float[] getBox(double[] b) {
		return getBox(b[0], b[1], b[2], b[3]);
	}

	public float[] getVectorCoord(double dx, double dy) {
		float[] v = new float[2];
		v[0] = (float) (dx * mx);
		v[1] = (float) (-dy * my);
		return v;
	}

	public float[] getVectorCoord(double[] dxdy) {
		return getVectorCoord(dxdy[0], dxdy[1]);
	}

	public double[] getWindow() {
		return window;
	}

	public float[] getViewport() {
		return viewport;
	}

	public void setViewport(float[] viewport) {
		this.viewport = viewport;
		mx = viewport[2] * fullWidth / (window[1] - window[0]);
		bx = viewport[0] * fullWidth;
		my = -viewport[3] * fullHeight / (window[3] - window[2]);
		by = (1 - viewport[1]) * fullHeight;
	}

	public void setWindow(double[] window) {
		this.window = window;
		mx = viewport[2] * fullWidth / (window[1] - window[0]);
		bx = viewport[0] * fullWidth;
		my = -viewport[3] * fullHeight / (window[3] - window[2]);
		by = (1 - viewport[1]) * fullHeight;
	}

}