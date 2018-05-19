package threads;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParserThread implements Runnable {
	private String url;
	private String totalText;
	private boolean isFinished;
	public HtmlParserThread(String url) {
		this.url = url;
	}
	
	@Override
	public void run() {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Elements el = doc.body().getAllElements();
		for (Element e : el) {
			totalText += e.text();
		}		
		isFinished = true;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public String getText() {
		return totalText;
	}
	
	

}
