package com.magestore.app.pos.task;

import com.magestore.app.lib.task.TaskListener;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.service.catalog.ProductService;
import com.magestore.app.lib.service.ServiceFactory;

import java.util.List;

/**
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class LoadProductTask extends AsyncTaskAbstractTask<Void, Void, List<Product>> {
    public LoadProductTask(TaskListener listener) {
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
            ServiceFactory serviceFactory = ServiceFactory.getFactory(null);
            ProductService productUseCase = serviceFactory.generateProductService();
            List<Product> listProduct = productUseCase.retrieveProductList(30);
            return listProduct;
        } catch (Exception e) {
            // ngừng thực hiện tiến trình
            cancel(e, true);
            return null;
        }
    }
}
