import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class QueryParser {

	public static ArrayList<String[]> parser(String word) throws IOException {

		ArrayList<String[]> result = new ArrayList<>();
		
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(word), StandardCharsets.UTF_8)) {
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
