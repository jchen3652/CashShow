package vision;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import algorithms.Algorithms;
import main.Config;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * Processes screenshot to get question and answer strings
 * 
 * @author James
 *
 */
public class ImageProcessor {
	private static BufferedImage phoneScreen = null;
	public static double resolutionModifier;
	public static double newResolutionModifier;

	public String rawQuestionText;
	public String[] rawAnswerStrings = new String[3];

	/**
	 * Retrieves Questions and Answers from Screenshot
	 * 
	 * @param image
	 *            Screenshot to process
	 * @throws IOException
	 */
	public ImageProcessor(BufferedImage image) throws IOException {
		phoneScreen = image;
		resolutionModifier = (1080.0 / (double) phoneScreen.getWidth());
	
		newResolutionModifier = phoneScreen.getHeight()/990.0;
		
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

		BufferedImage questionArea = phoneScreen.getSubimage((int) Math.round((Config.rawQuestionLocation[0])*newResolutionModifier),
				(int) Math.round((Config.rawQuestionLocation[1])*newResolutionModifier), (int) Math.round((Config.rawQuestionLocation[2])*newResolutionModifier),
				(int) Math.round((Config.rawQuestionLocation[3])*newResolutionModifier));
		
		

		// If in debugging mode, output the 
		if (Config.isDebug) {
			File phoneScreenFile = new File(Config.mainDirectory + Config.screenshotIdentifier);
			ImageIO.write(phoneScreen, "png", phoneScreenFile);

		}

		// crusty ass code
		//questionArea = sharpenImage(questionArea);
		questionArea = thresholdImage(questionArea, Config.questionTextThreshold);

		File outputfile = new File(Config.questionOutputPath);
		ImageIO.write(questionArea, "png", outputfile);

		ITesseract instance = new Tesseract();

		String result = null;
		try {
			result = Algorithms.cleanOCRError(instance.doOCR(questionArea));
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		result = Algorithms.cleanOCRError(result);

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
		System.out.println("Getting Answer List...");

		ITesseract instance = new Tesseract();

		//80, 30, 630, 170
		//		BufferedImage answer1 = thresholdImage(
		//				phoneScreen.getSubimage((int) (150 / resolutionModifier), (int) (780 / resolutionModifier),
		//						(int) (630 / resolutionModifier), (int) (150 / resolutionModifier)),
		//				Config.answerTextThreshold);
		//		BufferedImage answer2 = thresholdImage(
		//				phoneScreen.getSubimage((int) (150 / resolutionModifier), (int) (1000 / resolutionModifier),
		//						(int) (630 / resolutionModifier), (int) (150 / resolutionModifier)),
		//				Config.answerTextThreshold);
		//		BufferedImage answer3 = thresholdImage(
		//				phoneScreen.getSubimage((int) (150 / resolutionModifier), (int) (1200 / resolutionModifier),
		//						(int) (630 / resolutionModifier), (int) (150 / resolutionModifier)),
		//				Config.answerTextThreshold);

		BufferedImage[] allQuestionImg = new BufferedImage[3];
		allQuestionImg[0] = phoneScreen.getSubimage((int) Math.round((Config.rawAnswer1Location[0])*newResolutionModifier),
				(int) Math.round((Config.rawAnswer1Location[1])*newResolutionModifier), (int) Math.round((Config.rawAnswer1Location[2])*newResolutionModifier),
				(int) Math.round((Config.rawAnswer1Location[3])*newResolutionModifier));
		

		allQuestionImg[1] = phoneScreen.getSubimage((int) Math.round((Config.rawAnswer2Location[0])*newResolutionModifier),
				(int) Math.round((Config.rawAnswer2Location[1])*newResolutionModifier), (int) Math.round((Config.rawAnswer2Location[2])*newResolutionModifier),
				(int) Math.round((Config.rawAnswer2Location[3])*newResolutionModifier));

		
//		System.out.println("1: " + (int) Math.round(150.0 / resolutionModifier));
//		System.out.println("2: " + (int) Math.round(1000.0 / resolutionModifier));
//		System.out.println("3: " + (int) Math.round(630.0 / resolutionModifier));
//		System.out.println("4: " + (int) Math.round(150.0 / resolutionModifier));
		
		
		
		allQuestionImg[2] = phoneScreen.getSubimage((int) Math.round((Config.rawAnswer3Location[0])*newResolutionModifier),
				(int) Math.round((Config.rawAnswer3Location[1])*newResolutionModifier), (int) Math.round((Config.rawAnswer3Location[2])*newResolutionModifier),
				(int) Math.round((Config.rawAnswer3Location[3])*newResolutionModifier));
		
		
		
		
		

		//		BufferedImage answer1 = phoneScreen.getSubimage((int) Math.round(150.0 / resolutionModifier),
		//				(int) Math.round(780.0 / resolutionModifier), (int) Math.round(630.0 / resolutionModifier),
		//				(int) Math.round(150.0 / resolutionModifier));
		//		BufferedImage answer2 = phoneScreen.getSubimage((int) Math.round(150.0 / resolutionModifier),
		//				(int) Math.round(1000.0 / resolutionModifier), (int) (630 / resolutionModifier),
		//				(int) (150 / resolutionModifier));
		//		BufferedImage answer3 = phoneScreen.getSubimage((int) (150.0 / resolutionModifier),
		//				(int) (1200.0 / resolutionModifier), (int) (630 / resolutionModifier),
		//				(int) (150 / resolutionModifier));

		if (Config.isDebug) {
			for (int i = 0; i < 3; i++) {
				ImageIO.write(allQuestionImg[i], "png", new File((new StringBuilder(Config.mainDirectory)
						.append("answer" + Integer.toString(1 + i) + ".png").toString())));
			}

			//			ImageIO.write(answer1, "png",
			//					new File((new StringBuilder(Config.mainDirectory).append("answer1.png").toString())));
			//			ImageIO.write(answer2, "png",
			//					new File((new StringBuilder(Config.mainDirectory).append("answer2.png").toString())));
			//			ImageIO.write(answer3, "png",
			//					new File((new StringBuilder(Config.mainDirectory).append("answer3.png").toString())));
		}

		try {
			for (int i = 0; i < 3; i++) {
				rawAnswerStrings[i] = Algorithms.cleanOCRError(instance.doOCR(allQuestionImg[i]).trim());
			}

		} catch (TesseractException e) {
			e.printStackTrace();
		}

		System.out.println("Got Answer List");
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
		System.out.println("Threshold image completed");
		return result;
	}
}