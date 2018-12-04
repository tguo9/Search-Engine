import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class ThreadSafeQueryParser implements Query {

	private final TreeMap<String, List<SearchResult>> results;
	private final WorkQueue queue;
	private final ThreadSafeInvertedIndex index;

	/**
	 * The Thread Safe version of query parser
	 * 
	 * @param index
	 * @param queue
	 */
	public ThreadSafeQueryParser(ThreadSafeInvertedIndex index, WorkQueue queue) {

		this.results = new TreeMap<>();
		this.index = index;
		this.queue = queue;
	}

	@Override
	public void parseAndSearch(Path path, boolean exact) throws IOException {

		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {

				queue.execute(new QueryTask(line, exact));

			}

		} finally {

			queue.finish();
		}

	}

	@Override
	public void toJSONResult(Path path) throws IOException {

		synchronized (results) {
			JSONWriter.writesResult(results, path);
		}
	}

	/**
	 * The inner class for each task
	 * 
	 * @author Tao
	 *
	 */
	public class QueryTask implements Runnable {

		private String line;
		private boolean exact;

		public QueryTask(String line, boolean exact) {

			this.line = line;
			this.exact = exact;
		}

		@Override
		public void run() {

			var stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
			TreeSet<String> queries = new TreeSet<>();
			TextFileStemmer.stemLine(line, stemmer, queries);

			if (!queries.isEmpty()) {

				String key = String.join(" ", queries);

				synchronized (results) {
					if (results.containsKey(key)) {
						return;
					}
				}

				List<SearchResult> temp;
				if (exact == true) {
					temp = index.exactSearch(queries);

				} else {
					temp = index.partialSearch(queries);
				}

				synchronized (results) {
					results.put(key, temp);
				}
			}

		}

	}
}
