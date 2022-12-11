package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import accessories.PRNG;
import curves.CurveFactory.CurveType;
import curves.splineSegments.HermiteSegment;
import curves.splineSegments.InitialHermiteSegment;
import curves.splineSegments.LastHermiteSegment;

public class ClosedSpline extends ClosedCurve {
	// creating closed curves from cubic Hermite splines

	public enum SegmentParameter {
		P0, // control point at t=0, where t e [0, 1[ is the local parameter
		M0, // control tangential at t=0
		P1, // control point at t=1
		M1 // control tangential at t=1
	}

	static private int _nSegments = 4; // number of spline segments
	static private int _nocSpline = _nSegments * 4; // number of describing parameters
	static private double _parameterLimitSpline = _nSegments; // every segment is parametrised from 0 to 1, thus the
																// global parameter is in [0, n[
	private ArrayList<HermiteSegment> _segments;

	static {
		set_parameterLimit(_parameterLimitSpline);
	}

	public ClosedSpline(final int x_, final int y_, final double radius_) {
		super(x_, y_, radius_);
		_segments = new ArrayList<HermiteSegment>();

		if (CurveFactory.is_randomPattern()) {
			randomSetUp(radius_);
		} else {
			gradualSetUp();
		}
	}

	private void gradualSetUp() {
		// initial segment
		_segments.add(new InitialHermiteSegment(Arrays.copyOfRange(_coefficients, 0, 8)));

		// intermediate segments
		for (int ii = 1; ii < _nSegments - 1; ii ++)
			_segments.add(new HermiteSegment(_segments.get(ii - 1),
					Arrays.copyOfRange(_coefficients, (ii + 1) * 4, (ii + 2) * 4)));

		// sealing segment
		_segments.add(
				new LastHermiteSegment(_segments.get(_segments.size() - 1), (InitialHermiteSegment) _segments.get(0)));
	}

	private void randomSetUp(double radius_) {
		PRNG ng = PRNG.getInstance(-2, 2);
		double rand = ng.nextDouble();
		double angleStep = 2.0 * Math.PI / _nSegments, anglePoint = angleStep, angleTang = anglePoint + Math.PI / 2.0;
		double tangLenth = radius_;
		Point2D controlPoint = new Point2D.Double(), controlTangential = new Point2D.Double();
		controlPoint.setLocation(radius_ * Math.cos(anglePoint) * rand, radius_ * Math.sin(anglePoint) * rand);
		controlTangential.setLocation(tangLenth * Math.cos(angleTang) * rand, tangLenth * Math.sin(angleTang) * rand);

		_segments.add(new InitialHermiteSegment(new Point2D.Double(radius_, 0.0), new Point2D.Double(0.0, tangLenth),
				controlPoint, controlTangential));

		for (int ii = 1; ii < _nSegments - 1; ii++) {
			rand = ng.nextDouble();
			anglePoint += angleStep;
			angleTang += angleStep;
			controlPoint.setLocation(radius_ * Math.cos(anglePoint) * rand, radius_ * Math.sin(anglePoint) * rand);
			controlTangential.setLocation(tangLenth * Math.cos(angleTang) * rand,
					tangLenth * Math.sin(angleTang) * rand);

			_segments.add(new HermiteSegment(_segments.get(ii - 1), controlPoint, controlTangential));
		}

		_segments.add(
				new LastHermiteSegment(_segments.get(_segments.size() - 1), (InitialHermiteSegment) _segments.get(0)));
	}

	@Override
	protected Point2D calculatePoint(double p_) {
		/*
		 * since every segment is parametrised from 0 to 1 the global parameter consists
		 * of two parts: (I) the whole part is the index of segment (II) the fractional
		 * part is the local parameter within the segment
		 */
		int segmentIndex = (int) Math.floor(p_);
		double localParam = p_ % 1;
		return _segments.get(segmentIndex).calculatePoint(localParam);
	}

	@Override
	public CurveType getCurveType() {
		return CurveFactory.CurveType.SPLINE;
	}

	public static int get_noc() {
		return _nocSpline;
	}

	public String toString() {
		String out = "";
		for (int ii = 0; ii < _nSegments; ii++)
			out += "segment " + ii + ": " + _segments.get(ii) + "\n";

		return out;
	}
}
