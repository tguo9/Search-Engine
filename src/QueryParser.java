import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

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

				List<String> queries = TextFileStemmer.stemLine(line, stemmer);
				if (!queries.isEmpty()) {

					List<String> set = queries.stream().distinct().collect(Collectors.toList());
					queries.clear();
					queries = set;
					Collections.sort(queries);

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
