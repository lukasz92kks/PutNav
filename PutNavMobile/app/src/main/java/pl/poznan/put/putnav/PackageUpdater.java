package pl.poznan.put.putnav;

import android.app.ProgressDialog;
import android.os.Environment;

import java.io.*;
import java.net.*;
import android.os.AsyncTask;
import android.util.Log;


class PackageUpdater extends AsyncTask<URL, Integer, Long> {

    private File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "putnavadmin");
    private String versionFilename = "version.txt";
    private String packageFilename = "PutNavArchive.pna";
    private ProgressDialog dialog;

    public PackageUpdater(PreferencesActivity activity) {
        dialog = new ProgressDialog(activity);
    }

    protected Long doInBackground(URL... urls) {
        try {
            if(isNewVersionAvailable(new URL(urls[0] + "/" + versionFilename))) {
                publishProgress(0);
                Long result = download(new URL(urls[0] + "/" + packageFilename), packageFilename);
                publishProgress(1);
                extract();
                publishProgress(2);
                Thread.sleep(1000);
                return result;
            } else {
                publishProgress(3);
                Thread.sleep(1000);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Long(0);
     }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Sprawdzanie aktualizacji");
        dialog.show();
    }

    protected void onPostExecute(Long result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if(progress[0] == 0)
            dialog.setMessage("Pobieranie aktualizacji");
        else if(progress[0] == 1)
            dialog.setMessage("Instalowanie aktualizacji");
        else if(progress[0] == 2)
            dialog.setMessage("Aktualizacja zakończona");
        else if(progress[0] == 3)
            dialog.setMessage("Brak dostępnych aktualizacji");
    }

    private boolean isNewVersionAvailable(URL versionFileURL) {
        File localVersionFile = new File(appDir, versionFilename);
        if(!localVersionFile.exists()) {
            download(versionFileURL, versionFilename);
            return true;
        }

        Log.i(PackageUpdater.class.getName(), "localVersionFile: " + localVersionFile + " " + localVersionFile.exists());
        String oldVersion = getVersion(localVersionFile);
        download(versionFileURL, versionFilename);
        String newVersion = getVersion(localVersionFile);

        Log.i(PackageUpdater.class.getName(), "oldVersion: " + oldVersion);
        Log.i(PackageUpdater.class.getName(), "newVersion: " + newVersion);
        if(oldVersion.equals(newVersion))
            return false;
        else
            return true;
    }

    private String getVersion(File versionFile) {
        String version = "";

        try {
            FileReader fileReader = new FileReader(versionFile);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            if((version = bufferedReader.readLine()) != null) {
                System.out.println(version);
            }

            bufferedReader.close();
            return version;
        }
        catch(FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

        return version;
    }

    private Long download(URL url, String outputFilename) {
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
            OutputStream output = new FileOutputStream(new File(appDir, outputFilename));


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