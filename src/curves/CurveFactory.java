package curves;

import accessories.CoefficientGenerator;
import illustration.Saver;

public class CurveFactory {
	static private int _nCurves; // number of curves
	static private int _nRows; // number of rows of curves
	static private ClosedCurve[] _curves;
	static private final int _nStep = 500; // number of steps during drawing a single curve
	static private CurveType _type;
	static private boolean _randomPattern;

	public enum CurveType {
		// implemented types of curves to draw a closed line
		ELLIPSE, SYMMETRIC_TRIGONOMETRIC, ASYMMETRIC_TRIGONOMETRIC, SPLINE;
	}

	static public void generate_curves(final int nRows_, final int nColumns_, final int pageWidth_, CurveType type_,
			boolean isRand_) throws IllegalArgumentException {
		_nRows = nRows_;
		_nCurves = nRows_ * nColumns_;
		_type = type_;
		_randomPattern = isRand_;
		CoefficientGenerator.set_curveType(type_);
		CoefficientGenerator.set_isRandom(isRand_);

		switch (type_) {
		case ELLIPSE:
			_curves = new MyEllipse[_nCurves];
			break;
		case SPLINE:
			_curves = new ClosedSpline[_nCurves];
			break;
		case SYMMETRIC_TRIGONOMETRIC:
			_curves = new SymmTrigCurve[_nCurves];
			break;
		case ASYMMETRIC_TRIGONOMETRIC:
			_curves = new AsymmTrigCurve[_nCurves];
			break;
		default:
			throw new IllegalArgumentException("Invalid curve type");
		}

		int margin = (int) (pageWidth_ * Saver._marginRatio);
		int pageWithWithoutMargins = pageWidth_ - 2 * margin;
		int pageHeightWithoutMargins = (int) (pageWidth_ * Saver._pageRatio - 2 * margin);
		int rowSpread = pageHeightWithoutMargins / nRows_;
		int columnSpread = pageWithWithoutMargins / nColumns_;
		double radius = 0.04 * pageWidth_; // influences the size of each curve
		int x, y; // indices of a curve in the curve mx

		for (int ii = 0; ii < _nCurves; ii++) {
			x = ii % nColumns_;
			y = ii / nColumns_;

			switch (type_) {
			case ELLIPSE:
				_curves[ii] = new MyEllipse(x, y, radius);
				break;
			case SPLINE:
				_curves[ii] = new ClosedSpline(x, y, radius);
				break;
			case SYMMETRIC_TRIGONOMETRIC:
				_curves[ii] = new SymmTrigCurve(x, y, radius);
				break;
			case ASYMMETRIC_TRIGONOMETRIC:
				_curves[ii] = new AsymmTrigCurve(x, y, radius);
				break;
			}
			_curves[ii].set_origin(margin + columnSpread * (x + 0.5), margin + rowSpread * (y + 0.5));
		}
	}

	static public void print() {
		for (int ii = 0; ii < _nCurves; ii++)
			System.out.println("curve " + ii + ":\n" + _curves[ii]);
	}

	static public ClosedCurve[] get_curves() {
		return _curves;
	}

	static public void set_nCurves(final int n_) {
		_nCurves = n_;
	}

	public static int get_nCurves() {
		return _nCurves;
	}

	public static int get_nRows() {
		return _nRows;
	}

	public static int get_nStep() {
		return _nStep;
	}

	public static CurveType get_type() {
		return _type;
	}

	public static boolean is_randomPattern() {
		return _randomPattern;
	}
}
