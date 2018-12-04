import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeSet;

/* 
 * methods in InvertedIndex
 * 
 * And use @Override annotation
 */

/**
 * The thread safe version of InvertedIndex
 * 
 * @author Tao
 *
 */
public class ThreadSafeInvertedIndex extends InvertedIndex {

	private final ReadWriteLock lock;

	/**
	 * The constructor for Thread Safe InvertedIndex
	 * 
	 */
	public ThreadSafeInvertedIndex() {

		super();
		lock = new ReadWriteLock();

	}

	@Override
	public void add(String word, String path, int position) {
		lock.lockReadWrite();
		try {

			super.add(word, path, position);
		} finally {

			lock.unlockReadWrite();
		}

	}
	
	/**
	 * Add all methods
	 * 
	 * @param other
	 */
	public void addAll(ThreadSafeInvertedIndex other) {
		lock.lockReadWrite();
		try {

			super.addAll(other);
		} finally {

			lock.unlockReadWrite();
		}

	}

	@Override
	public void toJSON(Path path) throws IOException {
		lock.lockReadOnly();
		try {

			super.toJSON(path);
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
	
	@Override
	public int words() {

		return super.words();
	}

	@Override
	public int size() {
		return super.size();
	}

	@Override
	public int size(String word) {
		return super.size(word);
	}

	@Override
	public int size(String word, String path) {
		return super.size(word, path);
	}

}
