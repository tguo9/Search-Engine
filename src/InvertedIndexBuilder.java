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
	 * @throws IOException
	 */
	public static void buildMap(ArrayList<Path> filenames, InvertedIndex index) throws IOException {

		for (Path filename : filenames) {
			buildMap(filename, index);
		}
	}

	/**
	 * Parses the command-line arguments to build and use an in-memory search engine
	 * from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 * @return 0 if everything went well
	 * @throws IOException
	 */
	public static void buildMap(Path filename, InvertedIndex index) throws IOException {

		SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);

		try (BufferedReader reader = Files.newBufferedReader(filename, StandardCharsets.UTF_8)) {

			String thisLine = null;

			int indexCount = 1;
			
			// TODO call filename.toString() over and over and over again
			// TODO save the result here, and reuse later.
			// TODO e.g. String location = filename.toString()
			// TODO ... index.add(..., location, ...)

			while ((thisLine = reader.readLine()) != null) {

				String[] thatLine = TextParser.parse(thisLine);

				for (String word : thatLine) {

					String newWord = stemmer.stem(word).toString();
					index.add(newWord, filename.toString(), indexCount);
					indexCount++;
				}
			}

		}

	}

}
