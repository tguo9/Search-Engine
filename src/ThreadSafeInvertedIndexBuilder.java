import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class ThreadSafeInvertedIndexBuilder extends InvertedIndexBuilder {

	/**
	 * A terrible and naive approach to determining if a number is prime.
	 *
	 * @param number to test if prime
	 * @return true if the number is prime
	 */
	public static void buildMap(ArrayList<Path> filenames, InvertedIndex index, WorkQueue queue) {

		for (Path files : filenames) {

			queue.execute(new BuildMapTask(files, index));
		}

		queue.finish();
	}

	public static class BuildMapTask implements Runnable {
		private Path path;
		private InvertedIndex index;

		public BuildMapTask(Path path, InvertedIndex index) {
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
