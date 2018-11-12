import java.text.DecimalFormat;
import java.text.NumberFormat;

/*
 * This is a class to store the search result. It will give the matches, score, path and score.
 * Given the compareTo to sort the list.
 */

/**
 * Object for the search result.
 * 
 * @author Tao Guo
 */
public class SearchResult implements Comparable<SearchResult> {

	private int matches;
	private final String path;
	private double score;
	private final int total;

	// TODO Remove for now.... (only formatting should happen at output)
	private NumberFormat FORMATTER = new DecimalFormat("#0.000000000000000");

	/**
	 * Constructor for building SearchResult object
	 * 
	 * @param path      the path of the query
	 * @param frequency how many times query appears
	 * @param position  the first position of query in the file.
	 */
	public SearchResult(String path, int matches, int total) {
		this.total = total;
		this.path = path;
		this.matches = matches;
		this.score = (double) matches / total;

	}

	/**
	 * Compare SearchResult objects based on frequency, initial position and path.
	 * 
	 * @param s SearchResult Object for comparison
	 * @return comparison result of two SearchResult
	 */
	@Override
	public int compareTo(SearchResult other) {

		int temp = Double.compare(other.getScore(), this.getScore());
		if (temp == 0) {
			temp = Integer.compare(Integer.valueOf(other.matches), Integer.valueOf(this.matches));

			if (temp == 0) {
				temp = this.path.compareToIgnoreCase(other.path);
			}
		}
		return temp;
	}

	/**
	 * increment the frequency of this SearchResult
	 * 
	 * @param increment amount
	 */
	public void update(int matches, double total) {
		this.matches += matches;
//		this.score += total;
	}
	
	/**
	 * @return the matches
	 */
	public int getTotal() {
		return this.total;
	}

	/**
	 * @return the matches
	 */
	public int getMatches() {
		return this.matches;
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
