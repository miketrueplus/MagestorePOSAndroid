package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 12/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface ConfigTaxRules extends Model {
    List<String> getCustomerTcIds();
    void setCustomerTcIds(List<String> mListCustomerTcIds);
    List<String> getProductTcIds();
    void setProductTcIds(List<String> mListProductTcIds);
    List<String> getRatesIds();
    void setRatesIds(List<String> mListRatesIds);
    String getPriority();
    void setPriority(String strPriority);
}
