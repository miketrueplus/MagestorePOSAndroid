package com.magestore.app.pos.service.catalog;

import android.graphics.Bitmap;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.catalog.ProductDataAccess;
import com.magestore.app.lib.service.catalog.ProductService;
import com.magestore.app.pos.model.catalog.PosProduct;
import com.magestore.app.pos.service.AbstractService;
import com.magestore.app.util.ImageUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Các thao tác nghiệp vụ với product
 * Created by Mike on 12/18/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSProductService extends AbstractService implements ProductService {
//    Product mProduct;

//    /**
//     * Trả tham chiếu đến product dang thực hiện nghiệp vụ
//     *
//     * @return
//     */
//    @Override
//    public Product getProduct() {
//        return mProduct;
//    }
//
//    /**
//     * Đặt product để thực hiện các nghiệp vụ
//     *
//     * @param product
//     */
//    public void setProduct(Product product) {
//        mProduct = product;
//    }

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
//    public List<Product> retrieveProductList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException {
//        // Khởi tạo product gateway factory
//        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
//        ProductDataAccess productDataAccess = factory.generateProductDataAccess();
//
//        // Lấy list 30 products đầu tiên
//        return productDataAccess.retrieve(1, size);
//    }

    /**
     * Load bitmap từ image vào bitmap
     *
     * @param product
     * @return
     */
    @Override
    public Bitmap retrieveBitmap(Product product, int sizeWidth, int sizeHeight) {
        Bitmap bmp = null;
        bmp = (Bitmap) product.getBitmap();
        if (bmp != null && !bmp.isRecycled()) return bmp;
        if (product.getImage() == null) return null;

        // ảnh chưa được load vào product, load ảnh về
        bmp = ImageUtil.getBitmap(product, product.getImage(), sizeWidth, sizeWidth);

        // Đặt biến bitmap cho ảnh
        product.setBitmap(bmp);
        return bmp;
    }

    /**
     * Đếm số product
     * @return
     * @throws ParseException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ProductDataAccess productDataAccess = factory.generateProductDataAccess();
        return productDataAccess.count();
    }

    @Override
    public Product create() {
        return new PosProduct();
    }

    @Override
    public Product retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ProductDataAccess productDataAccess = factory.generateProductDataAccess();
        return productDataAccess.retrieve(strID);
    }

    @Override
    public List<Product> retrieve(int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ProductDataAccess productDataAccess = factory.generateProductDataAccess();
        return productDataAccess.retrieve(page, pageSize);
    }

    @Override
    public List<Product> retrieve() throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ProductDataAccess productDataAccess = factory.generateProductDataAccess();
        return productDataAccess.retrieve();
    }

    @Override
    public List<Product> retrieve(String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ProductDataAccess productDataAccess = factory.generateProductDataAccess();
        return productDataAccess.retrieve(searchString, page, pageSize);
    }

    @Override
    public boolean update(Product oldModel, Product newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ProductDataAccess productDataAccess = factory.generateProductDataAccess();
        return productDataAccess.update(oldModel, newModel);
    }

    @Override
    public boolean insert(Product... products) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ProductDataAccess productDataAccess = factory.generateProductDataAccess();
        return productDataAccess.insert(products);
    }

    @Override
    public boolean delete(Product... products) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ProductDataAccess productDataAccess = factory.generateProductDataAccess();
        return productDataAccess.delete(products);
    }
}