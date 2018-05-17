package algorithms;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.Config;

public class JSONTools {
	private InputStream is;
	private URL actualurl;
	private JSONObject json;

	public void loadQuery(String query) throws IOException {
		String url = (new StringBuilder("https://www.googleapis.com/customsearch/v1?key=").append(Config.GOOGLE_API_KEY)
				.append("&cx=").append(Config.SEARCH_ENGINE_ID).append("&q=").append(query).toString());

		url = StringUtils.replaceAll(url, " ", "%20");
		try {
			actualurl = new URL(url);
		} catch (MalformedURLException e) {
			System.out.println("URL is invalid");
		}

		try {
			is = actualurl.openStream();
		} catch (IOException e) {
			Config.printStream.println("Run out of API uses, switch in Config.java");
		}

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

		String jsonText = sb.toString();

		json = new JSONObject(jsonText);
		is.close();

	}

	public int getNumberOfResults() throws IOException {
		String totalResults1 = null;
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

	public String getAllSearchText() throws IOException {
		String totalResults = "";
		long startTime = System.currentTimeMillis();

		Config.printStream.println(String.valueOf((System.currentTimeMillis() - startTime)));
		try {
			//			JSONObject queries = json.getJSONObject("queries");

			JSONArray items = json.getJSONArray("items");
			for (Object o : items) {
				JSONObject iterated = null;

				iterated = ((JSONObject) o);

				String snippet = iterated.getString("snippet");
				String title = iterated.getString("title");
				totalResults = (new StringBuilder(totalResults)).append(title).toString();
				totalResults = (new StringBuilder(totalResults)).append(snippet).toString();

			}
			//		JSONObject totalResults = request.getJSONObject(0);
			//		totalResults1 = totalResults.getString("totalResults");
		} catch (JSONException e) {

			Config.printStream.println("0 google results");
		}
		return totalResults;
	}

	public ArrayList<String> getAllResultURLs() {
		ArrayList<String> list = new ArrayList<String>();
		JSONArray items = json.getJSONArray("items");
		for (Object o : items) {
			JSONObject iterated = null;

			iterated = ((JSONObject) o);

			String snippet = iterated.getString("link");
			if (snippet.endsWith(".doc") || snippet.endsWith(".pdf")) {

			} else {
				list.add(snippet);
			}
		}
		return list;
	}

	public static void main(String[] args) throws IOException {
		JSONTools tools = new JSONTools();
		tools.loadQuery("testing");

		for (String o : tools.getAllResultURLs()) {
			System.out.println(o);
		}
	}
}