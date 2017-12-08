package com.magestore.app.pos.model.registershift;

import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.config.PosConfigPriceFormat;
import com.magestore.app.pos.model.directory.PosCurrency;

/**
 * Created by Johan on 6/2/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosPointOfSales extends PosAbstractModel implements PointOfSales {
    String pos_id;
    String pos_name;
    String location_id;
    String staff_id;
    String store_id;
    String status;

    // Odoo
    String pos_session_username;
    String iface_tax_included;
    String receipt_header;
    boolean cash_control = true;
    String tip_product_id;
    String current_session_state;
    String create_date;
    String iface_print_auto;
    String iface_invoicing;
    String receipt_footer;
    String iface_cashdrawer;
    boolean iface_discount;
    float discount_pc;
    String discount_product_id;
    PosCurrency currency;
    PosConfigPriceFormat priceFormat;

    @Override
    public String getID() {
        if (pos_id == null) {
            return location_id;
        }
        return pos_id;
    }

    @Override
    public String getPosId() {
        return pos_id;
    }

    @Override
    public void setPosId(String strPosId) {
        pos_id = strPosId;
    }

    @Override
    public String getPosName() {
        return pos_name;
    }

    @Override
    public void setPosName(String strPosName) {
        pos_name = strPosName;
    }

    @Override
    public String getLocationId() {
        return location_id;
    }

    @Override
    public void setLocationId(String strLocationId) {
        location_id = strLocationId;
    }

    @Override
    public String getStoreId() {
        return store_id;
    }

    @Override
    public void setStoreId(String strStoreId) {
        store_id = strStoreId;
    }

    @Override
    public String getStaffId() {
        return staff_id;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public boolean getCashControl() {
        return cash_control;
    }

    @Override
    public void setCashControl(boolean bCashControl) {
        cash_control = bCashControl;
    }

    @Override
    public boolean getIfaceDiscount() {
        return iface_discount;
    }

    @Override
    public void setIfaceDiscount(boolean bIfaceDiscount) {
        iface_discount = bIfaceDiscount;
    }

    @Override
    public float getDiscountPC() {
        return discount_pc;
    }

    @Override
    public void setDiscountPC(float fDiscountPC) {
        discount_pc = fDiscountPC;
    }

    @Override
    public String getDiscountProductId() {
        return discount_product_id;
    }

    @Override
    public void setDiscountProductId(String strDiscountProductId) {
        discount_product_id = strDiscountProductId;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(Currency mCurrency) {
        currency = (PosCurrency) mCurrency;
    }

    @Override
    public ConfigPriceFormat getPriceFormat() {
        return priceFormat;
    }

    @Override
    public void setPriceFormat(ConfigPriceFormat mPriceFormat) {
        priceFormat = (PosConfigPriceFormat) mPriceFormat;
    }

    @Override
    public String getReceiptHeader() {
        return receipt_header;
    }

    @Override
    public void setReceiptHeader(String strReceiptHeader) {
        receipt_header = strReceiptHeader;
    }

    @Override
    public String getReceiptFooter() {
        return receipt_footer;
    }

    @Override
    public void setReceiptFooter(String strReceiptFooter) {
        receipt_footer = strReceiptFooter;
    }

    @Override
    public String getDisplayContent() {
        return pos_name;
    }
}
