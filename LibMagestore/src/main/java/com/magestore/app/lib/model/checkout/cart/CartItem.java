package com.magestore.app.lib.model.checkout.cart;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.lib.model.sales.OrderParentItem;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;

import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 12/26/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface CartItem extends Model {
//    Map<ProductOptionCustom, PosCartItem.ChooseProductOption> getChooseProductOptions();
//
//    void setChooseProductOptions(Map<ProductOptionCustom, PosCartItem.ChooseProductOption> choose_product_options);
//
//    PosCartItem.ChooseProductOption createChooseProductOption();

    boolean isShipable();

    void setShipable(boolean shipable);

    void setShipable();

    void setShipunable();

    float getCustomPrice();

    void setCustomPrice(float custom_price);

    float getDefaultCustomPrice();

    void setDefaultCustomPrice(float defaultCustomPrice);

    boolean haveCustomPriceOrDiscount();

    String getCustomPriceType();

    void setCustomPriceType(String custom_price_type);

    void setCustomPriceTypePercent();

    void setCustomPriceTypeFixed();

    boolean isCustomPriceTypePercent();

    boolean isCustomPriceTypeFixed();

    String getDiscount();

    void setDiscount(String discount);

    String getDiscoutType();

    void setDiscountType(String discout_type);

    void setDiscountTypePercent();

    void setDiscountTypeFixed();

    boolean isDiscountTypePercent();

    boolean isDiscountTypeFixed();

    PosCartItem.OptionsValue createOptionValue();

    List<PosCartItem.OptionsValue> getOptions();

    List<PosCartItem.OptionsValue> getSuperAttribute();

    List<PosCartItem.OptionsValue> getBundleOption();

    List<PosCartItem.OptionsValue> getBundleOptionQuantity();

    void setOptions(List<PosCartItem.OptionsValue> options);

    void setSuperAttribute(List<PosCartItem.OptionsValue> super_attribute);

    void setBundleOption(List<PosCartItem.OptionsValue> bundle_option);

    void setBundleOptionQty(List<PosCartItem.OptionsValue> bundle_option_qty);

    void clearOption();

    void clearSuperAtribute();

    void clearBundleOption();

    void clearBundleOptionQuantity();

    void insertOption(String code, String value);

    void insertSuperAttribute(String code, String value);

    void insertBundleOption(String code, String value);

    void insertBundleOptionQuantity(String code, String value);

    void setQuantity(float param_quantity);

    void setQuantity(String param_quantity);

    void setProduct(Product param_product);
    void setPrice(float param_price);
    void setOriginalPrice(float param_price);

    void setUnitPrice(float param_price);

    Product getProduct();
    float getQuantity();

    float getUnitPrice();

    float getPrice();
    float getBasePrice();
    float getBaseSubTotal();
    float getPriceInclTax();
    float getPriceInvoice();
    void setPriceInvoice(float fPriceInvoice);
    float getBasePriceInclTax();
    float getOriginalPrice();
    float getOriginalCustomPrice();
    String getItemId();
    void setItemId(String strItemId);
    float getBaseOriginalPrice();

    float getPriceShowView();
    void setPriceShowView(float fPriceShowView);
    String getProductId();
    String getType();

    void setRowTotal(float fRowTotal);

    void setType(String type);

    void setTypeNormal();

    void setTypeCustom();

    boolean isTypeNormal();

    boolean isTypeCustom();

    boolean isCustomPrice();

    void setIsCustomPrice(boolean bIsCustomPrice);

    String getTaxClassId();
    void setTaxClassId(String strTaxClassId);

    // Order history
    String getName();
    String getSku();
    float getSubtotal();
    float getRowTotal();
    float getBaseTaxAmount();
    float getTaxAmount();
    float getBaseRowTotalInclTax();
    float getDiscountAmount();
    float getBaseDiscountAmount();

    void setDiscountAmount(float discount_amount);

    float getQtyOrdered();
    float getQtyCanceled();
    float getQtyInvoiced();
    float getQtyRefunded();
    float getQtyShipped();
    float QtyShip();
    float QtyRefund();
    float QtyInvoice();
    float QtyInvoiceable();
    void setQtyInvoiceable(float intQtyInvoice);
    float getQtyChange();
    void setQtyChange(float intQtyChange);
    String getProductType();
    String getIsVirtual();
    void setIsVirtual(String strIsVirtual);
    String getParentItemId();

    void setId(String strID);

    // Param Order Shipment
    void setOrderItemId(String strOrderItemId);
    String getOrderItemId();

    // Param Order Refund
    void setReturnToStock(String strReturnToStock);
    String getReturnToStock();

    void setItemDescription(String description);

    String getItemDescription();

    String getOfflineItemId();
    void setOfflineItemId(String strOffilineItemId);

    boolean getIsSaveCart();
    void setIsSaveCart(boolean isSaveCart);

    OrderParentItem getOrderParentItem();

    float getBaseGiftVoucherDiscount();
    float getRewardpointsBaseDiscount();

    boolean isDecimal();

    float getQtyCurrent();
    void setQtyCurrent(float fQtyCurrent);

    float getSpecialPrice();
    boolean checkSpecialPrice();
}
