package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.service.catalog.ProductService;

import java.util.List;

/**
 * Controller quản lý các hoạt động và panel hiển thị danh sách sản phẩm
 * Created by Mike on 1/10/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ProductListController extends AbstractListController<Product> {
    // Service xử lý các vấn đề liên quan đến product
    ProductService mProductService;

    // Tham chiếu đến CartItemListController
    CartItemListController mCartItemListController;

    /**
     * Đặt product service
     * @param service
     */
    public void setProdcutService(ProductService service) {
        mProductService = service;
    }

    /**
     * Nhận lại product service
     * @return
     */
    public ProductService getProductService() {
        return mProductService;
    }

    /**
     * Get 1 order item controller
     * @return
     */
    public CartItemListController getOrderItemListController() {
        return mCartItemListController;
    }

    /**
     * Đặt 1 order item controller
     */
    public void setOrderItemListController(CartItemListController controller) {
        this.mCartItemListController = controller;
    }


    /**
     * Load danh mục sản phẩm
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    protected List<Product> loadDataBackground(Void... params) throws Exception {
        List<Product> listProduct = mProductService.retrieveProductList(30);
        return listProduct;
    }

    /**
     * Bind 1 sản phẩm vào controller để xử lý
     * @param item
     */
    @Override
    public void bindItem(Product item) {
        super.bindItem(item);
        if (mCartItemListController != null) mCartItemListController.bindItem(item);
    }
}
