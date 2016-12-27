package com.magestore.app.pos.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.magestore.app.lib.controller.ControllerListener;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.pos.SalesListActivity;
import com.magestore.app.pos.ui.SalesUI;
import com.magestore.app.util.ImageUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */
public class LoadProductImageController extends AsyncTaskAbstractController<Void, Product, Void> {
    private final List<Product> mListProduct;
    public static final String KEY_BITMAP = "LoadProductImageBitmap";
    public static final String KEY_IMAGEVIEW = "LoadProductImageImageview";

    public LoadProductImageController(ControllerListener listener, List<Product> listProduct) {
        super(listener);
        mListProduct = listProduct;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (mListProduct == null) return null;
        Rect pad = new Rect();

        // Duyệt danh mục product
        for (Product product: mListProduct) {
            // Kiểm tra xem đã load bitmap trước đó của product chưa
            Bitmap bmp = null;
            bmp = (Bitmap) product.getRefer(KEY_BITMAP);
            if (bmp != null && !bmp.isRecycled()) continue;
            if (product.getImage() == null) continue;

            // Nếu chưa, khởi tạo các biến để load ảnh từ mạng
            URL url = null;
            BufferedInputStream is = null;
            HttpURLConnection con = null;

            // Trước hết lấy kích thước của ảnh xem trước
            BitmapFactory.Options options = new BitmapFactory.Options();
            try {
                url = new URL(product.getImage());
                con = (HttpURLConnection)url.openConnection();
                is = new BufferedInputStream(con.getInputStream());
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, pad, options);
                options.inSampleSize = ImageUtil.calculateInSampleSize(options.outWidth, options.outHeight, 200, 200);
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
                url = new URL(product.getImage());
                con = (HttpURLConnection)url.openConnection();
                is = new BufferedInputStream(con.getInputStream());
                options.inJustDecodeBounds = false;
                bmp = BitmapFactory.decodeStream(is, pad, options);
                product.setRefer(KEY_BITMAP, bmp);

                // Báo hiệu đã load ảnh xong
                publishProgress(product);
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
        }


        return null;
    }

    @Override
    protected void onProgressUpdate(Product... values) {
        // gọi super
        super.onProgressUpdate(values);

        // Kiểm tra trước đã load danh sách ảnh chưa
        if (mListProduct == null) return;

        // Với mỗi sản phẩm
        for (Product product: values) {
            // Truy vấn vào bitmap
            Bitmap bmp = (Bitmap) product.getRefer(KEY_BITMAP);

            // Lấy ImageView của sản phẩm
            ImageView imgView = (ImageView) product.getRefer(KEY_IMAGEVIEW);

            // Đặt bitmap cho imageview
            if (imgView != null && bmp != null) {
                imgView.setImageBitmap(bmp);
                imgView.invalidate();
            }
        }
    }
}
