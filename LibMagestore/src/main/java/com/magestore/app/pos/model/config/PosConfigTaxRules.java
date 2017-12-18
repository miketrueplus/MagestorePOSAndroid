package com.magestore.app.pos.model.config;

import com.magestore.app.lib.model.config.ConfigTaxRules;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 12/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosConfigTaxRules extends PosAbstractModel implements ConfigTaxRules {
    List<String> customer_tc_ids;
    List<String> product_tc_ids;
    List<String> rates_ids;
    String priority;

    @Override
    public List<String> getCustomerTcIds() {
        return customer_tc_ids;
    }

    @Override
    public void setCustomerTcIds(List<String> mListCustomerTcIds) {
        customer_tc_ids = mListCustomerTcIds;
    }

    @Override
    public List<String> getProductTcIds() {
        return product_tc_ids;
    }

    @Override
    public void setProductTcIds(List<String> mListProductTcIds) {
        product_tc_ids = mListProductTcIds;
    }

    @Override
    public List<String> getRatesIds() {
        return rates_ids;
    }

    @Override
    public void setRatesIds(List<String> mListRatesIds) {
        rates_ids = mListRatesIds;
    }

    @Override
    public String getPriority() {
        return priority;
    }

    @Override
    public void setPriority(String strPriority) {
        priority = strPriority;
    }
}
