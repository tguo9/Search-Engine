import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.stemmer.Stemmer;

import java.io.BufferedReader;
import java.io.File;
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
		
		if (args.length < 3) {
			
			return;
		}
		
		ArgumentMap am = new ArgumentMap();
		am.parse(args);
		
		Path path = am.getPath("-path");
		System.out.println("HERE");
		System.out.println(path.toString());
		
		InvertedIndex ii = new InvertedIndex();
		
		Path index = null;
		
		File file = new File(path.toString());
		
		if (!file.exists()) {
			
			return;
		}
		
		if (am.hasFlag("-index")) {

			index = am.getPath("-index");
			
			if (index == null) {
				index = Paths.get("index.json");
			}
			
//			Path p = Paths.get("index.json");
			
//			index = p;
			
		} else {
			
			index = Paths.get("index.json");
		}
		
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
			
			String thisLine = null;
			
			while ((thisLine = reader.readLine()) != null) {
				List<String> list = TextFileStemmer.stemLine(thisLine);
				for (int i = 0; i < list.size(); i++) {
					ii.add(list.get(i), path, i);
				}
//				JSONWriter.write(ii.getMap(), index);
				JSONWriter.asNestedObjectii.getMap());
			}
			
			
			
		} catch (IOException e) {
			e.getMessage();
		}
		
		
		
	}

}