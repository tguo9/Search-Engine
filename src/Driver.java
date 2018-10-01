import java.nio.file.Paths;
import java.util.ArrayList;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// TODO Delete the old TODO comment!
// TODO Check the warnings before code review.

// TODO In Eclipse there is a "Organize Imports" feature that removes unused imports
// TODO Can configrue Eclipse to do this every time you save a file

// TODO Add Javadoc comments for all classes and methods


/**
 * TODO Fill in your own comments!
 */
public class Driver {

	// TODO Path objects instead of String objects
	/**
	 * Find the files. Adapted from a FileFinder
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 */
	public static void findFiles(String path, ArrayList<String> result) throws IOException {
		// TODO Can't use the File class!
		// TODO Have to use the Path, Paths, and Files classes instead
		
		File input = new File(path);
		File[] children = input.listFiles();

		// TODO Can't do listFiles anymore...
		// TODO Either use a DirectoryStream (see: https://github.com/usf-cs212-fall2018/lectures/blob/master/Files%20and%20Exceptions/src/DirectoryStreamDemo.java)
		// TODO Or use Files.walk(...) but make sure to turn on the symbolic link option (see Piazza)
		
		for (File file : children) {

			if (file.isFile() && (((file.getName()).toLowerCase()).endsWith("txt"))
					|| ((file.getName()).toLowerCase()).endsWith("text")) {

				result.add(file.toString());

			} else if (file.isDirectory()) {

				findFiles(file.getPath(), result);
			}
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
	public static void main(String[] args) throws IOException {
		ArgumentMap map = new ArgumentMap(args);

		// TODO Refactor this to "index"
		InvertedIndex ii = new InvertedIndex();
		
		
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
		ArrayList<String> filenames = new ArrayList<>();

		SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);

		Path index = null;

		// Empty check
		if (map.hasFlag("-index")) {
			index = map.getPath("-index");

			if (index == null) {

				index = Paths.get("index.json");
			}

			InvertedIndex empty = new InvertedIndex();

			JSONWriter.writesEmpty(empty.getMap(), index);

		} else if (!map.hasFlag("-index")) {
			return;
		}

		// Bad argument check
		if (args.length < 2) {
			return;
		}

		Path path = map.getPath("-path");

		if (path != null && Files.isDirectory(path)) {
			findFiles(path.toString(), filenames);

		} else if (Files.isRegularFile(path)) {

			filenames.add(path.toString());
		}

		for (String files : filenames) {
			// TODO Move this logic to a separate method and separate class
			// TODO Also create a buffered reader 
			// TODO try (BufferedReader reader = Files.newBufferedReader(path, StandardCharset.UTF8))
			try (BufferedReader reader = new BufferedReader(new FileReader(files))) {

				String thisLine = null;
				
				// Not sure why 0 is not woring
				int indexCount = 1;

				while ((thisLine = reader.readLine()) != null) {

					String[] thatLine = TextParser.parse(thisLine);

					for (String word : thatLine) {

						String newWord = stemmer.stem(word).toString();
						ii.add(newWord, files.toString(), indexCount);
						indexCount++;
					}
				}
			} catch (IOException e) {
				// TODO Improve
				e.getMessage();
			}
		}

		JSONWriter.writes(ii.getMap(), index);
	}
}