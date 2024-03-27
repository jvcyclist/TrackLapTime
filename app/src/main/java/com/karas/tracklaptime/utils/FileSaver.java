package com.karas.tracklaptime.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileSaver {

    public void writeToFile(String filename, String data, Context context) {
        try {
            File path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, filename);

            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(data.getBytes());
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
