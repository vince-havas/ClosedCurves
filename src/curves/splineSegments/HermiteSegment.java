package curves.splineSegments;

import java.awt.geom.Point2D;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import curves.ClosedSpline;
import curves.ClosedSpline.SegmentParameter;

public class HermiteSegment {
	protected HermiteSegment _previous; // geometric data at t=0 comes from the previous segment
	protected Point2D _point1; // control point at t=1, where t e [0, 1[ is the local parameter
	protected Point2D _tangential1; // control tangential at t=1

	protected HermiteSegment() {
	}

	public HermiteSegment(HermiteSegment prev_, Point2D lastPoint_, Point2D lastTangential_) {
		// a general segment gets its geometric parameters ( for parameter t=0 ) from
		// the previous segment
		_previous = prev_;
		_point1 = (Point2D) lastPoint_.clone();
		_tangential1 = (Point2D) lastTangential_.clone();
	}

	public HermiteSegment(HermiteSegment prev_, double[] coefs_) {
		this(prev_, new Point2D.Double(coefs_[0], coefs_[1]), new Point2D.Double(coefs_[2], coefs_[3]));
	}

	public Point2D calculatePoint(double t_) {
		double t2 = t_ * t_, t3 = t2 * t_;
		double x = (2 * t3 - 3 * t2 + 1) * this.get(SegmentParameter.P0).getX()
				+ (t3 - 2 * t2 + t_) * this.get(SegmentParameter.M0).getX()
				+ (-2 * t3 + 3 * t2) * this.get(SegmentParameter.P1).getX()
				+ (t3 - t2) * this.get(SegmentParameter.M1).getX();
		double y = (2 * t3 - 3 * t2 + 1) * this.get(SegmentParameter.P0).getY()
				+ (t3 - 2 * t2 + t_) * this.get(SegmentParameter.M0).getY()
				+ (-2 * t3 + 3 * t2) * this.get(SegmentParameter.P1).getY()
				+ (t3 - t2) * this.get(SegmentParameter.M1).getY();

		return new Point2D.Double(x, y);

	}

	public Point2D get(ClosedSpline.SegmentParameter part_) {
		switch (part_) {
		case P0:
			return _previous._point1;
		case M0:
			return _previous._tangential1;
		case P1:
			return _point1;
		case M1:
			return _tangential1;
		default:
			throw new IllegalArgumentException("invalid spline segment name");
		}
	}

	private String pointToString(String name_, Point2D p_) {
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		return name_ + ": (" + df.format(p_.getX()) + ", " + df.format(p_.getY()) + "); ";
	}

	public String toString() {
		String out = "";
		for (SegmentParameter sp : SegmentParameter.values())
			out += pointToString(sp.toString(), this.get(sp));

		return out;
	}
}
