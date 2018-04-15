package algorithms;

import java.net.UnknownHostException;
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
	
	public static String getGoogleResultsString(String keyword) {
		java.util.List<Result> results = new ArrayList<>();
		String googleResultsString = "";
		try {
			results = search(keyword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (Result result : results) {
				googleResultsString = (new StringBuilder(googleResultsString)).append(result.getHtmlSnippet()).toString();
				googleResultsString = (new StringBuilder(googleResultsString)).append(result.getTitle()).toString();
			}
		} catch (Exception e) {
			System.out.println("No results were found in Google Search");
		}

		for (String[] o : Config.searchReplaceList) {
			googleResultsString = StringUtils.replaceAll(googleResultsString, o[0], o[1]);
		}
		googleResultsString = googleResultsString.toLowerCase();

		return googleResultsString;
	}

	/**
	 * Gets the google result objects of the top 10 google results of the search query
	 * 
	 * @param keyword
	 *            Search Query
	 * @return List of result objects
	 */
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
			System.out.println("You ain't connected to the internet dumbass");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
}
