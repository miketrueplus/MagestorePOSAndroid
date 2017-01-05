package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.ControllerListener;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.usecase.ProductUseCase;
import com.magestore.app.lib.usecase.UseCaseFactory;

import java.util.List;

/**
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class LoadProductController extends AsyncTaskAbstractController<Void, Void, List<Product>> {
    public LoadProductController(ControllerListener listener) {
        super(listener);
    }

    /**
     * Thực hiện quá trình đăng nhập
     * @param params
     * @return
     */
    @Override
    protected List<Product> doInBackground(Void... params) {
        try {
            ProductUseCase productUseCase = UseCaseFactory.generateProductUseCase(null, null);
            List<Product> listProduct = productUseCase.retrieveProductList(30);
            return listProduct;
        } catch (Exception e) {
            // ngừng thực hiện tiến trình
            cancel(e, true);
            return null;
        }
    }
}
