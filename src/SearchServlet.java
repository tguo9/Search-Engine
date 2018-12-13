import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

@SuppressWarnings("serial")
public class SearchServlet extends HttpServlet {

	private QueryParser queryParser;
	private boolean exact;

	public SearchServlet(InvertedIndex index, QueryParser queryParser) {
		this.queryParser = queryParser;
		exact = false;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String query = request.getParameter("query");
		String visitURL = request.getParameter("visit");
		Map<String, Cookie> cookies = CookieConfigServlet.getCookieMap(request);

		out.printf("<!DOCTYPE html>%n");
		out.printf("<html>%n");
		out.printf("<head>%n");
		out.printf("	<meta charset=\"utf-8\">%n");
		out.printf("	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">%n");
		out.printf("	<title>%s</title>%n", "Search Engine");
		out.printf(
				"	<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.2/css/bulma.min.css\">%n");
		out.printf("	<script defer src=\"https://use.fontawesome.com/releases/v5.3.1/js/all.js\"></script>%n");
		out.printf("</head>%n");
		out.printf("%n");
		out.printf("<body>%n");
		out.printf("	<section class=\"hero is-primary is-bold\">%n");
		out.printf("	  <div class=\"hero-body\">%n");
		out.printf("	    <div class=\"container\">%n");
		out.printf(
				"	      <img align=\"center\" width=\"50\" height=\"50\" src=\"https://s3.amazonaws.com/noosa2017/cdn_uploads/2017/06/30233100/pumpkin.svg\">%n");
		out.printf("	      </img>%n");
		out.printf("	      <h1  align=\"center\" class=\"title\">%n");
		out.printf("	        ZUCCA%n");
		out.printf("	      </h1>%n");
		out.printf("	      <h2 align=\\\"center\\\" class=\"subtitle\">%n");
		out.printf("					<i class=\"fas fa-calendar-alt\"></i>%n");
		out.printf("					&nbsp;Updated %s%n", getDate());
		out.printf("	      </h2>%n");
		out.printf("	    </div>%n");
		out.printf("	  </div>%n");
		
		
		out.printf("	</section>%n");

		out.print(CookieConfigServlet.getTracking() ? "<p>Your activities will not be tracked.</p>" : "");

		if (CookieConfigServlet.getTracking() == false) {
			Cookie queries = cookies.get("queries");
			if (queries != null) {
				String decoded = URLDecoder.decode(queries.getValue(), StandardCharsets.UTF_8.name());
				String encoded = URLEncoder.encode(decoded + "," + (query == null ? "" : query),
						StandardCharsets.UTF_8.name());
				queries.setValue(encoded);
			} else {
				String encoded = URLEncoder.encode(",", StandardCharsets.UTF_8.name());
				queries = new Cookie("queries", encoded);

			}
			response.addCookie(queries);

			if (visitURL != null) {
				Cookie visited = cookies.get("visited");
				if (visited != null) {
					String decoded = URLDecoder.decode(visited.getValue(), StandardCharsets.UTF_8.name());
					String encoded = URLEncoder.encode(decoded + "," + visitURL, StandardCharsets.UTF_8.name());
					visited.setValue(encoded);
				} else {
					String encoded = URLEncoder.encode(",", StandardCharsets.UTF_8.name());
					visited = new Cookie("visited", encoded);

				}
				response.addCookie(visited);
				response.sendRedirect(visitURL);
			}

			Cookie lastVisit = cookies.get("lastVisit");
			Cookie visitCount = cookies.get("visitCount");
			if (lastVisit != null && visitCount != null) {
				String decodedLastVisit = URLDecoder.decode(lastVisit.getValue(), StandardCharsets.UTF_8.name());
				int count = Integer.parseInt(visitCount.getValue());
				out.println("<p >Welcome, this is your " + count + " visit, your last visit was on " + decodedLastVisit
						+ "</p>");
				String encodedLastVisit = URLEncoder.encode(getDate(), StandardCharsets.UTF_8.name());
				lastVisit.setValue(encodedLastVisit);
				visitCount.setValue(Integer.toString(count + 1));
			} else {
				out.println("<p>Welcome!</p>");
				String encoded = URLEncoder.encode(getDate(), StandardCharsets.UTF_8.name());
				lastVisit = new Cookie("lastVisit", encoded);
				visitCount = new Cookie("visitCount", "1");
			}

			response.addCookie(lastVisit);
			response.addCookie(visitCount);
		}

		printForm(request, response);

		if (query != null) {
			long start = System.currentTimeMillis();
			List<SearchResult> results = search(query);
			long end = System.currentTimeMillis();
			if (results != null) {
				out.println("<p>" + results.size() + " results. (" + (end - start) / 1000.0 + " seconds)</p>");

				for (SearchResult result : results) {
					out.println("<a href=\"" + result.getPath() + "\" target=\"_blank\">"
							+ result.getPath() + "</a>\n");
					
					var stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
					var html = HTMLFetcher.fetchHTML(result.getPath(), 3);
					if (html == null) {
						return;
					}
					int count = 0;
					for (String s : TextParser.parse(HTMLCleaner.stripHTML(html))) {
						
						out.println("<p>" + stemmer.stem(s).toString() + "</p>\n");
						
						count++;
						
						if (count > 2) {
							
							break;
						}
					}
					
					out.println("<br/>\n");
				}
			}
		}

		out.printf("</body>%n");
		out.printf("</html>%n");
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		String query = request.getParameter("query") == null ? "" : request.getParameter("query");

		if (request.getParameterValues("mode") != null) {
			List<String> mode = Arrays.asList(request.getParameterValues("mode"));
			exact = mode.contains("partial") ? false : true;
			CookieConfigServlet.setTracking(mode.contains("private") ? true : false);
		} else {
			exact = true;
			CookieConfigServlet.setTracking(false);
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect(request.getServletPath() + "?query=" + query);

	}

	private void printForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();

		out.printf("<form method=\"post\" action=\"%s\">%n", request.getServletPath());
		out.printf("<table cellspacing=\"2\" cellpadding=\"2\"%n");
		out.printf("<tr>%n");
		out.printf("\t<td>%n");
		out.printf("\t\t<input type=\"checkbox\" name=\"mode\" value=\"partial\" " + (exact ? "" : "checked")
				+ ">Partial Search %n");
		out.printf("\t</td>%n");
		out.printf("\t<td>%n");
		out.printf("\t\t&nbsp;&nbsp;<input type=\"checkbox\" name=\"mode\" value=\"private\" "
				+ (CookieConfigServlet.getTracking() ? "checked" : "") + "> Private Search%n");
		out.printf("\t</td>%n");
		out.printf("</tr>%n");
		out.printf("<tr>%n");
		out.printf("\t<td nowrap>Query:</td>%n");
		out.printf("\t<td>%n");
		out.printf("\t\t<input type=\"text\" name=\"query\" maxlength=\"50\" size=\"40\"> %n");
		out.printf("\t</td>%n");
		out.printf("\t<td>%n");
		out.printf("<input type=\"submit\" value=\"Search\">");
		out.printf("\t</td>%n");
		out.printf("\t<td>%n");
		out.printf(
				"<p> <a href=\"/history\">Search History</a>&nbsp;&nbsp;&nbsp;<a href=\"/crawler\">Web Crawler</a></p>\n%n");
		out.printf("\t</td>%n");
		out.printf("</tr>%n");
		out.printf("</table>%n");

		out.printf("</form>\n%n");
	}

	private List<SearchResult> search(String query) throws IOException {

		queryParser.parseAndSearch(query, exact);

		String queries[] = TextParser.parse(query);
		Arrays.sort(queries);
		query = String.join(" ", queries);

		List<SearchResult> results = queryParser.printResult(query);

		return results;
	}

	/**
	 * Get the current date and time.
	 * 
	 * @return date
	 */
	public static String getDate() {
		String format = "yyyy-MM-dd hh:mm a";
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(Calendar.getInstance().getTime());
	}

}
