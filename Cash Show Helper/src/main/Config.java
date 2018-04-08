package main;

public class Config {
	// Google API restricts the number of times you can search, listed are different sets of API Keys

	// ahitler3652@gmail.com
	//	private static final String GOOGLE_API_KEY = "AIzaSyCrhcL_hOd-GyIyZ2xQSB5Q6vt3e_JvmFo";
	//	private static final String SEARCH_ENGINE_ID = "003884082171968744521:go5drm1boe0";

	// 2563nehcsemaj@gmail.com
	//	 private static final String GOOGLE_API_KEY = "AIzaSyCBZsoCMF2_lTzhOAWZ2YYzeced9Eyy4A0";
	//	 private static final String SEARCH_ENGINE_ID = "015208795528623639953:larljf01apm";

	// jchen3652@gmail.com
	public static final String GOOGLE_API_KEY = "AIzaSyDhVVASBNyr0U-trn5eFaoJrNQJoHbPVzM";
	public static final String SEARCH_ENGINE_ID = "017356742749847709225:4h4bt-iqizy";

	// Random github
	// private static final String GOOGLE_API_KEY = "AIzaSyBFnKBQPESdi2sP1twKp59-3mBscTVw99k";
	// private static final String SEARCH_ENGINE_ID = "014723624719242706501:ky6zn2teax4"; 

	
	// Vision Constants
	public static final int questionTextThreshold = 192; //191 tried and tested
	public static final int answerTextThreshold = 192; // 191 tried and tested
	
	public static final String questionOutputPath = "D:\\Users\\James\\Desktop\\questionimage.png";
	public static final String answersOutputPath = "D:\\Users\\James\\Desktop\\answerarea.png";
	
	public static final int[] grayRToleranceRange = {200, 225};
	public static final int[] grayGToleranceRange = {200, 225};
	public static final int[] grayBToleranceRange = {205, 225};

	public static final int[] greenRToleranceRange = {50, 75};
	public static final int[] greenGToleranceRange = {215, 245};
	public static final int[] greenBToleranceRange = {50, 70};
}
