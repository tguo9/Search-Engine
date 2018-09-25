import java.util.TreeMap;
import java.util.TreeSet;

public class InvertedIndex {
	
	/**
	 * Data structure to store strings and their positions.
	 */

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private TreeMap<String, TreeMap<String, TreeSet<Integer>>> map;

	/**
	 * Initializes the map.
	 */
	public InvertedIndex() {
		this.map = new TreeMap<>();
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public void add(String word, String path, int position) {

		if (word == null) {
			
			return;
		}
		
		if (map.containsKey(word)) {
			
			TreeMap<String, TreeSet<Integer>> pathMap = map.get(word);
			if (pathMap.containsKey(path)) {
				
				var set = pathMap.get(path);
				set.add(position);
			} else {
				
				TreeSet<Integer> newSet = new TreeSet<>(); 
				newSet.add(position);
				pathMap.put(path, newSet);
			}
		} else {
			
			TreeSet<Integer> indices = new TreeSet<>(); 
			indices.add(position);
			TreeMap<String, TreeSet<Integer>> paths = new TreeMap<>(); 
			paths.put(path, indices);
			map.put(word, paths);
		}
	}
	
	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public TreeMap<String, TreeMap<String, TreeSet<Integer>>> getMap() {
		
		return map;
	}

	/**
	 * Returns a string representation of this map.
	 */
	@Override
	public String toString() {
		return this.map.toString();
	}
}
