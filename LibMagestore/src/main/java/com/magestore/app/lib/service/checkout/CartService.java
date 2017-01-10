package com.magestore.app.lib.service.checkout;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.checkout.cart.Items;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.service.Service;

/**
 * Giao diện các nghiệp vụ của đơn hàng
 * Created by Mike on 12/26/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface CartService extends Service {
    void setOrder(Order order);
    Order getOrder();

    /**
     * Khởi tạo 1 phiên sales mới
     * @param order
     */
    void newSales(Order order);
    void newSales();
    void newOrder();
    public Order createOrder();

    /**
     * Thêm 1 item vào đơn hàng
     * @param product
     * @param quantity
     * @param price
     */
    void addOrderItem(Product product, int quantity, float price);

    /**
     * Thêm 1 item vào đơn hàng
     * @param product
     * @param quantity
     */
    void addOrderItem(Product product, int quantity);

    /**
     * Trừ số lượng của 1 item trong đơn hàng
     * @param product
     * @param subQuantity
     */
    void subtructOrderItem(Product product, int subQuantity);

    /**
     * Loại 1 order item khỏi đơn hàng
     * @param position
     */
    public void delOrderItem(int position);

    /**
     *
     * @param items
     */
    public void delOrderItem(Items items);

    /**
     *
     * @param product
     */
    public void delOrderItem(Product product);

    /**
     * Tính toán sub total
     * @return
     */
    float calculateSubTotalOrderItems();

    /**
     * Tính toán thuế
     * @return
     */
    float calculateTaxOrderItems();

    /**
     * Tính toán tổng cuối cùng
     * @return
     */
    float calculateLastTotalOrderItems();

    /**
     * Tính toán tổng discount
     * @return
     */
    float calculateDiscountTotalOrderItems();

    /**
     * Thực hiện thanh toán
     */
    void doCheckoutOrder();

    /**
     * Thực hiện thanh toán
     */
    void doPaymentOrder();
}
