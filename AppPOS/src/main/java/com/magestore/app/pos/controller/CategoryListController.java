package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.service.catalog.CategoryService;

import java.util.List;

/**
 * Created by Johan on 2/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CategoryListController extends AbstractListController<Category> {

    CategoryService mCategoryService;

    public void setCategoryService(CategoryService mCategoryService) {
        this.mCategoryService = mCategoryService;
        setListService(mCategoryService);
    }

    @Override
    public List<Category> onRetrieveBackground(int page, int pageSize) throws Exception {
        List<Category> categories = super.onRetrieveBackground(page, pageSize);
        return categories;
    }

    @Override
    public void onRetrievePostExecute(List<Category> list) {
        super.onRetrievePostExecute(list);
    }
}
