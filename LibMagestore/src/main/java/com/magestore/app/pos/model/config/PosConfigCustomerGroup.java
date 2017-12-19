package com.magestore.app.pos.model.config;

import com.magestore.app.lib.model.config.ConfigCustomerGroup;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 12/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosConfigCustomerGroup extends PosAbstractModel implements ConfigCustomerGroup {
    String code;
    String tax_class_id;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String strCode) {
        code = strCode;
    }

    @Override
    public String getTaxClassId() {
        return tax_class_id;
    }

    @Override
    public void setTaxClassId(String strTaxClassId) {
        tax_class_id = strTaxClassId;
    }
}
