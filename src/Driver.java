import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.stemmer.Stemmer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * TODO Fill in your own comments!
 */
public class Driver {
	
	/**
	 * Parses the command-line arguments to build and use an in-memory search
	 * engine from files or the web.
	 *
	 * @param args the command-line arguments to parse
	 */
	public static void main(String[] args) {
//		System.out.println(Arrays.toString(args));
		
		if (args.length < 2) {
			
			return;
		}
		
		ArgumentMap am = new ArgumentMap();
		am.parse(args);
		
		InvertedIndex ii = new InvertedIndex();
		
		Path path = null;
		Path index = null;
		
		if (am.hasFlag("-index") && am.hasFlag("-path")) {

			path = am.getPath("-index");
			
			Path p = Paths.get("index.json");
			
		} else {
			
			
		}
		
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
			
			String thisLine = null;
			
			while ((thisLine = reader.readLine()) != null) {
				List<String> list = TextFileStemmer.stemLine(thisLine);
				for (int i = 0; i < list.size(); i++) {
					ii.add(list.get(i), path, i);
				}
				JSONWriter.asNestedObject(ii.getMap(), index);
			}
			
			
			
		} catch (IOException e) {
			e.getMessage();
		}
		
		
		
	}

}