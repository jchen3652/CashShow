package algorithms;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
	public static String getContainedText(ArrayList<String> urls, int index) throws IOException, UnsupportedMimeTypeException {

		String totalText = "";
		for (int i = 0; i < index; i++) {

			Document doc = Jsoup.connect(urls.get(i)).get();
			
			Elements el = doc.body().getAllElements();
			for (Element e : el) {
				totalText += e.text();
			}
		}
		return totalText;
	}
	public static void main(String[] args) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		list.add("https://jsoup.org/cookbook/extracting-data/attributes-text-html");
		System.out.println(getContainedText(list,1));
	}
}
