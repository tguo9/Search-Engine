import java.nio.file.Paths;
import java.util.ArrayList;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

	// TODO
	// https://github.com/usf-cs212-fall2018/template-project/blob/master/src/Driver.java#L8
	public static void main(String[] args) {
		ArgumentMap map = new ArgumentMap(args);

		// TODO Refactor this to "index"
		InvertedIndex index = new InvertedIndex();

		/*
		 * TODO Try to simplify Driver to make future projects easier
		 * 
		 * if (map.hasFlag(-path)) { ArrayList<String> filenames = new ArrayList<>();
		 * etc. to build your index }
		 * 
		 * 
		 * if (map.hasFlag(-index)) { Path output = map.get(...)
		 * JSONWriter.writes(ii.getMap(), index); }
		 * 
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
				FileFinder.traverse(path, filenames);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (Files.isRegularFile(path)) {

			filenames.add(path);
		}

		for (Path files : filenames) {
			// TODO Move this logic to a separate method and separate class
			try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

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