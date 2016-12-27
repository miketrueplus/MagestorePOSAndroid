package com.magestore.app.lib.parse.gson2pos;

import com.google.gson.InstanceCreator;
import com.magestore.app.lib.entity.pos.PosProduct;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Vector;

/**
 * Created by Mike on 12/19/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class Gson2PosInstance implements InstanceCreator<Gson2PosListProduct> {
    @Override
    public Gson2PosListProduct createInstance(Type type) {
        Vector<PosProduct> list = new Vector<PosProduct>();
        list.add(null);
        Gson2PosListProduct listProduct = new Gson2PosListProduct();
        listProduct.items = list;
        return listProduct;
    }
}
