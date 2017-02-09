package com.magestore.app.pos.service.catalog;

import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.catalog.CategoryDataAccess;
import com.magestore.app.lib.service.catalog.CategoryService;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Johan on 2/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCategoryService extends AbstractService implements CategoryService {
    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 0;
    }

    @Override
    public Category create() {
        return null;
    }

    @Override
    public Category retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<Category> retrieve(int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CategoryDataAccess categoryDataAccess = factory.generateCategoryDataAccess();

        // Lấy danh sách Category
        return categoryDataAccess.retrieve(page, pageSize);
    }

    @Override
    public List<Category> retrieve() throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public List<Category> retrieve(String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public boolean update(Category oldModel, Category newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public boolean insert(Category... categories) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public boolean delete(Category... categories) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public List<Category> getListCategory(Category category) throws IOException, InstantiationException, ParseException, IllegalAccessException{
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CategoryDataAccess categoryDataAccess = factory.generateCategoryDataAccess();

        // Lấy danh sách các category con
        return categoryDataAccess.getListCategory(category);
    }
}
