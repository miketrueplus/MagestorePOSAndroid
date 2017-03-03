package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.service.catalog.CategoryService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CategoryListController extends AbstractListController<Category> {

    CategoryService mCategoryService;
    ProductListController mProductListController;

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

    @Override
    public void bindItem(Category item) {
        super.bindItem(item);
        if (mProductListController != null) {
            mProductListController.setCategory(item);
        }
    }

    public List<Category> getListChildCategory(Category category) {
        try {
            return mCategoryService.getListCategory(category);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        List<Category> listCategory = new ArrayList<>();
        return listCategory;
    }

    public void selectCategoryChild(Category category) {
        mList = new ArrayList<>();
        mList.addAll(getListChildCategory(category));
        bindList(mList);
        getView().notifyDataSetChanged();
    }

    public void setProductListController(ProductListController mProductListController) {
        mProductListController = mProductListController;
    }
}
