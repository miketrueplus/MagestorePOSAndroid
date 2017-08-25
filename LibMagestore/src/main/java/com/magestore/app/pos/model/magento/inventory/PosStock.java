package com.magestore.app.pos.model.magento.inventory;

import com.magestore.app.lib.model.inventory.Stock;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.util.StringUtil;

/**
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosStock extends PosAbstractModel implements Stock {
    String item_id;
    String product_id;
    String stock_id;
    String qty;
    String min_qty;
    String use_config_min_qty;
    String is_qty_decimal;
    String backorders;
    String use_config_backorders;
    String min_sale_qty;
    String use_config_min_sale_qty;
    String max_sale_qty;
    String use_config_max_sale_qty;
    String is_in_stock;
    String low_stock_date;
    String notify_stock_qty;
    String use_config_notify_stock_qty;
    String manage_stock;
    String use_config_manage_stock;
    String stock_status_changed_auto;
    String use_config_qty_increments;
    String qty_increments;
    String use_config_enable_qty_inc;
    String enable_qty_increments;
    String is_decimal_divided;
    String website_id;
    String updated_time;
    String type_id;

    @Override
    public int getQuantity() {
        return Integer.parseInt(qty);
    }

    @Override
    public boolean isUseConfigMinQuantity() {
        return StringUtil.STRING_ONE.equals(use_config_min_qty);
    }

    @Override
    public int getMinQuantity() {
        return Integer.parseInt(min_qty);
    }

    @Override
    public boolean isUseConfigMinSaleQuantity() {
        return StringUtil.STRING_ONE.equals(use_config_min_sale_qty);
    }

    @Override
    public int getMinSaleQuantity() {
        return Integer.parseInt(min_sale_qty);
    }

    @Override
    public boolean isUseConfigMaxSaleQuantity() {
        return StringUtil.STRING_ONE.equals(use_config_max_sale_qty);
    }

    @Override
    public int getMaxSaleQuantity() {
        return Integer.parseInt(max_sale_qty);
    }

    @Override
    public int getQuantityIncrement() {
        if (qty_increments == null || StringUtil.STRING_EMPTY.equals(qty_increments)) return 1;
        if (!StringUtil.STRING_ONE.equals(enable_qty_increments)) return 1;
        return (int) Float.parseFloat(qty_increments);
    }
}
