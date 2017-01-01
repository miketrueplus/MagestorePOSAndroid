package com.magestore.app.lib.usecase.pos;

import android.graphics.Bitmap;

import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.entity.pos.PosProduct;
import com.magestore.app.lib.gateway.GatewayFactory;
import com.magestore.app.lib.gateway.ProductGateway;
import com.magestore.app.lib.gateway.pos.POSGatewayFactory;
import com.magestore.app.lib.parse.gson2pos.Gson2PosListProduct;
import com.magestore.app.lib.usecase.ProductUseCase;
import com.magestore.app.lib.usecase.UseCase;
import com.magestore.app.util.ImageUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

/**
 * Các thao tác nghiệp vụ với product
 * Created by Mike on 12/18/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSProductUseCase extends AbstractUseCase implements ProductUseCase {
    Product mProduct;

    /**
     * Trả tham chiếu đến product dang thực hiện nghiệp vụ
     *
     * @return
     */
    @Override
    public Product getProduct() {
        return mProduct;
    }

    /**
     * Đặt product để thực hiện các nghiệp vụ
     *
     * @param product
     */
    public void setProduct(Product product) {
        mProduct = product;
    }

    /**
     * Lấy danh sách product
     *
     * @param size
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ParseException
     */
    public List<Product> retrieveProductList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Khởi tạo product gateway factory
        GatewayFactory factory = GatewayFactory.getFactory(POSGatewayFactory.class);
        ProductGateway productGateway = factory.generateProductGateway();

        // Lấy list 30 products đầu tiên
        return productGateway.getProducts(size, 1);
    }

    /**
     * Load bitmap từ image vào bitmap
     *
     * @param product
     * @return
     */
    @Override
    public Bitmap retrieveBitmap(Product product) {
        Bitmap bmp = null;
        bmp = (Bitmap) product.getBitmap();
        if (bmp != null && !bmp.isRecycled()) return bmp;
        if (product.getImage() == null) return null;

        // ảnh chưa được load vào product, load ảnh về
        bmp = ImageUtil.getBitmap(product, product.getImage(), 200, 200);

        // Đặt biến bitmap cho ảnh
        product.setBitmap(bmp);
        return bmp;
    }
}