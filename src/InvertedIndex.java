import java.io.IOException;
import java.io.Writer;
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
	 * Data structure to store strings and their positions.
	 */

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;
	private final TreeMap<String, Integer> location;
	private final TreeMap<String, List<SearchResult>> scores;

//	private 

	/**
	 * Initializes the map.
	 */
	public InvertedIndex() {
		this.index = new TreeMap<>();
		this.location = new TreeMap<>();
		this.scores = new TreeMap<>();
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

		for (String word : query) {

			if (index.containsKey(word)) {

				for (String path : index.get(word).keySet()) {

					String curPath = path;

					if (scores.containsKey(word)) {

						List<SearchResult> res = scores.get(word);

						for (SearchResult nr : res) {

							if (nr.getPath().equals(curPath)) {

								nr.update(index.get(word).get(curPath).size(), 1);
							}
						}
					}
					searches.add(new SearchResult(curPath, index.get(word).get(curPath).size(), 1));
					scores.put(word, searches);
				}
			}

		}

		return searches;

//		for (String word : query) {
//
//			if (index.containsKey(word)) {
//
//				for (String path : index.get(word).keySet()) {
//
//					if (remove.containsKey(path)) {
//
//						// update
//						remove.get(path).update(index.get(word).get(path).size(), 1);
//					} else {
//
//						remove.put(path, new SearchResult(path, 1, 1));
//					}
//
//				}
//
//			}
//		}
//
//		List<SearchResult> results = new ArrayList<>(remove.values());
//
//		Collections.sort(results);
//
//		return results;
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


					if (scores.containsKey(word)) {

						List<SearchResult> res = scores.get(word);

						for (SearchResult nr : res) {

							if (nr.getPath().equals(path)) {

								nr.setMatches((index.get(word).get(path).size()));

							}
						}
					} else {
						
						hello.put(word,
								new SearchResult(path, index.get(word).get(path).size(), 
								(double)index.get(word).get(path).size()/location.get(path)));
						searches.add(new SearchResult(path, index.get(word).get(path).size(), (double)index.get(word).get(path).size()/location.get(path)));
						scores.put(word, searches);
						System.out.println(searches.toString());
					}
					
					
				}
			}

		}
		
		

		return searches;

//		HashMap<String, SearchResult> remove = new HashMap<>();
//
//		for (String word : query) {
//
//			if (index.containsKey(word)) {
//
//				for (String path : index.get(word).keySet()) {
//
//					if (remove.containsKey(path)) {
//
//						// update
//						remove.get(path).update(index.get(word).get(path).size(), 1);
//					} else {
//
//						remove.put(path, new SearchResult(path, 1, 1));
//					}
//
//				}
//
//			}
//		}
//
//		List<SearchResult> results = new ArrayList<>(remove.values());
//
//		Collections.sort(results);
//
//		return results;

//		TreeMap<String, SearchResult> remove = new TreeMap<>();
//
//		for (String word : query) {
//
//			if (index.containsKey(word)) {
//
//				for (String path : index.get(word).keySet()) {
//
//					if (scores.containsKey(word)) {
//
//						// get the whole list
//						// add
//
//						List<SearchResult> slist = scores.get(word);
//						boolean updated = false;
//
//						for (SearchResult s : slist) {
//
//							if (s.getPath().equals(path)) {
//
//								s.update(index.get(word).get(path).size(), 1);
//								updated = true;
//
//							} 
//							
//						}
//
//						if (updated == false) {
//							
//							slist.add(new SearchResult(path, index.get(word).get(path).size(), 1));
//						}
//						
//						scores.put(word, slist);
//
//					} else {
//
//						// crate a new one and put to map
//						List<SearchResult> newList = new ArrayList<>();
//						newList.add(new SearchResult(path, index.get(word).get(path).size(), 1));
//						scores.put(word, newList);
//					}
//				}
//			}
//			
////			System.out.println(scores.toString());
//
//		}
//		return scores.values();
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

	public TreeMap<String, List<SearchResult>> getScores() {

		return scores;
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
