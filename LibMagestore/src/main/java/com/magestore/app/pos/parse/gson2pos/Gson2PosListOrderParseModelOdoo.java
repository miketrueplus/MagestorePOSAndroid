package com.magestore.app.pos.parse.gson2pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.OrderCartItem;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.model.sales.PosOrder;
import com.magestore.app.pos.model.sales.PosOrderBillingAddress;
import com.magestore.app.pos.model.sales.PosOrderCartItem;
import com.magestore.app.pos.model.sales.PosOrderItemsInfoBuy;
import com.magestore.app.pos.model.sales.PosOrderStatus;
import com.magestore.app.pos.model.sales.PosOrderWebposPayment;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 9/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class Gson2PosListOrderParseModelOdoo extends Gson2PosAbstractParseImplement {
    @Override
    protected Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        builder.registerTypeAdapter(new TypeToken<List<PosOrder>>() {
        }
                .getType(), new OrderConverter());
        return builder.create();
    }

    private String ORDER_ID = "id";
    private String ORDER_NAME = "name";
    private String ORDER_CREATE_AT = "create_date";
    private String ORDER_STATUS = "state";
    private String ORDER_NOTE = "note";
    private String ORDER_GRAND_TOTAL = "amount_total";
    private String ORDER_TOTAL_PAID = "amount_paid";
    private String ORDER_TOTAL_TAX = "amount_tax";
    private String ORDER_TOTAL_CHANGE = "amount_return";
    private String ORDER_STAFF_NAME = "create_name";
    private String ORDER_STAFF_ID = "create_uid";
    private String ORDER_ITEMS = "lines";
    private String PAYMENT_DETAIL = "statement_detail";
    private String PAYMENT_ID = "journal_id";
    private String PAYMENT_NAME = "journal_name";
    private String PAYMENT_AMOUNT = "amount";
    private String PRODUCT_ID = "product_id";
    private String PRODUCT_NAME = "product_name";
    private String PRODUC_UNIT_PRICE = "price_unit";
    private String PRODUCT_SUBTOTAL = "price_subtotal";
    private String PRODUCT_SUTOTAL_INCl = "price_subtotal_incl";
    private String PRODUCT_DISCOUNT = "discount";
    private String PRODUCT_QTY = "qty";
    private String CUSTOMER_DETAIL = "partner_id";
    private String CUSTOMER_ID = "id";
    private String CUSTOMER_EMAIL = "email";
    private String CUSTOMER_NAME = "name";
    private String CUSTOMER_PHONE = "phone";
    private String CUSTOMER_STATE_ID = "state_id";
    private String CUSTOMER_STATE_NAME = "state_name";
    private String CUSTOMER_STATE_CODE = "state_code";
    private String CUSTOMER_COMPANY = "company_name";
    private String CUSTOMER_POSCODE = "zip";
    private String CUSTOMER_COUNTRY_ID = "country_id";
    private String CUSTOMER_STREET = "street";
    private String CUSTOMER_STREET2 = "street2";
    private String CUSTOMER_VAT = "vat";
    private String CUSTOMER_CITY = "city";
    private String TAX_DETAIL = "taxes_detail";
    private String TAX_AMOUNT = "amount";

    public class OrderConverter implements JsonDeserializer<List<PosOrder>> {
        @Override
        public List<PosOrder> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<PosOrder> listOrder = new ArrayList<>();
            if (json.isJsonArray()) {
                JsonArray arr_order = json.getAsJsonArray();
                if (arr_order != null && arr_order.size() > 0) {
                    float total_subtotal = 0;
                    float total_discount = 0;
                    float total_tax = 0;
                    for (JsonElement el_order : arr_order) {
                        JsonObject obj_order = el_order.getAsJsonObject();
                        PosOrder order = new PosOrder();
                        String id = obj_order.remove(ORDER_ID).getAsString();
                        order.setID(id);
                        String name = obj_order.remove(ORDER_NAME).getAsString();
                        order.setIncrementId(name);
                        String create_at = obj_order.remove(ORDER_CREATE_AT).getAsString();
                        order.setCreateAt(create_at);
                        String status = obj_order.remove(ORDER_STATUS).getAsString();
                        String order_status = "";
                        if (status.equals(StringUtil.STATUS_PAID)) {
                            order_status = StringUtil.STATUS_PENDING;
                        } else if (status.equals(StringUtil.STATUS_INVOICED)) {
                            order_status = StringUtil.STATUS_COMPLETE;
                        } else if (status.equals(StringUtil.STATUS_CANCEL)) {
                            order_status = StringUtil.STATUS_CANCELLED;
                        } else if (status.equals(StringUtil.STATUS_DONE)) {
                            order_status = StringUtil.STATUS_CLOSED;
                        }
                        order.setStatus(order_status);
                        float grand_total = obj_order.remove(ORDER_GRAND_TOTAL).getAsFloat();
                        order.setGrandTotal(grand_total);
                        order.setBaseGrandTotal(ConfigUtil.convertToBasePrice(grand_total));
                        float total_paid = obj_order.remove(ORDER_TOTAL_PAID).getAsFloat();
                        order.setTotalPaid(total_paid);
                        order.setBaseTotalPaid(ConfigUtil.convertToBasePrice(total_paid));
                        float tax_amount = obj_order.remove(ORDER_TOTAL_TAX).getAsFloat();
                        order.setTaxAmount(tax_amount);
                        order.setBaseTaxAmount(ConfigUtil.convertToBasePrice(tax_amount));
                        float total_change = obj_order.remove(ORDER_TOTAL_CHANGE).getAsFloat();
                        order.setWebposChange(total_change);
                        order.setWebposBaseChange(ConfigUtil.convertToBasePrice(total_change));
                        if (obj_order.has(ORDER_STAFF_NAME)) {
                            String staff_name = obj_order.remove(ORDER_STAFF_NAME).getAsString();
                            order.setWebposStaffName(staff_name);
                        }
                        if (obj_order.has(ORDER_STAFF_ID)) {
                            String staff_id = obj_order.remove(ORDER_STAFF_ID).getAsString();
                            order.setWebposStaffId(staff_id);
                        }
                        List<OrderWebposPayment> listPayment = new ArrayList<>();
                        JsonArray arr_payment = obj_order.getAsJsonArray(PAYMENT_DETAIL);
                        if (arr_payment != null && arr_payment.size() > 0) {
                            for (JsonElement el_payment : arr_payment) {
                                JsonObject obj_payment = el_payment.getAsJsonObject();
                                PosOrderWebposPayment payment = new PosOrderWebposPayment();
                                String payment_id = obj_payment.remove(PAYMENT_ID).getAsString();
                                String payment_name = obj_payment.remove(PAYMENT_NAME).getAsString();
                                float payment_amount = obj_payment.remove(PAYMENT_AMOUNT).getAsFloat();
                                payment.setID(payment_id);
                                payment.setMethodTitle(StringUtil.checkJsonData(payment_name) ? payment_name : "");
                                payment.setBasePaymentAmount(ConfigUtil.convertToBasePrice(payment_amount));
                                listPayment.add(payment);
                            }
                        }
                        JsonElement el_customer = obj_order.get(CUSTOMER_DETAIL);
                        if (el_customer.isJsonObject()) {
                            List<String> listStreet = new ArrayList<>();
                            JsonObject obj_customer = obj_order.get(CUSTOMER_DETAIL).getAsJsonObject();
                            String customer_id = obj_customer.remove(CUSTOMER_ID).getAsString();
                            String customer_email = obj_customer.remove(CUSTOMER_EMAIL).getAsString();
                            String customer_name = obj_customer.remove(CUSTOMER_NAME).getAsString();
                            String customer_phone = obj_customer.remove(CUSTOMER_PHONE).getAsString();
                            String state_id = "";
                            if (obj_customer.has(CUSTOMER_STATE_ID)) {
                                state_id = obj_customer.remove(CUSTOMER_STATE_ID).getAsString();
                            }
                            String state_name = "";
                            if (obj_customer.has(CUSTOMER_STATE_NAME)) {
                                state_name = obj_customer.remove(CUSTOMER_STATE_NAME).getAsString();
                            }
                            String state_code = "";
                            if (obj_customer.has(CUSTOMER_STATE_CODE)) {
                                state_code = obj_customer.remove(CUSTOMER_STATE_CODE).getAsString();
                            }
                            String company = obj_customer.remove(CUSTOMER_COMPANY).getAsString();
                            String poscode = obj_customer.remove(CUSTOMER_POSCODE).getAsString();
                            String country_id = obj_customer.remove(CUSTOMER_COUNTRY_ID).getAsString();
                            String street = "";
                            if (obj_customer.has(CUSTOMER_STREET)) {
                                street = obj_customer.remove(CUSTOMER_STREET).getAsString();
                            }
                            String street2 = "";
                            if (obj_customer.has(CUSTOMER_STREET2)) {
                                street2 = obj_customer.remove(CUSTOMER_STREET2).getAsString();
                            }
                            String vat = "";
                            if (obj_customer.has(CUSTOMER_VAT)) {
                                vat = obj_customer.remove(CUSTOMER_VAT).getAsString();
                            }
                            String city = "";
                            if (obj_customer.has(CUSTOMER_CITY)) {
                                city = obj_customer.remove(CUSTOMER_CITY).getAsString();
                            }
                            order.setCustomerId(customer_id);
                            order.setCustomerEmail(StringUtil.checkJsonData(customer_email) ? customer_email : "");
                            order.setCustomerFirstName(StringUtil.checkJsonData(customer_name) ? customer_name : "");
                            PosOrderBillingAddress billingAddress = new PosOrderBillingAddress();
                            billingAddress.setID(customer_id);
                            billingAddress.setEmail(StringUtil.checkJsonData(customer_email) ? customer_email : "");
                            billingAddress.setFirstName(StringUtil.checkJsonData(customer_name) ? customer_name : "");
                            billingAddress.setTelephone(StringUtil.checkJsonData(customer_phone) ? customer_phone : "");
                            billingAddress.setRegion(StringUtil.checkJsonData(state_name) ? state_name : "");
                            billingAddress.setRegionCode(StringUtil.checkJsonData(state_code) ? state_code : "");
                            billingAddress.setRegionId(StringUtil.checkJsonData(state_id) ? state_id : "");
                            billingAddress.setCompany(StringUtil.checkJsonData(company) ? company : "");
                            billingAddress.setPostCode(StringUtil.checkJsonData(poscode) ? poscode : "");
                            billingAddress.setCountry(StringUtil.checkJsonData(country_id) ? country_id : "");
                            billingAddress.setVat(StringUtil.checkJsonData(vat) ? vat : "");
                            billingAddress.setCity(StringUtil.checkJsonData(city) ? city : "");
                            billingAddress.setLastName("");
                            if (StringUtil.checkJsonData(street)) {
                                listStreet.add(street);
                            }
                            if (StringUtil.checkJsonData(street2)) {
                                listStreet.add(street2);
                            }
                            billingAddress.setStreet(listStreet);
                            order.setBillingAddress(billingAddress);
                        }
                        order.setWebposOrderPayments(listPayment);
                        String note = obj_order.remove(ORDER_NOTE).getAsString();
                        List<OrderStatus> listComment = new ArrayList<>();
                        PosOrderStatus comment = new PosOrderStatus();
                        comment.setComment(StringUtil.checkJsonData(note) ? note : "");
                        listComment.add(comment);
                        order.setOrderStatus(listComment);
                        List<CartItem> listItem = new ArrayList<>();
                        List<OrderCartItem> listOrderCartItem = new ArrayList<>();
                        JsonArray arr_item = obj_order.getAsJsonArray(ORDER_ITEMS);
                        if (arr_item != null && arr_item.size() > 0) {
                            for (JsonElement el_item : arr_item) {
                                JsonObject obj_item = el_item.getAsJsonObject();
                                float unit_price = obj_item.remove(PRODUC_UNIT_PRICE).getAsFloat();
                                if (unit_price > 0) {
                                    PosCartItem cartItem = new PosCartItem();
                                    PosOrderCartItem orderCartItem = new PosOrderCartItem();
                                    if (obj_item.has(PRODUCT_ID)) {
                                        String item_id = obj_item.remove(PRODUCT_ID).getAsString();
                                        cartItem.setID(item_id);
                                        orderCartItem.setID(item_id);
                                    }
                                    if (obj_item.has(PRODUCT_NAME)) {
                                        String item_name = obj_item.remove(PRODUCT_NAME).getAsString();
                                        cartItem.setName(item_name);
                                    }
                                    float qty = obj_item.remove(PRODUCT_QTY).getAsFloat();
                                    cartItem.setQuantity(qty);
                                    orderCartItem.setQty(String.valueOf(qty));
                                    cartItem.setQtyOrdered(qty);
                                    if (order.getStatus().equals(StringUtil.STATUS_COMPLETE)) {
                                        cartItem.setQtyInvoiced(qty);
                                    }
                                    cartItem.setOriginalPrice(unit_price);
                                    float total_tax_item = 0;
                                    if (obj_item.has(TAX_DETAIL)) {
                                        JsonArray arr_tax = obj_item.getAsJsonArray(TAX_DETAIL);
                                        if (arr_tax != null & arr_tax.size() > 0) {
                                            for (JsonElement el_tax : arr_tax) {
                                                JsonObject obj_tax = el_tax.getAsJsonObject();
                                                float amount = obj_tax.remove(TAX_AMOUNT).getAsFloat();
                                                total_tax_item += amount;
                                            }
                                        }
                                    }
                                    orderCartItem.setUnitPrice(String.valueOf(unit_price));
                                    orderCartItem.setBaseUnitPrice(String.valueOf(ConfigUtil.convertToBasePrice(unit_price)));
                                    orderCartItem.setOriginalPrice(String.valueOf(unit_price));
                                    orderCartItem.setBaseOriginalPrice(String.valueOf(ConfigUtil.convertToBasePrice(unit_price)));
                                    cartItem.setBasePriceInclTax(ConfigUtil.convertToBasePrice(unit_price + ((unit_price * total_tax_item) / 100)));
                                    float product_subtotal = obj_item.remove(PRODUCT_SUBTOTAL).getAsFloat();
                                    cartItem.setBaseSubtotal(ConfigUtil.convertToBasePrice(product_subtotal));
                                    float product_subtotal_incl = obj_item.remove(PRODUCT_SUTOTAL_INCl).getAsFloat();
                                    cartItem.setRowTotal(product_subtotal_incl);
                                    cartItem.setBaseRowTotalInclTax(ConfigUtil.convertToBasePrice(product_subtotal_incl));
                                    float product_discount_percent = obj_item.remove(PRODUCT_DISCOUNT).getAsFloat();
                                    float product_discount = (((unit_price + ((unit_price * total_tax_item) / 100)) * qty) * product_discount_percent) / 100;
                                    cartItem.setBaseDiscountAmount(ConfigUtil.convertToBasePrice(product_discount));
                                    cartItem.setDiscountAmount(product_discount);
                                    total_tax += total_tax_item;
                                    total_subtotal += product_subtotal;
                                    listItem.add(cartItem);
                                    listOrderCartItem.add(orderCartItem);
                                } else {
                                    total_discount = unit_price;
                                }
                            }
                        }
//                        order.setTaxAmount((total_subtotal * total_tax) / 100);
//                        order.setBaseTaxAmount(ConfigUtil.convertToBasePrice((total_subtotal * total_tax) / 100));
                        order.setDiscountAmount(total_discount);
                        order.setBaseDiscountAmount(ConfigUtil.convertToBasePrice(total_discount));
                        order.setBaseSubtotal(ConfigUtil.convertToBasePrice(total_subtotal));
                        order.setOrderItem(listItem);
                        PosOrderItemsInfoBuy itemsInfoBuy = new PosOrderItemsInfoBuy();
                        itemsInfoBuy.setListOrderCartItems(listOrderCartItem);
                        order.setItemsInfoBuy(itemsInfoBuy);
                        listOrder.add(order);
                    }
                }
            }
            return listOrder;
        }
    }
}
