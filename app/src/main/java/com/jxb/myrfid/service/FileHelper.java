package com.jxb.myrfid.service;

import java.io.BufferedInputStream;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;
import android.util.Log;  
  
public class FileHelper {  
    // private static String FILEPATH = "/data/local/tmp";  
  
    private static String FILEPATH = "/sdcard";  
  
    // private static String FILEPATH = "/tmp";  
  
    public static File newFile(String fileName) {  
        File file = null;  
        try {  
            file = new File(FILEPATH, fileName);  
            file.delete();  
            file.createNewFile();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return file;  
    }  
  
    public static void writeFile(File file, byte[] data, int offset, int count)  
            throws IOException {  
  
        FileOutputStream fos = new FileOutputStream(file, true);  
        fos.write(data, offset, count);  
        fos.flush();  
        fos.close();  
    }  
  
    public static byte[] readFile(String fileName) throws IOException {  
        File file = new File(FILEPATH, fileName);  
        file.createNewFile();  
        FileInputStream fis = new FileInputStream(file);  
        BufferedInputStream bis = new BufferedInputStream(fis);  
        int leng = bis.available();  
        Log.v(USBService.TAG, "filesize = " + leng);
        byte[] b = new byte[leng];  
        bis.read(b, 0, leng);  
        // Input in = new Input(fis);  
        // byte[] ret = in.readAll();  
        // in.close();  
        bis.close();  
        return b;  
  
    }  
}