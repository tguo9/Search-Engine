import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

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

	private static String indent(int times) {
		char[] tabs = new char[times];
		Arrays.fill(tabs, '\t');
		return String.valueOf(tabs);
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
	 * Returns the nested map of elements formatted as a nested pretty JSON object.
	 *
	 * @param elements the elements to convert to JSON
	 * @return {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asNestedObject(TreeMap, Writer, int)
	 */
	public static String asNestedObject(TreeMap<String, TreeMap<Path, TreeSet<Integer>>> elements) {
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
	public static void asNestedObject(TreeMap<String, TreeMap<Path, TreeSet<Integer>>> elements, Path path)
			throws IOException {
		// THIS METHOD IS PROVIDED FOR YOU. DO NOT MODIFY.
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
	public static void asNestedObject(TreeMap<String, TreeMap<Path, TreeSet<Integer>>> elements, Writer writer,
			int level) throws IOException {

		writer.write('{');
		writer.write(System.lineSeparator());

		if (elements.values().isEmpty()) {

			indent(level, writer);
			writer.write('}');
			return;
		}
		for (String element : elements.keySet()) {

			indent(level + 1, writer);
			quote(element, writer);
			writer.write(": {");
			writer.write(System.lineSeparator());
			indent(level + 1, writer);

			for (Path path : elements.get(element).keySet()) {

				indent(level + 1, writer);
				quote(path.toString(), writer);
				writer.write(": [");
				asArray(writer, elements.get(element).get(path), level + 1);
				if (path == elements.get(element).lastKey()) {

					writer.write(System.lineSeparator());
				} else {

					writer.write(',');
					writer.write(System.lineSeparator());

				}

			}

			indent(level + 1, writer);

			if (element == elements.lastKey()) {

				writer.write(System.lineSeparator());
			} else {

				writer.write(',');
				writer.write(System.lineSeparator());
			}

		}
		writer.write('}');
	}

	private static void asArray(Writer writer, TreeSet<Integer> treeSet, int i) {
		// TODO Auto-generated method stub

	}

	public static void asEmpty(TreeMap<String, TreeMap<Path, TreeSet<Integer>>> elements, Writer writer, int level)
			throws IOException {

		writer.write('{');
		writer.write(System.lineSeparator());

		indent(level, writer);
		writer.write('}');
	}

	public static String writes(TreeMap<String, TreeMap<Path, TreeSet<Integer>>> elements) {
		// THIS METHOD IS PROVIDED FOR YOU. DO NOT MODIFY.
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
	 * @see #writes(TreeMap, Writer, int)
	 */
	public static void writes(TreeMap<String, TreeMap<Path, TreeSet<Integer>>> elements, Path path)
			throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			writes(elements, writer, 0);
		}
	}
	
	public static void writes(TreeMap<String, TreeMap<Path, TreeSet<Integer>>> elements, Writer writer, int level) throws IOException {
		
		writer.write('{');
		writer.write(System.lineSeparator());

		for (String element : elements.keySet()) {

			indent(level + 1, writer);
			quote(element, writer);
			writer.write(": {");
			writer.write(System.lineSeparator());
			indent(level + 1, writer);

			for (Path path : elements.get(element).keySet()) {

				indent(level + 1, writer);
				quote(path.toString(), writer);
				writer.write(": [");
				asArray(writer, elements.get(element).get(path), level + 1);
				if (path == elements.get(element).lastKey()) {

					writer.write(System.lineSeparator());
				} else {

					writer.write(',');
					writer.write(System.lineSeparator());

				}

			}

			indent(level + 1, writer);

			if (element == elements.lastKey()) {

				writer.write(System.lineSeparator());
			} else {

				writer.write(',');
				writer.write(System.lineSeparator());
			}

		}
		writer.write('}');
	}

}
