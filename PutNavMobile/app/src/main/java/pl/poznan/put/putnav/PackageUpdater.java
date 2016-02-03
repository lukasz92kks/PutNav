package pl.poznan.put.putnav;

import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.net.*;
import android.os.AsyncTask;

/**
 * Created by £ukasz on 2016-02-02.
 */
class PackageUpdater extends AsyncTask<URL, Integer, Long> {

    private File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "putnavadmin");
    private String packageFilename = "putnavarchive.pna";

    protected Long doInBackground(URL... urls) {
        Long result = download(urls[0]);
        extract();

        return result;
     }

    protected void onPostExecute(Long result) {
    }

    private Long download(URL url) {
        try {

            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            if(!appDir.exists()) {
                appDir.mkdir();
            }
            // Output stream to write file
            OutputStream output = new FileOutputStream(new File(appDir, packageFilename));


            byte data[] = new byte[1024];

            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                //publishProgress(""+(int)((total*100)/lenghtOfFile));
                output.write(data, 0, count);
            }
            output.flush();

            output.close();
            input.close();
            return total;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void extract() {
        ArchiveFileManager archiveFileManager = new ArchiveFileManager();
        archiveFileManager.openArchiveFile(new File(appDir, packageFilename).getAbsolutePath());
        archiveFileManager.extractArchiveFile();
    }
}