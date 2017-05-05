package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderCartItem;
import com.magestore.app.lib.model.sales.OrderCustomSalesInfo;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.util.StringUtil;

/**
 * Created by folio on 5/4/2017.
 */

public class PosOrderCustomSalesInfo extends PosAbstractModel implements OrderCustomSalesInfo {
    String product_id;
    String product_name;
    String unit_price;
    String tax_class_id;
    String is_virtual;
    String qty;

    @Override
    public String getProductId() {
        return product_id;
    }

    @Override
    public String getProductName() {
        return product_name;
    }

    @Override
    public float getUnitPrice() {
        return Float.parseFloat(unit_price);
    }

    @Override
    public String getTaxClassId() {
        return tax_class_id;
    }

    @Override
    public boolean isVirtual() {
        return StringUtil.STRING_ONE.equals(is_virtual);
    }

    @Override
    public int getQty() {
        return (int) Float.parseFloat(qty);
    }
}
