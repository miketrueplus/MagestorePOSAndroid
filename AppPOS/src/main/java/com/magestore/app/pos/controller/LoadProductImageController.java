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

        // Duyệt danh mục product
        for (Product product: mListProduct) {
            // Kiểm tra xem đã load bitmap trước đó của product chưa
            Bitmap bmp = null;
            bmp = product.getBitmap();
            if (bmp != null && !bmp.isRecycled()) continue;
            if (product.getImage() == null) continue;

            // ảnh chưa được load vào product, load ảnh về
            bmp = ImageUtil.getBitmap(product, product.getImage(), 200, 200);
            if (bmp == null) continue;

            // đặt tham chiếu ảnh với bitmap
            product.setBitmap(bmp);

            // Báo hiệu đã load ảnh xong
            publishProgress(product);
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
