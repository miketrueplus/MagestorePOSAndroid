package com.magestore.app.util;

/**
 * Các lệnh tiện ích liên quan đến xử lý ảnh
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class ImageUtil {
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
