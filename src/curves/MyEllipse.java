package curves;

import java.awt.geom.Point2D;

import curves.CurveFactory.CurveType;

public class MyEllipse extends ClosedCurve {

	static private int _nocEllipse = 4; // currently only 3 is used, it was easier to keep it even
	static private double _parameterLimitEllipse = 2 * Math.PI;

	static {
		set_parameterLimit(_parameterLimitEllipse);
	}

	public MyEllipse(final int x_, final int y_, final double radius_) {
		super(x_, y_, radius_);
	}

	@Override
	public CurveType getCurveType() {
		return CurveFactory.CurveType.ELLIPSE;
	}

	@Override
	protected Point2D calculatePoint(double p_) {
		return new Point2D.Double(_coefficients[0] * Math.cos(p_), _coefficients[1] * Math.sin(p_ + _coefficients[2]));
	}

	protected static int get_noc() {
		return _nocEllipse;
	}

}
