package threads;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
		totalText = doc.html();
		isFinished = true;
		System.out.println("is finished");
	}

	public boolean isFinished() {
		return isFinished;
	}

	public String getText() {
		return totalText;
	}

	public static void main(String[] args) {
		String googleResult = "";

		String[] urls = {"http://www.google.com"};

		HtmlParserThread[] allParserThreads = new HtmlParserThread[1];

		for (int i = 0; i < 1; i++) {
			allParserThreads[i] = new HtmlParserThread(urls[i]);

		}

		for (HtmlParserThread o : allParserThreads) {
			o.run();
		}

		//		boolean doneParsing = false;
		//		while (!doneParsing) {
		//			doneParsing = true;
		//			for (HtmlParserThread o : allParserThreads) {
		//				if (o.isFinished()) {
		//					(new StringBuilder(googleResult)).append(o.getText()).toString();
		//				} else {
		//					doneParsing = false;
		//				}
		//
		//			}
		//		}

		//		System.out.println("Test successful");
	}

}
