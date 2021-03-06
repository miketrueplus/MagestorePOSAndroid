package com.magestore.app.pos.model.checkout;

import com.google.gson.annotations.Expose;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.CheckoutTotals;
import com.magestore.app.lib.model.checkout.Quote;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.model.plugins.GiftCardRespone;
import com.magestore.app.lib.model.plugins.RewardPoint;
import com.magestore.app.lib.model.plugins.StoreCredit;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.model.plugins.PosGiftCardRespone;
import com.magestore.app.pos.model.plugins.PosRewardPoint;
import com.magestore.app.pos.model.plugins.PosStoreCredit;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCheckout extends PosAbstractModel implements Checkout {
    String customer_id;
    List<PosCartItem> items;
    List<PosCheckoutPayment> payment;
    List<PosCheckoutShipping> shipping;
    Customer customer;
    List<PosCheckoutToTals> totals;
    PosQuote quote_init;
    String create_at;
    int status;

    private class CheckoutConfig {
        String apply_promotion = "0";
        String note;
        boolean create_invoice = true;
        boolean create_shipment = false;
        int cart_base_discount_amount = 0;
        int cart_discount_amount = 0;
        String cart_discount_name;
        String currency_code;
    }

    CheckoutConfig config;
    String coupon_code;

    private class Extension {
        String key;
        String value;
    }

    Extension extension_data;
    ArrayList<Model> session_data;
    ArrayList<Model> Sintegration;

    @Gson2PosExclude
    String quote_id;
    @Gson2PosExclude
    String store_id;
    @Gson2PosExclude
    String create_ship;
    @Gson2PosExclude
    String create_invoice;
    @Gson2PosExclude
    String note;
    @Gson2PosExclude
    Order order_success;

    @Gson2PosExclude
    String sub_title;
    @Expose(serialize = false, deserialize = false)
    float sub_total = 0;
    @Gson2PosExclude
    float sub_total_view;
    @Gson2PosExclude
    String shipping_title;
    @Expose(serialize = false, deserialize = false)
    float shipping_total = 0;
    @Gson2PosExclude
    String tax_title;
    @Expose(serialize = false, deserialize = false)
    float tax_total = 0;
    @Gson2PosExclude
    String discount_title;
    @Expose(serialize = false, deserialize = false)
    float discount_total = 0;
    @Gson2PosExclude
    String grand_title;
    @Expose(serialize = false, deserialize = false)
    float grand_total = 0;
    @Gson2PosExclude
    float grand_total_view;
    @Gson2PosExclude
    float real_amount;
    @Gson2PosExclude
    float remain_money;
    @Gson2PosExclude
    float exchange_money;
    @Gson2PosExclude
    String delivery_date;
    @Gson2PosExclude
    boolean is_pick_at_store;
    // plugin giftcard
    @Gson2PosExclude
    String giftcard_title;
    @Gson2PosExclude
    float giftcard_discount;
    @Gson2PosExclude
    String reward_point_use_point_title;
    @Gson2PosExclude
    float reward_point_use_point_value;
    @Gson2PosExclude
    int reward_point_earn_point_value;

    @Gson2PosExclude
    List<GiftCard> list_gift_card_use;

    @Gson2PosExclude
    PosGiftCardRespone giftcard;
    @Gson2PosExclude
    PosRewardPoint rewardpoints;
    @Gson2PosExclude
    PosStoreCredit storecredit;

    @Override
    public String getCustomerID() {
        return customer_id;
    }

    @Override
    public void setCustomerID(String strCustomerID) {
        this.customer_id = strCustomerID;
    }

    @Override
    public List<CartItem> getCartItem() {
        return (List<CartItem>) (List<?>) items;
    }

    @Override
    public void setCartItem(List<CartItem> cartItem) {
        this.items = (List<PosCartItem>) (List<?>) cartItem;
    }

    public List<CheckoutShipping> getCheckoutShipping() {
        return (List<CheckoutShipping>) (List<?>) shipping;
    }

    public void setCheckoutShipping(List<CheckoutShipping> shiping) {
        this.shipping = (List<PosCheckoutShipping>) (List<?>) shiping;
    }

    @Override
    public List<CheckoutPayment> getCheckoutPayment() {
        return (List<CheckoutPayment>) (List<?>) payment;
    }

    @Override
    public void setCheckoutPayment(List<CheckoutPayment> payment) {
        this.payment = (List<PosCheckoutPayment>) (List<?>) payment;
    }

    @Override
    public String getCouponCode() {
        return coupon_code;
    }

    @Override
    public void setCouponCode(String strCouponCode) {
        this.coupon_code = strCouponCode;
    }

    @Override
    public String getSubTitle() {
        return sub_title;
    }

    @Override
    public void setSubTitle(String strSubTitle) {
        sub_title = strSubTitle;
    }

    @Override
    public float getSubTotal() {
        return sub_total;
    }

    @Override
    public void setSubTotal(float total) {
        sub_total = total;
    }

    @Override
    public float getSubTotalView() {
        return sub_total_view;
    }

    @Override
    public void setSubTotalView(float total) {
        sub_total_view = total;
    }

    @Override
    public String getShippingTitle() {
        return shipping_title;
    }

    @Override
    public void setShippingTitle(String strShippingTitle) {
        shipping_title = strShippingTitle;
    }

    @Override
    public float getShippingTotal() {
        return shipping_total;
    }

    @Override
    public void setShippingTotal(float shipping) {
        shipping_total = shipping;
    }

    @Override
    public String getTaxTitle() {
        return tax_title;
    }

    @Override
    public void setTaxTitle(String strTaxTitle) {
        tax_title = strTaxTitle;
    }

    @Override
    public float getTaxTotal() {
        return tax_total;
    }

    @Override
    public void setTaxTotal(float total) {
        tax_total = total;
    }

    @Override
    public String getDiscountTitle() {
        return discount_title;
    }

    @Override
    public void setDiscountTitle(String strDiscountTitle) {
        discount_title = strDiscountTitle;
    }

    @Override
    public float getDiscountTotal() {
        return discount_total;
    }

    @Override
    public void setDiscountTotal(float total) {
        discount_total = total;
    }

    @Override
    public String getGrandTitle() {
        return grand_title;
    }

    @Override
    public void setGrandTitle(String strGrandTitle) {
        grand_title = strGrandTitle;
    }

    @Override
    public float getGrandTotalView() {
        return grand_total_view;
    }

    @Override
    public void setGrandTotalView(float total) {
        grand_total_view = total;
    }

    @Override
    public float getGrandTotal() {
        return grand_total;
    }

    @Override
    public void setGrandTotal(float total) {
        grand_total = total;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public List<CheckoutTotals> getTotals() {
        return (List<CheckoutTotals>) (List<?>) totals;
    }

    @Override
    public void setTotals(List<CheckoutTotals> checkoutTotals) {
        totals = (List<PosCheckoutToTals>) (List<?>) checkoutTotals;
    }

    @Override
    public Quote getQuote() {
        return quote_init;
    }

    @Override
    public void setQuote(Quote quote) {
        quote_init = (PosQuote) quote;
    }

    @Override
    public String getCreateShip() {
        return create_ship;
    }

    @Override
    public void setCreateShip(String strCreateShip) {
        create_ship = strCreateShip;
    }

    @Override
    public String getCreateInvoice() {
        return create_invoice;
    }

    @Override
    public void setCreateInvoice(String strCreateInvoice) {
        create_invoice = strCreateInvoice;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String strNote) {
        note = strNote;
    }

    @Override
    public float getRealAmount() {
        return real_amount;
    }

    @Override
    public void setRealAmount(float fRealAmount) {
        real_amount = fRealAmount;
    }

    @Override
    public float getRemainMoney() {
        return remain_money;
    }

    @Override
    public void setRemainMoney(float fRemainMoney) {
        remain_money = fRemainMoney;
    }

    @Override
    public float getExchangeMoney() {
        return exchange_money;
    }

    @Override
    public void setExchangeMoney(float fExchangeMoney) {
        exchange_money = fExchangeMoney;
    }

    @Override
    public String getCreateAt() {
        return create_at;
    }

    @Override
    public void setCreateAt(String strCreateAt) {
        create_at = strCreateAt;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int intStatus) {
        status = intStatus;
    }

    @Override
    public Order getOrderSuccess() {
        return order_success;
    }

    @Override
    public void setOrderSuccess(Order orderSuccess) {
        order_success = orderSuccess;
    }

    @Override
    public String getQuoteId() {
        return quote_id;
    }

    @Override
    public void setQuoteId(String quoteId) {
        quote_id = quoteId;
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
    public String getDeliveryDate() {
        return delivery_date;
    }

    @Override
    public void setDeliveryDate(String strDeliveryDate) {
        delivery_date = strDeliveryDate;
    }

    @Override
    public String getGiftCardTitle() {
        return giftcard_title;
    }

    @Override
    public void setGiftCardTitle(String strGiftCardTitle) {
        giftcard_title = strGiftCardTitle;
    }

    @Override
    public float getGiftCardDiscount() {
        return giftcard_discount;
    }

    @Override
    public void setGiftCardDiscount(float fGiftCardDiscount) {
        giftcard_discount = fGiftCardDiscount;
    }

    @Override
    public String getRewardPointUsePointTitle() {
        return reward_point_use_point_title;
    }

    @Override
    public void setRewardPointUsePointTitle(String strRewardPointUsePointTitle) {
        reward_point_use_point_title = strRewardPointUsePointTitle;
    }

    @Override
    public float getRewardPointUsePointValue() {
        return reward_point_use_point_value;
    }

    @Override
    public void setRewardPointUsePointValue(float fRewardPointUsePointValue) {
        reward_point_use_point_value = fRewardPointUsePointValue;
    }

    @Override
    public int getRewardPointEarnPointValue() {
        return reward_point_earn_point_value;
    }

    @Override
    public void setRewardPointEarnPointValue(int fRewardPointEarnPointValue) {
        reward_point_earn_point_value = fRewardPointEarnPointValue;
    }

    @Override
    public GiftCardRespone getGiftCard() {
        return (GiftCardRespone) giftcard;
    }

    @Override
    public RewardPoint getRewardPoint() {
        return (RewardPoint) rewardpoints;
    }

    @Override
    public void setRewardPoint(RewardPoint rewardPoint) {
        rewardpoints = (PosRewardPoint) rewardPoint;
    }

    @Override
    public StoreCredit getStoreCredit() {
        return (StoreCredit) storecredit;
    }

    @Override
    public boolean isPickAtStore() {
        return is_pick_at_store;
    }

    @Override
    public void setIsPickAtStore(boolean bIsPickAtStore) {
        is_pick_at_store = bIsPickAtStore;
    }

    @Override
    public List<GiftCard> getListGiftCardUse() {
        return list_gift_card_use;
    }

    @Override
    public void setListGiftCardUse(List<GiftCard> listListGiftCardUse) {
        list_gift_card_use = listListGiftCardUse;
    }
}
