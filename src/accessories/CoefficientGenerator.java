package accessories;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.DataFormatException;

import curves.ClosedCurve;
import curves.CurveFactory;
import curves.CurveFactory.CurveType;

public class CoefficientGenerator {
	private static CoefficientGenerator _coefGenerator = null;
	private ArrayList<Double> _initialCoeffs;
	private ArrayList<Double> _finalCoeffs;
	double[] _coeffSteps;
	private static CurveType _curveType;
	private static boolean _isRandom; // if true, random curves are drawn, if false, initial and final setups are
										// loaded from file, and the parameters of curves are gradually distributed
										// between them, the initial being the top left, the final the bottom right
	private static String _path = System.getProperty("user.dir") + "\\output\\initial_states.txt";

	private CoefficientGenerator() throws DataFormatException {
		final int nRows = CurveFactory.get_nRows();
		final int nColumns = CurveFactory.get_nCurves() / nRows;
		int noc = ClosedCurve.get_noc(_curveType);

		if (_isRandom) {
//			TODO
		} else {
			readInStates();
			_coeffSteps = new double[noc];
			int jj;
			for (int ii = 0; ii < noc / 2; ii++) {
				jj = ii + noc / 2;
				_coeffSteps[ii] = (_finalCoeffs.get(ii) - _initialCoeffs.get(ii)) / (nRows - 1);
				_coeffSteps[jj] = (_finalCoeffs.get(jj) - _initialCoeffs.get(jj)) / (nColumns - 1);
			}
		}
	}

	public void readInStates() throws DataFormatException {
		_initialCoeffs = new ArrayList<>();
		_finalCoeffs = new ArrayList<>();
		String[] states;
		File myObj = new File(_path);
		try (Scanner myReader = new Scanner(myObj)) {
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				states = data.split(":");
				if (states[0].equals(CurveFactory.get_type().toString())) {
					String[] initArray = states[1].split(",");
					String[] finalArray = states[2].split(",");
					if (initArray.length != finalArray.length)
						throw new DataFormatException("Initial and final states have different lenghts");

					for (int ii = 0; ii < finalArray.length; ii++) {
						_initialCoeffs.add(Double.valueOf(initArray[ii]));
						_finalCoeffs.add(Double.valueOf(finalArray[ii]));
					}
					break;
				}
			}
			myReader.close();
		} catch (NumberFormatException | FileNotFoundException e) {
			e.printStackTrace();
		}

		if (_initialCoeffs == null || _finalCoeffs == null)
			throw new DataFormatException("Couldn't read in curve states");
	}

	public static CoefficientGenerator getInstance() throws DataFormatException {
		if (_coefGenerator == null)
			_coefGenerator = new CoefficientGenerator();

		return _coefGenerator;
	}

	public double[] get_coeffSteps() {
		return _coeffSteps;
	}

	public ArrayList<Double> get_initialCoeffs() {
		return _initialCoeffs;
	}

	public static void set_curveType(CurveType curveType_) {
		CoefficientGenerator._curveType = curveType_;
	}

	public static void set_isRandom(boolean _isRandom) {
		CoefficientGenerator._isRandom = _isRandom;
	}
}
