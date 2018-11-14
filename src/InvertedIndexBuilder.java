import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class InvertedIndexBuilder {

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */
	public static void buildMap(ArrayList<Path> filenames, InvertedIndex index) throws IOException {

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
			}
		}
	}

}
