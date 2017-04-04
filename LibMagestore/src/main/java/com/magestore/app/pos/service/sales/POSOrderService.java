package com.magestore.app.pos.service.sales;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.service.sales.OrderService;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.model.sales.PosOrder;
import com.magestore.app.pos.service.AbstractService;

import java.util.ArrayList;
import java.util.List;

/**
 * Các nghiệp vụ xoay quanh đơn hàng
 * Created by Mike on 12/26/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSOrderService extends AbstractService implements OrderService {
    private Order mOrder;
    private List<Order> mOnHoldOrders;

    /**
     * Khởi tạo 1 đơn hàng mới để bán hàng và lưu đơn đang có vào OnHold
     * @return
     */
    @Override
    public void newSales() {
        Order order = new PosOrder();
        newSales(order);
    }


    @Override
    public void setOrder(Order order) {
        mOrder = order;
    }

    @Override
    public Order getOrder() {
        return mOrder;
    }

    @Override
    public void newOrder() {
        setOrder(createOrder());
    }

    @Override
    public Order createOrder() {
        return new PosOrder();
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
        order.newOrderItems();
        setOrder(order);
    }

    /**
     * Tính toán tổng giá trị sub total của đơn hàng
     * @return
     */
    @Override
    public synchronized float calculateSubTotalOrderItems() {
        // Khởi tạo danh sách order items
        List<CartItem> listItems =  mOrder.getOrderItems();
        if (listItems == null) return 0;

        float total = 0;
        for (CartItem item : listItems) {
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
            total += item.getPrice();
        }
        mOrder.setValue("sub_total", total);
        return total;
    }

    /**
     * Tính toán thuế
     * @return
     */
    @Override
    public synchronized float calculateTaxOrderItems() {
        float sub_total = calculateSubTotalOrderItems();
        float tax_total = sub_total * (float) 0.1;
        mOrder.setValue("tax_total", tax_total);
        return tax_total;
    }

    /**
     * Tính toán tổng discount
     * @return
     */
    @Override
    public synchronized float calculateDiscountTotalOrderItems() {
        mOrder.setValue("discount_total", (float) 0);
        return 0;
    }

    /**
     * Tính toán tổng giá trị cuối cùng của đơn hàng
     * @return
     */
    @Override
    public synchronized float calculateLastTotalOrderItems() {
        float last_total = calculateSubTotalOrderItems() + calculateTaxOrderItems() - calculateDiscountTotalOrderItems() ;
        mOrder.setValue("last_total", last_total);
        return last_total;
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

        // Khởi tạo danh sách order cartItem
        List<CartItem> listItems =  mOrder.getOrderItems();
        if (listItems == null) {
            mOrder.newOrderItems();
            listItems = mOrder.getOrderItems();
        }

        // Kiểm tra xem đã có order item với mặt hàng tương ứng chưa
        CartItem cartItem = null;
        for (CartItem item : listItems) {
            String itemID = item.getProduct().getID();
            if (itemID == null) continue;
            if (itemID.equals(product.getID())) {
                cartItem = item;
                break;
            }
        }

        // nếu chưa thì thêm mới
        if (cartItem == null) {
            // Khởi tạo product order item
            cartItem = new PosCartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(price * quantity);
            cartItem.setOriginalPrice(product.getPrice());

            // Thêm vào danh sách order cartItem
            mOrder.getOrderItems().add(cartItem);
        }
        // có rồi thì cập nhật lại số lượng
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            float totalPrice = cartItem.getPrice();
            totalPrice += (price * quantity);
            cartItem.setPrice(totalPrice);
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

        // Khởi tạo danh sách order cartItem
        List<CartItem> listItems =  mOrder.getOrderItems();
        if (listItems == null) {
            mOrder.newOrderItems();
            listItems = mOrder.getOrderItems();
        }

        // Kiểm tra xem đã có order item với mặt hàng tương ứng chưa
        CartItem cartItem = null;
        for (CartItem item : listItems) {
            String itemID = item.getProduct().getID();
            if (itemID == null) continue;
            if (itemID.equals(product.getID())) {
                cartItem = item;
                break;
            }
        }

        // đã có item, giảm trừ số lượng xuống nhưng không thể ít hơn 1
        if (cartItem != null) {
            // Tính số lượng item mới
            float newQuantity = cartItem.getQuantity() - subQuantity;
            if (newQuantity < 0) newQuantity = 1;

            // Cập nhật số lượng mới
            cartItem.setQuantity(newQuantity);
            cartItem.setPrice(newQuantity * product.getPrice());

            // Nếu số lượng bằng 0, xóa luôn khỏi danh sách
            if (newQuantity == 0) listItems.remove(cartItem);
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
        List<CartItem> listItems =  mOrder.getOrderItems();
        if (listItems == null) return;

        // remove order item vị trí thứ n
        listItems.remove(position);
    }

    /**
     *
     * @param cartItem
     */
    public void delOrderItem(CartItem cartItem) {
        // nếu chưa có đơn hàng, bo qua
        if (mOrder == null) return;

        // Khởi tạo danh sách order cartItem
        List<CartItem> listItems =  mOrder.getOrderItems();
        if (listItems == null) return;

        // remove order item vị trí thứ n
        listItems.remove(cartItem);
    }

    @Override
    public void delOrderItem(Product product) {
        // nếu chưa có đơn hàng, bo qua
        if (mOrder == null) return;

        // Khởi tạo danh sách order cartItem
        List<CartItem> listItems =  mOrder.getOrderItems();
        if (listItems == null) {
            mOrder.newOrderItems();
            listItems = mOrder.getOrderItems();
        }

        // Kiểm tra xem đã có order item với mặt hàng tương ứng chưa
        // remove order item đó đi
        CartItem cartItem = null;
        for (CartItem item : listItems) {
            String itemID = item.getProduct().getID();
            if (itemID == null) continue;
            if (itemID.equals(product.getID())) {
                cartItem = item;
                break;
            }
        }
        delOrderItem(cartItem);
    }

    @Override
    public void doCheckoutOrder() {

    }

    @Override
    public void doPaymentOrder() {

    }
}