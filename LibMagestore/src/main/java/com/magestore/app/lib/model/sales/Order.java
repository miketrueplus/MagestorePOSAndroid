package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.cart.Items;
import com.magestore.app.pos.model.sales.PosOrderAttributes;
import com.magestore.app.pos.model.sales.PosOrderBillingAddress;
import com.magestore.app.pos.model.sales.PosOrderPayment;
import com.magestore.app.pos.model.sales.PosOrderWebposPayment;

import java.util.List;

/**
 * Created by Mike on 12/11/2016.
 */

public interface Order extends Model {
    Order newInstance();
    List<Items> newOrderItems();
    List<Items> getOrderItems();
    float getSubTotal();
    float getDiscountTotal();
    float getLastTotal();
    float getTaxTotal();
    PosOrderAttributes getExtensionAttributes();
    PosOrderBillingAddress getBillingAddress();
    PosOrderPayment getPayment();
    String getIncrementId();
    String getCustomerFirstname();
    String getCustomerLastname();
    float getGrandTotal();
    String getCreatedAt();
    String getCustomerEmail();
    String getStatus();
    String getWebposStaffName();
    String getBillingAddressName();
    String getBillingAddressFullAddress();
    String getBillingAddressTelephone();
    String getShippingAddressName();
    String getShippingFullAddress();
    String getShippingTelePhone();
    String getShippingDescription();
    List<OrderWebposPayment> getWebposOrderPayments();
    List<OrderStatus> getOrderStatus();
    boolean checkComment();
}
