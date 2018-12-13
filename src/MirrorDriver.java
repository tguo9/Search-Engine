
public class MirrorDriver {

	public static void main(String args[]) throws Exception {
		String arguments[] = { "-url", "https://www.cs.usfca.edu/~cs212/simple/hello.html", "-index", "index.json", "-limit", "10", "-threads", "5",
				"-port", "8080", };

		Driver.main(arguments);
	}
}
