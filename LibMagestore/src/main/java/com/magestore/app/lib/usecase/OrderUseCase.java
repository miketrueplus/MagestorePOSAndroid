package com.magestore.app.lib.usecase;

import com.magestore.app.lib.entity.Order;
import com.magestore.app.lib.entity.OrderItem;
import com.magestore.app.lib.entity.Product;

import java.util.List;

/**
 * Giao diện các nghiệp vụ của đơn hàng
 * Created by Mike on 12/26/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface OrderUseCase {
    void setOrder(Order order);
    Order getOrder();

    /**
     * Khởi tạo 1 phiên sales mới
     * @param order
     */
    void newSales(Order order);

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
     * @param orderItem
     */
    public void delOrderItem(OrderItem orderItem);

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
