import java.nio.file.Paths;
import java.util.Arrays;
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
		
		ArgumentMap am = new ArgumentMap();
		am.parse(args);
		
		InvertedIndex ii = new InvertedIndex();
		TextFileStemmer tfs = new TextFileStemmer();
		
		if (am.hasFlag("-index")) {

			Path path = am.getPath("-index");
			Path p = Paths.get(path.toString());
			
		} 
		if (am.hasFlag("-path")) {

			Path path = am.getPath("-path");
			
			
		}
		
	}

}