package com.magestore.app.lib.service;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Region;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 1/24/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ChildListService<TParent extends Model, TModel extends Model> extends Service {
    TModel create(TParent parent);
    int count(TParent parent) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    TModel retrieve(TParent parent, String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    List<TModel> retrieve(TParent parent, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    List<TModel> retrieve(TParent parent) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    List<TModel> retrieve(TParent parent, String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    boolean update(TParent parent, TModel oldModel, TModel newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    boolean insert(TParent parent, TModel... childs) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    boolean delete(TParent parent, TModel... childs) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    Region createRegion();
}