import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class TextFileStemmer {

	/**
	 * Returns a list of cleaned and stemmed words parsed from the provided line.
	 * Uses the English {@link SnowballStemmer.ALGORITHM} for stemming.
	 *
	 * @param line the line of words to clean, split, and stem
	 * @return list of cleaned and stemmed words
	 *
	 * @see SnowballStemmer
	 * @see SnowballStemmer.ALGORITHM#ENGLISH
	 * @see #stemLine(String, Stemmer)
	 */
	public static List<String> stemLine(String line) {
		// This is provided for you.
		return stemLine(line, new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH));
	}

	/**
	 * Returns a list of cleaned and stemmed words parsed from the provided line.
	 *
	 * @param line    the line of words to clean, split, and stem
	 * @param stemmer the stemmer to use
	 * @return list of cleaned and stemmed words
	 *
	 * @see Stemmer#stem(CharSequence)
	 * @see TextParser#parse(String)
	 */
	public static List<String> stemLine(String line, Stemmer stemmer) {
		List<String> returnList = new ArrayList<String>();

		String[] arr = TextParser.parse(line);

		for (int i = 0; i < arr.length; i++) {

			CharSequence cs = stemmer.stem(arr[i]);
			String temp = cs.toString();
			returnList.add(temp);
		}

		return returnList;
	}

	/**
	 * Reads a file line by line, parses each line into cleaned and stemmed words,
	 * and then writes that line to a new file.
	 *
	 * @param inputFile  the input file to parse
	 * @param outputFile the output file to write the cleaned and stemmed words
	 * @throws IOException if unable to read or write to file
	 *
	 * @see #stemLine(String)
	 * @see TextParser#parse(String)
	 */
	public static void stemFile(Path inputFile, Path outputFile) throws IOException {

		if (outputFile == null) {
			
			return;
		}
		
		String thisLine = null;

		try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8);
				BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {

			while ((thisLine = reader.readLine()) != null) {
				List<String> list = stemLine(thisLine);
				for (int i = 0; i < list.size(); i++) {
					writer.write(list.get(i));
					writer.write(" ");
				}
				writer.write(System.lineSeparator());
			}

		} catch (Exception e) {
			e.getMessage();
		}

	}

	/**
	 * Uses {@link #stemFile(Path, Path)} to stem a single hard-coded file. Useful
	 * for development.
	 *
	 * @param args unused
	 * @throws IOException
	 */
//	public static void main(String[] args) throws IOException {
//		Path inputPath = Paths.get("test", "words.tExT");
//		Path outputPath = Paths.get("out", "words.tExT");
//
//		Files.createDirectories(Paths.get("out"));
//
//		System.out.println(inputPath);
//		stemFile(inputPath, outputPath);
//	}
}