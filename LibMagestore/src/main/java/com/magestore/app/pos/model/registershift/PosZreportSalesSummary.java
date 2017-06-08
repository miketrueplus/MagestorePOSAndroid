package com.magestore.app.pos.model.registershift;

import com.magestore.app.lib.model.registershift.ZreportSalesSummary;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosZreportSalesSummary extends PosAbstractModel implements ZreportSalesSummary {
    float grand_total;
    float discount_amount;
    float total_refunded;
    float giftvoucher_discount;
    float rewardpoints_discount;

    @Override
    public float getGrandTotal() {
        return grand_total;
    }

    @Override
    public float getDiscountAmount() {
        return discount_amount;
    }

    @Override
    public float getTotalRefunded() {
        return total_refunded;
    }

    @Override
    public float getGiftvoucherDiscount() {
        return giftvoucher_discount;
    }

    @Override
    public float getRewardpointsDiscount() {
        return rewardpoints_discount;
    }
}
