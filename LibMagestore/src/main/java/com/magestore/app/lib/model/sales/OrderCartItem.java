package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.magento.checkout.cart.PosCartItem;

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

    float getUnitPrice();

    float getBaseUnitPrice();

    float getOriginalPrice();

    float getBaseOriginalPrice();

    String getOptionsLabel();

    List<OrderCustomSalesInfo> getCustomSalesInfo();
}
