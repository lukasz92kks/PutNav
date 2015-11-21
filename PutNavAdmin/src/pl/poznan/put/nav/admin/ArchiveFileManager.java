package pl.poznan.put.nav.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArchiveFileManager {

	private String archiveFile;
	private String archiveExtension = ".pna";	// PutNavArchive
	private String imageDirectory = "images";
	private String mapDirectory = "maps";
	private String databaseFileName = "database.db";
	
	public void openArchiveFile(String fileName) {
		if(isArchiveFileFormatCorrect(fileName))
			archiveFile = fileName;
	}

	public void addImageFile(String fileName) {
		addFile(imageDirectory + File.separator + fileName);
	}
	
	public void addMapFile(String fileName) {
		addFile(mapDirectory + File.separator + fileName);
	}
	
	public void addDatabase(String fileName) {
		addFile(fileName);
	}
	
	private void addFile(String fileName) {
		FileOutputStream fos;
		ZipOutputStream zipOutputStream;
		try {
			fos = new FileOutputStream(archiveFile);
			zipOutputStream = new ZipOutputStream(fos);
			
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file);
			ZipEntry zipEntry = new ZipEntry(file.getName());
			zipOutputStream.putNextEntry(zipEntry);
			
			byte[] bytes = new byte[1024];
			int length;
			while( (length = fis.read(bytes)) >= 0) {
				zipOutputStream.write(bytes, 0, length);
			}
			
			fis.close();
			zipOutputStream.closeEntry();
			zipOutputStream.close();
			fos.close();
			
			System.out.println(fileName + " added to archive file");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Correct format is *.pna file that include:
	 * images/
	 * maps/
	 * database.db
	 * @param fileName archive file name
	 * @return true if archive file format is correct
	 */
	private boolean isArchiveFileFormatCorrect(String fileName) {
		if(!fileName.endsWith(archiveExtension))
			return false;
		return true;
	}
}
