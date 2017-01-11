package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.service.catalog.ProductService;
import com.magestore.app.lib.service.customer.CustomerService;

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

    // Tham chiếu đến OrderItemListController
    OrderItemListController mOrderItemListController;

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
    public OrderItemListController getOrderItemListController() {
        return mOrderItemListController;
    }

    /**
     * Đặt 1 order item controller
     */
    public void setOrderItemListController(OrderItemListController controller) {
        this.mOrderItemListController = controller;
    }


    @Override
    protected List<Product> loadDataBackground(Void... params) throws Exception {
        List<Product> listProduct = mProductService.retrieveProductList(30);
        return listProduct;
    }

    @Override
    public void bindItem(Product item) {
        super.bindItem(item);
        if (mOrderItemListController != null) mOrderItemListController.bindItem(item);
    }
}
