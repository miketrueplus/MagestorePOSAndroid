package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

/**
 * Created by baonguyen on 28/04/2017.
 */

public interface OrderCredit extends Model {
    void setAmount(float fAmount);

    float getAmount();

    void setCustomerId(String id);

    String getCustomerId();

    void setOrderIncrementId(String id);

    String getOrderIncrementId();
}
