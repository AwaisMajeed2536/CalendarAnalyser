package com.example.bisma.calendar_analyzer.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;

/**
 * Created on 15.03.2016.
 *
 * @author Sławomir Onyszko
 */
public class BitmapHelper {

    private BitmapHelper() {
    }

    public static byte[] decodeByteFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        return baos.toByteArray();
    }

    public static Bitmap readBitmapFromPath(Context context, Uri path) throws Exception {
        InputStream stream = context.getContentResolver().openInputStream(path);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        if (stream != null) {
            stream.close();
        }
        return bitmap;
    }

    public static void writeToPublicDirectory(String filename, byte[] data, String directory, String environmentDirectory) throws Exception {
        File publicDirectory = new File(Environment.getExternalStoragePublicDirectory(environmentDirectory), directory);
        boolean result = publicDirectory.mkdirs();
        File targetFile = new File(publicDirectory, filename);
        FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
        fileOutputStream.write(data);
        fileOutputStream.close();
    }

    public static void writeToPublicDirectory(String filename, String string, String directory, String environmentDirectory) throws Exception {
        File publicDirectory = new File(Environment.getExternalStoragePublicDirectory(environmentDirectory), directory);
        boolean result = publicDirectory.mkdirs();
        File file = new File(publicDirectory, filename);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(string);
        outputStreamWriter.close();
    }

    public static void downloadFile(String url, File outputFile) {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL u = new URL(url);
                URLConnection conn = u.openConnection();
                int contentLength = conn.getContentLength();

                DataInputStream stream = new DataInputStream(u.openStream());

                byte[] buffer = new byte[contentLength];
                stream.readFully(buffer);
                stream.close();

                DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
                fos.write(buffer);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                return; // swallow a 404
            } catch (IOException e) {
                return; // swallow a 404
            }
        }
    }

}