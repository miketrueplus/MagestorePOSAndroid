package com.magestore.app.pos.parse.gson2pos;

import com.magestore.app.lib.parse.ParseModel;

/**
 * Created by Mike on 12/19/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class Gson2PosGenericParseModel<T> implements ParseModel {
    public T items;
    public int total_count;
}