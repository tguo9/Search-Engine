import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeSet;

public class ThreadSafeInvertedIndex extends InvertedIndex {

	private final ReadWriteLock lock;

	/**
	 * TODO
	 */
	public ThreadSafeInvertedIndex() {

		super();
		lock = new ReadWriteLock();

	}
	
	/*
	 * TODO Use the @Override annotation for all of these methods
	 * And you can skip the Javadoc
	 */
	
	// TODO Every public method in INvertedIndex must be overridden and locked
	// TODO Lock EVERYTHING
	// TODO Use the try/finally pattern: https://github.com/usf-cs212-fall2018/lectures/blob/master/Multithreading%20Synchronization/src/ConcurrentSet.java
	
	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public void add(String word, String path, int position) {

		super.add(word, path, position);
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
		return super.exactSearch(query);

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
