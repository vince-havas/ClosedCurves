package curves;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.zip.DataFormatException;

import accessories.CoefficientGenerator;
import curves.CurveFactory.CurveType;

public abstract class ClosedCurve {

	protected double[] _coefficients;
	static private double _parameterLimit;
	static protected double _paramStep;
	static protected double _paramBase;
	private Point2D _origin;

	public ClosedCurve(final int x_, final int y_, final double radius_) {
		if(CurveFactory.is_randomPattern() && CurveFactory.get_type()==CurveFactory.CurveType.SPLINE)
			return;
		
		int noc = get_noc(this.getCurveType());
		_coefficients = new double[noc];

		CoefficientGenerator generator = null;
		try {
			generator = CoefficientGenerator.getInstance();
		} catch (DataFormatException e) {
			e.printStackTrace();
		}

		int jj;
		for (int ii = 0; ii < noc / 2; ii++) {
			jj = noc / 2 + ii;
			_coefficients[jj] = generator.get_coeffSteps()[jj] * x_ + generator.get_initialCoeffs().get(jj);
			_coefficients[jj] *= radius_;
			_coefficients[ii] = generator.get_coeffSteps()[ii] * y_ + generator.get_initialCoeffs().get(ii);
			_coefficients[ii] *= radius_;
		}

	}

	abstract protected Point2D calculatePoint(final double p_);

	abstract public CurveFactory.CurveType getCurveType();

	public ClosedCurve() {
	}

//	public ClosedCurve(double[] c_) throws IllegalArgumentException {
//		if (c_.length != _nCoeff)
//			throw new IllegalArgumentException("invalid number of coefficients in ClosedCurve constructor");
//
//		_coefficients = new double[_nCoeff];
//		for (int ii = 0; ii < _nCoeff; ii++)
//			_coefficients[ii] = c_[ii];
//	}

	private Point2D shift_point(Point2D base_, Point2D diff_) {
		Point2D out = new Point2D.Double(base_.getX() + diff_.getX(), base_.getY() + diff_.getY());
		return out;
	}

	public GeneralPath draw_me() {
		double t = 0.0;
		GeneralPath polyline = new GeneralPath();
		Point2D point = calculatePoint(t);
		point = shift_point(point, _origin);

		polyline.moveTo(point.getX(), point.getY());
		for (; t < _parameterLimit; t += _parameterLimit / CurveFactory.get_nStep()) {
			point = shift_point(calculatePoint(t), _origin);
			polyline.lineTo(point.getX(), point.getY());
		}

		polyline.closePath();
		return polyline;
	}

	public String toString() {
		String out = "";
		for (int ii = 0; ii < get_noc(this.getCurveType()); ii++)
			out += "coef_" + ii + ": " + _coefficients[ii] + ", ";

		return out;
	}

	public void set_origin(double x_, double y_) {
		_origin = new Point2D.Double();
		_origin.setLocation(x_, y_);
	}

	public static void set_parameterLimit(double parameter_limit_) {
		ClosedCurve._parameterLimit = parameter_limit_;
	}

	public double get_parameter_limit() {
		return _parameterLimit;
	}

	static public void set_paramStep(double param_step_) {
		_paramStep = param_step_;
	}

	static public void set_paramBase(double param_base_) {
		_paramBase = param_base_;
	}

//	static protected void set_noc(final int n_) {
//		_nCoeff = n_;
//	}

	public static int get_noc(CurveType ct_) {
		switch (ct_) {
		case ASYMMETRIC_TRIGONOMETRIC:
			return AsymmTrigCurve.get_noc();
		case ELLIPSE:
			return MyEllipse.get_noc();
		case SPLINE:
			return ClosedSpline.get_noc();
		case SYMMETRIC_TRIGONOMETRIC:
			return SymmTrigCurve.get_noc();
		default:
			throw new IllegalArgumentException("Unknown curve type: " + ct_.toString());
		}
	}
}
