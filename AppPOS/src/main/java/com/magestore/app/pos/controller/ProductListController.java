package com.magestore.app.pos.controller;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.observe.GenericState;
import com.magestore.app.lib.service.catalog.ProductService;
import com.magestore.app.pos.task.LoadProductImageTask;

import java.util.ArrayList;
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
    CategoryListController mCategoryListController;

    /**
     * Đặt category id
     *
     * @param category
     */
    public void setCategory(Category category) {
        mCategory = category;
        clearList();
        doRetrieve();
    }

    public void setCategoryListController(CategoryListController mCategoryListController) {
        this.mCategoryListController = mCategoryListController;
    }

    @Override
    public void onRetrievePostExecute(List<Product> list) {
        super.onRetrievePostExecute(list);
        // load ảnh
        doLoadProductsImage();
    }

    // Tham chiếu đến CartItemListController
    CheckoutListController mCheckoutListController;

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

    /**
     * Nhận lại product service
     *
     * @return
     */
    public ProductService getProductService() {
        return mProductService;
    }

    /**
     * Get 1 order item controller
     *
     * @return
     */
    public CheckoutListController getmCheckoutListController() {
        return mCheckoutListController;
    }

    /**
     * Đặt 1 order item controller
     */
    public void setCheckoutListController(CheckoutListController controller) {
        this.mCheckoutListController = controller;
    }

    public void doRetrieveByCategoryID(Category category) {
        Map<String, Object> wraper = new HashMap<>();
        doAction(ACTION_CODE_RETRIEVE_BY_CATEGORY_ID, null, wraper, category);
    }

    @Override
    public List<Product> onRetrieveBackground(int page, int pageSize) throws Exception {
        if (mCategory == null || !(mListService instanceof ProductService))
            return super.onRetrieveBackground(page, pageSize);
        else
            return ((ProductService) mListService).retrieve(mCategory.getID(), null, page, pageSize);
    }

//    @Override
//    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
//        if (actionType == ACTION_CODE_RETRIEVE_BY_CATEGORY_ID) {
//            String categoryID = ((Category) models[0]).getID();
//            wraper.put("list_product_by_category", mProductService.retrieve(categoryID, null, 1, 30));
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
//        if (success && actionType == ACTION_CODE_RETRIEVE_BY_CATEGORY_ID) {
//            List<Product> listProduct = (List<Product>) wraper.get("list_product_by_category");
//            mList = new ArrayList<>();
//            mList.addAll(listProduct);
//            bindList(mList);
//            mView.notifyDataSetChanged();
//        }
//    }

    /**
     * Bind 1 sản phẩm vào controller để xử lý
     *
     * @param item
     */
    @Override
    public void bindItem(Product item) {
        super.bindItem(item);
    }

    public void selectCategoryChild(Category category) {
        mCategoryListController.selectCategoryChild(category);
    }

    @Override
    public void displaySearch(Product model) {
        super.displaySearch(model);
        bindItem(model);
    }
}
