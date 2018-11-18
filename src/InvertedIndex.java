import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Data straturce for the project. Store all the data.
 * 
 * @author Tao Guo
 */
public class InvertedIndex {

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;

	/**
	 * Data structure to store strings and their positions.
	 */
	private final TreeMap<String, Integer> location;

	/**
	 * Initializes the map.
	 */
	public InvertedIndex() {
		this.index = new TreeMap<>();
		this.location = new TreeMap<>();
	}

	/**
	 * Add word to index
	 * 
	 * @param word
	 * @param path
	 * @param position
	 */
	public void add(String word, String path, int position) {

		index.putIfAbsent(word, new TreeMap<>());
		index.get(word).putIfAbsent(path, new TreeSet<>());
		index.get(word).get(path).add(position);

		location.putIfAbsent(path, 0);
		location.put(path, location.get(path) + 1);

		/*
		 * TODO What happens if add(...) is called twice with the same parameters? The
		 * second time, the position isn't actually re-added to the set. But, you still
		 * increase the count by one. To avoid this issue, fix the method like this:
		 * 
		 * index.putIfAbsent(word, new TreeMap<>()); index.get(word).putIfAbsent(path,
		 * new TreeSet<>()); boolean success = index.get(word).get(path).add(position);
		 * 
		 * if (success) { location.put(path, location.getOrDefault(path, 0) + 1); }
		 */
	}

	/**
	 * Check word contains in index
	 * 
	 * @param word
	 * @return
	 */
	public boolean contains(String word) {

		return index.containsKey(word);
	}

	/**
	 * Check word contains in location
	 * 
	 * @param word
	 * @param location
	 * @return
	 */
	public boolean contains(String word, String location) {

		return contains(word) ? index.get(word).containsKey(location) : false;
	}

	/**
	 * Check word contains in location and position
	 * 
	 * @param word
	 * @param location
	 * @param position
	 * @return
	 */
	public boolean contains(String word, String location, int position) {

		return contains(word, location) ? index.get(word).get(location).contains(position) : false;

	}

	/**
	 * The method for writing JSON
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void toJSON(Path path) throws IOException {
		JSONWriter.writes(index, path);
	}

	/**
	 * The method for writing location
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void toJSONLocations(Path path) throws IOException {
		JSONWriter.asObject(location, path);
	}

	/**
	 * The method for partial search
	 * 
	 * @param query
	 * @return
	 */
	public List<SearchResult> partialSearch(TreeSet<String> query) {

		ArrayList<SearchResult> searches = new ArrayList<>();

		HashMap<String, SearchResult> lookup = new HashMap<>();

		for (String word : query) {

			for (String key : index.keySet()) {

				if (!key.startsWith(word)) {
					break;
				}

				searchHelper(key, lookup, searches);
			}

		}

		Collections.sort(searches);

		return searches;

	}

	/**
	 * The method for exact search
	 * 
	 * @param query
	 * @return
	 */
	public List<SearchResult> exactSearch(TreeSet<String> query) {

		ArrayList<SearchResult> searches = new ArrayList<>();

		HashMap<String, SearchResult> lookup = new HashMap<>();

		for (String word : query) {

			if (index.containsKey(word)) {

				searchHelper(word, lookup, searches);
			}

		}

		Collections.sort(searches);

		return searches;

	}

	/**
	 * The helper method for search
	 * 
	 * @param word
	 * @param lookup
	 * @param searches
	 */
	private void searchHelper(String word, HashMap<String, SearchResult> lookup, ArrayList<SearchResult> searches) {

		for (String path : index.get(word).keySet()) {

			if (lookup.containsKey(path)) {

				lookup.get(path).update((index.get(word).get(path).size()));

			} else {
				SearchResult result = new SearchResult(path, index.get(word).get(path).size(), location.get(path));
				lookup.put(path, result);
				searches.add(result);
			}

		}
	}

	/**
	 * 
	 * @return the size of the words
	 */
	public int words() {

		return index.size();
	}

	/**
	 * Return the toString
	 */
	@Override
	public String toString() {
		return this.index.toString();
	}

	/**
	 * 
	 * @return size of the map
	 */
	public int size() {
		return index.size();
	}

	/**
	 * Return the amount of paths found in a specific word.
	 * 
	 * @param word to be checked
	 * @return the amount of paths under the word, 0 if the word is not found.
	 */
	public int size(String word) {
		return contains(word) ? index.get(word).size() : 0;
	}

	/**
	 * Return the amount of indices found in a specific path under a word.
	 * 
	 * @param word to be checked
	 * @param path to be checked
	 * 
	 * @return the amount of indices under the path, 0 if the word or path is not
	 *         found.
	 */
	public int size(String word, String path) {
		return contains(word, path) ? index.get(word).get(path).size() : 0;
	}

}
