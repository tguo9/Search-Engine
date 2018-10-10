import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Parser for Query Parsing
 * 
 * @author Tao Guo
 */
public class QueryParser {

	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */

	private final static TreeMap<String, List<SearchResult>> results = new TreeMap<>();

	public static void parseAndSearch(Path path, InvertedIndex index, String mode) throws IOException {

		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] queries = TextParser.parse(line);
				if (queries.length == 0) {
					continue;
				}
				Arrays.sort(queries);

				if (mode.equals("exact")) {

					results.put(String.join(" ", queries), index.exactSearch(queries));
				} else {

					results.put(String.join(" ", queries), index.partialSearch(queries));
				}
			}
		}
	}
	
	public static TreeMap<String, List<SearchResult>> getMap() {
		
		return results;
	}

}
