package main;

import java.io.PrintStream;

import consoleOutput.CashShowNew;
import consoleOutput.ConsoleOutput;

public class Config {
//		public static final PrintStream printStream = System.out;
	public static final CashShowNew printStream = Main.console;
	
	public static final int msWaitForQuestionTime = 750;
	public static final boolean internetIsShit = false;
	public static final int htmlToParse = 5;
	public static final boolean isDebug = false;

	/**
	 * This is used to tell the program whether the show is a pre-recorded show
	 * or a live show, because the sequences for each are different. The program
	 * knows how to do both, just tell it which one it is in here
	 * 
	 */
	public static boolean isLiveShow;

	//************************************************************************************************
	// This program uses Google's customsearch API to search for answers, and there is a daily 100
	// request limit. If the program returns a 403 error, change the config by commenting the old
	// information and commenting a new config

	// dtrump
//		public static final String GOOGLE_API_KEY = "AIzaSyBzRCDL-xwRaIosRsprqkfE5wPxQyZTwqg";
//		public static final String SEARCH_ENGINE_ID = "016621176033020077306:dcds0p6z8xs";

	//nibbakilla
//		public static final String GOOGLE_API_KEY = "AIzaSyAUnxZBnD6Ea6eK_2Rm_z0KhVOL7ENZByg";
//		public static final String SEARCH_ENGINE_ID = "008475191042483784633:9jfsg3fl0tm";

	// ahitler  //ded
//		public static final String GOOGLE_API_KEY = "AIzaSyCrhcL_hOd-GyIyZ2xQSB5Q6vt3e_JvmFo";
//		public static final String SEARCH_ENGINE_ID = "003884082171968744521:go5drm1boe0";

	// 	nehc //ded
	//	public static final String GOOGLE_API_KEY = "AIzaSyCBZsoCMF2_lTzhOAWZ2YYzeced9Eyy4A0";
	//	public static final String SEARCH_ENGINE_ID = "015208795528623639953:larljf01apm";

	// jcehn
		public static final String GOOGLE_API_KEY = "AIzaSyDhVVASBNyr0U-trn5eFaoJrNQJoHbPVzM";
		public static final String SEARCH_ENGINE_ID = "017356742749847709225:4h4bt-iqizy";

	// Random github
	//	 public static final String GOOGLE_API_KEY = "AIzaSyBFnKBQPESdi2sP1twKp59-3mBscTVw99k";
	//	 public static final String SEARCH_ENGINE_ID = "014723624719242706501:ky6zn2teax4"; 

	//************************************************************************************************

	/**
	 * Where all screenshots will be stored. If a blank string, screenshots stay
	 * in the project folder
	 */
	public static final String mainDirectory = "";

	public static final String phoneScreenIdentifier = "phoneScreen.png";
	public static final String questionOutputPath = mainDirectory + "questionimage.png";

	// Old useless constants
	//************************************************************************************************
	@Deprecated
	public static final int[] phoneScreenArea = {682, 40, 557, 990};
	@Deprecated
	public static final int timerPixelXLocation = 951;
	@Deprecated
	public static final int timerPixelYLocation = 133;
	@Deprecated
	public static final int whitePixelXLocation = 950;
	@Deprecated
	public static final int whitePixelYLocation = 335;
	//************************************************************************************************

	public static final double[] rawQuestionLocation = {36.0, 181.0, 480.0, 129.0};
	public static final double[] rawAnswer1Location = {77.0, 402.0, 325.0, 77.0};
	public static final double[] rawAnswer2Location = {77.0, 516.0, 325.0, 77.0};
	public static final double[] rawAnswer3Location = {77.0, 619.0, 325.0, 77.0};

	public static final double googleResultsScaleDown = 1;

	// Vision constants
	//************************************************************************************************
	public static final int questionTextThreshold = 191; //191 tried and tested
	public static final int answerTextThreshold = 191; // 191 tried and tested

	public static final double timerXLocation = 269.0;
	public static final double timerYLocation = 93.0;

	public static final int[] grayRTOL = {200, 230};
	public static final int[] grayGTOL = {200, 230};
	public static final int[] grayBTOL = {205, 230};

	public static final int[] greenRTOL = {45, 80};
	public static final int[] greenGTOL = {210, 250};
	public static final int[] greenBTOL = {45, 75};

	public static final int[] whiteRTOL = {254, 255};
	public static final int[] whiteGTOL = {254, 255};
	public static final int[] whiteBTOL = {254, 255};

	public static final int whiteXLocation = 501;
	public static final int whiteYLocation = 330;

	/**
	 * Combinations of characters that should automatically be replaced on text
	 * from OCR
	 */
	public static final String[][] ocrReplaceArray = {{"\n", " "}, {",", ","}, {"‘", "\'"}, {"ﬁ", "fi"}, {"“", "\""},
			{"”", "\""}, {"u n", "un"}, {"—", "-"}, {"ofthe", "of the"}, {"ﾻ", "-"}};

	/**
	 * Combinations of characters that should automatically be replaced on text
	 * from a google search
	 */
	public static final String[][] googleResultReplaceList = {{"&nbsp;...", " "}};

	/**
	 * Strings that will be removed form the question
	 */
	public static final String[] searchFilterTerms = {"which of the following ", "of these ", "one of"};

	/**
	 * Strings that show that the question is negated
	 */
	public static final String[] negatedGiveaways = {" not ", "n't "};

	/**
	 * Strings that will be removed from the question if it is negated
	 */
	public static final String[] negationRemove = {"is not", "n't"};

	/**
	 * This doesn't do anything rn
	 */
	public static final String[] uselessWords = {" a ", " the ", " what ", " is ", " an ", " of "};

}
