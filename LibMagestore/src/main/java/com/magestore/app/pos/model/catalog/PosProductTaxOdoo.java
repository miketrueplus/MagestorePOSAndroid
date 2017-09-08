package com.magestore.app.pos.model.catalog;

import java.util.List;

/**
 * Created by Johan on 9/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosProductTaxOdoo extends PosProduct {
    private List<String> taxes_id;
    private List<PosProductTaxDetailOdoo> taxes_detail;

    @Override
    public List<String> getTaxId() {
        return taxes_id;
    }

    @Override
    public void setTaxId(List<String> listTaxId) {
        taxes_id = listTaxId;
    }
}
