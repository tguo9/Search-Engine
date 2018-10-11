
/**
 * Object for the search result.
 * 
 * @author Tao Guo
 */
public class SearchResult implements Comparable<SearchResult> {

	private String path; // where
	private int position; // word position
	private int words; // count
	private int locations;
	private int frequency; // frequency

	/**
	 * Parses the command-line arguments to build and use an in-memory search engine
	 * from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 */
	public SearchResult(String path, int position, int frequency) {

		this.frequency = frequency;
		this.position = position;
		this.frequency = frequency;
		
	}

	/**
	 * Parses the command-line arguments to build and use an in-memory search engine
	 * from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 */
	public void addFrequency(int frequency) {
		this.frequency += frequency;
	}
	
	/**
	 * Parses the command-line arguments to build and use an in-memory search engine
	 * from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	/**
	 * Parses the command-line arguments to build and use an in-memory search engine
	 * from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 */
	public int getFrequency() {
		return frequency;
	}
	
	/**
	 * Parses the command-line arguments to build and use an in-memory search engine
	 * from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Parses the command-line arguments to build and use an in-memory search engine
	 * from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Parses the command-line arguments to build and use an in-memory search engine
	 * from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 */
	@Override
	public int compareTo(SearchResult o) {

		if (this.frequency == o.frequency) {
			if (this.position == o.position) {
				
				return path.compareTo(o.getPath());
			} else {
				
				return Integer.compare(position, o.position);
			}
		} else {
			
			return (-1) * Integer.compare(frequency, o.frequency);
		}
	}

}
