package algorithms;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import main.Config;

/**
 * This is where all the search and answer finding algorithms are located
 * 
 * @author James
 *
 */
public class Algorithms {

	/**
	 * Finds and replaces all potential OCR mistake cases
	 * 
	 * @param text
	 *            input text
	 * @return filtered text
	 */
	public static String cleanOCRError(String text) {
		for (String[] o : Config.ocrReplaceArray) {
			text = StringUtils.replaceAll(text, o[0], o[1]);
		}
		text = text.trim();
		return text;
	}

	/**
	 * This algorithm google searches "question" + " " + "potential answer". It
	 * gets the number of google page results and returns that number. It is
	 * used secondarily, if none of the answer strings are contained in the
	 * first 10 text thumbnails of a google search of the question
	 * 
	 * @param question
	 *            Cash Show Question
	 * @param answerCandidate
	 *            Potential answer
	 * @return Number of google page results for question + space + answer
	 * @throws IOException
	 */
	public static int googleResultsAlgorithm(String question, String answerCandidate) throws IOException {
		question = StringUtils.replaceAll(question, " ", "%20"); //question.replaceAll(" ", "%20");
		answerCandidate = answerCandidate.replaceAll(" ", "%20");
		return (int) (JSONUtils.getNumberOfResults(question + "%20" + answerCandidate));
	}

	/**
	 * Combined score calculation algorithm, all other algorithms are called in
	 * this
	 * 
	 * @param searchResult
	 *            Google Search Result
	 * @param answerCandidate
	 *            Potential answer
	 * @return Calculated Score
	 * @throws IOException
	 * @throws JSONException
	 */
	public static int primaryAlgorithm(String question, String googleResultsString, String answerCandidate)
			throws JSONException, IOException {

		int score = 0;
		googleResultsString = stringLowerCase(googleResultsString);
		if (answerCandidate.startsWith("A ")) {
			answerCandidate = answerCandidate.substring(2);
		}
		if (answerCandidate.startsWith("The ")) {
			answerCandidate = answerCandidate.substring(4);
		}

		answerCandidate = answerCandidate.toLowerCase();
		score += occuranceAlgorithmScore(googleResultsString, answerCandidate);
		Config.printStream.println((new StringBuilder("Searched for: ").append(answerCandidate)).toString());

		if (question.toLowerCase().contains(answerCandidate)) {
			Config.printStream.println("Question contains Answer String");
			score -= Algorithms.occuranceAlgorithmScore(googleResultsString.toLowerCase(),
					answerCandidate.toLowerCase());
		}

		return score;
	}

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
	 * Lower cases every single String inside a string array
	 * 
	 * @param array
	 *            Inputted array to be made completely lower case
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
	 * first 10 google headers. This is the ideal algorithm
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
		return StringUtils.countMatches(totalText, whatToFind);
	}

	public static String removeNegation(String str) {
		for (String o : Config.negationRemove) {
			str = str.replace(o, "");
		}
		return str;
	}

	public static String filterQuestionText(String str) {
		for (String o : Config.searchFilterTerms) {
			str = str.replace(o, "");
		}
		return str;
	}
}
