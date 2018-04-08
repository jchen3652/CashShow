package algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.Config;

public class JSONs {

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	/**
	 * Returns the number of Google CustomSearch results that exist for the
	 * given search term
	 * 
	 * @param searchQuery
	 *            Google search term
	 * @return Number of Google search results for the given term
	 * @throws JSONException
	 * @throws IOException
	 */
	public static int getNumberOfResults(String searchQuery) throws JSONException, IOException {
		JSONObject json = readJsonFromUrl(
				"https://www.googleapis.com/customsearch/v1?key=" + Config.GOOGLE_API_KEY + "&cx=017576662512468239146:omuauf_lfve&q="
						+ searchQuery);
		JSONObject queries = json.getJSONObject("queries");
		JSONArray request = queries.getJSONArray("request");
		JSONObject totalResults = request.getJSONObject(0);
		String totalResults1 = totalResults.getString("totalResults");
		//String kinds = json.getString("kind");
		return (int) getNumberOfResults(totalResults1.toString());

	}

	public static void main(String[] args) throws IOException, JSONException {
		System.out.println(getNumberOfResults("hitler"));

	}
}