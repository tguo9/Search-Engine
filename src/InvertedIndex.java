import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Data straturce for the project. Store all the data.
 * 
 * @author Tao Guo
 */
@SuppressWarnings("serial")
public class InvertedIndex extends HttpServlet {

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;

	/**
	 * Data structure to store strings and their positions.
	 */
	private final TreeMap<String, Integer> location;

	/**
	 * Initializes the map.
	 */
	public InvertedIndex() {
		this.index = new TreeMap<>();
		this.location = new TreeMap<>();
	}

	/**
	 * Add word to index
	 * 
	 * @param word
	 * @param path
	 * @param position
	 */
	public void add(String word, String path, int position) {

		index.putIfAbsent(word, new TreeMap<>());
		index.get(word).putIfAbsent(path, new TreeSet<>());
		boolean success = index.get(word).get(path).add(position);

		if (success) {

			location.put(path, location.getOrDefault(path, 0) + 1);
		}

	}

	/**
	 * Add another object to index
	 * 
	 * @param word
	 * @param path
	 * @param position
	 */
	public void addAll(InvertedIndex other) {
		for (String word : other.index.keySet()) {
			if (this.index.containsKey(word) == false) {
				this.index.put(word, other.index.get(word));
			} else {
				for (String path : other.index.get(word).keySet()) {
					if (!this.index.get(word).containsKey(path)) {
						this.index.get(word).put(path, other.index.get(word).get(path));
					} else {
						this.index.get(word).get(path).addAll(other.index.get(word).get(path));
					}
				}
			}
		}

		for (String local : other.location.keySet()) {

			if (!this.location.containsKey(local)) {

				this.location.put(local, other.location.get(local));
			} else {

				this.location.put(local, other.location.get(local) + this.location.get(local));
			}

		}

	}

	/**
	 * Check word contains in index
	 * 
	 * @param word
	 * @return
	 */
	public boolean contains(String word) {

		return index.containsKey(word);
	}

	/**
	 * Check word contains in location
	 * 
	 * @param word
	 * @param location
	 * @return
	 */
	public boolean contains(String word, String location) {

		return contains(word) ? index.get(word).containsKey(location) : false;
	}

