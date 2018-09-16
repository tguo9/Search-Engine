import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class InvertedIndex {

	/**
	 * Data structure to store strings and their positions.
	 */

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private TreeMap<String, TreeMap<Paths, TreeSet<Integer>>> map;

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
	public boolean add(String words, Paths path, int position) {

		var pathMap = new TreeMap<Paths, TreeSet<Integer>>();
		var set = new TreeSet<Integer>();

		if (!map.containsKey(words)) {

			set.add(position);
			pathMap.put(path, set);
			map.put(words, pathMap);

			return true;

		} else {

			set = pathMap.get(path);

			if (set.contains(position)) {
				return false;
			}
			set.add(position);

			pathMap.put(path, set);
			map.put(words, pathMap);

			return true;
		}

	}

	/**
	 * Adds the array of words at once, assuming the first word in the array is at
	 * position 1.
	 *
	 * @param words array of words to add
	 * @return true if this map is changed as a result of the call (i.e. if one or
	 *         more words or positions were added to the map)
	 *
	 * @see #addAll(String[], int)
	 */
	public boolean addAll(Path[] words) {
		return addAll(words, 1);
	}

	/**
	 * Adds the array of words at once, assuming the first word in the array is at
	 * the provided starting position
	 *
	 * @param words array of words to add
	 * @param start starting position
	 * @return true if this map is changed as a result of the call (i.e. if one or
	 *         more words or positions were added to the map)
	 */
	public boolean addAll(String[] words, int start) {

		int count = start;

		for (int i = 0; i < words.length; i++) {

			if (add(words[i], i + start)) {

				count++;
			}

		}
		if (count == start) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the number of times a word was found (i.e. the number of positions
	 * associated with a word in the map).
	 *
	 * @param word word to look for
	 * @return number of times the word was found
	 */
	public int count(String word) {

		return map.get(word).size();
	}

	/**
	 * Returns the number of words stored in the map.
	 *
	 * @return number of words
	 */
	public int words() {

		return map.size();
	}

	/**
	 * Tests whether the map contains the specified word.
	 *
	 * @param word word to look for
	 * @return true if the word is stored in the map
	 */
	public boolean contains(String word) {

		return map.containsKey(word);
	}

	/**
	 * Tests whether the map contains the specified word at the specified position.
	 *
	 * @param word     word to look for
	 * @param position position to look for word
	 * @return true if the word is stored in the map at the specified position
	 */
	public boolean contains(String word, int position) {

		if (map.containsKey(word)) {
			if (map.get(word).contains(position)) {
				return true;
			}
			return false;
		}
		return false;
	}

//	/**
//	 * Returns a copy of the words in this map as a sorted list.
//	 *
//	 * @return sorted list of words
//	 *
//	 * @see ArrayList#ArrayList(java.util.Collection)
//	 * @see Collections#sort(List)
//	 */
//	public ArrayList<String> copyWords() {
//
//		var returnList = new ArrayList<String>();
//		Collections.sort(returnList);
//		return returnList;
//	}

//	/**
//	 * Returns a copy of the positions for a specific word.
//	 *
//	 * @param word to find in map
//	 * @return sorted list of positions for that word
//	 *
//	 * @see ArrayList#ArrayList(java.util.Collection)
//	 * @see Collections#sort(List)
//	 */
//	public ArrayList<Integer> copyPositions(String word) {
//
//		if (map.containsKey(word)) {
//			ArrayList<Integer> returnList = new ArrayList<Integer>(map.get(word));
//			Collections.sort(returnList);
//			return returnList;
//		} else {
//
//			return new ArrayList<Integer>();
//		}
//	}

	/**
	 * Returns a string representation of this map.
	 */
	@Override
	public String toString() {
		return this.map.toString();
	}
}
