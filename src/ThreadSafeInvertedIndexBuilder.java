import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * The thread safe version of InvertedIndexBuilder
 * 
 * @author Tao
 *
 */
public class ThreadSafeInvertedIndexBuilder extends InvertedIndexBuilder {

	/**
	 * A terrible and naive approach to determining if a number is prime.
	 *
	 * @param number to test if prime
	 * @return true if the number is prime
	 */
	public static void buildMap(List<Path> filenames, ThreadSafeInvertedIndex index, WorkQueue queue) {

		for (Path files : filenames) {

			queue.execute(new BuildMapTask(files, index));
		}

		queue.finish();
	}

	/**
	 * The inner class for each thread
	 * 
	 * @author Tao
	 *
	 */
	public static class BuildMapTask implements Runnable {
		private Path path;
		private ThreadSafeInvertedIndex index;

		public BuildMapTask(Path path, ThreadSafeInvertedIndex index) {
			this.path = path;
			this.index = index;
		}

		public void run() {
			try {

				InvertedIndex local = new InvertedIndex();
				InvertedIndexBuilder.buildMap(path, local);

				index.addAll(local);

			} catch (IOException e) {
				System.out.println("There is an error while duild the map");
			}
		}

	}

}
