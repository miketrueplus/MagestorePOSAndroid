package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractController;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.catalog.ProductOptionService;
import com.magestore.app.pos.R;
import com.magestore.app.pos.panel.ProductOptionPanel;
import com.magestore.app.pos.view.MagestoreDialog;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 3/1/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ProductOptionController extends AbstractController<Product, ProductOptionPanel, ProductOptionService> {
    MagestoreDialog mDialog;
    /**
     * Hiển thị dialog show chọn option
     * @param state
     */
    public void doShowProductOptionInput(State state) {
        Product product = (Product) state.getTag(CartItemListController.STATE_ON_SHOW_PRODUCT_OPTION);
        doShowProductOptionInput(product);
    }

    /**
     * Hiển thị dialog show chọn option
     * @param product
     */
    public void doShowProductOptionInput(Product product) {
        // khởi tạo và hiển thị dialog
        if (mDialog == null) {
            mDialog = com.magestore.app.pos.util.DialogUtil.dialog(getView().getContext(),
                    getView().getContext().getString(R.string.product_option),
                    getView());
        }
        mDialog.show();
        getView().clearList();
        if (product.getProductOption() != null) bindItem(product);
        else doLoadItem(product);
    }

    /**
     * Gán product vào controller và view
     * @param item
     */
    @Override
    public void bindItem(Product item) {
        super.bindItem(item);
        getView().bindItem(item);
    }

    /**
     * Load product option
     * @param item
     * @return
     * @throws Exception
     */
    @Override
    public boolean doLoadItemBackground(Product... item) throws Exception  {
        getService().retrieve(item[0]);
        return true;
    }

    /**
     * Load product option thành công, gán vào view để xử lý
     * @param success
     * @param item
     */
    @Override
    public void onLoadItemPostExecute(boolean success, Product... item) {
        super.onLoadItemPostExecute(success, item);
        if (success) bindItem(item[0]);
    }
}
