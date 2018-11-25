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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadSafeQueryParser implements Query {

	private final TreeMap<String, List<SearchResult>> results;
	private final WorkQueue queue;
	private final ThreadSafeInvertedIndex index;

	public ThreadSafeQueryParser(ThreadSafeInvertedIndex index, WorkQueue queue) {

		this.results = new TreeMap<>();
		this.index = index;
		this.queue = queue;
	}

	public void parseAndSearch(Path path, boolean exact) throws IOException {

		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {

				queue.execute(new QueryTask(line, exact));

			}

		}

		queue.finish();

	}

	public void toJSONResult(Path path) throws IOException {

		synchronized (results) {
			JSONWriter.writesResult(results, path);
		}
	}

	public class QueryTask implements Runnable {

		private String line;
		private boolean exact;
//		private TreeMap<String, List<SearchResult>> results;
//		private InvertedIndex index;

		public QueryTask(String line, boolean exact) {

			this.line = line;
			this.exact = exact;
//			this.results = results;
//			this.index = index;
		}

		@Override
		public void run() {
			List<String> queries = TextFileStemmer.stemLine(line);
			if (!queries.isEmpty()) {

				TreeSet<String> set = new TreeSet<>();

				for (String q : queries) {

					if (!set.contains(q)) {

						set.add(q);
					}
				}
				queries.clear();
				queries.addAll(set);
				Collections.sort(queries);

				List<SearchResult> temp;
				if (exact == true) {

					temp = index.exactSearch(set);

				} else {
					temp = index.partialSearch(set);

				}

				String key = String.join(" ", queries);
				synchronized (results) {
					results.put(key, temp);
				}

			}

		}

	}
}
