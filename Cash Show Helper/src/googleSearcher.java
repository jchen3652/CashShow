import java.util.ArrayList;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

public class googleSearcher {
	public static final int HTTP_REQUEST_TIMEOUT = 0;
	public static final String GOOGLE_API_KEY = "AIzaSyBFnKBQPESdi2sP1twKp59-3mBscTVw99k"; // Crappy one "AIzaSyBFnKBQPESdi2sP1twKp59-3mBscTVw99k" Good one "AIzaSyDhVVASBNyr0U-trn5eFaoJrNQJoHbPVzM"
																							// 
	public static final String SEARCH_ENGINE_ID = "014723624719242706501:ky6zn2teax4"; // Crappy One "014723624719242706501:ky6zn2teax4" Good one "017356742749847709225:4h4bt-iqizy"
																						// 
	public static String fullSearchableText = "";
	static Customsearch customsearch = new Customsearch.Builder(new NetHttpTransport(), new JacksonFactory(), /*new HttpRequestInitializer() {
			//customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
			
			public void initialize(HttpRequest httpRequest) {
				try {
					// set connect and read timeouts
					httpRequest.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
					httpRequest.setReadTimeout(HTTP_REQUEST_TIMEOUT);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
		}*/null).setApplicationName("My Project").build();
	
	
	public static void main(String[] args) {
		loadFullSearchableText("keyw");
		
		
		System.out.println(fullSearchableText);
	}

	public static String getFullSearchableText() {
		return fullSearchableText;
	}

	public static void loadFullSearchableText(String keyword) {
		java.util.List<Result> results = new ArrayList<>();

		try {
			results = search(keyword);

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (Result result : results) {
				fullSearchableText = fullSearchableText + result.getHtmlSnippet();
				fullSearchableText = fullSearchableText + result.getTitle();
				// fullSearchableText = fullSearchableText + result.getHtmlTitle();

				// System.out.println(result.getHtmlSnippet());
				//
				// System.out.println(result.getDisplayLink());
				// System.out.println(result.getTitle());

				/*
				 * // all attributes: System.out.println(result.toString());
				 */
			}
		} catch (Exception e) {
			System.out.println("No results found");
		}
		fullSearchableText = fullSearchableText.replaceAll("&#39;", "'").replaceAll("<br>", " ").replaceAll("<b>", "")
				.replaceAll("</b>", "");

	}

	public static java.util.List<Result> search(String keyword) {
		
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	

		java.util.List<Result> resultList = null;
		try {
			
			Customsearch.Cse.List list = customsearch.cse().list(keyword);
			
			list.setKey(GOOGLE_API_KEY);
			list.setCx(SEARCH_ENGINE_ID);
			Search results = list.execute();
			resultList = results.getItems();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
}
