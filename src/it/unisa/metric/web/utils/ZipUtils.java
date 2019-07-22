package it.unisa.metric.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import edu.stanford.nlp.io.EncodingPrintWriter.out;
import it.unisa.metric.Constants;
import it.unisa.metric.web.WebConstants;

public class ZipUtils {
	
	private String zipFile;
	private String oputputFolderName;
	private String destinationFolder;

	public ZipUtils(String zipFile, String oputputFolderName) {
		
		this.oputputFolderName = oputputFolderName;
		
		destinationFolder= getOutputDir(zipFile, oputputFolderName);
		this.zipFile=getPathName(zipFile);
	}

	public String getZipFile() {
		return zipFile;
	}
	
	public String getDestinationFolder() {
		return destinationFolder;
	}
	
	public String getOputputFolderName() {
		return oputputFolderName;
	}

	

	/**
	 * Unzip it
	 * @param zipFile input zip file
	 * @param output zip file output folder
	 */
	public String unZipIt(){
		File directory = new File(destinationFolder);
		String baseDestinationFolder = null;

		// if the output directory doesn't exist, create it
		if(!directory.exists()) 
			directory.mkdirs();

		// buffer for read and write data to file
		byte[] buffer = new byte[2048];

		try {
			FileInputStream fInput = new FileInputStream(zipFile);
			ZipInputStream zipInput = new ZipInputStream(fInput);

			ZipEntry entry = zipInput.getNextEntry();
			baseDestinationFolder = destinationFolder + File.separator + entry.getName();
			while(entry != null){
				String entryName = entry.getName();
				File file = new File(destinationFolder + File.separator + entryName);

				System.out.println("Unzip file " + entryName + " to " + file.getAbsolutePath());

				// create the directories of the zip directory
				if(entry.isDirectory()) {
					File newDir = new File(file.getAbsolutePath());
					if(!newDir.exists()) {
						boolean success = newDir.mkdirs();
						if(success == false) {
							System.out.println("Problem creating Folder");
						}
					}
				}
				else {
					FileOutputStream fOutput = new FileOutputStream(file);
					int count = 0;
					while ((count = zipInput.read(buffer)) > 0) {
						// write 'count' bytes to the file output stream
						fOutput.write(buffer, 0, count);
					}
					fOutput.close();
				}
				// close ZipEntry and take the next one
				zipInput.closeEntry();
				entry = zipInput.getNextEntry();
			}

			// close the last ZipEntry
			zipInput.closeEntry();

			zipInput.close();
			fInput.close();
			System.out.println("Unzip completed");
		} catch (IOException e) {
			e.printStackTrace();
		}    

		finally {
			return baseDestinationFolder;
		}
	}    

	private String getPathName(String zipFile) {
		return WebConstants.APPLICATION_PATH+File.separator+WebConstants.UPLOAD_DIRECTORY+File.separator+zipFile; 
	}

	private String getOutputDir(String zipFile, String oputputFolderName) {
		return WebConstants.APPLICATION_PATH+File.separator+WebConstants.UNZIPPED_DIRECTORY+File.separator+oputputFolderName.split("\\.")[0];
	}
}
