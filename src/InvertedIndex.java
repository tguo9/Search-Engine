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
		
		/*
		 * Update your location map here... add 1 to the count every time a word
		 * is added for a location
		 */
	}

	/*
	 * TODO
	 * This method allows the location map to be inconsistent with the index
	 * Because its public, anyone can do add(hello.txt, -12)
	 * 
	 * To protect data integrity... lets change this a bit.
	 */
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
	public void toJSONLoc(Path path) throws IOException { // TODO toJSONLocations ... locationsToJSON
		JSONWriter.asObject(location, path);
	}

	// TODO Move this method... most likely to your query parser.
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

		/*
		 * TODO (Fix this stuff for exact search too)
		 * Temporary storage! Why is this good??
		 * Map has a faster contains(...) method than a list, which requires a linear search.
		 * If we only need this map for the contains(...) method, do not use a TreeMap!
		 * Use a HashMap
		 * Also, call it "lookup" instead of hello
		 */
		TreeMap<String, SearchResult> hello = new TreeMap<>(); 

		for (String word : query) {

			/*
			 * TODO Currently looping through every key... but your keys are sorted!
			 * 
			 * 1) Have to start at the "right" key in the map... hints:
			 * Use either headMap or tailMap given the query... and an approach similar to:
			 * https://github.com/usf-cs212-fall2018/lectures/blob/master/Data%20Structures/src/FindDemo.java
			 * 
			 * 2) If you start at the right key, you can break when your key no longer starts with your query
			 */
			
			for (String k : index.keySet()) { // TODO Avoid the 1 letter variable name... String key : index.keySet()

				if (k.startsWith(word)) {

					for (String path : index.get(k).keySet()) {
						
						if (hello.containsKey(path)) {

							hello.get(path).update((index.get(k).get(path).size()),
									(double) index.get(k).get(path).size() / location.get(path));

						} else {

							hello.put(path, new SearchResult(path, index.get(k).get(path).size(),
									(double) index.get(k).get(path).size() / location.get(path)));
							
							/*
							 * TODO
							 * SearchResult result = new SearchResult(....)
							 * 
							 * hello.put(path, result);
							 * searches.add(result);
							 */
						}

					}
				}
			}

		}
		
		/*
		 * TODO Pull out the code of this loop since its the same in both partial and exact search,
		 * and make searchHelper(String key, look up map, result list)
		 */

		searches.addAll(hello.values()); // TODO Avoid this extra "loop"
		Collections.sort(searches);

		return searches;

	}

	// TODO Remove
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

	// TODO Breaks encapsulation
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

	// TODO Breaks encapsulation
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

	// TODO Javadoc
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
