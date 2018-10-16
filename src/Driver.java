import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

// TODO To find your TODO comments... open Window -> Views -> Tasks

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
		ArgumentMap map = new ArgumentMap(args);

		InvertedIndex index = new InvertedIndex();

		if (map.hasFlag("-path")) {
			// TODO Will be simplified by the changes in your FileFinder class.
			ArrayList<Path> filenames = new ArrayList<>();

			Path path = map.getPath("-path");

			if (path == null) {

				return;
			}

			if (path != null && Files.isDirectory(path)) {
				try {
					FileFinder.traverse(path, filenames);
				} catch (IOException e) {
					e.getMessage();
				}

			} else if (Files.isRegularFile(path)) {

				filenames.add(path);
			}

			InvertedIndexBuilder.buildMap(filenames, index);
		}

		if (map.hasFlag("-index")) {
			Path indexFlag = map.getPath("-index", Paths.get("index.json"));
			
			try {
				index.toJSON(indexFlag);
			} catch (IOException e) {
				// TODO System.out.println("There is an error when writing JSON file: " + indexFlag);
				System.out.println("There is an error when writing JSON file");
				return;
			}
		}
	}
}