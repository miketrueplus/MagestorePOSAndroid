package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderCartItem;
import com.magestore.app.lib.model.sales.OrderCustomSalesInfo;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.util.StringUtil;

import java.util.List;

/**
 * Created by folio on 5/3/2017.
 */

public class PosOrderCartItem extends PosAbstractModel implements OrderCartItem {
    String child_id;
    String qty;
    String unit_price;
    String base_unit_price;
    String original_price;
    String base_original_price;
    String options_label;
    List<PosOrderCustomSalesInfo> custom_sales_info;

//    public class OptionsValue {
//        public String code;
//        public String value;
//    }

    List<PosCartItem.OptionsValue> options;
    List<PosCartItem.OptionsValue> super_attribute;
    List<PosCartItem.OptionsValue> bundle_option;
    List<PosCartItem.OptionsValue> bundle_option_qty;

    @Override
    public List<PosCartItem.OptionsValue> getOptions() {
        return options;
    }

    @Override
    public List<PosCartItem.OptionsValue> getSuperAttribute() {
        return super_attribute;
    }

    @Override
    public List<PosCartItem.OptionsValue> getBundleOption() {
        return bundle_option;
    }

    @Override
    public List<PosCartItem.OptionsValue> getBundleOptionQty() {
        return bundle_option_qty;
    }

    @Override
    public PosCartItem.OptionsValue createOptionValue() {
        PosCartItem cartItem = new PosCartItem();
        PosCartItem.OptionsValue value = cartItem.createOptionValue();
        return value;
    }

    @Override
    public String getChildId() {
        return child_id;
    }

    @Override
    public int getQty() {
        return (int) Float.parseFloat(qty);
    }

    @Override
    public void setQty(String iQty) {
        qty = iQty;
    }

    @Override
    public float getUnitPrice() {
        if (StringUtil.isNullOrEmpty(unit_price)) return 0.0f;
        return Float.parseFloat(unit_price);
    }

    @Override
    public void setUnitPrice(String fUnitPrice) {
        unit_price = fUnitPrice;
    }

    @Override
    public float getBaseUnitPrice() {
        if (StringUtil.isNullOrEmpty(base_unit_price)) return 0.0f;
        return Float.parseFloat(base_unit_price);
    }

    @Override
    public void setBaseUnitPrice(String fBaseUnitPrice) {
        base_unit_price = fBaseUnitPrice;
    }

    @Override
    public float getOriginalPrice() {
        if (StringUtil.isNullOrEmpty(original_price)) return 0.0f;
        return Float.parseFloat(original_price);
    }

    @Override
    public void setOriginalPrice(String fOriginalPrice) {
        original_price = fOriginalPrice;
    }

    @Override
    public float getBaseOriginalPrice() {
        if (StringUtil.isNullOrEmpty(base_original_price)) return 0.0f;
        return Float.parseFloat(base_original_price);
    }

    @Override
    public void setBaseOriginalPrice(String fBaseOriginalPrice) {
        base_original_price = fBaseOriginalPrice;
    }

    @Override
    public String getOptionsLabel() {
        return options_label;
    }

    @Override
    public List<OrderCustomSalesInfo> getCustomSalesInfo() {
        return (List<OrderCustomSalesInfo>) (List<?>) custom_sales_info;
    }
}
