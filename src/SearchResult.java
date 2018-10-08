import java.nio.file.Path;

public class SearchResult implements Comparable<SearchResult> {

	private Path location; 
	private int words;
	private int locations; 
	private int frequency;
	
	public SearchResult(Path location, int words, int locations, int frequency) {
		
		this.frequency = frequency;
		this.locations = locations;
		this.frequency = frequency;
	}
	
	
	
	@Override
	public int compareTo(SearchResult o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
