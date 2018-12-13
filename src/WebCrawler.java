import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * A class to crawl web site
 * 
 * @author Tao
 *
 */
@SuppressWarnings("serial")
public class WebCrawler extends HttpServlet{

	private final HashSet<URL> linkSet;
	private final WorkQueue queue;
	private final ThreadSafeInvertedIndex index;

	/**
	 * The constructor for web crawler
	 * 
	 * @param worker
	 * @param index
	 */
	public WebCrawler(WorkQueue worker, ThreadSafeInvertedIndex index) {
		this.queue = worker;
		this.linkSet = new HashSet<URL>();
		this.index = index;
	}

	/**
	 * The method to crawl
	 * 
	 * @param url
	 * @param limit
	 */
	public void crawl(URL url, int limit) {
		linkSet.add(url);
		queue.execute(new CrawlerTask(url, limit));
		queue.finish();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		PrintWriter out = response.getWriter();
		out.printf("<html>%n%n");
		out.printf("<head><title>%s</title></head>%n", "HERE");
		out.printf("<body>%n");

		out.printf("<h1>Search Engine</h1>%n%n");

		out.printf("<p>This request was handled by thread %s.</p>%n", Thread.currentThread().getName());

		out.printf("%n</body>%n");
		out.printf("</html>%n");

		response.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect(request.getServletPath());
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
				if (html == null) {
					return;
				}

				InvertedIndex temp = new InvertedIndex();
				var stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
				int start = 1;
				for (String s : TextParser.parse(HTMLCleaner.stripHTML(html))) {
					temp.add(stemmer.stem(s).toString(), url.toString(), start);
					start++;
				}
				index.addAll(temp);

				if (linkSet.size() < limit) {
					ArrayList<URL> links = LinkParser.listLinks(url, LinkParser.fetchHTML(url));
					for (URL link : links) {
						synchronized (linkSet) {
							if (linkSet.size() >= limit) {
								break;
							} else {
								if (linkSet.contains(link) == false) {
									linkSet.add(link);
									queue.execute(new CrawlerTask(link, limit));

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
