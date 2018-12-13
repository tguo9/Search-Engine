import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Parser for Query Parsing
 * 
 * @author Tao Guo
 */
public class QueryParser implements Query {

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

	@Override
	public void parseAndSearch(String path, boolean exact) throws IOException {

		var stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);

		TreeSet<String> queries = new TreeSet<>();
		TextFileStemmer.stemLine(path, stemmer, queries);

		if (!queries.isEmpty()) {

			String result = String.join(" ", queries);

			if (exact == true) {

				results.put(result, index.exactSearch(queries));

			} else {
				results.put(result, index.partialSearch(queries));

			}

		}
	}

	public List<SearchResult> printResult(String query) {

		return results.get(query);

	}

	@Override
	public void toJSONResult(Path path) throws IOException {

		JSONWriter.writesResult(results, path);
	}

}
