package com.magestore.app.lib.model.checkout.cart;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOptionCustom;
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

    List<PosCartItem.OptionsValue> getOptions();

    List<PosCartItem.OptionsValue> getSuperAttribute();

    List<PosCartItem.OptionsValue> getBundleOption();

    List<PosCartItem.OptionsValue> getBundleOptionQuantity();

    void clearOption();

    void clearSuperAtribute();

    void clearBundleOption();

    void clearBundleOptionQuantity();

    void insertOption(String code, String value);

    void insertSuperAttribute(String code, String value);

    void insertBundleOption(String code, String value);

    void insertBundleOptionQuantity(String code, String value);

    void setQuantity(int param_quantity);
    void setProduct(Product param_product);
    void setPrice(float param_price);
    void setOriginalPrice(float param_price);

    void setUnitPrice(float param_price);

    Product getProduct();
    int getQuantity();

    float getUnitPrice();

    float getPrice();
    float getOriginalPrice();
    String getItemId();
    void setItemId(String strItemId);

    // Order history
    String getName();
    String getSku();
    float getSubtotal();
    float getRowTotal();
    float getTaxAmount();
    float getDiscountAmount();
    int getQtyOrdered();
    int getQtyCanceled();
    int getQtyInvoiced();
    int getQtyRefunded();
    int getQtyShipped();
    int QtyShip();
    int QtyRefund();
    int QtyInvoice();
    String getProductType();
    String getIsVirtual();
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
}
