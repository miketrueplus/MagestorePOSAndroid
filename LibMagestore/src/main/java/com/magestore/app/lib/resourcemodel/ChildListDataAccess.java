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
public interface ChildListDataAccess<TParent extends Model, TModel extends Model> extends DataAccess {
    int count(TParent parent) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    TModel retrieve(TParent parent, String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    List<TModel> retrieve(TParent parent) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    List<TModel> retrieve(TParent parent, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    List<TModel> retrieve(TParent parent, String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    boolean update(TParent parent, TModel oldChild, TModel newChild) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    boolean insert(TParent parent, TModel... childs) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    boolean delete(TParent parent, TModel... childs) throws ParseException, InstantiationException, IllegalAccessException, IOException;
}