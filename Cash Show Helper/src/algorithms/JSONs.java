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
	static InputStream is;
	static BufferedReader rd;
	static InputStreamReader isr;
	static URL actualurl;
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		byte[] bytes = new byte[1024 << 9];
	    int bytesRead;
		
		
		while ((bytesRead = is.read(bytes)) != -1) {
			sb.append(new String(bytes, 0, bytesRead));
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		
		
			actualurl = new URL(url);
			
			is = actualurl.openStream();
			
			isr = new InputStreamReader(is, Charset.forName("UTF-8"));
			rd = new BufferedReader(isr);
			
			String jsonText = readAll(rd);
			
			JSONObject json = new JSONObject(jsonText);
			is.close();
			return json;
		
			
		
	}
	public static int getNumberOfResults(String question) throws JSONException, IOException {
		JSONObject json = readJsonFromUrl("https://www.googleapis.com/customsearch/v1?key=" + Config.GOOGLE_API_KEY + "&cx=" + Config.SEARCH_ENGINE_ID + "&q=" + question);
		JSONObject queries = json.getJSONObject("queries");
		JSONArray request = queries.getJSONArray("request");
		JSONObject totalResults = request.getJSONObject(0);
		String totalResults1 = totalResults.getString("totalResults");
		 //String kinds = json.getString("kind");
		return Integer.parseInt(totalResults1.toString());
	
	}
	

	public static void main(String[] args) throws IOException, JSONException {
		System.out.println(getNumberOfResults("hitler did nothing wrong"));
		//		JSONObject json = readJsonFromUrl("https://www.googleapis.com/customsearch/v1?key=AIzaSyCrhcL_hOd-GyIyZ2xQSB5Q6vt3e_JvmFo&cx=017576662512468239146:omuauf_lfve&q=which%22animal%22helped%22to%22build%22more%22civilizations%22than%22any%22other%22iguanas");
//		JSONObject queries = json.getJSONObject("queries");
//		JSONArray request = queries.getJSONArray("request");
//		JSONObject totalResults = request.getJSONObject(0);
//		String totalResults1 = totalResults.getString("totalResults");
//		 //String kinds = json.getString("kind");
//		System.out.println(totalResults1.toString());
	
	}
}