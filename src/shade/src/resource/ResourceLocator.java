package shade.src.resource;

import java.io.*;

public class ResourceLocator {

	public static String ext(File f) {
		if (f.isFile())
			return ext(f.getAbsolutePath());
		return "";
	}

	public static String ext(String f) {
		return f.substring(f.lastIndexOf('.'));
	}

	public static File findDir(String p) throws FileNotFoundException {
		String path = p;
		File f = new File(path);
		if (!f.exists()) {
			if (!path.startsWith(File.separator))
				path = File.separator + path;
			f = new File("." + path).getAbsoluteFile();
			if (f.exists() && f.isDirectory())
				return f.getAbsoluteFile();
		} else if (f.isDirectory())
			return f.getAbsoluteFile();
		throw new FileNotFoundException("No such directory: " + p);
	}

	public static File findFile(String p) throws FileNotFoundException {
		String path = p;
		File f = new File(path);
		if (!f.exists()) {
			if (!path.startsWith(File.separator))
				path = File.separator + path;
			f = new File("");
			f = new File(f.getAbsoluteFile(), path).getAbsoluteFile();
			if (f.exists() && f.isFile())
				return f.getAbsoluteFile();
		} else if (f.isFile())
			return f.getAbsoluteFile();
		throw new FileNotFoundException("No such file: " + p);
	}

	public static InputStream getAsStream(String path) throws IOException {
		try {
			InputStream i = ResourceLocator.class.getClassLoader().getResourceAsStream(path);
			if (i != null)
				return i;
		} catch (Exception e) {
		}
		return findFile(path).toURI().toURL().openStream();
	}

	public static String join(String a, String b) {
		String j = a;
		if (!(a.endsWith(File.separator) || b.startsWith(File.separator)))
			j += File.separator;
		j += b;
		return j;
	}

	public static String shorten(String s) {
		String p = new File("").getAbsolutePath() + File.separator;
		if (s.startsWith(p))
			return s.replace(p, "");
		return s;
	}
}