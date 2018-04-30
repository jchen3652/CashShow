package algorithms;

public class GoogleSearcherThread implements Runnable {
	private String googleResult;
	private String question;
	boolean isFinished;

	public GoogleSearcherThread(String question) {
		this.question = question;
	}
	
	public GoogleSearcherThread() {
		
	}

	@Override
	public void run() {
		googleResult = GoogleSearcher.getGoogleResultsString(question);
		isFinished = true;
	}
	
	public void setQuery(String query) {
		question = query;
	}
	
	public String getResult() {
		while(isFinished != true) {
		}
		return googleResult;
	}
}
