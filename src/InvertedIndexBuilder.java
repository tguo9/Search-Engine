import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class InvertedIndexBuilder {

	public static void buildMap(ArrayList<Path> filenames, Path path) {

		InvertedIndex index = new InvertedIndex();
		SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);

		for (Path files : filenames) {
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
		index.toJSON(indexFlag);
	}

}
