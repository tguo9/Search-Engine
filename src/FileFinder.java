import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Class for the finding the file
 * 
 * @author Tao Guo
 */
public class FileFinder {
	
	/**
	 * Find the files. Adapted from a FileFinder
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 */
	public static void traverse(Path path, ArrayList<Path> result) throws IOException {

		try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {
			// Efficiently iterate through the files and subdirectories.
			for (Path file : listing) {

				// Check if this is a subdirectory
				if (Files.isDirectory(file)) {
					// Add a slash so we can tell it is a directory
					// Recursively traverse the subdirectory.
					// Add a little bit of padding so files in subdirectory
					// are indented under that directory.
					traverse(file, result);
				} else {
					// Add the file size next to the name
					if (file.getFileName().toString().toLowerCase().endsWith("txt")
							|| file.getFileName().toString().toLowerCase().endsWith("text")) {

						result.add(file);
					}

				}
			}
		}

	}

}
