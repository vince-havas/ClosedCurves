package curves;

import java.awt.geom.Point2D;

import curves.CurveFactory.CurveType;

public class AsymmTrigCurve extends ClosedCurve {

	static private int _nocAsymTrig = 8; // currently only 7 is used, it was easier to keep it even
	static private double _parameterLimitAsymTrig = 2 * Math.PI;
	static {
		set_parameterLimit(_parameterLimitAsymTrig);
	}

	public AsymmTrigCurve(final int x_, final int y_, final double radius_) {
		super(x_, y_, radius_);
		}

	@Override
	public CurveType getCurveType() {
		return CurveFactory.CurveType.ASYMMETRIC_TRIGONOMETRIC;
	}

	@Override
	protected Point2D calculatePoint(double p_) {
		// calculate the curve's point for a given parameter value
		final double cos = Math.cos(p_ + _coefficients[1] / 150.0), sin = Math.sin(p_);

		final double x = _coefficients[0] * cos + _coefficients[1] * Math.pow(sin, 3.0)
				+ _coefficients[2] * Math.pow(cos, 5.0);
		final double y = _coefficients[3] * sin + _coefficients[4] * Math.pow(cos, 2.0)
				+ _coefficients[5] * Math.pow(sin, 6.0) + _coefficients[6];

		return new Point2D.Double(x, y);
	}

	protected static int get_noc() {
		return _nocAsymTrig;
	}

}
