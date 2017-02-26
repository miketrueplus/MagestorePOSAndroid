package com.magestore.app.lib.view.adapter;

import com.magestore.app.lib.model.Model;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Mike on 2/21/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ModelViewCollection<TModelView extends ModelView> extends HashMap<Model, TModelView> {
    @Override
    public TModelView put(Model key, TModelView value) {
        value.setModel(key);
        return super.put(key, value);
    }
}