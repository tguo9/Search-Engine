import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Class for the InvertedIndex
 * 
 * @author Tao Guo
 */
public class InvertedIndexBuilder {

	/**
	 * Parses the command-line arguments to build and use an in-memory search engine
	 * from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 */
	public static void buildMap(ArrayList<Path> filenames, InvertedIndex index) {

		SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);

		for (Path files : filenames) {
			try (BufferedReader reader = Files.newBufferedReader(files, StandardCharsets.UTF_8)) {

				String thisLine = null;

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
				System.out.println("There is an error when build the map");;
			}
		}
	}

}