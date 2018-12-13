import java.io.IOException;
import java.nio.file.Path;

/**
 * The interface for single thread and muti-thread QueryParser
 * 
 * @author Tao
 *
 */
public interface Query {

	/**
	 * Writes the result to JSON format.
	 * 
	 * @param path
	 * @throws IOException
	 */
	void toJSONResult(Path path) throws IOException;

	/**
	 * Parse the given path and store into a local TreeMap.
	 * 
	 * @param path  of queries
	 * @param exact search methods
	 * @throws IOException
	 */
	void parseAndSearch(String path, boolean exact) throws IOException;

}
