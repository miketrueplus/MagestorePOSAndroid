package com.magestore.app.lib.view.item;

import com.magestore.app.lib.model.Model;

import java.util.HashMap;

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