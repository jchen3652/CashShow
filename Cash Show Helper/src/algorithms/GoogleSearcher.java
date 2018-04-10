package algorithms;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import main.Config;

public class GoogleSearcher {

	static Customsearch customsearch = new Customsearch.Builder(new NetHttpTransport(), new JacksonFactory(), null)
			.setApplicationName("My Project").build();

	public static void main(String[] args) {
		search("hitler");
	}

	public static String loadFullSearchableText(String keyword) {
		java.util.List<Result> results = new ArrayList<>();
		String fullSearchableText = "";
		try {
			results = search(keyword);

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (Result result : results) {
				fullSearchableText = fullSearchableText + result.getHtmlSnippet();
				fullSearchableText = fullSearchableText + result.getTitle();
			}
		} catch (Exception e) {
			System.out.println("No results found, setting fake searchableText");
			fullSearchableText = "DogsdfdDogs";
		}
		fullSearchableText = fullSearchableText.replaceAll("&#39;", "'").replaceAll("<br>", " ").replaceAll("<b>", "")
				.replaceAll("</b>", " ").replaceAll("&nbsp;...", " ").toLowerCase();
		
		return fullSearchableText;
	}

	public static java.util.List<Result> search(String keyword) {

		java.util.List<Result> resultList = null;
		try {

			Customsearch.Cse.List list = customsearch.cse().list(keyword);

			list.setKey(Config.GOOGLE_API_KEY);
			list.setCx(Config.SEARCH_ENGINE_ID);
			Search results = list.execute();
			resultList = results.getItems();
			//System.out.println(results.toString());
		} catch (UnknownHostException e) {
			System.out.println("Unable to search google, check connection");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
}
