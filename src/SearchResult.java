import java.text.DecimalFormat;
import java.text.NumberFormat;

/*
 * TODO Javadoc
 */

/**
 * Object for the search result.
 * 
 * @author Tao Guo
 */
public class SearchResult implements Comparable<SearchResult> {

	private int matches;
	private String path; // TODO make final
	private double score;
	
	// TODO private final int total; and set this in the constructor instead of passing in the score
	// TODO every time the matches value changes, you can use this to automatically update the score
	
	// TODO Remove for now.... (only formatting should happen at output)
	private NumberFormat FORMATTER = new DecimalFormat("#0.000000000000000");

	/**
	 * Constructor for building SearchResult object
	 * 
	 * @param path      the path of the query
	 * @param frequency how many times query appears
	 * @param position  the first position of query in the file.
	 */
	public SearchResult(String path, int matches, double score) {
	// TODO public SearchResult(String path, int matches, int total) { and use this.score = (double) matches / total; 
		this.path = path;
		this.matches = matches;
		this.score = score;
	}

	/**
	 * Compare SearchResult objects based on frequency, initial position and path.
	 * 
	 * @param s SearchResult Object for comparison
	 * @return comparison result of two SearchResult
	 */
	@Override
	public int compareTo(SearchResult other) {
		// TODO Can access private data directly, for example: int temp = Integer.compare(other.matches, this.matches);
		
		int temp = Double.compare(other.getScore(), this.getScore());
		if (temp == 0) {
			temp = Integer.compare(Integer.valueOf(other.getMatches()), Integer.valueOf(this.getMatches()));

			if (temp == 0) {
				temp = this.getPath().compareToIgnoreCase(other.getPath());
			}
		}
		return temp;
	}

	/**
	 * increment the frequency of this SearchResult
	 * 
	 * @param frequency increment amount
	 */
	public void update(int matches, double total) { // TODO Only need matches
		this.matches += matches;
		this.score += total;
	}

	/**
	 * @return the frequency
	 */
	public int getMatches() {
		return this.matches;
	}

	public void setMatches(Integer integer) { // TODO Remove?
		this.matches = integer;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	public double getScore() {
		String temp = FORMATTER.format(score);
		score = Double.valueOf(temp);

		return score;
	}

	public String toString() {

		return "Path: " + path + "Matches: " + matches + "Score: " + score;

	}

}
