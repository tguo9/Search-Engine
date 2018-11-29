
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

	/**
	 * Build this object
	 * 
	 * @param path
	 * @param matches
	 * @param total
	 */
	public SearchResult(String path, int matches, int total) {
		this.total = total;
		this.path = path;
		this.matches = matches;
		this.score = (double) matches / total;

	}

	/**
	 * Compare two object
	 * 
	 * @return result
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
	 * Increment the matches of the SearchResult
	 * 
	 * @param increment matches
	 */
	public void update(int matches) {
		this.matches += matches;
		this.score = (double) (this.matches) / (this.total);
	}

	/**
	 * Get the total number of words
	 * 
	 * @return total
	 */
	public int getTotal() {
		return this.total;
	}

	/**
	 * Get the matches
	 * 
	 * @return matches
	 */
	public int getMatches() {
		return this.matches;
	}

	/**
	 * Get the word path
	 * 
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Get the word score
	 * 
	 * @return score
	 */
	public double getScore() {

		return score;
	}

	/**
	 * String to print this object
	 * 
	 * @return string
	 */
	public String toString() {

		return "Path: " + path + " Matches: " + matches + " Score: " + score;

	}

}