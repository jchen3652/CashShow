package main;

public class Config {
	public static final String mainDirectory = "D:\\Users\\James\\Desktop\\";
	public static final String screenshotIdentifier = "testscreenshot.png";

	//************************************************************************************************
	// Google API restricts the number of times you can search, listed are different sets of API Keys

	//dtrump3652@gmail.com
	public static final String GOOGLE_API_KEY = "AIzaSyBzRCDL-xwRaIosRsprqkfE5wPxQyZTwqg";
	public static final String SEARCH_ENGINE_ID = "016621176033020077306:dcds0p6z8xs";

	//nibbakilla3652@gmail.com
	//	public static final String GOOGLE_API_KEY = "AIzaSyAUnxZBnD6Ea6eK_2Rm_z0KhVOL7ENZByg";
	//	public static final String SEARCH_ENGINE_ID = "008475191042483784633:9jfsg3fl0tm";

	// ahitler3652@gmail.com
	//	public static final String GOOGLE_API_KEY = "AIzaSyCrhcL_hOd-GyIyZ2xQSB5Q6vt3e_JvmFo";
	//	public static final String SEARCH_ENGINE_ID = "003884082171968744521:go5drm1boe0";

	// 2563nehcsemaj@gmail.com
	//	public static final String GOOGLE_API_KEY = "AIzaSyCBZsoCMF2_lTzhOAWZ2YYzeced9Eyy4A0";
	//	public static final String SEARCH_ENGINE_ID = "015208795528623639953:larljf01apm";

	// jchen3652@gmail.com
	//	public static final String GOOGLE_API_KEY = "AIzaSyDhVVASBNyr0U-trn5eFaoJrNQJoHbPVzM";
	//	public static final String SEARCH_ENGINE_ID = "017356742749847709225:4h4bt-iqizy";

	// Random github
	//	 public static final String GOOGLE_API_KEY = "AIzaSyBFnKBQPESdi2sP1twKp59-3mBscTVw99k";
	//	 public static final String SEARCH_ENGINE_ID = "014723624719242706501:ky6zn2teax4"; 
	//************************************************************************************************
	public static final int[] phoneScreenArea = {680, 40, 557, 990};

	public static final boolean isDebug = true;

	public static final double googleResultsScaleDown = 1;

	// Vision Constants
	public static final int questionTextThreshold = 197; //191 tried and tested
	public static final int answerTextThreshold = 220; // 191 tried and tested

	public static final String questionOutputPath = "D:\\Users\\James\\Desktop\\questionimage.png";
	public static final String answersOutputPath = "D:\\Users\\James\\Desktop\\answerarea.png";

	public static final int timerPixelXLocation = 951;
	public static final int timerPixelYLocation = 135;

	public static final int[] grayRTOL = {200, 230};
	public static final int[] grayGTOL = {200, 230};
	public static final int[] grayBTOL = {205, 230};

	public static final int[] greenRTOL = {45, 80};
	public static final int[] greenGTOL = {210, 250};
	public static final int[] greenBTOL = {45, 75};

	public static final int[] whiteRTOL = {254, 255};
	public static final int[] whiteGTOL = {254, 255};
	public static final int[] whiteBTOL = {254, 255};

	public static final int whitePixelXLocation = 950;
	public static final int whitePixelYLocation = 335;

}
