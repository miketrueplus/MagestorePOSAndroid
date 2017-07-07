package com.magestore.app.pos.model.checkout.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.lib.model.catalog.ProductOptionCustomValue;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.sales.OrderParentItem;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.catalog.PosProductOption;
import com.magestore.app.pos.model.catalog.PosProductOptionCustom;
import com.magestore.app.pos.model.checkout.PosQuoteItems;
import com.magestore.app.pos.model.sales.PosOrderParentItem;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;
import com.magestore.app.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 12/26/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCartItem extends PosAbstractModel implements CartItem {
    @Gson2PosExclude
    String discount;
    @Gson2PosExclude
    String discout_type;
    @Gson2PosExclude
    float custom_price;
    @Gson2PosExclude
    float default_custom_price;
    @Gson2PosExclude
    String custom_price_type;
    @Gson2PosExclude
    String is_shipable = StringUtil.STRING_ZERO;
    @Gson2PosExclude
    String tax_class_id;

    @Override
    public boolean isShipable() {
        return StringUtil.STRING_ONE.equals(is_shipable);
    }

    @Override
    public void setShipable(boolean shipable) {
        is_shipable = shipable ? StringUtil.STRING_ONE : StringUtil.STRING_ZERO;
    }

    @Override
    public void setShipable() {
        is_shipable = StringUtil.STRING_ONE;
    }

    @Override
    public void setShipunable() {
        is_shipable = StringUtil.STRING_ZERO;
    }

    @Override
    public float getCustomPrice() {
        return custom_price;
    }

    @Override
    public void setCustomPrice(float custom_price) {
        this.custom_price = custom_price;
    }

    @Override
    public float getDefaultCustomPrice() {
        return default_custom_price;
    }

    @Override
    public void setDefaultCustomPrice(float defaultCustomPrice) {
        default_custom_price = defaultCustomPrice;;
    }

    @Override
    public boolean haveCustomPriceOrDiscount() {
        return isCustomPrice();
    }

    @Override
    public String getCustomPriceType() {
        return custom_price_type;
    }

    @Override
    public void setCustomPriceType(String custom_price_type) {
        this.custom_price_type = custom_price_type;
    }

    @Override
    public void setCustomPriceTypePercent() {
        setCustomPriceType(StringUtil.TYPE_PERCENT);
    }

    @Override
    public void setCustomPriceTypeFixed() {
        setCustomPriceType(StringUtil.TYPE_FIXED);
    }

    @Override
    public boolean isCustomPriceTypePercent() {
        return StringUtil.TYPE_PERCENT.equals(custom_price_type);
    }

    @Override
    public boolean isCustomPriceTypeFixed() {
        return !isCustomPriceTypePercent();
    }

    @Override
    public String getDiscount() {
        return discount;
    }

    @Override
    public void setDiscount(String discount) {
        this.discount = discount;
    }

    @Override
    public String getDiscoutType() {
        return discout_type;
    }

    @Override
    public void setDiscountType(String discout_type) {
        this.discout_type = discout_type;
    }

    @Override
    public void setDiscountTypePercent() {
        setDiscountType(StringUtil.TYPE_PERCENT);
    }

    @Override
    public void setDiscountTypeFixed() {
        setDiscountType(StringUtil.TYPE_FIXED);
    }

    @Override
    public boolean isDiscountTypePercent() {
        return StringUtil.TYPE_PERCENT.equals(discout_type);
    }

    @Override
    public boolean isDiscountTypeFixed() {
        return !isDiscountTypePercent();
    }

    //    public class ChooseProductOption {
//        public List<ProductOptionCustomValue> productOptionCustomValueList;
//        public int qty;
//    }
//    Map<ProductOptionCustom, ChooseProductOption> choose_product_options;

    public class OptionsValue {
        @Gson2PosExclude(toJson = false, fromJson = true)
        public String id;
        public String code;
        @Expose(serialize = true, deserialize = false)
        public String value;
        public List<String> values;
    }

    List<OptionsValue> options;
    List<OptionsValue> super_attribute;
    List<OptionsValue> bundle_option;
    List<OptionsValue> bundle_option_qty;

    @Override
    public List<OptionsValue> getOptions() {
        return options;
    }

    @Override
    public List<OptionsValue> getSuperAttribute() {
        return super_attribute;
    }

    @Override
    public List<OptionsValue> getBundleOption() {
        return bundle_option;
    }

    @Override
    public List<OptionsValue> getBundleOptionQuantity() {
        return bundle_option_qty;
    }

    @Override
    public void setOptions(List<OptionsValue> options) {
        this.options = options;
    }

    @Override
    public void setSuperAttribute(List<OptionsValue> super_attribute) {
        this.super_attribute = super_attribute;
    }

    @Override
    public void setBundleOption(List<OptionsValue> bundle_option) {
        this.bundle_option = bundle_option;
    }

    @Override
    public void setBundleOptionQty(List<OptionsValue> bundle_option_qty) {
        this.bundle_option_qty = bundle_option_qty;
    }

    @Override
    public void clearOption() {
        options = null;
        super_attribute = null;
        bundle_option = null;
        bundle_option_qty = null;
    }

    @Override
    public void clearSuperAtribute() {
        super_attribute = null;
    }

    @Override
    public void clearBundleOption() {
        bundle_option = null;
    }

    @Override
    public void clearBundleOptionQuantity() {
        bundle_option_qty = null;
    }

    @Override
    public void insertOption(String code, String value) {
        if (options == null) options = new ArrayList<>();
        OptionsValue quoteCustomeOption = new OptionsValue();
        quoteCustomeOption.code = code;
        quoteCustomeOption.value = value;
        options.add(quoteCustomeOption);
    }

    @Override
    public void insertSuperAttribute(String code, String value) {
        if (super_attribute == null) super_attribute = new ArrayList<>();
        OptionsValue quoteCustomeOption = new OptionsValue();
        quoteCustomeOption.code = code;
        quoteCustomeOption.value = value;
        super_attribute.add(quoteCustomeOption);
    }

    @Override
    public void insertBundleOption(String code, String value) {
        if (bundle_option == null) bundle_option = new ArrayList<>();
        OptionsValue quoteCustomeOption = new OptionsValue();
        quoteCustomeOption.code = code;
        quoteCustomeOption.value = value;
        bundle_option.add(quoteCustomeOption);
    }

    @Override
    public void insertBundleOptionQuantity(String code, String value) {
        if (bundle_option_qty == null) bundle_option_qty = new ArrayList<>();
        OptionsValue quoteCustomeOption = new OptionsValue();
        quoteCustomeOption.code = code;
        quoteCustomeOption.value = value;
        bundle_option_qty.add(quoteCustomeOption);
    }

    Product product;
    String qty;
    @Gson2PosExclude
    String child_id;
    //    @Gson2PosExclude
//    String bundle_option;
    float price;
    float price_incl_tax;
    float base_price;
    @Gson2PosExclude
    float base_price_incl_tax;
    @Gson2PosExclude
    String offline_item_id;
    @Gson2PosExclude
    float unit_price;
    @Gson2PosExclude
    float price_show_view;


    public static final String TYPE_NORMAL = "type_normal";
    public static final String TYPE_CUSTOM = "type_custom";
    String type;

    @Gson2PosExclude
    boolean is_custom_price;

    // Order history
    String name;
    String sku;
    String item_description;

    float row_total = 0;
    float row_total_incl_tax = 0;
    float tax_amount = 0;
    float discount_amount = 0;
    @Gson2PosExclude
    float base_row_total_incl_tax;
    @Gson2PosExclude
    float base_discount_amount;
    @Gson2PosExclude
    float base_tax_amount;
    @Gson2PosExclude
    float base_row_total;
    @Gson2PosExclude
    float qty_ordered;
    @Gson2PosExclude
    float qty_canceled;
    @Gson2PosExclude
    float qty_invoiced;
    @Gson2PosExclude
    float qty_refunded;
    @Gson2PosExclude
    float qty_shipped;
    @Gson2PosExclude
    String product_type;
    @Gson2PosExclude
    String parent_item_id;
    @Gson2PosExclude
    float qty_change;
    @Gson2PosExclude
    float qty_invoiceable;

    @Gson2PosExclude
    float base_original_price;
    @Gson2PosExclude
    float base_unit_price;
    //    @Gson2PosExclude
//    int bundle_option_qty;
    @Gson2PosExclude
    String is_virtual;
    @Gson2PosExclude
    String item_id;
    @Gson2PosExclude
    boolean isSaveCart;
    @Gson2PosExclude
    float original_price;
    @Gson2PosExclude
    PosOrderParentItem parent_item;
    @Gson2PosExclude
    float price_invoice;

    @Gson2PosExclude
    float base_gift_voucher_discount;
    @Gson2PosExclude
    float rewardpoints_base_discount;

    // Params Order Shipment
    String order_item_id;

    // Params Order Refund
    String return_to_stock;

    @Override
    public void setQuantity(float param_quantity) {
        qty = Float.toString(param_quantity);
        price = unit_price * param_quantity;
    }

    @Override
    public void setQuantity(String param_quantity) {
        qty = param_quantity;
        price = unit_price * Float.parseFloat(param_quantity);
    }

    @Override
    public void setProduct(Product param_product) {
        product = param_product;
    }

    @Override
    public void setPrice(float param_price) {
        price = param_price;
    }

    @Override
    public void setOriginalPrice(float param_price) {
        original_price = param_price;
    }

    @Override
    public void setUnitPrice(float param_price) {
        this.unit_price = param_price;
        price = unit_price * Float.parseFloat(qty);
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public float getQuantity() {
        float quantity = 0;
        try {
            quantity = Float.parseFloat(qty);
        }catch (Exception e){

        }
        return quantity;
    }

    @Override
    public float getUnitPrice() {
        return unit_price;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public float getBasePrice() {
        return base_price;
    }

    @Override
    public float getBaseSubTotal() {
        return base_row_total;
    }

    @Override
    public float getPriceInclTax() {
        return price_incl_tax;
    }

    @Override
    public float getPriceInvoice() {
        return price_invoice;
    }

    @Override
    public void setPriceInvoice(float fPriceInvoice) {
        price_invoice = fPriceInvoice;
    }

    @Override
    public float getBasePriceInclTax() {
        return base_price_incl_tax;
    }

    @Override
    public float getOriginalPrice() {
        return original_price;
    }

    @Override
    public String getItemId() {
        return item_id;
    }

    @Override
    public void setItemId(String strItemId) {
        item_id = strItemId;
    }

    @Override
    public float getBaseOriginalPrice() {
        return base_original_price;
    }

    @Override
    public float getPriceShowView() {
        return price_show_view;
    }

    @Override
    public void setPriceShowView(float fPriceShowView) {
        price_show_view = fPriceShowView;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setTypeNormal() {
        setType(TYPE_NORMAL);
    }

    @Override
    public void setTypeCustom() {
        setType(TYPE_CUSTOM);
    }

    @Override
    public boolean isTypeNormal() {
        return TYPE_NORMAL.equals(getType());
    }

    @Override
    public boolean isTypeCustom() {
        return TYPE_CUSTOM.equals(getType());
    }

    @Override
    public boolean isCustomPrice() {
        return is_custom_price;
    }

    @Override
    public void setIsCustomPrice(boolean bIsCustomPrice) {
        is_custom_price = bIsCustomPrice;
    }

    @Override
    public String getTaxClassId() {
        return tax_class_id;
    }

    @Override
    public void setTaxClassId(String strTaxClassId) {
        tax_class_id = strTaxClassId;
    }

    // Order history
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSku() {
        return (sku != null ? sku : (getProduct() != null ? getProduct().getSKU() : StringUtil.STRING_EMPTY));
    }

    @Override
    public float getSubtotal() {
        return row_total;
    }

    @Override
    public float getRowTotal() {
        return row_total_incl_tax;
    }

    @Override
    public float getBaseTaxAmount() {
        return base_tax_amount;
    }

    @Override
    public float getTaxAmount() {
        return tax_amount;
    }

    @Override
    public float getBaseRowTotalInclTax() {
        return base_row_total_incl_tax;
    }

    @Override
    public float getDiscountAmount() {
        return discount_amount;
    }

    @Override
    public float getBaseDiscountAmount() {
        return base_discount_amount;
    }

    @Override
    public void setDiscountAmount(float discount_amount) {
        this.discount_amount = discount_amount;
    }

    @Override
    public float getQtyOrdered() {
        return qty_ordered;
    }

    @Override
    public float getQtyCanceled() {
        return qty_canceled;
    }

    @Override
    public float getQtyInvoiced() {
        return qty_invoiced;
    }

    @Override
    public float getQtyRefunded() {
        return qty_refunded;
    }

    @Override
    public float getQtyShipped() {
        return qty_shipped;
    }

    @Override
    public float QtyShip() {
        return qty_ordered - qty_shipped - qty_refunded - qty_canceled;
    }

    @Override
    public float QtyRefund() {
        return qty_invoiced - qty_refunded;
    }

    @Override
    public float QtyInvoice() {
        return qty_ordered - qty_invoiced - qty_refunded - qty_canceled;
    }

    @Override
    public float QtyInvoiceable() {
        return qty_invoiceable;
    }

    @Override
    public void setQtyInvoiceable(float intQtyInvoice) {
        qty_invoiceable = intQtyInvoice;
    }

    @Override
    public float getQtyChange() {
        return qty_change;
    }

    @Override
    public void setQtyChange(float intQtyChange) {
        qty_change = intQtyChange;
    }

    @Override
    public String getProductType() {
        return product_type;
    }

    @Override
    public String getIsVirtual() {
        return is_virtual;
    }

    @Override
    public void setIsVirtual(String strIsVirtual) {
        is_virtual = strIsVirtual;
    }

    @Override
    public String getParentItemId() {
        return parent_item_id;
    }

    @Override
    public void setId(String strID) {
        id = strID;
    }

    @Override
    public void setOrderItemId(String strOrderItemId) {
        order_item_id = strOrderItemId;
    }

    @Override
    public String getOrderItemId() {
        return order_item_id;
    }

    @Override
    public void setReturnToStock(String strReturnToStock) {
        return_to_stock = strReturnToStock;
    }

    @Override
    public String getReturnToStock() {
        return return_to_stock;
    }
//
//    @Override
//    public Map<ProductOptionCustom, ChooseProductOption> getChooseProductOptions() {
//        return choose_product_options;
//    }
//
//    @Override
//    public void setChooseProductOptions(Map<ProductOptionCustom, ChooseProductOption> choose_product_options) {
//        this.choose_product_options = choose_product_options;
//    }
//
//    /**
//     * Khởi tạo ChooseProductOption
//     * @return
//     */
//    @Override
//    public ChooseProductOption createChooseProductOption() {
//        ChooseProductOption chooseProductOption = new ChooseProductOption();
//        chooseProductOption.productOptionCustomValueList = new ArrayList<ProductOptionCustomValue>();
//        chooseProductOption.qty = 1;
//        return chooseProductOption;
//    }

    @Override
    public void setItemDescription(String description) {
        this.item_description = description;
    }

    @Override
    public String getItemDescription() {
        return (item_description != null ? item_description : getSku());
    }

    @Override
    public String getOfflineItemId() {
        return offline_item_id;
    }

    @Override
    public void setOfflineItemId(String strOffilineItemId) {
        offline_item_id = strOffilineItemId;
    }

    @Override
    public boolean getIsSaveCart() {
        return isSaveCart;
    }

    @Override
    public void setIsSaveCart(boolean isSaveCart) {
        this.isSaveCart = isSaveCart;
    }

    @Override
    public OrderParentItem getOrderParentItem() {
        return parent_item;
    }

    @Override
    public float getBaseGiftVoucherDiscount() {
        return base_gift_voucher_discount;
    }

    @Override
    public float getRewardpointsBaseDiscount() {
        return rewardpoints_base_discount;
    }
}
