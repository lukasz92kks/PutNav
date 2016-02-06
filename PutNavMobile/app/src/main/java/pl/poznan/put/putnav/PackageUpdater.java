package pl.poznan.put.putnav;

import android.app.ProgressDialog;
import android.os.Environment;

import java.io.*;
import java.net.*;
import android.os.AsyncTask;


class PackageUpdater extends AsyncTask<URL, Integer, Long> {

    private File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "putnavadmin");
    private String packageFilename = "putnavarchive.pna";
    private ProgressDialog dialog;

    public PackageUpdater(PreferencesActivity activity) {
        dialog = new ProgressDialog(activity);
    }

    protected Long doInBackground(URL... urls) {
        Long result = download(urls[0]);
        publishProgress(1);
        extract();
        publishProgress(2);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
     }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Pobieranie aktualizacji");
        dialog.show();
    }

    protected void onPostExecute(Long result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if(progress[0] == 1)
            dialog.setMessage("Instalowanie aktualizacji");
        else if(progress[0] == 2)
            dialog.setMessage("Aktualizacja zakonczona");
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