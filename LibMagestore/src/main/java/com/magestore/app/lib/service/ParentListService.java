package com.magestore.app.lib.service;

import com.magestore.app.lib.model.Model;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 1/24/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ParentListService<TModel extends Model, TChild extends Model> extends Service {
    int count(TModel parent) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    List<TChild> retrieve(TModel parent, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    boolean update(TModel parent, TChild... childs) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    boolean insert(TModel parent, TChild... childs) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    boolean delete(TModel parent, TChild... models) throws IOException, InstantiationException, ParseException, IllegalAccessException;
}