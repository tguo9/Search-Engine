import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadSafeInvertedIndex extends InvertedIndex {

	private final ReadWriteLock lock;

	public ThreadSafeInvertedIndex() {

		super();
		lock = new ReadWriteLock();

	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public void add(String word, String path, int position) {

		lock.lockReadWrite();
		super.add(word, path, position);
		lock.unlockReadWrite();
	}

	public void addAll(ThreadSafeInvertedIndex other) {

		super.addAll(other);
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public void toJSON(Path path) throws IOException {
		lock.lockReadOnly();
		super.toJSON(path);
		lock.unlockReadOnly();
	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public List<SearchResult> partialSearch(TreeSet<String> query) {

		lock.lockReadOnly();
		try {

			return super.partialSearch(query);
		} finally {

			lock.unlockReadOnly();
		}

	}

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public List<SearchResult> exactSearch(TreeSet<String> query) {

		lock.lockReadOnly();
		try {

			return super.exactSearch(query);
		} finally {

			lock.unlockReadOnly();
		}

	}

	/**
	 * Returns a string representation of this map.
	 */
	@Override
	public String toString() {
		lock.lockReadOnly();
		try {
			return super.toString();
		} finally {
			lock.unlockReadOnly();
		}
	}

}
