package algorithms;

public class Algorithms {

	public static void main(String[] args) {

		System.out.println(primaryAlgorithm(GoogleSearcher.loadFullSearchableText(
				"Which country is considered to have the world’s largest lottery game"), "Spain"));
		///]]System.out.println(primaryAlgorithm("", "Dogs"));
		//System.out.println(primaryAlgorithm("", "Dogs"));

		//System.out.println(primaryAlgorithm("", "Dogs"));

	}

	public static String cleanOCRError(String text) {
		text = text.replaceAll("\n", " ").replaceAll("ﾓ", "\"").replaceAll("‘", "\'").replaceAll("ﾗ", "-")
				.replaceAll("ﬁ", "fi").replaceAll("tﾑ", "t'").replaceAll("“", "\"").replaceAll("E! ", "Et ");
		return text;
	}

	public static int primaryAlgorithm(String result, String answerCandidate) {

		int score = 0;
		result = stringLowerCase(result);
		answerCandidate = stringLowerCase(answerCandidate);

		score += occuranceAlgorithmScore(result, answerCandidate);
		return score;
	}

	/*
	 * public static int getNumberOfResults(String result, String
	 * answerCandidate) { int score = 0;
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
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
		String[] splitAnswers = answerCandidate.split(" ");

		for (String o : splitAnswers) {
			//	count += numberOfTimesContained(googleResult, o);
		}

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
