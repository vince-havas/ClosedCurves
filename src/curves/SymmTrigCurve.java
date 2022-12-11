package curves;

import java.awt.geom.Point2D;

import curves.CurveFactory.CurveType;

public class SymmTrigCurve extends ClosedCurve {
	// symmetric closed curves based on trigonometric expressions

	static private int _nocSymTrig = 6;
	static private double _paramLimitSymTrig = 2 * Math.PI;

	static {
		set_parameterLimit(_paramLimitSymTrig);
	}

	public SymmTrigCurve(final int x_, final int y_, final double radius_) {
		super(x_, y_, radius_);
	}

	protected Point2D calculatePoint(double p_) {
		// calculate the curve's point for a given parameter value
		final double cos = Math.cos(p_), sin = Math.sin(p_);

		final double x = _coefficients[0] * cos + _coefficients[1] * Math.pow(cos, 3.0)
				+ _coefficients[2] * Math.pow(cos, 5.0);
		final double y = _coefficients[3] * sin + _coefficients[4] * Math.pow(sin, 2.0)
				+ _coefficients[5] * (Math.pow(sin, 6.0) - 1);

		return new Point2D.Double(x, y);
	}

	protected static int get_noc() {
		return _nocSymTrig;
	}

	@Override
	public CurveType getCurveType() {
		return CurveFactory.CurveType.SYMMETRIC_TRIGONOMETRIC;
	}
}
