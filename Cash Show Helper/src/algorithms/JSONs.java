package algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONObject;

import main.Config;

public class JSONs {
	static InputStream is;
	static BufferedReader rd;
	static InputStreamReader isr;
	static URL actualurl;

	private static String readAll(Reader rd) {
		StringBuilder sb = new StringBuilder();
		byte[] bytes = new byte[1024 << 9];
		int bytesRead;

		try {
			while ((bytesRead = is.read(bytes)) != -1) {
				sb.append(new String(bytes, 0, bytesRead));
			}
		} catch (IOException e) {
			System.out.println("IO Exception Occured");
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException {
		JSONObject json = null;
		try {
			actualurl = new URL(url);
		} catch (MalformedURLException e) {
			System.out.println("URL is incorrect");
		}

		
			is = actualurl.openStream();
			isr = new InputStreamReader(is, Charset.forName("UTF-8"));
			rd = new BufferedReader(isr);
			String jsonText = readAll(rd);

			json = new JSONObject(jsonText);
			is.close();
		
		return json;

	}

	public static int getNumberOfResults(String question) throws IOException {
		String totalResults1 = null;
		JSONObject json = readJsonFromUrl(
				(new StringBuilder("https://www.googleapis.com/customsearch/v1?key=").append(Config.GOOGLE_API_KEY)
						.append("&cx=").append(Config.SEARCH_ENGINE_ID).append("&q=").append(question).toString()));
		try {
		JSONObject queries = json.getJSONObject("queries");
		JSONArray request = queries.getJSONArray("request");
		JSONObject totalResults = request.getJSONObject(0);
		totalResults1 = totalResults.getString("totalResults");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return Integer.parseInt(totalResults1.toString());
	}

	public static void main(String[] args) throws IOException {
		System.out.println(getNumberOfResults("hitler did nothing wrong"));

	}
}