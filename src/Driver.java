import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Driver for the project.
 * 
 * @author Tao Guo
 */
public class Driver {

	/**
	 * Parses the command-line arguments to build and use an in-memory search engine
	 * from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 */
	public static void main(String[] args) {

		boolean multi = false;

		ArgumentMap map = new ArgumentMap(args);

		ThreadSafeInvertedIndex threadSafe = null;

		InvertedIndex index = null;

		Query query = null;

		WorkQueue queue = null;

		if (map.hasValue("-threads") && (Integer.valueOf(map.getString("-threads")) > 0)) {

			multi = true;
			queue = new WorkQueue(Integer.valueOf(map.getString("-threads")));

			threadSafe = new ThreadSafeInvertedIndex();

			index = threadSafe;

			query = new ThreadSafeQueryParser(threadSafe, queue);

			if (map.hasValue("-path")) {
				try {
					ThreadSafeInvertedIndexBuilder.buildMap(FileFinder.traverse(Paths.get(map.getString("-path"))),
							threadSafe, queue);
				} catch (IOException e) {
					System.out.println("There is an error to build map");
					return;
				}
			}
		} else {
			index = new InvertedIndex();

			query = new QueryParser(index);

			if (map.hasValue("-path")) {
				try {
					InvertedIndexBuilder.buildMap(FileFinder.traverse(Paths.get(map.getString("-path"))), index);
				} catch (IOException e) {
					System.out.println("There is an error to build map");
					return;
				}
			}
		}

		if (map.hasFlag("-path")) {

			Path path = map.getPath("-path");

			if (path == null) {
				System.out.println("The path is invaild.");
				return;
			}

			try {
				InvertedIndexBuilder.buildMap(FileFinder.traverse(path), index);
			} catch (IOException e) {
				System.out.println("There is an error when reading the file: " + path);
			}

		}

		Path indexFlag = null;
		if (map.hasFlag("-index")) {
			indexFlag = map.getPath("-index", Paths.get("index.json"));
			try {
				index.toJSON(indexFlag);
			} catch (IOException e) {

				System.out.println("There is an error when writing JSON file");
				return;
			}
		}

		if (map.hasFlag("-locations")) {

			Path locationsFlag = map.getPath("-locations", Paths.get("locations.json"));

			try {

				index.toJSONLocations(locationsFlag);
			} catch (IOException e) {

				System.out.println("There is an error when writing JSON file");
			}
		}

		if (map.hasFlag("-search")) {

			Path searchPath = map.getPath("-search");

			boolean mode = false;

			if (map.hasFlag("-exact")) {

				mode = true;

			}

			try {
				query.parseAndSearch(searchPath, mode);
			} catch (IOException e) {
				System.out.println("There is an error when writing JSON file");
				return;
			}

		}

		if (map.hasFlag("-results")) {

			Path resultFlag = map.getPath("-results", Paths.get("results.json"));

			try {
				query.toJSONResult(resultFlag);
			} catch (IOException e) {
				System.out.println("There is an error when writing JSON file");
			}
		}

		if (multi) {
			queue.shutdown();
		}

	}
}