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
public interface ParentListDataAccess<TModel extends Model, TChild extends Model> extends DataAccess {
    int count(TModel parent, TChild child) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    List<TChild> retrieve(TModel parent, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    boolean update(TModel parent, TChild oldChild, TChild newChild) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    boolean insert(TModel parent, TChild... childs) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    boolean delete(TModel parent, TChild... models) throws ParseException, InstantiationException, IllegalAccessException, IOException;
}