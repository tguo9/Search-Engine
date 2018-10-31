import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Writer for the JSON output file.
 * 
 * @author Tao Guo
 */
public class JSONWriter {

	/**
	 * Writes several tab <code>\t</code> symbols using the provided {@link Writer}.
	 *
	 * @param times  the number of times to write the tab symbol
	 * @param writer the writer to use
	 * @throws IOException if the writer encounters any issues
	 */
	public static void indent(int times, Writer writer) throws IOException {
		for (int i = 0; i < times; i++) {
			writer.write('\t');
		}
	}

	/**
	 * Writes the element surrounded by quotes using the provided {@link Writer}.
	 *
	 * @param element the element to quote
	 * @param writer  the writer to use
	 * @throws IOException if the writer encounters any issues
	 */
	public static void quote(String element, Writer writer) throws IOException {
		writer.write('"');
		writer.write(element);
		writer.write('"');
	}

	/**
	 * Returns the map of elements formatted as a pretty JSON object.
	 *
	 * @param elements the elements to convert to JSON
	 * @return {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asObject(TreeMap, Writer, int)
	 */
	public static String asObject(TreeMap<String, Integer> elements) {
		// THIS METHOD IS PROVIDED FOR YOU. DO NOT MODIFY.
		try {
			StringWriter writer = new StringWriter();
			asObject(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the map of elements formatted as a pretty JSON object to the specified
	 * file.
	 *
	 * @param elements the elements to convert to JSON
	 * @param path     the path to the file write to output
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see #asObject(TreeMap, Writer, int)
	 */
	public static void asObject(TreeMap<String, Integer> elements, Path path) throws IOException {
		// THIS METHOD IS PROVIDED FOR YOU. DO NOT MODIFY.
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asObject(elements, writer, 0);
		}
	}

	/**
	 * Writes the map of elements as a pretty JSON object using the provided
	 * {@link Writer} and indentation level.
	 *
	 * @param elements the elements to convert to JSON
	 * @param writer   the writer to use
	 * @param level    the initial indentation level
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see Writer#write(String)
	 * @see Writer#append(CharSequence)
	 *
	 * @see System#lineSeparator()
	 *
	 * @see #indent(int, Writer)
	 * @see #quote(String, Writer)
	 */
	public static void asObject(TreeMap<String, Integer> elements, Writer writer, int level) throws IOException {

		writer.write("{");
		writer.write(System.lineSeparator());

		if (elements.values().isEmpty()) {

			indent(level, writer);
			writer.write("}");
			return;
		}
		for (String element : (elements).headMap(elements.lastKey()).keySet()) {

			indent(level + 1, writer);
			quote(element, writer);
			writer.write(": ");
			writer.write(elements.get(element).toString());
			writer.write(',');
			writer.write(System.lineSeparator());
		}

		indent(level + 1, writer);
		quote(elements.lastKey(), writer);
		writer.write(": ");
		writer.write(elements.get(elements.lastKey()).toString());
		writer.write(System.lineSeparator());
		indent(level, writer);

		writer.write("}");

	}

	/**
	 * Returns the set of elements formatted as a pretty JSON array of numbers.
	 *
	 * @param elements the elements to convert to JSON
	 * @return {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asArray(TreeSet, Writer, int)
	 */
	public static String asArray(TreeSet<Integer> elements) {
		try {
			StringWriter writer = new StringWriter();
			asArray(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the set of elements formatted as a pretty JSON array of numbers to the
	 * specified file.
	 *
	 * @param elements the elements to convert to JSON
	 * @param path     the path to the file write to output
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asArray(TreeSet<Integer> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asArray(elements, writer, 0);
		}
	}

	/**
	 * Writes the set of elements formatted as a pretty JSON array of numbers using
	 * the provided {@link Writer} and indentation level.
	 *
	 * @param elements the elements to convert to JSON
	 * @param writer   the writer to use
	 * @param level    the initial indentation level
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see Writer#write(String)
	 * @see Writer#append(CharSequence)
	 *
	 * @see System#lineSeparator()
	 *
	 * @see #indent(int, Writer)
	 */
	public static void asArray(TreeSet<Integer> elements, Writer writer, int level) throws IOException {

		writer.write('[');
		writer.write(System.lineSeparator());

		if (elements.isEmpty()) {
			indent(level, writer);
			writer.write(']');
			return;
		}
		indent(level + 1, writer);
		for (Integer element : elements.headSet(elements.last())) {

			writer.write(element.toString());
			writer.write(',');
			writer.write(System.lineSeparator());
			indent(level + 1, writer);

		}
		writer.write(elements.last().toString());
		writer.write(System.lineSeparator());
		indent(level, writer);
		writer.write(']');

	}

	/**
	 * Returns the nested map of elements formatted as a nested pretty JSON object.
	 *
	 * @param elements the elements to convert to JSON
	 * @return {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asNestedObject(TreeMap, Writer, int)
	 */
	public static String asNestedObject(TreeMap<String, TreeSet<Integer>> elements) {
		// THIS METHOD IS PROVIDED FOR YOU. DO NOT MODIFY.
		try {
			StringWriter writer = new StringWriter();
			asNestedObject(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the nested map of elements formatted as a nested pretty JSON object to
	 * the specified file.
	 *
	 * @param elements the elements to convert to JSON
	 * @param path     the path to the file write to output
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see #asNestedObject(TreeMap, Writer, int)
	 */
	public static void asNestedObject(TreeMap<String, TreeSet<Integer>> elements, Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asNestedObject(elements, writer, 0);
		}
	}

	/**
	 * Writes the nested map of elements as a nested pretty JSON object using the
	 * provided {@link Writer} and indentation level.
	 *
	 * @param elements the elements to convert to JSON
	 * @param writer   the writer to use
	 * @param level    the initial indentation level
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see Writer#write(String)
	 * @see Writer#append(CharSequence)
	 *
	 * @see System#lineSeparator()
	 *
	 * @see #indent(int, Writer)
	 * @see #quote(String, Writer)
	 *
	 * @see #asArray(TreeSet, Writer, int)
	 */
	public static void asNestedObject(TreeMap<String, TreeSet<Integer>> elements, Writer writer, int level)
			throws IOException {

		writer.write('{');
		writer.write(System.lineSeparator());

		if (elements.values().isEmpty()) {

			indent(level, writer);
			writer.write('}');
			return;
		}
		for (String element : (elements).headMap(elements.lastKey()).keySet()) {

			indent(level + 1, writer);
			quote(element, writer);
			writer.write(": ");
			asArray(elements.get(element), writer, level + 1);
			writer.write(',');
			writer.write(System.lineSeparator());
		}

		indent(level + 1, writer);
		quote(elements.lastKey(), writer);
		writer.write(": ");
		asArray(elements.get(elements.lastKey()), writer, level + 1);
		writer.write(System.lineSeparator());
		indent(level, writer);

		writer.write('}');
	}

	/**
	 * Returns the nested map of elements formatted as a nested pretty JSON object.
	 *
	 * @param elements the elements to convert to JSON
	 * @return {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asNestedObject(TreeMap, Writer, int)
	 */
	public static String writes(TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements) {

		try {
			StringWriter writer = new StringWriter();
			writes(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the nested map of elements formatted as a nested pretty JSON object to
	 * the specified file.
	 *
	 * @param elements the elements to convert to JSON
	 * @param path     the path to the file write to output
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see #asNestedObject(TreeMap, Writer, int)
	 */
	public static void writes(TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements, Path path)
			throws IOException {

		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writes(elements, writer, 0);
		}
	}

	/**
	 * Writes the nested map of elements as a nested pretty JSON object using the
	 * provided {@link Writer} and indentation level.
	 *
	 * @param elements the elements to convert to JSON
	 * @param writer   the writer to use
	 * @param level    the initial indentation level
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see Writer#write(String)
	 * @see Writer#append(CharSequence)
	 *
	 * @see System#lineSeparator()
	 *
	 * @see #indent(int, Writer)
	 * @see #quote(String, Writer)
	 *
	 * @see #asArray(TreeSet, Writer, int)
	 */
	public static void writes(TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements, Writer writer, int level)
			throws IOException {

		writer.write('{');
		writer.write(System.lineSeparator());

		if (!elements.isEmpty()) {

			for (String key : elements.headMap(elements.lastKey()).keySet()) {

				indent(level + 1, writer);

				quote(key.toString(), writer);

				writer.write(": ");

				asNestedObject(elements.get(key), writer, level);

				writer.write(",");

				writer.write(System.lineSeparator());

			}

			indent(level + 1, writer);
			quote(elements.lastKey(), writer);
			writer.write(": ");
			asNestedObject(elements.get(elements.lastKey()), writer, level + 1);
			writer.write(System.lineSeparator());
		}

		indent(level, writer);
		writer.write('}');
	}

	/**
	 * Returns the nested map of elements formatted as a nested pretty JSON object.
	 *
	 * @param elements the elements to convert to JSON
	 * @return {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asNestedObject(TreeMap, Writer, int)
	 */
	public static String writesResult(TreeMap<String, List<SearchResult>> elements) {
		try {
			StringWriter writer = new StringWriter();
			writesResult(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the nested map of elements formatted as a nested pretty JSON object to
	 * the specified file.
	 *
	 * @param elements the elements to convert to JSON
	 * @param path     the path to the file write to output
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see #asNestedObject(TreeMap, Writer, int)
	 */
	public static void writesResult(TreeMap<String, List<SearchResult>> index, Path path) throws IOException {

		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writesResult(index, writer, 0);
		}

	}

	/**
	 * Writes the nested map of elements as a nested pretty JSON object using the
	 * provided {@link Writer} and indentation level.
	 *
	 * @param elements the elements to convert to JSON
	 * @param writer   the writer to use
	 * @param level    the initial indentation level
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see Writer#write(String)
	 * @see Writer#append(CharSequence)
	 *
	 * @see System#lineSeparator()
	 *
	 * @see #indent(int, Writer)
	 * @see #quote(String, Writer)
	 *
	 * @see #asArray(TreeSet, Writer, int)
	 */
	public static void writesResult(TreeMap<String, List<SearchResult>> elements, Writer writer, int level)
			throws IOException {

		writer.write("[");
		writer.write(System.lineSeparator());

		for (String q : elements.keySet()) {

			indent(1, writer);
			writer.write("{");
			writer.write(System.lineSeparator());

			indent(2, writer);
			quote("queries", writer);
			writer.write(": ");
			quote(q, writer);
			writer.write(",");

			writer.write(System.lineSeparator());

			asInner(q, elements.get(q), writer, 1);
			writer.write(System.lineSeparator());

			indent(1, writer);
			writer.write("}");

			if (q.equals(elements.lastKey())) {

				writer.write(System.lineSeparator());
			} else {

				writer.write(",");
				writer.write(System.lineSeparator());
			}

		}
		writer.write("]");
	}

	public static void asInner(String q, List<SearchResult> results, Writer writer, int level) throws IOException {

		indent(2, writer);
		quote("results", writer);
		writer.write(": [");
		writer.write(System.lineSeparator());

		for (SearchResult r : results) {

			asResult(r, writer, level + 1);
			indent(3, writer);
			writer.write("}");
			if (!r.equals(results.get(results.size() - 1))) {

				writer.write(",");
			}

			writer.write(System.lineSeparator());
		}
		indent(2, writer);
		writer.write("]");

	}

	@SuppressWarnings("deprecation")
	public static void asResult(SearchResult result, Writer writer, int level) throws IOException {

		indent(3, writer);
		writer.write("{");
		writer.write(System.lineSeparator());

		indent(4, writer);
		quote("where", writer);
		writer.write(": ");
		quote(result.getPath(), writer);
		writer.write(",");
		writer.write(System.lineSeparator());

		indent(4, writer);
		quote("count", writer);
		writer.write(": ");
		writer.write(new Integer(result.getMatches()).toString());
		writer.write(",");
		writer.write(System.lineSeparator());

		indent(4, writer);
		quote("score", writer);
		writer.write(": ");
		DecimalFormat FORMATTER = new DecimalFormat("0.000000");
		writer.write(FORMATTER.format((new Double(result.getScore()))).toString());
		writer.write(System.lineSeparator());

	}

}
