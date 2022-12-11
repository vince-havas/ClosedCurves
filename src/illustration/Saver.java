package illustration;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import curves.ClosedCurve;
import curves.CurveFactory;

public class Saver {
	static public double _pageRatio = 1.414, _marginRatio = 1.5 / 21.0;

	static public void saveDrawing(int width_, String name_) {
		int height = (int) (width_ * _pageRatio);

		// TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
		// into integer pixels
		BufferedImage bi = new BufferedImage(width_, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = bi.createGraphics();

		GeneralPath polyline = new GeneralPath();
		g2.setPaint(Color.white);
		g2.fillRect(0, 0, width_, height);
		g2.setPaint(Color.black);
		g2.setStroke(new BasicStroke(4));

		for (ClosedCurve curve : CurveFactory.get_curves()) {
			polyline = curve.draw_me();
			g2.draw(polyline);
		}

		try {
			ImageIO.write(bi, "PNG", new File("c:\\Users\\Vince\\Desktop\\" + name_ + ".PNG"));
			System.out.println("drawing saved");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
