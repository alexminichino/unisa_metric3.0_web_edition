package it.unisa.metric.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class FileUtils {

	public static <E> boolean serializeObj(E obj, String path, String fileName) {
		try {
			// creates the save directory if it does not exists
	        File fileSaveDir = new File(path);
	        if (!fileSaveDir.exists()) {
	            fileSaveDir.mkdirs();
	        }
	        path+=File.separator+fileName;
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
			return true;
		} 
		catch (IOException i) {
			return false;
		}
	}

	public static <E> E deserializeObj(String path, String fileName) {
		try {
			// creates the save directory if it does not exists
	        File fileSaveDir = new File(path);
	        if (!fileSaveDir.exists()) {
	            fileSaveDir.mkdirs();
	        }
			FileInputStream fileIn = new FileInputStream(path+File.separator+fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			E obj = (E) in.readObject();
			in.close();
			fileIn.close();
			return obj;
		} 
		catch (IOException i) {
			i.printStackTrace();
			return null;
		} 
		catch (ClassNotFoundException c) {
			c.printStackTrace();
			return null;
		}

	}
	
	public static void deleteFile(String path) throws IOException {
		Files.deleteIfExists(Paths.get(path));
	}
	
	public static void DeleteDir(String path) throws IOException {
		org.apache.commons.io.FileUtils.deleteDirectory(new File(path));
	}
	
	public static ArrayList<String> getListOfSubDir(String absolutePathDirectory) {
		File file = new File(absolutePathDirectory);
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		return new ArrayList<>(Arrays.asList(directories));
	}
}
