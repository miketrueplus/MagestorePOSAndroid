package com.magestore.app.lib.service.catalog;

import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.service.ListService;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Johan on 2/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface CategoryService extends Service, ListService<Category> {
    List<Category> getListCategory(Category category) throws IOException, InstantiationException, ParseException, IllegalAccessException;
}
