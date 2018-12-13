import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Demonstrates how to create, use, and clear cookies.
 *
 * @see CookieBaseServlet
 * @see CookieIndexServlet
 * @see CookieConfigServlet
 */
@SuppressWarnings("serial")
public class CookieConfigServlet extends CookieBaseServlet {

	private static boolean tracking = true;

	/**
	 * return tracking
	 * 
	 * @return boolean of Do not track, true for private mode, false for tracking
	 *         mode.
	 */
	public static boolean getTracking() {
		return tracking;
	}

	/**
	 * Set tracking
	 * 
	 * @param DNT boolean, true for private mode, false for tracking mode.
	 */
	public static void setTracking(boolean tracking) {
		CookieConfigServlet.tracking = tracking;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log.info("GET " + request.getRequestURL().toString());

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");

		out.printf("<!DOCTYPE html>%n");
		out.printf("<html>%n");
		out.printf("<head>%n");
		out.printf("	<meta charset=\"utf-8\">%n");
		out.printf("	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">%n");
		out.printf("	<title>%s</title>%n", "History");
		out.printf(
				"	<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.2/css/bulma.min.css\">%n");
		out.printf("	<script defer src=\"https://use.fontawesome.com/releases/v5.3.1/js/all.js\"></script>%n");
		out.printf("</head>%n");
		out.printf("%n");
		out.printf("<body>%n");
		out.printf("	<section class=\"hero is-primary is-bold\">%n");
		out.printf("	  <div class=\"hero-body\">%n");

		out.printf(
				"	      <img align=\"center\" width=\"50\" height=\"50\" src=\"https://s3.amazonaws.com/noosa2017/cdn_uploads/2017/06/30233100/pumpkin.svg\">%n");
		out.printf("	      </img>%n");
		out.printf("	    <div class=\"container\">%n");
		out.printf("	      <h1 class=\"title\">%n");
		out.printf("	        History Board%n");
		out.printf("	      </h1>%n");
		out.printf("	      <h2 class=\"subtitle\">%n");
		out.printf("					<i class=\"fas fa-calendar-alt\"></i>%n");
		out.printf("					&nbsp;Updated %s%n", getShortDate());
		out.printf("	      </h2>%n");
		out.printf("	    </div>%n");
		out.printf("	  </div>%n");
		out.printf("	</section>%n");
		out.printf("%n");

		Map<String, Cookie> cookies = getCookieMap(request);

		out.print("<h2>Search History: </h2>");
		Cookie queries = cookies.get("queries");
		if (queries != null) {
			out.println("<p>");
			String decoded = URLDecoder.decode(queries.getValue(), StandardCharsets.UTF_8.name());
			System.out.println(decoded);
			for (String query : decoded.split(",")) {
				if (!query.trim().isEmpty()) {
					out.printf("[%s]<br/>", query);
				}
			}
			out.println("</p>");
		} else {
			out.print("<p>No search history</p>");
			String encoded = URLEncoder.encode(",", StandardCharsets.UTF_8.name());
			queries = new Cookie("queries", encoded);

		}
		out.printf("<form method=\"post\" action=\"%s\">%n", request.getRequestURI());
		out.printf("\t<input type=\"submit\" name=\"clear\" value=\"Clear search history\">%n");
		out.printf("</form>%n");

		out.print("<h2>Visit History: </h2>");
		Cookie visited = cookies.get("visited");
		if (visited != null) {
			out.println("<p>");
			String decoded = URLDecoder.decode(visited.getValue(), StandardCharsets.UTF_8.name());
			for (String url : decoded.split(",")) {
				if (!url.trim().isEmpty()) {
					out.println("<a href=\"" + url + "\"> " + url + "</a><br/>");
				}
			}
			out.println("</p>");
		} else {
			out.print("<p>No visit history</p>");
			String encoded = URLEncoder.encode(",", StandardCharsets.UTF_8.name());
			visited = new Cookie("visited", encoded);
		}
		out.printf("<form method=\"post\" action=\"%s\">%n", request.getRequestURI());
		out.printf("\t<input type=\"submit\" name=\"clear\" value=\"Clear visit history\">%n");
		out.printf("\t<br/><br/><br/><input type=\"submit\" name=\"clear\" value=\"Clear all history and cookies\">%n");
		out.printf("\t<a href=\"/\">back</a>%n");
		out.printf("</form>%n");

		out.print(
				"<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js\"></script>\r\n    <script src=\"js/bootstrap.min.js\"></script>");
		out.printf("</body>%n");
		out.printf("</html>%n");
		response.addCookie(queries);
		response.addCookie(visited);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("POST " + request.getRequestURL().toString());
		Map<String, Cookie> cookies = getCookieMap(request);

		String value = request.getParameter("clear");
		if (value.equals("Clear search history")) {
			Cookie queries = cookies.get("queries");
			if (queries != null) {
				queries.setValue(null);
				queries.setMaxAge(0);
				response.addCookie(queries);
			}
		} else if (value.equals("Clear visit history")) {
			Cookie visited = cookies.get("visited");
			if (visited != null) {
				visited.setValue(null);
				visited.setMaxAge(0);
				response.addCookie(visited);
			}
		} else if (value.equals("Clear all history and cookies")) {
			clearCookies(request, response);
		}

		response.sendRedirect(request.getServletPath());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
