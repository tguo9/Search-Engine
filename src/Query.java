import java.io.IOException;
import java.nio.file.Path;

public interface Query {

	void toJSONResult(Path path) throws IOException;

	/**
	 * Parse the given path and store into a local TreeMap.
	 * 
	 * @param path  of queries
	 * @param exact search methods
	 * @throws IOException
	 */
	void parseAndSearch(Path path, boolean exact) throws IOException;

}