	/**
	 * Check word contains in location and position
	 * 
	 * @param word
	 * @param location
	 * @param position
	 * @return
	 */
	public boolean contains(String word, String location, int position) {

		return contains(word, location) ? index.get(word).get(location).contains(position) : false;

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		PrintWriter out = response.getWriter();

		out.printf("<!DOCTYPE html>%n");
		out.printf("<html>%n");
		out.printf("<head>%n");
		out.printf("	<meta charset=\"utf-8\">%n");
		out.printf("	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">%n");
		out.printf("	<title>%s</title>%n", "Index");
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
		out.printf("	      <h1 class=\"title\">%n");
		out.printf("	        Index Board%n");
		out.printf("	      </h1>%n");

		out.printf("	    </div>%n");
		out.printf("	  </div>%n");
		out.printf("	</section>%n");
		out.printf("%n");
		out.printf("	<section class=\"section\">%n");
		out.printf("		<div class=\"container\">%n");
		out.printf("			<h2 class=\"title\">Index</h2>%n");
		out.printf("%n");

		if (index.isEmpty()) {
			out.printf("				<p>No messages.</p>%n");
		} else {
			for (String message : index.keySet()) {
				out.printf("				<div class=\"box\">%n");
				out.print(message);
				for (String info : index.get(message).keySet()) {

					out.printf("\n");
					out.printf("<a href=\"" + info + "\" target=\"_blank\">" + info + "</a>\n");
				}
				out.printf("				</div>%n");

				out.printf("%n");
			}
		}

		out.printf("			</div>%n");
		out.printf("%n");
		out.printf("		</div>%n");
		out.printf("	</section>%n");
		out.printf("%n");

		out.printf("	<section class=\"section\">%n");
		out.printf("		<div class=\"container\">%n");
		out.printf("			<h2 class=\"title\">Location</h2>%n");
		out.printf("%n");

		if (location.isEmpty()) {
			out.printf("				<p>No messages.</p>%n");
		} else {
			for (String message : location.keySet()) {
				out.printf("				<div class=\"box\">%n");
				out.print(message);

				out.print("\t");
				out.print(location.get(message));
				out.printf("				</div>%n");

				out.printf("%n");
			}
		}

		out.printf("			</div>%n");
		out.printf("%n");
		out.printf("		</div>%n");
		out.printf("	</section>%n");

		out.printf("	<footer class=\"footer\">%n");
		out.printf("	  <div class=\"content has-text-centered\">%n");
		out.printf("	    <p>%n");
		out.printf("	      This request was handled by thread %s.%n", Thread.currentThread().getName());
		out.printf("	    </p>%n");
		out.printf("	  </div>%n");
		out.printf("	</footer>%n");
		out.printf("</body>%n");
		out.printf("</html>%n");

		response.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * The method for writing JSON
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void toJSON(Path path) throws IOException {
		JSONWriter.writes(index, path);
	}

	/**
	 * The method for writing location
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void toJSONLocations(Path path) throws IOException {
		JSONWriter.asObject(location, path);
	}

	/**
	 * The method for partial search
	 * 
	 * @param query
	 * @return
	 */
	public List<SearchResult> partialSearch(Set<String> query) {

		ArrayList<SearchResult> searches = new ArrayList<>();

		HashMap<String, SearchResult> lookup = new HashMap<>();

		for (String word : query) {

			for (String key : index.tailMap(word).keySet()) {

				if (!key.startsWith(word)) {
					break;
				}

				searchHelper(key, lookup, searches);
			}

		}

		Collections.sort(searches);

		return searches;

	}

	/**
	 * The method for exact search
	 * 
	 * @param query
	 * @return
	 */
	public List<SearchResult> exactSearch(Set<String> query) {

		ArrayList<SearchResult> searches = new ArrayList<>();

		HashMap<String, SearchResult> lookup = new HashMap<>();

		for (String word : query) {

			if (index.containsKey(word)) {

				searchHelper(word, lookup, searches);
			}

		}

		Collections.sort(searches);

		return searches;

	}

	/**
	 * The helper method for search
	 * 
	 * @param word
	 * @param lookup
	 * @param searches
	 */
	private void searchHelper(String word, HashMap<String, SearchResult> lookup, ArrayList<SearchResult> searches) {

		for (String path : index.get(word).keySet()) {

			if (lookup.containsKey(path)) {

				lookup.get(path).update((index.get(word).get(path).size()));

			} else {
				SearchResult result = new SearchResult(path, index.get(word).get(path).size(), location.get(path));
				lookup.put(path, result);
				searches.add(result);
			}

		}
	}

	/**
	 * Get the size of the word
	 * 
	 * @return the size of the words
	 */
	public int words() {

		return index.size();
	}

	/**
	 * Return the toString
	 */
	@Override
	public String toString() {
		return this.index.toString();
	}

	/**
	 * Methods to get the size of the map
	 * 
	 * @return size of the map
	 */
	public int size() {
		return index.size();
	}

	/**
	 * Return the amount of paths found in a specific word.
	 * 
	 * @param word to be checked
	 * @return the amount of paths under the word, 0 if the word is not found.
	 */
	public int size(String word) {
		return contains(word) ? index.get(word).size() : 0;
	}

	/**
	 * Return the amount of indices found in a specific path under a word.
	 * 
	 * @param word to be checked
	 * @param path to be checked
	 * 
	 * @return the amount of indices under the path, 0 if the word or path is not
	 *         found.
	 */
	public int size(String word, String path) {
		return contains(word, path) ? index.get(word).get(path).size() : 0;
	}

}
