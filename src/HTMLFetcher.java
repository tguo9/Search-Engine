import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLFetcher {

	/**
	 * Given a map of headers (as returned either by
	 * {@link URLConnection#getHeaderFields()} or by
	 * {@link HttpsFetcher#fetchURL(URL)}, determines if the content type of the
	 * response is HTML.
	 *
	 * @param headers map of HTTP headers
	 * @return true if the content type is html
	 *
	 * @see URLConnection#getHeaderFields()
	 * @see HttpsFetcher#fetchURL(URL)
	 */
	public static boolean isHTML(Map<String, List<String>> headers) {

		if (headers.containsKey("Content-Type")) {
			if (headers.get("Content-Type").toString().contains("html")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Given a map of headers (as returned either by
	 * {@link URLConnection#getHeaderFields()} or by
	 * {@link HttpsFetcher#fetchURL(URL)}, returns the status code as an int value.
	 * Returns -1 if any issues encountered.
	 *
	 * @param headers map of HTTP headers
	 * @return status code or -1 if unable to determine
	 * @throws IOException
	 *
	 * @see URLConnection#getHeaderFields()
	 * @see HttpsFetcher#fetchURL(URL)
	 */
	public static int getStatusCode(Map<String, List<String>> headers) throws IOException {
		if (headers.containsKey(null)) {

			ArrayList<String> matches = new ArrayList<String>();
			Pattern pattern = Pattern.compile("\\s\\d{3}\\s");
			String string = headers.get(null).toString();
			Matcher match = pattern.matcher(string);
			int index = 0;
			while ((index < string.length()) && match.find(index)) {
				matches.add(string.substring(match.start(), match.end()));
				if (match.start() == match.end()) {
					index = match.end() + 1;
				} else {
					index = match.end();
				}
			}

			return Integer.parseInt(String.join("", matches).trim());
		}
		return -1;
	}

	/**
	 * Given a map of headers (as returned either by
	 * {@link URLConnection#getHeaderFields()} or by
	 * {@link HttpsFetcher#fetchURL(URL)}, returns whether the status code
	 * represents a redirect response *and* the location header is properly
	 * included.
	 *
	 * @param headers map of HTTP headers
	 * @return true if the HTTP status code is a redirect and the location header is
	 *         non-empty
	 * @throws IOException
	 *
	 * @see URLConnection#getHeaderFields()
	 * @see HttpsFetcher#fetchURL(URL)
	 */
	public static boolean isRedirect(Map<String, List<String>> headers) throws IOException {
		int status = getStatusCode(headers);
		return (status >= 300) && (status < 400);
	}

	/**
	 * Uses {@link HttpsFetcher#fetchURL(URL)} to fetch the headers and content of
	 * the specified url. If the response was HTML, returns the HTML as a single
	 * {@link String}. If the response was a redirect and the value of redirects is
	 * greater than 0, will return the result of the redirect (decrementing the
	 * number of allowed redirects). Otherwise, will return {@code null}.
	 *
	 * @param url       the url to fetch and return as html
	 * @param redirects the number of times to follow a redirect response
	 * @return the html as a single String if the response code was ok, otherwise
	 *         null
	 * @throws IOException
	 *
	 * @see #isHTML(Map)
	 * @see #getStatusCode(Map)
	 * @see #isRedirect(Map)
	 */
	public static String fetchHTML(URL url, int redirects) throws IOException {
		var header = HttpsFetcher.fetchURL(url);
		if (isHTML(header) && redirects >= 0) {
			if (redirects == 0) {
				int statusCode = getStatusCode(header);
				if (statusCode >= 200 && statusCode < 300) {
					return String.join("\n", header.get("Content"));
				}
				return null;
			}

			if (isRedirect(header)) {
				var redirect = header.get("Location").toString();
				return fetchHTML(redirect.substring(1, redirect.length() - 1), redirects - 1);
			} else {
				return String.join("\n", header.get("Content"));
			}
		}
		return null;

	}

	/**
	 * @see #fetchHTML(URL, int)
	 */
	public static String fetchHTML(String url) throws IOException {
		return fetchHTML(new URL(url), 0);
	}

	/**
	 * @see #fetchHTML(URL, int)
	 */
	public static String fetchHTML(String url, int redirects) throws IOException {
		return fetchHTML(new URL(url), redirects);
	}

	/**
	 * @see #fetchHTML(URL, int)
	 */
	public static String fetchHTML(URL url) throws IOException {
		return fetchHTML(url, 0);
	}

}
