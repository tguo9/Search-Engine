
/**
 * Object for the search result.
 * 
 * @author Tao Guo
 */
public class SearchResult implements Comparable<SearchResult> {

	private int matches;
	private String path;
	private double score;

	/**
	 * Constructor for building SearchResult object
	 * 
	 * @param path      the path of the query
	 * @param frequency how many times query appears
	 * @param position  the first position of query in the file.
	 */
	public SearchResult(String path, int matches, double score) {
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
	public int compareTo(SearchResult s) {
		return (this.path).compareTo(s.path);
	}

	/**
	 * increment the frequency of this SearchResult
	 * 
	 * @param frequency increment amount
	 */
	public void update(int matches, int total) {
		this.matches += matches;
		this.score = (this.matches) / total;
	}

	/**
	 * @return the frequency
	 */
	public int getMatches() {
		return this.matches;
	}
	
	public void setMatches(Integer integer)
	{
		this.matches = integer;
}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	
	public double getScore() {
		
		return score;
	}
	
	public String toString() {
		
		
		return "Path: " + path + "Matches: " + matches + "Score " + score;
		
		
	}

}
