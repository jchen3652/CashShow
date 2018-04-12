package algorithms;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

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
		System.out.println(search("Testing"));
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
			e.printStackTrace();
		}

		for (String[] o : Config.searchReplaceList) {
			fullSearchableText = StringUtils.replaceAll(fullSearchableText, o[0], o[1]);
		}
		fullSearchableText = fullSearchableText.toLowerCase();

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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
}
