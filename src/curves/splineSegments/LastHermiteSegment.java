package curves.splineSegments;

import java.awt.geom.Point2D;

import curves.ClosedSpline;

public class LastHermiteSegment extends HermiteSegment {
	protected InitialHermiteSegment _next; // the segment which seals the curve gets its geometric data from the
											// previous and the opening segments

	public LastHermiteSegment(HermiteSegment prev_, InitialHermiteSegment next_) {
		_point1 = null;
		_tangential1 = null;
		_previous = prev_;
		_next = next_;
	}
	
	public Point2D get(ClosedSpline.SegmentParameter part_) {
		switch (part_) {
		case P0:
			return _previous._point1;
		case M0:
			return _previous._tangential1;
		case P1:
			return _next.get_initPoint();
		case M1:
			return _next.get_initTangential();
		default:
			throw new IllegalArgumentException("invalid spline segment name");
		}
	}
}
