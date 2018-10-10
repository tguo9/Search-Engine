import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// TODO Delete the old TODO comment!
// TODO Check the warnings before code review.

// TODO Add Javadoc comments for all classes and methods

/**
 * Fill in your own comments!
 */
public class Driver {

	// TODO Path objects instead of String objects

	/*
	 * TODO Driver.main should not throw any exceptions in your production ready
	 * release. Instead of a stack trace, make sure the user sees user-friendly
	 * error messages.
	 */

	/*
	 * TODO Driver should have only project-specific command-line argument code All
	 * generally useful code should be in another class that another developer could
	 * reuse.
	 */

	/**
	 * Parses the command-line arguments to build and use an in-memory search engine
	 * from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 */
	public static void main(String[] args) {
		ArgumentMap map = new ArgumentMap(args);

		InvertedIndex index = new InvertedIndex();

		InvertedIndex locatation = new InvertedIndex();

		if (map.hasFlag("-path")) {
			ArrayList<Path> filenames = new ArrayList<>();

			Path path = map.getPath("-path");

			if (path == null) {

				return;
			}

			if (path != null && Files.isDirectory(path)) {
				try {
					FileFinder.traverse(path, filenames);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.getMessage();
				}

			} else if (Files.isRegularFile(path)) {

				filenames.add(path);
			}

			InvertedIndexBuilder.buildMap(filenames, index);
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

				index.toJSONLoc(locationsFlag);
			} catch (IOException e) {

				System.out.println("There is an error when writing JSON file");
			}
		}

		if (map.hasFlag("-search")) {

			Path searchPath = map.getPath("-search");

			String mode = "partial";

			if (map.hasFlag("-exact")) {

				mode = "exact";

			}

			try {
				QueryParser.searcher(searchPath, index, mode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (map.hasFlag("-results")) {

			Path resultFlag = map.getPath("-results", Paths.get("results.json"));
		}

	}
}