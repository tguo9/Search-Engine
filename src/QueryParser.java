import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
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

	private final TreeMap<String, List<SearchResult>> results;
	private final InvertedIndex index;

	/**
	 * Build the query
	 * 
	 * @param index
	 */
	public QueryParser(InvertedIndex index) {

		this.results = new TreeMap<>();
		this.index = index;
	}

	/*
	 * TODO Try to clean up the method below based on what you have learned so far
	 * about efficiency. Simplify, reduce loops, remove temporoary storage, avoid
	 * creating too many objects. If you get stuck or want to be sure you fixed it
	 * correctly, post on Piazza and directly link to this method in your
	 * repository!
	 */
	/**
	 * Parse and search method.
	 * 
	 * @param path
	 * @param exact
	 * @throws IOException
	 */
	public void parseAndSearch(Path path, boolean exact) throws IOException {
		SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);

		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				/*
				 * TODO It is inefficient to stem the line into a list, and then move the
				 * contents of that list into a set.
				 * 
				 */

				List<String> queries = TextFileStemmer.stemLine(line, stemmer);
				if (!queries.isEmpty()) {

					TreeSet<String> set = new TreeSet<>();

					for (String q : queries) {

						if (!set.contains(q)) {

							set.add(q);
						}
					}
					queries.clear();
					queries.addAll(set);

					String result = String.join(" ", queries);

					if (results.containsKey(result)) {

						continue;
					}

					if (exact == true) {

						results.put(result, index.exactSearch(set));

					} else {
						results.put(result, index.partialSearch(set));

					}

				}

			}

		}

	}

	/**
	 * Write the result to JSON file
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void toJSONResult(Path path) throws IOException {

		JSONWriter.writesResult(results, path);
	}

}
