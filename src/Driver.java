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
	 * @return 0 if everything went well
	 */
	public static void main(String[] args) {
		// TODO Fill in
//		System.out.println(Arrays.toString(args));
		
		ArgumentMap am = new ArgumentMap();
		am.parse(args);
		
		if (am.hasFlag("-index")) {

			System.out.println(am.numFlags());
			
		} 
		if (am.hasFlag("-path")) {

			System.out.println(am.getPath(args[2]));
			
			
		}
	}

}