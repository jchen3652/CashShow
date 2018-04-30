package vision;

import java.awt.image.BufferedImage;

import algorithms.Algorithms;
import main.Main;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCRThread implements Runnable {
	private BufferedImage img;
	private String result;
	private ITesseract instance;
	private boolean isFinished = false;
	public OCRThread() {
		instance = new Tesseract();
		instance.setDatapath(Main.tessDataPath);
		instance.setLanguage("eng");
		
	}
	public OCRThread(BufferedImage img) {
		this.img = img;
		instance = new Tesseract();
		instance.setDatapath(Main.tessDataPath);
		instance.setLanguage("eng");
		
		
	}
	@Override
	public void run() {
		try {
			result = Algorithms.cleanOCRError(instance.doOCR(img));
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isFinished = true;
	}
	public void setImage(BufferedImage img) {
		this.img = img;
	}
	
	public String getResult() {
		while(!isFinished) {
			
		}
		return result;
	}

}
