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

@Deprecated
public class GoogleSearcher {

	static Customsearch customsearch = new Customsearch.Builder(new NetHttpTransport(), new JacksonFactory(), null)
			.setApplicationName("My Project").build();

	public static void main(String[] args) {
		System.out.println(getGoogleResultsString("which of these is one of jupiter's moons"));
	}

	/**
	 * Gets the large blob of google search data associated with the specific
	 * search term
	 * 
	 * @param query
	 *            Search term
	 * @return Search data for specified search term
	 */
	public static String getGoogleResultsString(String query) {
		java.util.List<Result> results = new ArrayList<>();
		String googleResultsString = "";
		try {
			results = search(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (Result result : results) {
				googleResultsString = (new StringBuilder(googleResultsString)).append(result.getSnippet())
						.toString();
				googleResultsString = (new StringBuilder(googleResultsString)).append(result.getTitle()).toString();
			}
		} catch (Exception e) {
			System.out.println("No results were found in Google Search");
		}

		for (String[] o : Config.googleResultReplaceList) {
			googleResultsString = StringUtils.replaceAll(googleResultsString, o[0], o[1]);
		}
		googleResultsString = googleResultsString.toLowerCase();

		return googleResultsString;
	}

	/**
	 * Gets the google result objects of the top 10 google results of the search
	 * query
	 * 
	 * @param keyword
	 *            Search Query
	 * @return List of result objects
	 */
	public static java.util.List<Result> search(String keyword) {

		java.util.List<Result> resultList = null;
		try {

			Customsearch.Cse.List list = customsearch.cse().list(keyword);
			
			list.setNum((long) 10);
			list.setKey(Config.GOOGLE_API_KEY);
			list.setCx(Config.SEARCH_ENGINE_ID);
			
			Search results = list.execute();
			
			resultList = results.getItems();
			//Main.console.out.println(results.toString());

		} catch (UnknownHostException e) {
			System.out.println("You ain't connected to the internet dumbass");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
}