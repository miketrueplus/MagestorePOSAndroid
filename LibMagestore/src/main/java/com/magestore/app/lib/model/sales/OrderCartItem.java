package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.model.sales.PosOrderCustomSalesInfo;

import java.util.List;

/**
 * Created by folio on 5/3/2017.
 */

public interface OrderCartItem extends Model {

    List<PosCartItem.OptionsValue> getOptions();

    List<PosCartItem.OptionsValue> getSuperAttribute();

    List<PosCartItem.OptionsValue> getBundleOption();

    List<PosCartItem.OptionsValue> getBundleOptionQty();

    PosCartItem.OptionsValue createOptionValue();

    String getChildId();

    int getQty();
    void setQty(String iQty);

    float getUnitPrice();
    void setUnitPrice(String fUnitPrice);

    float getBaseUnitPrice();
    void setBaseUnitPrice(String fBaseUnitPrice);

    float getOriginalPrice();
    void setOriginalPrice(String fOriginalPrice);

    float getBaseOriginalPrice();
    void setBaseOriginalPrice(String fBaseOriginalPrice);

    String getOptionsLabel();

    List<OrderCustomSalesInfo> getCustomSalesInfo();
}
