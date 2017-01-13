package com.magestore.app.pos.task;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.magestore.app.lib.service.catalog.ProductService;
import com.magestore.app.lib.task.TaskListener;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.util.ImageUtil;

import java.util.List;

/**
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 */
public class LoadProductImageTask extends AsyncTaskAbstractTask<Void, Product, Void> {
    private final List<Product> mListProduct;
    private final ProductService mProductService;
    public static final String KEY_IMAGEVIEW = "LoadProductImageImageview";

    public LoadProductImageTask(ProductService productService, TaskListener listener, List<Product> listProduct) {
        super(listener);
        mListProduct = listProduct;
        mProductService = productService;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (mListProduct == null) return null;

        // Duyệt danh mục product
        for (Product product: mListProduct) {
            // Kiểm tra xem đã load bitmap trước đó của product chưa
            Bitmap bmp = mProductService.retrieveBitmap(product, 200, 200);
            if (bmp == null) continue;

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
            Bitmap bmp = product.getBitmap();

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