package algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import main.Config;

public class JSONUtils {
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
		url = StringUtils.replaceAll(url, " ", "%20");
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

	public static int getNumberOfResults(String query) throws IOException {
		String totalResults1 = null;
		
		JSONObject json = new JSONObject(IOUtils.toString(new URL((new StringBuilder("https://www.googleapis.com/customsearch/v1?key=").append(Config.GOOGLE_API_KEY)
				.append("&cx=").append(Config.SEARCH_ENGINE_ID).append("&q=").append(query).toString().replaceAll(" ", "%20"))), Charset.forName("UTF-8")));
		
//		JSONObject json = readJsonFromUrl(
//				(new StringBuilder("https://www.googleapis.com/customsearch/v1?key=").append(Config.GOOGLE_API_KEY)
//						.append("&cx=").append(Config.SEARCH_ENGINE_ID).append("&q=").append(query).toString()));
		try {
			JSONObject queries = json.getJSONObject("queries");
			JSONArray request = queries.getJSONArray("request");
			JSONObject totalResults = request.getJSONObject(0);
			totalResults1 = totalResults.getString("totalResults");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Integer.parseInt(totalResults1.toString());
	}

	public static String getAllSearchText(String query) throws IOException {
		String totalResults = "";
		long startTime = System.currentTimeMillis();
		JSONObject json = readJsonFromUrl(
				(new StringBuilder("https://www.googleapis.com/customsearch/v1?key=").append(Config.GOOGLE_API_KEY)
						.append("&cx=").append(Config.SEARCH_ENGINE_ID).append("&q=").append(query).toString()));
		Config.printStream.println(String.valueOf((System.currentTimeMillis() - startTime)));
		try {
			//			JSONObject queries = json.getJSONObject("queries");
			
			JSONArray items = json.getJSONArray("items");
			for (Object o : items) {
				JSONObject iterated = null;
				
					iterated = ((JSONObject) o);
				
				String snippet = iterated.getString("snippet");
				totalResults = (new StringBuilder(totalResults)).append(snippet).toString();
				
			}
			//		JSONObject totalResults = request.getJSONObject(0);
			//		totalResults1 = totalResults.getString("totalResults");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalResults;
	}

	public static void main(String[] args) throws IOException {
		//		System.out.println(getNumberOfResults("Bob%22dylan%22Tony%22Award"));
		getAllSearchText("test");
	}
}