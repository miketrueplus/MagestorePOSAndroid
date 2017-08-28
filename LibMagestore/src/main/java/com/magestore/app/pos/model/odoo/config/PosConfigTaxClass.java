package com.magestore.app.pos.model.odoo.config;

import com.magestore.app.lib.model.config.ConfigTaxClass;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 5/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosConfigTaxClass extends PosAbstractModel implements ConfigTaxClass {
    String class_id;
    String class_name;
    String class_type;

    @Override
    public String getID() {
        return class_id;
    }


    @Override
    public void setClassID(String strClassID) {
        class_id = strClassID;
    }

    @Override
    public String getClassName() {
        return class_name;
    }

    @Override
    public String getClassType() {
        return class_type;
    }

    @Override
    public void setClassName(String strClassName) {
        class_name = strClassName;
    }

    @Override
    public void setClassType(String strClassType) {
        class_type = strClassType;
    }

    @Override
    public String getDisplayContent() {
        return class_name;
    }
}
