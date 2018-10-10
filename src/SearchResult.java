
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

	public SearchResult(String path, int position, int frequency) {

		this.frequency = frequency;
		this.position = position;
		this.frequency = frequency;
	}

	public void addFrequency(int frequency) {
		this.frequency += frequency;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getFrequency() {
		return frequency;
	}

	public int getPosition() {
		return position;
	}

	public String getPath() {
		return path;
	}

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
