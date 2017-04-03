package com.magestore.app.pos.controller;

import android.os.AsyncTask;
import android.os.Build;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.service.catalog.ProductService;
import com.magestore.app.pos.task.LoadProductImageTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller quản lý các hoạt động và panel hiển thị danh sách sản phẩm
 * Created by Mike on 1/10/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ProductListController extends AbstractListController<Product> {

    static final int ACTION_CODE_RETRIEVE_BY_CATEGORY_ID = 0;
    private Category mCategory = null;

    // Service xử lý các vấn đề liên quan đến product
    ProductService mProductService;

    /**
     * Đặt category id
     *
     * @param category
     */
    public void bindCategory(Category category) {
        mCategory = category;
        clearList();
        doRetrieve();
    }

    /**
     * Gán product theo cơ chế subject observ
     *
     * @param state
     */
    public void bindCategory(State state) {
        bindCategory(((CategoryListController) state.getController()).getSelectedItem());
    }


    /**
     * Load xong product thì load ảnh
     * @param list
     */
    @Override
    public void onRetrievePostExecute(List<Product> list) {
        super.onRetrievePostExecute(list);
        // load ảnh
//        doLoadProductsImage();
    }

    /**
     * Load ảnh cho sản phẩm
     */
    public void doLoadProductsImage() {
        LoadProductImageTask task = new LoadProductImageTask(mProductService, null, mList);
        if (task != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
            {
                task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else // Below Api Level 13
            {
                task.execute();
            }
    }

    /**
     * Đặt product service
     *
     * @param service
     */
    public void setProdcutService(ProductService service) {
        mProductService = service;
        setListService(mProductService);
    }

    @Override
    public List<Product> onRetrieveBackground(int page, int pageSize) throws Exception {
        if (mCategory == null || !(getListService() instanceof ProductService))
            return super.onRetrieveBackground(page, pageSize);
        else
            return ((ProductService) getListService()).retrieve(mCategory.getID(), null, page, pageSize);
    }


    /**
     * Bind 1 sản phẩm vào controller để xử lý
     *
     * @param item
     */
    @Override
    public void bindItem(Product item) {
        super.bindItem(item);

    }

    /**
     * Hiển thị kết quả tìm kiếm từ search panel truyền xuống
     * @param model
     */
    @Override
    public void displaySearch(Product model) {
        super.displaySearch(model);
        bindItem(model);
    }
}
