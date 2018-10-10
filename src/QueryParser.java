import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class QueryParser {

	public static void searcher(Path searchPath, InvertedIndex index, String mode) throws IOException {
		
		var result = parser(searchPath);
		
		for (String[] arr: result) {
			
			if (mode.equals("exact")) {
				
				index.exactSearch(arr);
			} else {
				
				index.partialSearch(arr);
			}
		}
		
		
		
	}
	
	public static ArrayList<String[]> parser(Path path) throws IOException {

		ArrayList<String[]> result = new ArrayList<>();
		
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String thisLine = null;
			while ((thisLine = reader.readLine()) != null) {
				TextFileStemmer.stemLine(thisLine);
				String queries[] = TextParser.parse(thisLine);
				
				if (queries.length == 0) {
					continue;
				}
				Arrays.sort(queries);
				result.add(queries);
			}
		}
		return result;
	}

}
