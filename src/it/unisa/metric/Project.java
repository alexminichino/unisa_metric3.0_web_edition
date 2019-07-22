package it.unisa.metric;

import java.io.File;
import java.io.IOException;
import java.util.List;
/**
 * Represent the project in input of the application.
 * @author Andrea d'Argenio, Alexander Minichino
 * @version 2.0
 * @since 1.0
 *
 */
public class Project {
	/**
	 * Project name.
	 */
	private String projectName;
	/**
	 * Project directory.
	 */
	private String projectDir;
	/**
	 * Project path.
	 */
	private String projectPath;
	/**
	 * Project source directory.
	 */
	private String sourcePath;
	/**
	 * Project library directory.
	 */
	private String libraryPath;
	/**
	 * Project binary directory.
	 */
	private String binaryPath;
	/**
	 * Project source files' list.
	 */
	private List<String> sourceFiles;
	
	/**
	 * Creates a project from <code>path</code>.
	 * @param path Project path/
	 * @throws LocalException If errors occurs.
	 */
	public Project(Parameters params) throws LocalException {
		String path = params.getProjectPath();
		try {
			Utils.print("Current directory:" + new File(".").getCanonicalPath());
		} catch (IOException e) {
			throw new LocalException("Unable to detect current working directory.");
		}		
		
		if (path == null)
			throw new LocalException("Project path is missed.");
		if (!FileManager.directoryExists(path))
			throw new LocalException("Project path is not valid.");

		Utils.print("Project setting.");
		if (path.endsWith(File.separator))
			path = path.substring(0, path.length() - 1);

		projectPath = path;
		int pos = path.lastIndexOf(File.separator);
		if (pos > -1) {
			projectDir = path.substring(0, pos);
			projectName = path.substring(pos + 1);
		} else {
			projectDir = ".";
			projectName = path;
		}

		sourcePath = projectPath + File.separator + (params.haveSourcePath()? params.getSourcePath():Constants.sourcePath);
		if (!FileManager.directoryExists(sourcePath)) {
			throw new LocalException("Source directory is not present.");
		}

		libraryPath = projectPath + File.separator + Constants.libraryPath;
		if (!FileManager.directoryExists(libraryPath)) {
			if(!FileManager.createDirectory(libraryPath))
				throw new LocalException("Library directory is not available.");
		}

		binaryPath = projectPath + File.separator + Constants.binaryPath;
		if (!FileManager.directoryExists(binaryPath)) {
			if(!FileManager.createDirectory(binaryPath))
				throw new LocalException("Binary directory is not available.");
		}
		
		scan();
	}
	
	/**
	 * Gives the project name.
	 * @return Project name.
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Gives the project directory.
	 * @return Project directory.
	 */
	public String getProjectDir() {
		return projectDir;
	}

	/**
	 * Gives the project path.
	 * @return Project path.
	 */
	public String getProjectPath() {
		return projectPath;
	}

	/**
	 * Gives the project source directory.
	 * @return Project source directory.
	 */
	public String getSourcePath() {
		return sourcePath;
	}

	/**
	 * Gives the project library directory.
	 * @return Project library directory.
	 */
	public String getLibraryPath() {
		return libraryPath;
	}

	/**
	 * Gives the project binary directory.
	 * @return Project binary directory.
	 */
	public String getBinaryPath() {
		return binaryPath;
	}

	/**
	 * Shows information about the project.
	 */
	public void print() {
		Utils.print("Project name:" + projectName);
		Utils.print("Project directory:" + projectDir);
		Utils.print("Project path:" + projectPath);
		Utils.print("Source path:" + sourcePath);
		Utils.print("Library path:" + libraryPath);
		Utils.print("Binary path:" + binaryPath);		
	}
	
	/**
	 * Scans project for java source files.
	 */
	private void scan() {
			Utils.print("File scanning.");
			Utils.print("Source directory:" + getSourcePath());
		    sourceFiles = FileManager.scanProject(getSourcePath());
	}
	
	/**
	 * Gives the project source files' list.
	 * @return Project source files' list.
	 */
	public List<String> getSourceFiles() {
		return sourceFiles;
	}
}
