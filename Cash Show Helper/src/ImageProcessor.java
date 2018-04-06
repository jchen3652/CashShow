import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageProcessor {
	static BufferedImage img = null;
	static double resolutionModifier;

	private static final int questionTextThreshold = 210;
	private static final int answerTextThreshold = 210;

	private static final String questionOutputPath = "D:\\Users\\James\\Desktop\\questionimage.png";
	private static final String answersOutputPath = "D:\\Users\\James\\Desktop\\answerarea.png";
	
	public String humanQuestionText;
	public String[] humanAnswerText;
	
	public ImageProcessor(String file) throws IOException {
		img = ImageIO.read(new File(file));

		resolutionModifier = (1080.0 / (double) img.getWidth());
		System.out.println("Resolution Downscale Factor: " + resolutionModifier);
	}

	/**
	 * Uses OCR to find the question text
	 * 
	 * @return String with the question text filtered into a single line and lower
	 *         case
	 * @throws IOException
	 */
	public String getQuestionText() throws IOException {
		System.out.println("Getting Question String...");
		
		// img = ImageIO.read(new
		// File("D:\\Users\\James\\Desktop\\Screenshot_2018-02-24-18-32-55 (1).png"));

		BufferedImage questionArea = img.getSubimage((int) Math.round((70 / resolutionModifier)),
				(int) Math.round((350 / resolutionModifier)), (int) Math.round((920 / resolutionModifier)),
				(int) Math.round((250 / resolutionModifier)));
		questionArea = thresholdImage(questionArea, questionTextThreshold);
		// Kernel kernel = new Kernel(3, 3, new float[] { -1, -1, -1, -1, 9, -1, -1, -1,
		// -1 });
		// BufferedImageOp op = new ConvolveOp(kernel);
		// questionArea = op.filter(questionArea, null);
		File outputfile = new File(questionOutputPath);
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

		answerArea = thresholdImage(answerArea, answerTextThreshold);

		// Kernel kernel = new Kernel(3, 3, new float[] { -1, -1, -1, -1, 9, -1, -1, -1,
		// -1 });
		// BufferedImageOp op = new ConvolveOp(kernel);
		// answerArea = op.filter(answerArea, null);
		File outputfile = new File(answersOutputPath);
		ImageIO.write(answerArea, "png", outputfile);

		ITesseract instance = new Tesseract();

		String result = null;
		try {
			result = instance.doOCR(answerArea);
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		humanAnswerText = result.split("\n");
		humanAnswerText[2] = humanAnswerText[2].replaceAll("/n", "").replaceAll("ﬁ", "fi");
		

		
		System.out.println("Got Answer List");
		return humanAnswerText;

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