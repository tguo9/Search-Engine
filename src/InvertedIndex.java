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

//	private 

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
	public List<SearchResult> partialSearch(TreeSet<String> arr) {

//		HashMap<String, SearchResult> results = new HashMap<>();
		ArrayList<SearchResult> finalResults = new ArrayList<>();

		for (String query : arr) {
			boolean found = false;
			for (String word : index.tailMap(query).keySet()) {
				if (word.startsWith(query)) {
//					search(word, results, finalResults);
					found = true;
				} else {
					if (found) {
						break;
					}
				}
			}
		}

		Collections.sort(finalResults);
		return finalResults;
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public List<SearchResult> exactSearch(TreeSet<String> query) {
		
//		HashMap<String, SearchResult> results = new HashMap<>();
//		ArrayList<SearchResult> finalResults = new ArrayList<>();
//
//		for (String word: query)
//		{
//			if (this.contains(word))
//			{
//				for (String path: index.get(word).keySet())
//				{
//					TreeSet<Integer> indices = index.get(word).get(path);
//
//					if (results.containsKey(path))
//					{
//						SearchResult result = results.get(path);
//						result.addFrequency(indices.size());
//						result.setPosition(indices.first());
//					} else
//					{
//						SearchResult result = new SearchResult(path, indices.size(), indices.iterator().next());
//						results.put(path, result);
//						finalResults.add(result);
//					}
//		}
//			}
//		}
//
//		Collections.sort(finalResults);
//return finalResults;
		
		List<SearchResult> results = new ArrayList<>();

		for (String word : query) {
			
				if (this.contains(word)) {
				TreeMap<String, Integer> remove = new TreeMap<>();
					
				for (String path : index.get(word).keySet()) {
					
					if (remove.containsKey(path)) {
						
						
						remove.put(path, remove.get(path)+1);
					} else {
						
						remove.put(path, 1);
					}
					
				}
				
				for (String p: remove.keySet()) {
					
					results.add(new SearchResult(p, remove.get(p), 1));
				}
				

				
				
			
			
		}
		/*
		int count = 1;

		for (String word : query) {

			System.out.println(word);
			
			

			if (this.contains(word)) {
				
				TreeSet<String> remove = new TreeSet<>();

				for (String path : index.get(word).keySet()) {
					
//					
//					
					for (SearchResult r: results) {
						
						if (r.getPath().equals(path)) {
							
							r.addFrequency(1);
						} else {
							
							results.add(r);
						}
					}
					
//					
//					
//
				}

			}
			*/

//			System.out.println(s);

//			if (index.get(s).keySet() != null) {
//				System.out.println(index.get(s).toString());
//			}

//			for (String x: index.get(s).keySet()) {

//					for (SearchResult r : results) {
//
//						r.setPosition(location.values().size());
//						results.add(r);
//
//					}

//			}

//		}

//		System.out.println(results.toString());
		}
		Collections.sort(results);

		return results;
		
			
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
