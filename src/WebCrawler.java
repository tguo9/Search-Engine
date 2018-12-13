import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
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
public class WebCrawler extends HttpServlet {

	private int limit;
	private HashSet<URL> urls;
	private WorkQueue queue;
	private InvertedIndex index;
	private HashSet<URL> seeds;

	/**
	 * Initialize Crawler
	 * 
	 * @param index wordIndex to build
	 */
	public WebCrawler(WorkQueue queue, InvertedIndex index) {
		this.queue = queue;
		this.index = index;
		this.urls = new HashSet<>();
		this.seeds = new HashSet<URL>();
	}

	/**
	 * The method to crawl
	 * 
	 * @param url
	 * @param limit
	 */
	public void crawl(URL seed, int limit) {
		this.limit = limit;
		urls.add(seed);
		seeds.add(seed);
		queue.execute(new CrawlerTask(seed, limit));
		queue.finish();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.printf("<!DOCTYPE html>%n");
		out.printf("<html>%n");
		out.printf("<head>%n");
		out.printf("	<meta charset=\"utf-8\">%n");
		out.printf("	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">%n");
		out.printf("	<title>%s</title>%n", "Web Crawler");
		out.printf(
				"	<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.2/css/bulma.min.css\">%n");
		out.printf("	<script defer src=\"https://use.fontawesome.com/releases/v5.3.1/js/all.js\"></script>%n");
		out.printf("</head>%n");
		out.printf("%n");

		out.printf("	<section class=\"hero is-primary is-bold\">%n");
		out.printf("	  <div class=\"hero-body\">%n");
		out.printf("	    <div class=\"container\">%n");
		out.printf("	      <h1 align=\\\"center\\\" class=\"title\">%n");
		out.printf("	        Web Crawler%n");
		out.printf("	      </h1>%n");
		out.printf("	      </h2>%n");
		out.printf("	    </div>%n");
		out.printf("	  </div>%n");
		
		
		out.printf("	</section>%n");

		out.println("<h4>Seeds: </h4>");

		for (URL u : seeds) {
			out.println("<a href=\"" + u + "\">" + u + "</a>" + "<br/>");
		}

		out.print("<form id=\"form1\" name=\"form1\" method=\"post\" action=\"/crawler\">"
				+ "<p>Seeds URL:  <input type=\"text\" name=\"url\" size=\"20\" maxlength=\"70\"> Links Limit:  "
				+ "<input type=\"text\" name=\"limit\" size=\"20\" maxlength=\"70\">"

				+ "<input class=\\\"button is-primary\\\" type=\"submit\" name=\"submit\" id=\"submit\" value=\"Crawl\" /> <a href=\"/\">Go Back</a></p></form>");

		if (request.getParameter("error") != null) {
			out.println("<p><strong>Invalid Input</strong></p>");
		}
		out.println("<p>" + urls.size() + " links are crawled. </p>");
		out.println("<p>");
		synchronized (urls) {
			for (URL u : urls) {
				out.println("<a href=\"" + u + "\">" + u + "</a>" + "<br/>");
			}
		}
		out.println("</p>");
		out.printf("</head>%n");
		out.printf("</html>%n");
		
		
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String urlString = request.getParameter("url");
			String limit = request.getParameter("limit");

			this.limit = this.limit + Integer.parseInt(limit);

			if (urlString == null || limit == null) {
				throw new IllegalArgumentException();
			}
			URL url = new URL(urlString);

			crawl(url, this.limit);

			response.setStatus(HttpServletResponse.SC_OK);
			response.sendRedirect(request.getServletPath());

		} catch (IllegalArgumentException | MalformedURLException e) {
			response.sendRedirect(request.getServletPath() + "?error=1");
		}
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

				if (seeds.size() < limit) {
					ArrayList<URL> links = LinkParser.listLinks(url, LinkParser.fetchHTML(url));
					for (URL link : links) {
						synchronized (seeds) {
							if (seeds.size() >= limit) {
								break;
							} else {
								if (seeds.contains(link) == false) {
									seeds.add(link);
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
