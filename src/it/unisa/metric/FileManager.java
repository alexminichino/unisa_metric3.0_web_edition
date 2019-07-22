package it.unisa.metric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * A file filter.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 */
class TypeFilter implements FileFilter {
	/**
	 * <code>True</code> if directories are allowed.
	 */
	boolean _isDir = true;

	/**
	 * Creates a TypeFilter and sets if directories are allowed.
	 * @param isDir <code>true</code> if directories are allowed
	 */
	public TypeFilter(boolean isDir) {
		_isDir = isDir;
	}

	@Override
	/**
	 * Tests whether or not the specified abstract pathname should be included in a pathname list.
	 * @param dir the file.
	 * @return <code>true</code> if the file is accepted; <code>false</code> otherwise.
	 */
	public boolean accept(File dir) {
		if (_isDir) {
			return (null != dir && dir.exists() && dir.isDirectory());
		}

		return (null != dir && dir.exists() && dir.isFile() && dir.getAbsolutePath().endsWith(Constants.fileExtension));
	}
}
/**
 * A jar file filter.
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 */
class JarFilenameFilter implements FilenameFilter {
	/**
	 * Tests if the file is a jar file.
	 * @param dir File.
	 * @param fileName The name of file.
	 * @return <code>True</code> if file is a jar file; <code>false</code> otherwise.
	 */
	@Override
	public boolean accept(File dir, String fileName) {
		return fileName.endsWith(Constants.jarExtension);
	}

}

/**
 * A file manager
 * @author Andrea d'Argenio
 * @version 1.0
 * @since 1.0
 */
public class FileManager {
	/**
	 * Tests if <code>path</code> is an existing file.
	 * @param path The file path.
	 * @return <code>True</code> if the file exists; <code>false</code> otherwise.
	 */
	public static boolean fileExists(String path) {
		if (path == null)
			return false;
		File f = new File(path);
		return f.exists() && f.isFile();
	}

	/**
	 * Tests if <code>path</code> is an existing directory.
	 * @param path The directory path.
	 * @return <code>True</code> if the directory exists; <code>false</code> otherwise.
	 */
	public static boolean directoryExists(String path) {
		if (path == null)
			return false;
		File f = new File(path);
		return f.exists() && f.isDirectory();
	}
	
	/**
	 * Create a directory from <code>path</code>.
	 * @param path The path of the directory.
	 * @return <code>True</code> if the directory creation is done; <code>false</code> otherwise.
	 */
	public static boolean createDirectory(String path) {
		if (path == null)
			return false;
		File f = new File(path);
		return f.mkdir();
	}	
	
	/**
	 * Scans the <code>path</code> directory recursively and adds files to <code>files</code>.
	 * @param path The directory.
	 * @param files The list that contains all files.
	 */
	private static void scanDir(File path, List<String> files) {
		if (path == null || !path.exists())
			return;

		if (path.isDirectory()) {
			File[] dirContents = path.listFiles(new TypeFilter(false));
			Arrays.sort(dirContents);
			for (File f : dirContents) {
				files.add(f.getAbsolutePath());
			}

			dirContents = path.listFiles(new TypeFilter(true));
			Arrays.sort(dirContents);
			for (File f : dirContents) {
				scanDir(f, files); // Recursively
			}
		}

	}

	/**
	 * Remove <code>prefix</code> from all filenames in <code>files</code>.
	 * @param prefix The prefix to delete from filenames.
	 * @param files The list of filenames.
	 */
	private static void cleanFilenames(String prefix, List<String> files) {
		for (int c = 0; c < files.size(); c++) {
			String file = files.get(c);
			if (file.startsWith(prefix)) {
				files.set(c, file.substring(prefix.length() + 1));
			}
		}

	}

	/**
	 * Scans project directory searching files.
	 * <code>package-info.java</code> files are excluded.
	 * @param path The root directory of project.
	 * @return List of filenames in project.
	 */
	public static List<String> scanProject(String path) {
		List<String> files = new ArrayList<String>();

		if (path != null) {
			scanDir(new File(path), files);
			cleanFilenames(new File(path).getAbsolutePath(), files);
		}
		
		for(int i = 0; i < files.size(); i++) {
			if(files.get(i).endsWith("package-info.java"))
				files.remove(i--);
		}
		
		return files;
	}

	/**
	 * Reads file into string.
	 * @param filePath File path
	 * @return File content in a string.
	 * @throws IOException if i/o problems occurs.
	 */
	public static String readFileToString(String filePath) throws IOException {
		BufferedReader reader = null;
		StringBuilder result = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line + "\n");
			}
		} finally {
			if (reader != null)
				reader.close();
		}

		return result.toString();
	}	
	
}
