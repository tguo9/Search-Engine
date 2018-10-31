import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
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
	 * Data structure to store strings and their positions.
	 */

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;
	private final TreeMap<String, Integer> location;

	/**
	 * Initializes the map.
	 */
	public InvertedIndex() {
		this.index = new TreeMap<>();
		this.location = new TreeMap<>();
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public void add(String word, String path, int position) {

		index.putIfAbsent(word, new TreeMap<>());
		index.get(word).putIfAbsent(path, new TreeSet<>());
		index.get(word).get(path).add(position);
	}

	/**
	 * Writes the nested map of elements formatted as a nested pretty JSON object to
	 * the specified file.
	 *
	 * @param elements the elements to convert to JSON
	 * @param path     the path to the file write to output
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see #asNestedObject(TreeMap, Writer, int)
	 */
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
	public void toJSONResult(TreeMap<String, List<SearchResult>> results, Path path) throws IOException {

		JSONWriter.writesResult(results, path);
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public List<SearchResult> partialSearch(TreeSet<String> query) {

		ArrayList<SearchResult> searches = new ArrayList<>();

		TreeMap<String, SearchResult> hello = new TreeMap<>();

		for (String word : query) {

			for (String k : index.keySet()) {

				if (k.startsWith(word)) {

					for (String path : index.get(k).keySet()) {

						if (hello.containsKey(path)) {

							hello.get(path).update((index.get(k).get(path).size()),
									(double) index.get(k).get(path).size() / location.get(path));

						} else {

							hello.put(path, new SearchResult(path, index.get(k).get(path).size(),
									(double) index.get(k).get(path).size() / location.get(path)));
						}

					}
				}
			}

		}

		searches.addAll(hello.values());
		Collections.sort(searches);

		return searches;

	}

	public boolean findPartial(String word) {

		for (String k : index.keySet()) {

			if (word.startsWith(k)) {

				return true;
			}
		}
		return false;
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public List<SearchResult> exactSearch(TreeSet<String> query) {

		ArrayList<SearchResult> searches = new ArrayList<>();

		TreeMap<String, SearchResult> hello = new TreeMap<>();

		for (String word : query) {

			if (index.containsKey(word)) {

				for (String path : index.get(word).keySet()) {

					if (hello.containsKey(path)) {

						hello.get(path).update((index.get(word).get(path).size()),
								(double) index.get(word).get(path).size() / location.get(path));

					} else {

						hello.put(path, new SearchResult(path, index.get(word).get(path).size(),
								(double) index.get(word).get(path).size() / location.get(path)));
					}

				}
			}

		}

		searches.addAll(hello.values());
		Collections.sort(searches);

		return searches;

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
