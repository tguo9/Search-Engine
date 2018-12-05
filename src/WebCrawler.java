import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * A class to crawl webs
 * 
 * @author Tao
 *
 */
public class WebCrawler {

	private final HashSet<URL> linkSet;
	private final WorkQueue worker;
	private final ThreadSafeInvertedIndex index;

	public WebCrawler(WorkQueue worker, ThreadSafeInvertedIndex threadSafe) {
		this.worker = worker;
		this.linkSet = new HashSet<URL>();
		this.index = threadSafe;
	}

	/**
	 * The method to crawl
	 * 
	 * @param seed
	 * @param limit
	 */
	public void crawl(URL seed, int limit) {
		linkSet.add(seed);
		worker.execute(new CrawlerTask(seed, limit));
		worker.finish();
	}

	private class CrawlerTask implements Runnable {

		private final URL url;
		private final int limit;

		public CrawlerTask(URL url, int limit) {
			this.url = url;
			this.limit = limit;
		}

		@Override
		public void run() {
			try {

				var html = HTMLFetcher.fetchHTML(url, 3);
				if(html == null) {
					return;
				}

				InvertedIndex temp = new InvertedIndex();
				var stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
				int start = 1;
				for(String s: TextParser.parse(HTMLCleaner.stripHTML(html))) {
					temp.add(stemmer.stem(s).toString(), url.toString(), start++);
				}
				index.addAll(temp);

				if(linkSet.size() < limit) {
					ArrayList<URL> links = LinkParser.listLinks(url, LinkParser.fetchHTML(url));
					for (URL link : links) {
						synchronized (linkSet) {
							if (linkSet.size() >= limit) {
								break;
							} else {
								if (linkSet.contains(link) == false) {
									linkSet.add(link);
									worker.execute(new CrawlerTask(link, limit));

								}
							}
						}
					}
				}
			} catch (IOException e) {
				System.err.println("There is an error with: " + url.toString());
			}
		}
	}
}
