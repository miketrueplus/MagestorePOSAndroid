package com.magestore.app.lib.resourcemodel;

import com.magestore.app.lib.model.Model;

import java.io.IOException;
import com.magestore.app.lib.parse.ParseException;
import java.util.List;

/**
 * Created by Mike on 1/25/2017.
 * Magestore
 * mike@trueplus.vn
 */
public interface ListDataAccess<TModel extends Model> extends DataAccess {
    int count() throws ParseException, InstantiationException, IllegalAccessException, IOException;
    List<TModel> retrieve() throws ParseException, InstantiationException, IllegalAccessException, IOException;
    List<TModel> retrieve(int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    TModel retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    boolean update(TModel oldModel, TModel newModel) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    boolean insert(TModel... models) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    boolean delete(TModel... models) throws ParseException, InstantiationException, IllegalAccessException, IOException;
}
