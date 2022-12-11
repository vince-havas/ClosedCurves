package illustration;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import javax.swing.JFrame;

import curves.ClosedCurve;
import curves.CurveFactory;

public class Illustrator extends Canvas {
	private static final long serialVersionUID = 1L;

	static public void setUp(int width_) {
		JFrame frame = new JFrame("Curves");
		Canvas canvas = new Illustrator();
		canvas.setSize(width_, (int) (width_ * Saver._pageRatio));
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		GeneralPath polyline = new GeneralPath();

		for (ClosedCurve curve : CurveFactory.get_curves()) {
			polyline = curve.draw_me();
			g2.draw(polyline);
		}
	}
}
