import java.nio.file.Path;

public class SearchResult implements Comparable<SearchResult> {

	private Path location; 
	private int words;
	private int locations; 
	private int frequency;
	
	public SearchResult() {
		
		
	}
	
	@Override
	public int compareTo(SearchResult o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
