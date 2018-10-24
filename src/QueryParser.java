import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

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

	private final TreeMap<String, List<SearchResult>> results;
	private static SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);

	public QueryParser(InvertedIndex index) {

		this.results = new TreeMap<>();
	}

	public TreeMap<String, List<SearchResult>> parseAndSearch(Path path, InvertedIndex index, String mode)
			throws IOException {

		List<String> queries = null;

		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				queries = TextFileStemmer.stemLine(line, stemmer);
				if (!queries.isEmpty()) {

					TreeSet<String> set = new TreeSet<>();

					queries = TextFileStemmer.stemLine(line);

					for (String q : queries) {

						if (!set.contains(q)) {

							set.add(q);
						}
					}
					queries.clear();
					queries.addAll(set);
					Collections.sort(queries);

					if (results.containsKey(String.join(" ", queries))) {

						continue;
					}

					if (mode.equals("exact")) {
//						System.out.println(set.toString());
						results.put(String.join(" ", queries), index.exactSearch(set));

					} else {
						results.put(String.join(" ", queries), index.partialSearch(set));

					}

				}
			}

		}

		return results;
	}

	public void loopSet() {

	}

}
