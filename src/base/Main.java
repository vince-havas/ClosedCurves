package base;

import curves.CurveFactory;
import curves.CurveFactory.CurveType;
import illustration.Illustrator;
import illustration.Saver;

public class Main {

	public static void main(String[] args) {
		final boolean isRand = true, saveIt = false;
		int rows, columns, pageWidth;
		if (saveIt) {
			rows = 17;
			columns = 11;
			pageWidth = 2100;
		} else {
			rows = 3;
			columns = 2;
			pageWidth = 450;
		}
		CurveType ct = CurveType.SPLINE;
		CurveFactory.generate_curves(rows, columns, pageWidth, ct, isRand);
//		CurveFactory.print();

		if (saveIt)
			Saver.saveDrawing(pageWidth, "pict_name");
		else
			Illustrator.setUp(pageWidth);

		System.out.println("\ndone");
	}
}
