package algorithms;

import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import main.Config;

public class Algorithms {
	public static void main(String[] args) throws JSONException, IOException, InterruptedException {
		//System.out.println(googleResultsAlgorithm("Which animal helped build more civilizations than any other", "Dog"));
		//Thread.sleep(2000);
		System.out.println(LocalDateTime.now());

		System.out.println(googleResultsAlgorithm("In what city is Romeo and Juliet set?", "Rome"));
		System.out.println(googleResultsAlgorithm("In what city is Romeo and Juliet set?", "Verona"));
		System.out.println(googleResultsAlgorithm("In what city is Romeo and Juliet set?", "Venice"));
		System.out.println(LocalDateTime.now());

	}

	public static boolean arrayContains(String string, String[] array) {
		string = string.toLowerCase();

		boolean contains = false;
		for (int i = 0; i < 3; i++) {
			array[i] = array[i].toLowerCase();
			if (string.contains(array[i])) {
				contains = true;
			}
		}
		return contains;
	}

	public static String cleanOCRError(String text) {

		for (String[] o : Config.ocrReplaceList) {
			text = StringUtils.replaceAll(text, o[0], o[1]);
		}
		text = text.trim();
		return text;
	}

	public static int googleResultsAlgorithm(String question, String answerCandidate) throws IOException {

		question = StringUtils.replaceAll(question, " ", "%20"); //question.replaceAll(" ", "%20");
		answerCandidate = answerCandidate.replaceAll(" ", "%20");
		return (int) (JSONs.getNumberOfResults(question + "%20" + answerCandidate) / Config.googleResultsScaleDown);
	}

	/**
	 * Combined score calculation algorithm, all other algorithms are called
	 * with this
	 * 
	 * @param searchResult
	 *            Google Search Result
	 * @param answerCandidate
	 *            Potential answer
	 * @return Calculated Score
	 * @throws IOException
	 * @throws JSONException
	 */
	public static int primaryAlgorithm(String question, String searchResult, String answerCandidate)
			throws JSONException, IOException {
		
			
		
		
		
		
		int score = 0;
		searchResult = stringLowerCase(searchResult);
		answerCandidate = answerCandidate.replaceAll("A ", "").toLowerCase();
		score += occuranceAlgorithmScore(searchResult, answerCandidate);
		return score;
	}

	/*
	 * public static int getNumberOfResults(String result, String
	 * answerCandidate) { int score = 0;
	 * 
	 * }
	 */

	/**
	 * Converts a string to lower case
	 * 
	 * @param str
	 *            Input String
	 * @return Lower Case String
	 */
	public static String stringLowerCase(String str) {
		return str.toLowerCase();
	}

	/**
	 * Lower cases every single String of a string array
	 * 
	 * @param array
	 *            input array
	 * @return lower cased array
	 */
	public static String[] stringArrayLowerCase(String[] array) {
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].toLowerCase();
		}
		return result;
	}

	/**
	 * Basic scoring algorithm for how many occurrences the word shows up on the
	 * first 9 google headers
	 * 
	 * @param googleResult
	 *            The blob of search data you are looking in
	 * @param answerCandidate
	 *            The answer that you are checking the occurrence of
	 * @return Score for google occurrence test
	 */
	public static int occuranceAlgorithmScore(String googleResult, String answerCandidate) {
		int count = 0;

		count += numberOfTimesContained(googleResult, answerCandidate);

		return count;
	}

	/**
	 * Returns the number of times a substring exists in a larger string
	 * 
	 * @param totalText
	 *            The larger string you are searching in
	 * @param whatToFind
	 *            The substring that you are searching for
	 * @return number of times
	 */
	public static int numberOfTimesContained(String totalText, String whatToFind) {
		int lastIndex = 0;
		int count = 0;
		while (lastIndex != -1) {
			lastIndex = totalText.indexOf(whatToFind, lastIndex);
			if (lastIndex != -1) {
				count++;
				lastIndex += whatToFind.length();
			}
		}
		return count;
	}

}
