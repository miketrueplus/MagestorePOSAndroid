package com.magestore.app.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

/**
 * Các hàm tiện ích về file
 * Created by Mike on 1/17/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class FileUtil {
    static File dirCached;
    static File dirTemp;
    static File dirExternalCached;

    /**
     * Định vị các directory
     * @param context
     */
    public static void registerDir(Context context) {
        dirCached = context.getCacheDir();
        dirTemp = context.getFilesDir();
        dirExternalCached = context.getExternalCacheDir();
    }

    /**
     * Kiểm tra có thể truy xuất cả internal và external không
     */
    public static boolean isExternaleAccessable() {
        return isExternalStorageReadable() && isExternalStorageWritable();
    }

    /**
     * Kiểm tra external có phù hợp với việc ghi file
     * @return true
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Kiểm tra external có phù hợp với việc đọc file */

    /**
     * Kiểm tra external có phù hợp với việc đọc file
     * @return true
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Tạo file tạm
     * @param context
     * @param url
     * @return
     * @throws IOException
     */
    public static File getTempFile(Context context, String url) throws IOException {
        String fileName = Uri.parse(url).getLastPathSegment();
        File file = File.createTempFile(fileName, null, context.getCacheDir());
        return file;
    }

    /**
     * Tạo file tạm
     * @param url
     * @return
     * @throws IOException
     */
    public static File getTempFile(String url) throws IOException {
        return getTempFile(null, url);
    }

    /**
     * Tạo file tạm
     * @param context
     * @param fileName
     * @return
     * @throws IOException
     */
    public static File getCachedFile(Context context, String fileName) throws IOException {
        File file = new File(context.getFilesDir(), fileName);
        return file;
    }

    /**
     * Tạo file tạm
     * @param fileName cached
     * @return
     */
    public static File getCachedFile(String fileName) throws IOException {
        if (isExternaleAccessable())
            return new File(dirExternalCached, fileName);
        else return new File(dirCached, fileName);
    }

    /**
     * Ghi nội dung 1 chuỗi cho file
     * @param file
     * @param str
     * @throws Exception
     */
    public static void writeString(File file, String str) throws IOException {
        // chuẩn bị tham chiếu
        FileOutputStream fOut = null;
        OutputStreamWriter myOutWriter = null;

        try {
            // tạo file
            file.createNewFile();

            // tạo stream
            fOut = new FileOutputStream(file);
            myOutWriter = new OutputStreamWriter(fOut);

            // ghi chuỗi ra
            myOutWriter.append(str);

            fOut.flush();
        } catch (IOException e) {
            throw e;
        }
        finally {
            if (myOutWriter != null) myOutWriter.close();
            myOutWriter = null;

            if (fOut != null) fOut.close();
            fOut = null;
        }
    }

    /**
     * Ghi nội dung file từ 1 input stream load được
     * @param file
     * @param is
     * @throws Exception
     */
    public static void writeInputStream(File file, InputStream is) throws IOException {
        final int BUFFER_SIZE = 2048;
        // chuẩn bị tham chiếu
        FileOutputStream fOut = null;
        byte[] buf = new byte[BUFFER_SIZE];
        try {
            // tạo file
            file.createNewFile();

            // tạo stream out
            fOut = new FileOutputStream(file);

            // ghi chuỗi ra file
            int n;
            while ((n = is.read(buf)) > 0) {
                fOut.write(buf, 0, n);
            }

            // flush
            fOut.flush();
        } catch (IOException e) {
            throw e;
        }
        finally {
            if (fOut != null) fOut.close();
            fOut = null;
        }
    }

    /**
     * Đọc nội dung từ 1 file vào input stream
     */
    public static InputStream readFile(File file) throws Exception {
        return new FileInputStream(file);
    }

    public static String readFileToString(File file) throws  Exception {
        String content = new Scanner(file).useDelimiter("\\Z").next();
        return content;
    }
}
