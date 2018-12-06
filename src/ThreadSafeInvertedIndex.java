import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeSet;

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

	@Override
	public int words() {
		lock.lockReadOnly();
		try {
			return super.size();
		} finally {

			lock.unlockReadOnly();
		}
	}

	@Override
	public int size() {
		lock.lockReadOnly();
		try {

			return super.size();
		} finally {

			lock.unlockReadOnly();
		}
	}

	@Override
	public int size(String word) {
		lock.lockReadOnly();
		try {

			return super.size(word);
		} finally {

			lock.unlockReadOnly();
		}
	}

	@Override
	public int size(String word, String path) {
		lock.lockReadOnly();
		try {

			return super.size(word, path);
		} finally {

			lock.unlockReadOnly();
		}
	}

	@Override
	public boolean contains(String word) {
		lock.lockReadOnly();
		try {
			return super.contains(word);
		} finally {

			lock.unlockReadOnly();
		}
	}

	@Override
	public boolean contains(String word, String location) {
		lock.lockReadOnly();
		try {
			return super.contains(word, location);
		} finally {

			lock.unlockReadOnly();
		}
	}

	@Override
	public boolean contains(String word, String location, int position) {
		lock.lockReadOnly();
		try {
			return super.contains(word, location, position);
		} finally {

			lock.unlockReadOnly();
		}

	}

	@Override
	public void toJSONLocations(Path path) throws IOException {

		lock.lockReadOnly();
		try {

			super.toJSONLocations(path);
		} finally {

			lock.unlockReadOnly();
		}
	}

}
