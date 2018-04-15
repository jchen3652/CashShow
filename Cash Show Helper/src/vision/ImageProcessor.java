package vision;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import algorithms.Algorithms;
import main.Config;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageProcessor {
	static BufferedImage img = null;
	static double resolutionModifier;

	public String humanQuestionText;
	public String[] humanAnswerText = new String[3];

	public ImageProcessor(BufferedImage image) throws IOException {
		img = image;
		resolutionModifier = (1080.0 / (double) img.getWidth());

		System.out.println("Resolution Downscale Factor: " + resolutionModifier);
	}

	/**
	 * Uses OCR to find the question text
	 * 
	 * @return String with the question text filtered into a single line and
	 *         lower case
	 * @throws IOException
	 */
	public String getQuestionText() throws IOException {
		System.out.println("Getting Question String...");
		
		BufferedImage questionArea = img.getSubimage((int) Math.round((70 / resolutionModifier)),
				(int) Math.round((350 / resolutionModifier)), (int) Math.round((920 / resolutionModifier)),
				(int) Math.round((250 / resolutionModifier)));

		
		
		
		// crusty ass code
		//questionArea = sharpenImage(questionArea);
		questionArea = thresholdImage(questionArea, Config.questionTextThreshold);

		File outputfile = new File(Config.questionOutputPath);
		ImageIO.write(questionArea, "png", outputfile);

		ITesseract instance = new Tesseract();

		String result = null;
		try {
			result = instance.doOCR(questionArea);
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = Algorithms.cleanOCRError(result);

		humanQuestionText = result;
		System.out.println("Got Question String");
		return result;
	}

	/**
	 * Returns a list of the answer question strings
	 * 
	 * @return Answer question list
	 * @throws IOException
	 */
	public String[] getAnswerList() throws IOException {
		System.out.println("Getting Answer List...");
		BufferedImage answerArea = img.getSubimage((int) (70 / resolutionModifier), (int) (750 / resolutionModifier),
				(int) (710 / resolutionModifier), (int) (600 / resolutionModifier));
		// crusty ass code
		//answerArea = sharpenImage(answerArea);
		answerArea = thresholdImage(answerArea, Config.answerTextThreshold);

		File outputfile = new File(Config.answersOutputPath);
		ImageIO.write(answerArea, "png", outputfile);

		ITesseract instance = new Tesseract();

		String result1 = null;
		String result2 = null;
		String result3 = null;

		BufferedImage subimage1 = answerArea.getSubimage((int) (80 / resolutionModifier),
				(int) (30 / resolutionModifier), (int) (630 / resolutionModifier), (int) (170 / resolutionModifier));
		BufferedImage subimage2 = answerArea.getSubimage((int) (80 / resolutionModifier),
				(int) (250 / resolutionModifier), (int) (630 / resolutionModifier), (int) (150 / resolutionModifier));
		BufferedImage subimage3 = answerArea.getSubimage((int) (80 / resolutionModifier),
				(int) (440 / resolutionModifier), (int) (630 / resolutionModifier), (int) (160 / resolutionModifier));

		if (Config.isDebug) {
			ImageIO.write(subimage1, "png", new File((new StringBuilder(Config.mainDirectory).append("subimage1.png").toString())));
			ImageIO.write(subimage2, "png", new File((new StringBuilder(Config.mainDirectory).append("subimage2.png").toString())));
			ImageIO.write(subimage3, "png", new File((new StringBuilder(Config.mainDirectory).append("subimage3.png").toString())));
		}
		try {
			result1 = instance.doOCR(subimage1);
			result2 = instance.doOCR(subimage2);
			result3 = instance.doOCR(subimage3);
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		humanAnswerText[0] = result1;
		humanAnswerText[1] = result2;
		humanAnswerText[2] = result3;

		try {
			humanAnswerText[2] = StringUtils.replaceAll(humanAnswerText[2], "/n", "");
		} catch (Exception e) {
			System.out.println("No image was detected");
		}
		for (int i = 0; i < humanAnswerText.length; i++) {
			humanAnswerText[i] = Algorithms.cleanOCRError(humanAnswerText[i]);
		}

		System.out.println("Got Answer List");
		return humanAnswerText;
	}

	public BufferedImage sharpenImage(BufferedImage image) {
		Kernel kernel = new Kernel(3, 3, new float[] {0.0f, -1.0f, 0.0f, -1.0f, 5.0f, -1.0f, 0.0f, -1.0f, 0.0f});
		BufferedImageOp op = new ConvolveOp(kernel);
		image = op.filter(image, null);
		return image;
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
		System.out.println("Threshold image completed");
		return result;
	}

}