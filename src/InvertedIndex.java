import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Data straturce for the project.
 * Store all the data.
 * 
 * @author Tao Guo
 */
public class InvertedIndex {

	/**
	 * Data structure to store strings and their positions.
	 */

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;
	private final TreeMap<String, Integer> location;
//	private final TreeMap<String, ArrayList<SearchResult>> result;

	/**
	 * Initializes the map.
	 */
	public InvertedIndex() {
		this.index = new TreeMap<>();
		this.location = new TreeMap<>();
//		this.result = new TreeMap<>();
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

	public void add(String path, int count) {

		location.put(path, count);
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public boolean contains(String word) {

		return index.containsKey(word);
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public boolean contains(String word, String location) {

		return contains(word) ? index.get(word).containsKey(location) : false;
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
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

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public void toJSONEmpty(Path path) throws IOException {

		TreeMap<String, TreeMap<String, TreeSet<Integer>>> empty = new TreeMap<>();

		JSONWriter.writesEmpty(empty, path);
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public void toJSONLoc(Path path) throws IOException {
		JSONWriter.asObject(location, path);
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public void toJSONResult(Path path) throws IOException {
		
		if (QueryParser.getMap().isEmpty()) {
			return;
		}

		JSONWriter.writesResult(QueryParser.getMap(), path);
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public ArrayList<SearchResult> partialSearch(String[] arr) {

		HashMap<String, SearchResult> tempResults = new HashMap<>();
		ArrayList<SearchResult> returnResults = new ArrayList<>();

		for (String query : arr) {
			for (String word : index.keySet()) {
				if (word.startsWith(query)) {
					search(word, tempResults);
				}
			}
		}
		for (String s : tempResults.keySet()) {
			returnResults.add(tempResults.get(s));
		}

		Collections.sort(returnResults);
		return returnResults;
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public ArrayList<SearchResult> exactSearch(String[] arr) {

		HashMap<String, SearchResult> tempResults = new HashMap<>();
		ArrayList<SearchResult> returnResults = new ArrayList<>();

		for (String query : arr) {
			if (this.contains(query)) {
				search(query, tempResults);
			}
		}
		for (String s : tempResults.keySet()) {
			returnResults.add(tempResults.get(s));
		}

		Collections.sort(returnResults);
		return returnResults;
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	private void search(String query, HashMap<String, SearchResult> results) {
		for (String path : index.get(query).keySet()) {

			TreeSet<Integer> intSet = index.get(query).get(path);
			SearchResult newResult = new SearchResult(path, intSet.size(), intSet.iterator().next());
			SearchResult finalResult;
			if (results.containsKey(path)) {
				finalResult = combineResult(results.get(path), newResult);
			} else {
				finalResult = newResult;
			}
			results.put(path, finalResult);
		}

	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	private SearchResult combineResult(SearchResult thisResult, SearchResult otherResult) {
		thisResult.addFrequency(otherResult.getFrequency());
		thisResult.setPosition(Math.min(thisResult.getPosition(), otherResult.getPosition()));
		return thisResult;
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public int words() {

		return index.size();
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public TreeMap<String, TreeSet<Integer>> locations(String word) {

		return index.get(word);
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
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
