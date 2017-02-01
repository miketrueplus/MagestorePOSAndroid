package com.magestore.app.lib.resourcemodel;

import com.magestore.app.lib.model.Model;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 1/25/2017.
 * Magestore
 * mike@trueplus.vn
 */
public interface ParentListDataAccess<TModel extends Model> extends DataAccess {
    int count() throws ParseException, InstantiationException, IllegalAccessException, IOException;
    List<TModel> retrieve(int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    boolean update(TModel... models) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    boolean insert(TModel... models) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    boolean delete(TModel... models) throws IOException, InstantiationException, ParseException, IllegalAccessException;
}
