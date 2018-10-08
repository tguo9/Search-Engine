import java.nio.file.Path;

public class SearchResult implements Comparable<SearchResult> {

	private Path path; //where
	private int position; //word position
	private int words; //count
	private int locations; 
	private int frequency; // frequency
	
	public SearchResult(Path path, int position, int frequency) {
		
		this.frequency = frequency;
		this.position = position;
		this.frequency = frequency;
	}
	
	public void addFrequency(int frequency){
		this.frequency += frequency;
	}

	
	public void setPosition(int position){
		this.position = position;
	}

	public int getFrequency() {
		return frequency;
	}

	public int getPosition(){
		return position;
	}

	public Path getPath(){
		return path;
	}
	
	@Override
	public int compareTo(SearchResult o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
