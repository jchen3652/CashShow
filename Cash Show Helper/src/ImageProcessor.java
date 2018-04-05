
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageProcessor {
	BufferedImage img = null;

	public ImageProcessor(String file) throws IOException {
		img = ImageIO.read(new File(Main.screenshotDirectory));
	}

	/**
	 * Converts an image to a binary one based on given threshold
	 * 
	 * @param image
	 *            the image to convert. Remains untouched.
	 * @param threshold
	 *            the threshold in [0,255]
	 * @return a new BufferedImage instance of TYPE_BYTE_GRAY with only 0'S and
	 *         255's
	 */
	public BufferedImage thresholdImage(BufferedImage image, int threshold) {
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		result.getGraphics().drawImage(image, 0, 0, null);
		WritableRaster raster = result.getRaster();
		int[] pixels = new int[image.getWidth()];
		for (int y = 0; y < image.getHeight(); y++) {
			raster.getPixels(0, y, image.getWidth(), 1, pixels);
			for (int i = 0; i < pixels.length; i++) {
				if (pixels[i] < threshold)
					pixels[i] = 0;
				else
					pixels[i] = 255;
			}
			raster.setPixels(0, y, image.getWidth(), 1, pixels);
		}
		return result;
	}

	/**
	 * Uses OCR to find the question text
	 * 
	 * @return String with the question text filtered into a single line
	 * @throws IOException
	 */
	public String getQuestionText() throws IOException {
		// img = ImageIO.read(new
		// File("D:\\Users\\James\\Desktop\\Screenshot_2018-02-24-18-32-55 (1).png"));

		BufferedImage questionArea = img.getSubimage(70, 350, 920, 250);
		questionArea = thresholdImage(questionArea, 200);
		// Kernel kernel = new Kernel(3, 3, new float[] { -1, -1, -1, -1, 9, -1, -1, -1,
		// -1 });
		// BufferedImageOp op = new ConvolveOp(kernel);
		// questionArea = op.filter(questionArea, null);
		File outputfile = new File("D:\\Users\\James\\Desktop\\dimage.png");
		ImageIO.write(questionArea, "png", outputfile);

		ITesseract instance = new Tesseract();

		String result = null;
		try {
			result = instance.doOCR(questionArea);
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = result.replaceAll("\n", " ");
		return result;
	}

	public String[] getAnswerList() throws IOException {
		// img = ImageIO.read(new File("D:\\Users\\James\\Desktop\\Screen Shot.png"));

		BufferedImage answerArea = img.getSubimage(70, 750, 710, 600);
		// answerArea = thresholdImage(answerArea, 200);

		// Kernel kernel = new Kernel(3, 3, new float[] { -1, -1, -1, -1, 9, -1, -1, -1,
		// -1 });
		// BufferedImageOp op = new ConvolveOp(kernel);
		// answerArea = op.filter(answerArea, null);
		File outputfile = new File("D:\\Users\\James\\Desktop\\answerarea.png");
		ImageIO.write(answerArea, "png", outputfile);

		ITesseract instance = new Tesseract();

		String result = null;
		try {
			result = instance.doOCR(answerArea);
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] listOfResults = result.split("\n");
		listOfResults[2] = listOfResults[2].replaceAll("/n", "").replaceAll("ﬁ", "fi");
		return listOfResults;

	}

}
