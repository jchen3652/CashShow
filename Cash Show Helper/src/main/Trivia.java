package main;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import algorithms.Algorithms;
import algorithms.HtmlParser;
import algorithms.JSONTools;
import chromeWindow.ChromeWindow;
import threads.GoogleSearcherThread;
import threads.PrimaryAlgorithmThread;
import vision.ScreenUtils;

public class Trivia {
	private String question = null;
	private JSONTools json;

	private String googleResult = null;
	private String[] answerCandidates = null;

	private boolean isNegated;
	private int[] allScores = new int[3];

	public Trivia() {

	}

	public Trivia(String question, String[] answerCandidates) {
		this.question = question;
		this.answerCandidates = answerCandidates;

	}

	public static void main(String[] args) throws IOException, AWTException {
		Robot robot = new Robot();
		System.setProperty("webdriver.chrome.args", "--disable-logging");
		System.setProperty("webdriver.chrome.silentOutput", "true");

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--log-level=3");

		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		ChromeDriver driver = new ChromeDriver(options);
		driver.switchTo().defaultContent();
		org.openqa.selenium.Point windowPosition = new org.openqa.selenium.Point(0, 0);
		driver.manage().window().setPosition(windowPosition);
		driver.manage().window().setSize(
				new Dimension((int) ((int) ScreenUtils.getScreenWidth() / 3.5), (int) ScreenUtils.getScreenHeight()));

		// 
		driver.get("about:blank");
		for (int i = 0; i < 5; i++) {
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_MINUS);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_MINUS);
		}
//oh rip
		String question = "what company was first to release in north america a video game console that stored games on compact discs?";
		question = StringUtils.lowerCase(question);
		question = Algorithms.filterQuestionText(question);
		question = Algorithms.removeNegation(question);
		System.out.println(question);
		String[] allAnswers = {"Sony", "Sega", "Philips"};

		ChromeWindow window = new ChromeWindow(driver, question);
		window.run();

		Trivia trivia = new Trivia();
		trivia.setQuestionText(question);
		trivia.setAnswerArray(allAnswers);
		GoogleSearcherThread gt = new GoogleSearcherThread();
		gt.setQuery(trivia.getFilteredQuestionText());
		gt.run();
		
		trivia.setJSONTools(gt.getResult());
		trivia.calculate();

		//		for(int i = 0; i < 3; i ++) {
		//			trivia.allScores[i] = Algorithms.googleResultsAlgorithm(question, allAnswers[i]);
		//		}

		trivia.printInfo();
	}

	public void calculate() throws IOException {
		String filteredQuestion = StringUtils.lowerCase(question);
		for (String o : Config.negatedGiveaways) {
			if (filteredQuestion.contains(o)) {
				isNegated = true;
				System.out.println("Is negated");
				Config.printStream.println("Is negated");
			} else {
				isNegated = false;
			}
		}
		googleResult = json.getAllSearchText();

		//		HtmlParserThread[] allParserThreads = new HtmlParserThread[2];
		//
		//		for (int i = 0; i < 2; i++) {
		//			allParserThreads[i] = new HtmlParserThread(json.getAllResultURLs().get(i));
		//
		//		}
		//
		//		boolean doneParsing = false;
		//		while (!doneParsing) {
		//			doneParsing = true;
		//			for (HtmlParserThread o : allParserThreads) {
		//				if (o.isFinished()) {
		//					(new StringBuilder(googleResult)).append(o.getText()).toString();
		//				} else {
		//					doneParsing = false;
		//				}
		//
		//			}
		//		}

		googleResult = (new StringBuilder(googleResult)).append(HtmlParser.getContainedText(json.getAllResultURLs(), Config.htmlToParse))
				.toString();
		filteredQuestion = Algorithms.filterQuestionText(filteredQuestion);
		if (isNegated) {
			filteredQuestion = Algorithms.removeNegation(filteredQuestion);
		}
		Config.printStream.println("Filtered question: " + filteredQuestion);
		PrimaryAlgorithmThread[] algorithms = new PrimaryAlgorithmThread[3];

		//		if (!isNegated) {
		for (int i = 0; i < 3; i++) {
			algorithms[i] = new PrimaryAlgorithmThread(filteredQuestion, googleResult, answerCandidates[i]);
			algorithms[i].run();
		}

		for (int i = 0; i < 3; i++) {
			try {
				allScores[i] += algorithms[i].getScore();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int numberOfZeros = 0;
		for (int o : allScores) {
			if (o == 0) {
				numberOfZeros++;
			}

		}
		System.out.println(numberOfZeros);

		// If the first algorithm failed, try deep search
		if ((allScores[0] < 1 && allScores[1] < 1 && (allScores[2] < 1)) || (isNegated && numberOfZeros == 2)) {
			Config.printStream.println("No results, alternate started");
			for (int i = 0; i < 3; i++) {
				allScores[i] = Algorithms.googleResultsAlgorithm(question, answerCandidates[i]);
			}

		}

		for (int i : allScores) {
			i = (int) Math.round(((double) i) / Config.googleResultsScaleDown);
		}
		//		} else {
		//			Main.console.println("Negation detected");
		//			for (int i = 0; i < 3; i++) {
		//				allScores[i] = Algorithms.googleResultsAlgorithm(question, answerCandidates[i]);
		//			}
		//		}

	}

	public String getAnswer(int index) {
		return answerCandidates[index];
	}

	public void setAnswer(String answer, int index) {
		answerCandidates[index] = answer;
	}

	public String[] getAnswerArray() {
		return answerCandidates;
	}

	public void setAnswerArray(String[] answerArray) {
		this.answerCandidates = answerArray;
	}

	public String getQuestionText() {
		return question;
	}

	public String getFilteredQuestionText() {
		return Algorithms.removeNegation(Algorithms.filterQuestionText(StringUtils.lowerCase(question)));
	}

	public void setQuestionText(String question) {
		this.question = question;
	}

	public void setJSONTools(JSONTools tools) {
		this.json = tools;
	}

	public int getScore(int index) {
		return allScores[index];
	}

	public void printInfo() {
		int largestIndex = 0;
		int largestScore = allScores[0];

		int smallestIndex = 0;
		int smallestScore = allScores[0];

		int totalScore = 0;
		for (int o : allScores) {
			totalScore += o;
		}

		for (int i = 0; i < 3; i++) {
			double percent = Math.round((double) allScores[i] / ((double) totalScore) * 100.0);
			Config.printStream.println((new StringBuilder(answerCandidates[i])).append(": ").append(allScores[i])
					.append(" (").append(percent).append("%)").toString());

			if (allScores[i] > largestScore) {
				largestScore = allScores[i];
				largestIndex = i;
			}

			if (allScores[i] < smallestScore) {
				smallestScore = allScores[i];
				smallestIndex = i;
			}

			allScores[i] = 0;
		}
		if (isNegated) {
			Config.printStream
					.println((new StringBuilder("Best Answer: ").append(answerCandidates[smallestIndex])).toString());
		} else {
			Config.printStream
					.println((new StringBuilder("Best Answer: ").append(answerCandidates[largestIndex])).toString());
		}
	}
}