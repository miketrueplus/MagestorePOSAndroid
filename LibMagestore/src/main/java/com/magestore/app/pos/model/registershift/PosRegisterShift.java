package com.magestore.app.pos.model.registershift;

import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosRegisterShift extends PosAbstractModel implements RegisterShift {
    String entity_id;
    String shift_id;
    String staff_id;
    String location_id;
    float float_amount;
    float base_float_amount;
    float closed_amount;
    float base_closed_amount;
    float cash_left;
    float base_cash_left;
    float total_sales;
    float base_total_sales;
    float base_balance;
    float balance;
    float cash_sale;
    float base_cash_sale;
    float cash_added;
    float base_cash_added;
    float cash_removed;
    float base_cash_removed;
    String opened_at;
    String closed_at;
    String opened_note;
    String closed_note;
    String status;
    String base_currency_code;
    String shift_currency_code;
    String indexeddb_id;
    String updated_at;
    String staff_name;

    @Override
    public String getID() {
        return entity_id;
    }

    @Override
    public float getTotalSales() {
        return total_sales;
    }

    @Override
    public String getOpenedAt() {
        return opened_at;
    }

    @Override
    public String getClosedAt() {
        return closed_at;
    }

    @Override
    public String getUpdateAt() {
        return updated_at;
    }

    @Override
    public String getOpenAndClose() {
        return opened_at + "-" + closed_at;
    }
}
