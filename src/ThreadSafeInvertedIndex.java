import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeSet;

/*
 * TODO NOT THREAD SAFE.
 * 
 * You are missing some methods. You need to override EVERY public method
 * in InvertedIndex and call the appropriate lock.
 * 
 * I ALREADY SAID THIS:
 * https://github.com/usf-cs212-fall2018/project-tguo9/blob/92f48fa83c0d932564337917f75934345781f6d9/src/ThreadSafeInvertedIndex.java
 * 
 * Do not make me repeat it again.
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

	@Override
	public void addAll(InvertedIndex other) {
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
