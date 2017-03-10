package com.magestore.app.pos.model.catalog;

import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 2/16/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosProductOptionJsonConfigAttributes extends PosAbstractModel {
    public class Option {
        public String id;
        public String label;
        public List<String> products;
    }
    public List<Option> options;
    public String code;
    public String label;
    public String position;

}
