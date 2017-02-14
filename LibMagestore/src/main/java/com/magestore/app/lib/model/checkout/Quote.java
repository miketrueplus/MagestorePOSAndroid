package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import java.util.List;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface Quote extends Model {
    void setQuoteId(String strId);
    String getStoreId();
    void setStoreId(String strStoreId);
    String getCustomerId();
    void setCustomerId(String strCustomerId);
    String getCurrencyId();
    void setCurrencyId(String strCurrencyId);
    String getTillId();
    void setTillId(String strTillId);
    List<QuoteItems> getItems();
    QuoteCustomer getCustomer();
}
