package vision;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import algorithms.Algorithms;
import main.Config;
import main.Main;
import net.sourceforge.tess4j.TesseractException;

/**
 * Processes screenshot to get question and answer strings
 * 
 * @author James
 *
 */
public class ImageProcessor {
	private BufferedImage phoneScreen = null;
	private double newResolutionModifier;
	public String rawQuestionText;
	public String[] rawAnswerStrings = new String[3];

	public static void main(String[] args) {

	}

	public ImageProcessor() {

	}

	/**
	 * Retrieves Questions and Answers from Screenshot
	 * 
	 * @param image
	 *            Screenshot to process
	 * @throws IOException
	 */
	public ImageProcessor(BufferedImage image) throws IOException {
		phoneScreen = image;
		newResolutionModifier = phoneScreen.getHeight() / 990.0;

	}

	public void setImage(BufferedImage image) throws IOException {
		phoneScreen = image;
		newResolutionModifier = phoneScreen.getHeight() / 990;
	}

	public BufferedImage increaseBrightness(BufferedImage image) {
		float brightenFactor = 1.2f;
		RescaleOp op = new RescaleOp(brightenFactor, 0, null);
		image = op.filter(image, image);
		return image;
	}

	/**
	 * Uses OCR to find the question text
	 * 
	 * @return String with the question text filtered into a single line and
	 *         lower case
	 * @throws IOException
	 */
	public String getQuestionText() throws IOException {
		Main.output.println("Getting Question String...");

		BufferedImage questionArea = phoneScreen.getSubimage(
				(int) Math.round((Config.rawQuestionLocation[0]) * newResolutionModifier),
				(int) Math.round((Config.rawQuestionLocation[1]) * newResolutionModifier),
				(int) Math.round((Config.rawQuestionLocation[2]) * newResolutionModifier),
				(int) Math.round((Config.rawQuestionLocation[3]) * newResolutionModifier));

//		questionArea = sharpenImage(questionArea);
//		questionArea = thresholdImage(questionArea, Config.questionTextThreshold);

		if (Config.isDebug) {
			File phoneScreenFile = new File(Config.mainDirectory + Config.phoneScreenIdentifier);
			ImageIO.write(phoneScreen, "png", phoneScreenFile);

			File outputfile = new File(Config.questionOutputPath);
			ImageIO.write(questionArea, "png", outputfile);

		}

		String result = null;
		
		OCRThread ocr = new OCRThread(questionArea);
		ocr.run();
		result = ocr.getResult();
		
//		try {
//			result = Algorithms.cleanOCRError(Main.instance.doOCR(questionArea));
//		} catch (TesseractException e) {
//			e.printStackTrace();
//		}
//
//		result = Algorithms.cleanOCRError(result);

		rawQuestionText = result;
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
		Main.output.println("Getting Answer List...");

		BufferedImage[] allAnswerImg = new BufferedImage[3];
		allAnswerImg[0] = phoneScreen.getSubimage(
				(int) Math.round((Config.rawAnswer1Location[0]) * newResolutionModifier),
				(int) Math.round((Config.rawAnswer1Location[1]) * newResolutionModifier),
				(int) Math.round((Config.rawAnswer1Location[2]) * newResolutionModifier),
				(int) Math.round((Config.rawAnswer1Location[3]) * newResolutionModifier));

		allAnswerImg[1] = phoneScreen.getSubimage(
				(int) Math.round((Config.rawAnswer2Location[0]) * newResolutionModifier),
				(int) Math.round((Config.rawAnswer2Location[1]) * newResolutionModifier),
				(int) Math.round((Config.rawAnswer2Location[2]) * newResolutionModifier),
				(int) Math.round((Config.rawAnswer2Location[3]) * newResolutionModifier));

		allAnswerImg[2] = phoneScreen.getSubimage(
				(int) Math.round((Config.rawAnswer3Location[0]) * newResolutionModifier),
				(int) Math.round((Config.rawAnswer3Location[1]) * newResolutionModifier),
				(int) Math.round((Config.rawAnswer3Location[2]) * newResolutionModifier),
				(int) Math.round((Config.rawAnswer3Location[3]) * newResolutionModifier));

//		for (BufferedImage o : allAnswerImg) {
//
//			o = sharpenImage(o);
//			o = thresholdImage(o, Config.answerTextThreshold);
//		}

		if (Config.isDebug) {
			for (int i = 0; i < 3; i++) {
				ImageIO.write(allAnswerImg[i], "png", new File((new StringBuilder(Config.mainDirectory)
						.append("answer" + Integer.toString(1 + i) + ".png").toString())));
			}

		}
		OCRThread ocr0 = new OCRThread(allAnswerImg[0]);
		OCRThread ocr1 = new OCRThread(allAnswerImg[1]);
		OCRThread ocr2 = new OCRThread(allAnswerImg[2]);
		
		ocr0.run();
		ocr1.run();
		ocr2.run();
		
		rawAnswerStrings[0] = ocr0.getResult().trim();
		rawAnswerStrings[1] = ocr1.getResult().trim();
		rawAnswerStrings[2] = ocr2.getResult().trim();
		
//		try {
//			
//			for (int i = 0; i < 3; i++) {
//				rawAnswerStrings[i] = Algorithms.cleanOCRError(Main.instance.doOCR(allAnswerImg[i]).trim());
//			}
//
//		} catch (TesseractException e) {
//			e.printStackTrace();
//		}

		Main.output.println("Got Answer List");
		return rawAnswerStrings;
	}

	/**
	 * Increases the sharpness of a BufferedImage
	 * 
	 * @param image
	 *            Desired image to be sharpened
	 * @return Sharpened Image
	 */
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

		return result;
	}
}