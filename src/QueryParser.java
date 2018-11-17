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

	// TODO Clean up old Javadoc
	/**
	 * Adds the word and the position it was found to the map.
	 *
	 * @param words    word to clean and add to map
	 * @param position position word was found
	 * @return true if this map did not already contain this word and position
	 */

	private TreeMap<String, List<SearchResult>> results; // TODO Make final
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
	 * TODO Try to clean up the method below based on what you have learned so
	 * far about efficiency. Simplify, reduce loops, remove temporoary storage,
	 * avoid creating too many objects. If you get stuck or want to be sure you
	 * fixed it correctly, post on Piazza and directly link to this method in
	 * your repository!
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

		List<String> queries = null; // TODO Remove

		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				/*
				 * TODO It is inefficient to stem the line into a list,
				 * and then move the contents of that list into a set.
				 * 
				 */
				
				
				queries = TextFileStemmer.stemLine(line, stemmer);
				if (!queries.isEmpty()) {

					TreeSet<String> set = new TreeSet<>();

					// TODO Why did you stem a SECOND time, and this time without using the stemmer???
					queries = TextFileStemmer.stemLine(line);

					for (String q : queries) {

						if (!set.contains(q)) {

							set.add(q);
						}
					}
					queries.clear();
					queries.addAll(set);
					Collections.sort(queries); // TODO The set is already sorted!

					// TODO Why call join(...) multiple times? Save the result so you do not have to recalculate this!
					if (results.containsKey(String.join(" ", queries))) {

						continue;
					}

					if (exact == true) {

						results.put(String.join(" ", queries), index.exactSearch(set));

					} else {
						results.put(String.join(" ", queries), index.partialSearch(set));

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
