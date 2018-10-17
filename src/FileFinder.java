import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileFinder {

	public static boolean isTextFile(Path path) {
		String name = path.getFileName().toString().toLowerCase();
		return Files.isRegularFile(path) && (name.endsWith(".txt") || name.endsWith(".text"));
	}

	public static ArrayList<Path> traverse(Path path) throws IOException {
		ArrayList<Path> paths = new ArrayList<>();

		if (isTextFile(path)) {
			paths.add(path);
		} else {
			traverse(path, paths);
		}

		return paths;
	}

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
