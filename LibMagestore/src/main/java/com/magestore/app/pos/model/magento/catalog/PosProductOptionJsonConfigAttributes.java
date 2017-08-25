package com.magestore.app.pos.model.magento.catalog;

import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

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
