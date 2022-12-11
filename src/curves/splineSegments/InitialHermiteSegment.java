package curves.splineSegments;

import java.awt.geom.Point2D;

import curves.ClosedSpline;

public class InitialHermiteSegment extends HermiteSegment {
	private Point2D _point0; // control point at t=0, where t e [0, 1[ is the local parameter
	private Point2D _tangential0; // control tangential at t=0

	public InitialHermiteSegment(Point2D initPoint_, Point2D initTangential_, Point2D lastPoint_,
			Point2D lastTangential_) {
		_previous = null;
		_point0 = (Point2D) initPoint_.clone();
		_tangential0 = (Point2D) initTangential_.clone();
		_point1 = (Point2D) lastPoint_.clone();
		_tangential1 = (Point2D) lastTangential_.clone();
	}

	public InitialHermiteSegment(double[] coefs_) {
		this(new Point2D.Double(coefs_[0], coefs_[1]), new Point2D.Double(coefs_[2], coefs_[3]),
				new Point2D.Double(coefs_[4], coefs_[5]), new Point2D.Double(coefs_[6], coefs_[7]));
	}

	public Point2D get_initPoint() {
		return _point0;
	}

	public Point2D get_initTangential() {
		return _tangential0;
	}

	public Point2D get(ClosedSpline.SegmentParameter part_) {
		switch (part_) {
		case P0:
			return _point0;
		case M0:
			return _tangential0;
		case P1:
			return _point1;
		case M1:
			return _tangential1;
		default:
			throw new IllegalArgumentException("invalid spline segment name");
		}
	}
}
