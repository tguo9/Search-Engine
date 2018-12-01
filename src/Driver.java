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
		InvertedIndex index = null;
		Query query = null;
		WorkQueue queue = null;

		if (map.hasFlag("-threads")) {
			multi = true;
			ThreadSafeInvertedIndex threadSafe = new ThreadSafeInvertedIndex();
			index = threadSafe;
			queue = new WorkQueue(Integer.valueOf(map.getString("-threads")));
			
			query = new ThreadSafeQueryParser(threadSafe, queue);

			if (map.hasFlag("-path")) {

				Path path = map.getPath("-path");

				if (path == null) {
					System.out.println("The path is invaild.");
					return;
				}

				try {
					ThreadSafeInvertedIndexBuilder.buildMap(FileFinder.traverse(path), index);
				} catch (IOException e) {
					System.out.println("There is an error when reading the file: " + path);
				}
			}			
		}
		else {
			index = new InvertedIndex();

			query = new QueryParser(index);

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
		}

		if (map.hasFlag("-index")) {
			Path indexFlag = map.getPath("-index", Paths.get("index.json"));
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

			try {
				query.parseAndSearch(searchPath, map.hasFlag("-exact"));
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