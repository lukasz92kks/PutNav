package pl.poznan.put.putnav;

import android.os.Environment;
import android.util.Log;

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

/**
 * Created by £ukasz on 2016-02-02.
 */
public class ArchiveFileManager {

    private String archiveFile;
    private String archiveExtension = ".pna";	// PutNavArchive
    private String destDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "putnavadmin").getAbsolutePath();
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
        addFile(destDirectory + File.separator + imageDirectory + File.separator + fileName, FileType.Photo);
    }

    public void addMapFile(String fileName) {
        addFile(destDirectory + File.separator + mapDirectory + File.separator + fileName, FileType.Map);
    }

    public void addDatabase(String fileName) {
        addFile(destDirectory + File.separator + fileName, FileType.Database);
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
                String filePath = destDirectory + File.separator + zipEntry.getName();
                filePath = filePath.replace("\\", "/");
                Log.i(ArchiveFileManager.class.getName(), "Filepath: " + filePath);
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
            Log.i(ArchiveFileManager.class.getName(), archiveFile + " extracted in " + destDirectory + " directory.");

            //copyFile("/storage/emulated/0/putnavadmin/", "database.db", "/data/data/pl.poznan.put.putnav/databases/");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getDatabasePath() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "putnavadmin/database.db");
        if(file.exists())
            return file.getAbsolutePath();
        else
            return "database.db";
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

        dir = new File(destDirectory + File.separator + mapDirectory);
        if(!dir.exists()) dir.mkdir();

        dir = new File(destDirectory + File.separator + imageDirectory);
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

    private void copyFile(String inputPath, String inputFile, String outputPath) {

        FileInputStream in = null;
        FileOutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
}
