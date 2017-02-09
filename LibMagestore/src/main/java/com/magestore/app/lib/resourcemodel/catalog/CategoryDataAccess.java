package com.magestore.app.lib.resourcemodel.catalog;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.ListDataAccess;

import java.io.IOException;
import java.util.List;

/**
 * Created by Johan on 2/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface CategoryDataAccess extends DataAccess, ListDataAccess<Category> {
    List<Category> getListCategory(Category category) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
}
