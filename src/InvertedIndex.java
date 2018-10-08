import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class InvertedIndex {

	/**
	 * Data structure to store strings and their positions.
	 */

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;

	/**
	 * Initializes the map.
	 */
	public InvertedIndex() {
		this.index = new TreeMap<>();
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

		if (index.containsKey(word)) {

			TreeMap<String, TreeSet<Integer>> pathMap = index.get(word);
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
			index.put(word, paths);
		}
	}

	public boolean contains(String word) {

		return index.containsKey(word);
	}

	public boolean contains(String word, String location) {

		return contains(word) ? index.get(word).containsKey(location) : false;
	}

	public boolean contains(String word, String location, int position) {

		return contains(word, location) ? index.get(word).get(location).contains(position) : false;

	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public void toJSON(Path path) throws IOException {
		JSONWriter.writes(index, path);
	}
	
	public void toJSONEmpty(Path path) throws IOException {
		
		TreeMap<String, TreeMap<String, TreeSet<Integer>>> empty = new TreeMap<>();
		
		JSONWriter.writesEmpty(empty, path);
	}

	public static ArrayList<String> partialSearch(String[] arr) {

		return null;
	}
	
	public int words() {
		
		return index.size();
	}
	
	public TreeMap<String, TreeSet<Integer>> locations(String word) {
		
		return index.get(word);
	}
	
	public TreeSet<Integer> positions(String word, String location) {
		
		return index.get(word).get(location);
	}

	/**
	 * Returns a string representation of this map.
	 */
	@Override
	public String toString() {
		return this.index.toString();
	}
}
