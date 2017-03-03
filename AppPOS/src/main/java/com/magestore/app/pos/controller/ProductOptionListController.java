package com.magestore.app.pos.controller;

import android.view.View;
import android.widget.Toast;

import com.magestore.app.lib.controller.AbstractChildListController;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.catalog.ProductOptionService;
import com.magestore.app.pos.R;
import com.magestore.app.pos.panel.CustomerAddressDetailPanel;
import com.magestore.app.pos.view.MagestoreDialog;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 3/1/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ProductOptionListController extends AbstractChildListController<Product, ProductOption> {
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
//        if (mDialog == null) {
//            mDialog = com.magestore.app.pos.util.DialogUtil.dialog(mView.getContext(),
//                    mView.getContext().getString(R.string.product_option),
//                    mView);
//        }
//        mDialog.show();

        // bind product
//        bindParent(product);

        ProductOptionService productOptionService = null;
        try {
            ServiceFactory factory = ServiceFactory.getFactory(getMagestoreContext());
            productOptionService = factory.generateProductOptionService();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        try {
            productOptionService.retrieve(product);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onRetrievePostExecute(List<ProductOption> list) {
//        super.onRetrievePostExecute(list);
        mView.bindList(list);
    }
}
