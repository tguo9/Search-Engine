import java.nio.file.Paths;
import java.util.ArrayList;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

// TODO Delete the old TODO comment!
// TODO Check the warnings before code review.

// TODO In Eclipse there is a "Organize Imports" feature that removes unused imports
// TODO Can configrue Eclipse to do this every time you save a file
// TODO Add Javadoc comments for all classes and methods



/**
 * Fill in your own comments!
 */
public class Driver {

	// TODO Path objects instead of String objects
	/**
	 * Find the files. Adapted from a FileFinder
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 */
	public static void traverse(Path path, ArrayList<Path> result) throws IOException {
		// TODO Can't use the File class!
		// TODO Have to use the Path, Paths, and Files classes instead

		// TODO Can't do listFiles anymore...
		// TODO Either use a DirectoryStream (see: https://github.com/usf-cs212-fall2018/lectures/blob/master/Files%20and%20Exceptions/src/DirectoryStreamDemo.java)
		// TODO Or use Files.walk(...) but make sure to turn on the symbolic link option (see Piazza)
		
		try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {
			// Efficiently iterate through the files and subdirectories.
			for (Path file : listing) {
				// Print the name with the proper padding/prefix.
				System.out.print(file.getFileName());

				// Check if this is a subdirectory
				if (Files.isDirectory(file)) {
					// Add a slash so we can tell it is a directory

					// Recursively traverse the subdirectory.
					// Add a little bit of padding so files in subdirectory
					// are indented under that directory.
					traverse(file);
				} else {
					// Add the file size next to the name
					result.add(file);
				}
			}
}
		

	}
	
	public static void traverse(Path directory) throws IOException {
		if (Files.isDirectory(directory)) {
			traverse(directory);
		}
}

	/*
	 * TODO
	 * Driver.main should not throw any exceptions in your production ready release.
	 * Instead of a stack trace, make sure the user sees user-friendly error messages.
	 */
	
	/*
	 * TODO
	 * Driver should have only project-specific command-line argument code
	 * All generally useful code should be in another class that another developer
	 * could reuse.
	 */
	
	// TODO https://github.com/usf-cs212-fall2018/template-project/blob/master/src/Driver.java#L8
	public static void main(String[] args) {
		ArgumentMap map = new ArgumentMap(args);

		// TODO Refactor this to "index"
		InvertedIndex index = new InvertedIndex();
		
		
		/*
		 * TODO Try to simplify Driver to make future projects easier
		 
		 if (map.hasFlag(-path)) {
		 	ArrayList<String> filenames = new ArrayList<>();
		 	etc. to build your index
		 }
		 
		 
		 if (map.hasFlag(-index)) {
		 	Path output = map.get(...)
		 	JSONWriter.writes(ii.getMap(), index);
		 }
		 
		 */
		
		
		
		
		

		// TODO ArrayList<Path>
		ArrayList<Path> filenames = new ArrayList<>();

		SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);

		Path indexFlag = null;

		// Empty check
		if (map.hasFlag("-index")) {
			indexFlag = map.getPath("-index");

			if (indexFlag == null) {

				indexFlag = Paths.get("index.json");
			}

			InvertedIndex empty = new InvertedIndex();

			try {
				JSONWriter.writesEmpty(empty.getMap(), indexFlag);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (!map.hasFlag("-index")) {
			return;
		}

		// Bad argument check
		if (args.length < 2) {
			return;
		}

		Path path = map.getPath("-path");

		if (path != null && Files.isDirectory(path)) {
			try {
				traverse(path, filenames);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (Files.isRegularFile(path)) {

			filenames.add(path);
		}

		for (Path files : filenames) {
			// TODO Move this logic to a separate method and separate class
			// TODO Also create a buffered reader 
			// TODO try (BufferedReader reader = Files.newBufferedReader(path, StandardCharset.UTF8))
			try (BufferedReader reader = new BufferedReader(new FileReader(files.toString()))) {

				String thisLine = null;
				
				// Not sure why 0 is not working
				int indexCount = 1;

				while ((thisLine = reader.readLine()) != null) {

					String[] thatLine = TextParser.parse(thisLine);

					for (String word : thatLine) {

						String newWord = stemmer.stem(word).toString();
						index.add(newWord, files.toString(), indexCount);
						indexCount++;
					}
				}
			} catch (IOException e) {
				// TODO Improve
				e.getMessage();
			}
		}

		try {
			JSONWriter.writes(index.getMap(), indexFlag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}