package algorithms;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;

public class HtmlParser {
	public static String getContainedText(ArrayList<String> urls, int index) throws IOException, UnsupportedMimeTypeException {

		
		
		String totalText = "";
		for (int i = 0; i < index; i++) {
			try {
			Document doc = Jsoup.connect(urls.get(i)).get();
			
			totalText = (new StringBuilder(totalText)).append(doc.text()).toString();
			

			} catch(Exception e) {
				
			}
		}
		return totalText;
	}

	public static void main(String[] args) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		list.add("https://infowars.com");
		System.out.println(getContainedText(list, 1));
	}
}
