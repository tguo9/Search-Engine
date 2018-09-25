import java.nio.file.Paths;
import java.util.ArrayList;
import opennlp.tools.stemmer.snowball.SnowballStemmer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * TODO Fill in your own comments!
 */
public class Driver {

	/**
	 * Find the files. Adapted from a FileFinder
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 */
	public static void findFiles(String path, ArrayList<String> result) throws IOException {

		File input = new File(path);
		File[] children = input.listFiles();

		for (File file : children) {

			if (file.isFile() && (((file.getName()).toLowerCase()).endsWith("txt"))
					|| ((file.getName()).toLowerCase()).endsWith("text")) {

				result.add(file.toString());

			} else if (file.isDirectory()) {

				findFiles(file.getPath(), result);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		ArgumentMap map = new ArgumentMap(args);

		InvertedIndex ii = new InvertedIndex();

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
				e.getMessage();
			}
		}

		JSONWriter.writes(ii.getMap(), index);
	}
}