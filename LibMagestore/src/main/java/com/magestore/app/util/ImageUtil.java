package com.magestore.app.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.magestore.app.lib.model.Model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Các lệnh tiện ích liên quan đến xử lý ảnh
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class ImageUtil {
    /**
     * Trả về bmp ảnh từ trên mạng, kiểm tra cache trong RAM và Storage trước
     * @param model
     * @param remotePath
     * @return
     */
    public static Bitmap getBitmap(Model model, String remotePath, int width, int height) {
        // Nếu chưa, khởi tạo các biến để load ảnh từ mạng
        URL url = null;
        BufferedInputStream is = null;
        HttpURLConnection con = null;
        Rect pad = new Rect();

        // Trước hết lấy kích thước của ảnh xem trước
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            url = new URL(remotePath);
            con = (HttpURLConnection)url.openConnection();
            is = new BufferedInputStream(con.getInputStream());
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, pad, options);
            options.inSampleSize = ImageUtil.calculateInSampleSize(options.outWidth, options.outHeight, width, height);
            options.inJustDecodeBounds = false;
        } catch (IOException e) {
        }
        finally {
            try {
                is.close();
            } catch (IOException e) {

            }
            is = null;
            con.disconnect();
            con = null;
        }

        // Nếu ảnh quá to thì lấy kích thước bé hơn, đặt lại sample size
        try {
            url = new URL(remotePath);
            con = (HttpURLConnection)url.openConnection();
            is = new BufferedInputStream(con.getInputStream());
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(is, pad, options);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                is.close();
            } catch (IOException e) {

            }
            is = null;
            con.disconnect();
            con = null;
        }
        return null;
    }
    /**
     * Tính toánh sample sizes của bitmap để điểu chỉnh thu nhỏ kích thước nếu ảnh quá to
     * @param outWidth
     * @param outHeight
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(
            int outWidth, int outHeight, int reqWidth, int reqHeight) {
        // Raw height and width of image
        int inSampleSize = 1;

        if (outHeight > reqHeight || outWidth > reqWidth) {

            final int halfHeight = outHeight / 2;
            final int halfWidth = outWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
