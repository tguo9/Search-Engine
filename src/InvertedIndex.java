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
	private TreeMap<String, TreeMap<Path, TreeSet<Integer>>> map;

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
	public void add(String word, Path path, int position) {

		if (word == null) {
			
			return;
		}
		
		if (map.containsKey(word)) {
			
			TreeMap<Path, TreeSet<Integer>> pathMap = map.get(word);
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
			TreeMap<Path, TreeSet<Integer>> paths = new TreeMap<>(); 
			paths.put(path, indices);
			map.put(word, paths);
		}
		
//		var pathMap = new TreeMap<Path, TreeSet<Integer>>();
//		var set = new TreeSet<Integer>();
//
//		if (!map.containsKey(word)) {
//
//			set.add(position);
//			pathMap.put(path, set);
//			map.put(word, pathMap);
//
//
//		} else {
//
//			pathMap = map.get(word);
//
//			if (pathMap.containsKey(path)) {
//
//				set = pathMap.get(path);
//				set.add(position);
//			} else {
//
//				set.add(position);
//				pathMap.put(path, set);
//
//			}
//
//			map.put(word, pathMap);
//
//		}

	}
	
	public TreeMap<String, TreeMap<Path, TreeSet<Integer>>> getMap() {
		
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
