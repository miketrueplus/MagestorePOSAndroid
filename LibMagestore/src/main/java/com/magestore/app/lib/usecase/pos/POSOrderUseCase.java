package com.magestore.app.lib.usecase.pos;

import com.magestore.app.lib.entity.Order;
import com.magestore.app.lib.entity.OrderItem;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.entity.pos.PosOrder;
import com.magestore.app.lib.entity.pos.PosOrderItem;
import com.magestore.app.lib.usecase.OrderUseCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 12/26/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class POSOrderUseCase extends AbstractUseCase implements OrderUseCase {
    private Order mOrder;
    private List<Order> mOnHoldOrders;


    @Override
    public void setOrder(Order order) {
        mOrder = order;
    }

    @Override
    public Order getOrder() {
        return mOrder;
    }

    /**
     * Hold order hiện tại lại
     */
    public void holdOrder() {
        // Nếu chưa có order, bỏ qua
        if (mOrder == null) return;

        // Khởi tạo mảng chứa On Hold Order nếu chưa có
        if (mOnHoldOrders == null) mOnHoldOrders = new ArrayList<Order>();

        // Kiểm tra đơn hiện tại đã được hold chưa
        boolean isHold = false;
        for (Order order: mOnHoldOrders) {
            isHold = (order == mOrder);
            if (isHold) break;
        }

        // Lưu order hiện tại vào on hold nếu chưa được hold trước đó
        if (!isHold) mOnHoldOrders.add(mOrder);
    }

    /**
     * Khởi tạo 1 đơn hàng mới để bán hàng và lưu đơn đang có vào OnHold
     * @return
     */
    @Override
    public void newSales(Order order) {
        holdOrder();
        setOrder(order);
    }

    /**
     * Tính toán tổng giá trị sub total của đơn hàng
     * @return
     */
    @Override
    public float calculateSubTotalOrderItems() {
        // Khởi tạo danh sách order items
        List<OrderItem> listOrderItem =  mOrder.getOrderItems();
        if (listOrderItem == null) return 0;

        float total = 0;
        for (OrderItem item : listOrderItem) {
            total += item.getQuantity() * item.getPrice();
        }
        return total;
    }

    /**
     * Tính toán thuế
     * @return
     */
    @Override
    public float calculateTaxOrderItems() {
        return calculateSubTotalOrderItems() * (float) 0.1;
    }

    /**
     * Tính toán tổng discount
     * @return
     */
    @Override
    public float calculateDiscountTotalOrderItems() {
        return 0;
    }

    /**
     * Tính toán tổng giá trị cuối cùng của đơn hàng
     * @return
     */
    @Override
    public float calculateLastTotalOrderItems() {
        return calculateSubTotalOrderItems() + calculateTaxOrderItems() - calculateDiscountTotalOrderItems() ;
    }



    /**
     * Thêm 1 order item
     * @param product
     * @param quantity
     * @param price
     */
    @Override
    public void addOrderItem(Product product, int quantity, float price) {
        // nếu chưa có đơn hàng, bo qua
        if (mOrder == null) return;

        // Khởi tạo danh sách order items
        List<OrderItem> listOrderItem =  mOrder.getOrderItems();
        if (listOrderItem == null) {
            mOrder.newOrderItems();
            listOrderItem = mOrder.getOrderItems();
        }

        // Kiểm tra xem đã có order item với mặt hàng tương ứng chưa
        OrderItem orderItem = null;
        for (OrderItem item : listOrderItem) {
            String itemID = item.getProduct().getID();
            if (itemID == null) continue;
            if (itemID.equals(product.getID())) {
                orderItem = item;
                break;
            }
        }

        // nếu chưa thì thêm mới
        if (orderItem == null) {
            // Khởi tạo product order item
            orderItem = new PosOrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(price * quantity);
            orderItem.setOriginalPrice(product.getPrice());

            // Thêm vào danh sách order items
            mOrder.getOrderItems().add(orderItem);
        }
        // có rồi thì cập nhật lại số lượng
        else {
            orderItem.setQuantity(orderItem.getQuantity() + quantity);
            float totalPrice = orderItem.getPrice();
            totalPrice += (price * quantity);
            orderItem.setPrice(totalPrice);
        }
    }

    /**
     * Thêm product
     * @param product
     * @param quantity
     */
    @Override
    public void addOrderItem(Product product, int quantity) {
        addOrderItem(product, quantity, product.getPrice());
    }

    /**
     * Trừ số lượng một mặt hàng trên đơn hàng
     * @param product
     * @param subQuantity
     */
    @Override
    public void subtructOrderItem(Product product, int subQuantity) {
        // nếu chưa có đơn hàng, bỏ qua
        if (mOrder == null) return;

        // Khởi tạo danh sách order items
        List<OrderItem> listOrderItem =  mOrder.getOrderItems();
        if (listOrderItem == null) {
            mOrder.newOrderItems();
            listOrderItem = mOrder.getOrderItems();
        }

        // Kiểm tra xem đã có order item với mặt hàng tương ứng chưa
        OrderItem orderItem = null;
        for (OrderItem item : listOrderItem) {
            String itemID = item.getProduct().getID();
            if (itemID == null) continue;
            if (itemID.equals(product.getID())) {
                orderItem = item;
                break;
            }
        }

        // đã có item, giảm trừ số lượng xuống nhưng không thể ít hơn 1
        if (orderItem != null) {
            // Tính số lượng item mới
            int newQuantity = orderItem.getQuantity() - subQuantity;
            if (newQuantity < 0) newQuantity = 1;

            // Cập nhật số lượng mới
            orderItem.setQuantity(newQuantity);
            orderItem.setPrice(newQuantity * product.getPrice());

            // Nếu số lượng bằng 0, xóa luôn khỏi danh sách
            if (newQuantity == 0) listOrderItem.remove(orderItem);
        }
    }

    /**
     * Xóa 1 mặt hàng trong order item
     * @param position
     */
    public void delOrderItem(int position) {
        // nếu chưa có đơn hàng, bo qua
        if (mOrder == null) return;

        // Khởi tạo danh sách order items
        List<OrderItem> listOrderItem =  mOrder.getOrderItems();
        if (listOrderItem == null) return;

        // remove order item vị trí thứ n
        listOrderItem.remove(position);
    }

    /**
     *
     * @param orderItem
     */
    public void delOrderItem(OrderItem orderItem) {
        // nếu chưa có đơn hàng, bo qua
        if (mOrder == null) return;

        // Khởi tạo danh sách order items
        List<OrderItem> listOrderItem =  mOrder.getOrderItems();
        if (listOrderItem == null) return;

        // remove order item vị trí thứ n
        listOrderItem.remove(orderItem);
    }

    @Override
    public void delOrderItem(Product product) {
        // nếu chưa có đơn hàng, bo qua
        if (mOrder == null) return;

        // Khởi tạo danh sách order items
        List<OrderItem> listOrderItem =  mOrder.getOrderItems();
        if (listOrderItem == null) {
            mOrder.newOrderItems();
            listOrderItem = mOrder.getOrderItems();
        }

        // Kiểm tra xem đã có order item với mặt hàng tương ứng chưa
        // remove order item đó đi
        OrderItem orderItem = null;
        for (OrderItem item : listOrderItem) {
            String itemID = item.getProduct().getID();
            if (itemID == null) continue;
            if (itemID.equals(product.getID())) {
                orderItem = item;
                break;
            }
        }
        delOrderItem(orderItem);
    }

    @Override
    public void doCheckoutOrder() {

    }

    @Override
    public void doPaymentOrder() {

    }
}