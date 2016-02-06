package pl.poznan.put.nav.admin.managers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

enum FileType { 
	Database,
	Map,
	Photo
}

public class ArchiveFileManager {

	private String archiveFile;
	private String archiveExtension = ".pna";	// PutNavArchive
	private String destDirectory = "temp";
	private String imageDirectory = "images";
	private String mapDirectory = "maps";
	private String databaseFileName = "database.db";
	
	private FileOutputStream fos;
	private ZipOutputStream zip;
	
	public void openArchiveFile(String fileName) {
		if(isArchiveFileFormatCorrect(fileName))
			archiveFile = fileName;
	}
	
	public String getArchiveFile() {
		return archiveFile;
	}
	
	public String getArchiveExtension() {
		return archiveExtension;
	}

	public String getDestDirectory() {
		return destDirectory;
	}

	public String getImageDirectory() {
		return imageDirectory;
	}

	public String getMapDirectory() {
		return mapDirectory;
	}

	public String getDatabaseFileName() {
		return databaseFileName;
	}

	public void addImageFile(String fileName) {
		addFile(destDirectory + "/" + imageDirectory + "/" + fileName, FileType.Photo);
	}
	
	public void addMapFile(String fileName) {
		addFile(destDirectory + "/" + mapDirectory + "/" + fileName, FileType.Map);
	}
	
	public void addDatabase(String fileName) {
		addFile(destDirectory + "/" + fileName, FileType.Database);
	}
	
	private void addFile(String fileName, FileType type) {
		
		try {
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file);
			ZipEntry zipEntry = new ZipEntry(file.getPath().substring(destDirectory.length()+1));
			zip.putNextEntry(zipEntry);
			
			byte[] bytes = new byte[1024];
			int length;
			while( (length = fis.read(bytes)) >= 0) {
				zip.write(bytes, 0, length);
			}
			
			fis.close();
			
			System.out.println(fileName + " added to archive file");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openToWrite() {
		try {
			fos = new FileOutputStream(archiveFile);
			zip = new ZipOutputStream(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void closeWrite() {
		try {
			zip.closeEntry();
			zip.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void extractArchiveFile() {
		makeDirs();
		
		try {
			ZipInputStream zip = new ZipInputStream(new FileInputStream(archiveFile));
			ZipEntry zipEntry = zip.getNextEntry();
			while(zipEntry != null) {
				String filePath = destDirectory + "/" + zipEntry.getName();
				if(!zipEntry.isDirectory()) {
					extractFile(zip, filePath);
				} else {
					File dir = new File(filePath);
					dir.mkdir();
				}
				zip.closeEntry();
				zipEntry = zip.getNextEntry();
			}
			zip.close();
			System.out.println(archiveFile + " extracted in " + destDirectory + " directory.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void extractFile(ZipInputStream zip, String file) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		byte[] bytes = new byte[1024];
		int length = 0;
		while( (length = zip.read(bytes)) != -1) {
			bos.write(bytes, 0, length);
		}
		bos.close();
	}
	
	private void makeDirs() {
		File dir;
		
		dir = new File(destDirectory);
		if(!dir.exists()) dir.mkdir();
		
		dir = new File(destDirectory + "/" + mapDirectory);
		if(!dir.exists()) dir.mkdir();
		
		dir = new File(destDirectory + "/" + imageDirectory);
		if(!dir.exists()) dir.mkdir();
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
