package com.magestore.app.pos.service.checkout;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Thực hiện các nghiệp vụ, service của cart
 * Created by Mike on 1/12/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCartService extends AbstractService implements CartService {
    /**
     * Tính toán tổng giá trị sub total của đơn hàng
     * @return
     */
    @Override
    public synchronized float calculateSubTotal(Checkout checkout) {
        // Khởi tạo danh sách order items
        if (checkout == null) return 0;
        List<CartItem> listItems =  checkout.getCartItem();
        if (listItems == null) return 0;

        float total = 0;
        for (CartItem item : listItems) {
//            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
            total += item.getPrice();
        }
        checkout.setSubTotal(total);
        return total;
    }

    /**
     * Tính toán thuế
     * @return
     */
    @Override
    public synchronized float calculateTaxTotal(Checkout checkout) {
        float sub_total = calculateSubTotal(checkout);
        float tax_total = sub_total * (float) 0.1;
        checkout.setTaxTotal(tax_total);
        return tax_total;
    }

    /**
     * Tính toán tổng discount
     * @return
     */
    @Override
    public synchronized float calculateDiscountTotal(Checkout checkout) {
        checkout.setDiscountTotal(0);
        return 0;
    }

    /**
     * Tính toán tổng giá trị cuối cùng của đơn hàng
     * @return
     */
    @Override
    public synchronized float calculateLastTotal(Checkout checkout) {
        float last_total = calculateSubTotal(checkout) + calculateTaxTotal(checkout) - calculateDiscountTotal(checkout);
        checkout.setGrandTotal(last_total);
        return last_total;
    }

    @Override
    public CartItem create(Checkout checkout) throws InstantiationException, IllegalAccessException, ParseException, IOException {
        CartItem cartItem = new PosCartItem();

        // Thêm vào danh sách order cartItem
        insert(checkout, cartItem);
        return cartItem;
    }

    @Override
    public CartItem create(Product product) {
        return create(product, 1, product.getFinalPrice());
    }

    @Override
    public CartItem create(Product product, int quantity, float price) {
        CartItem cartItem = new PosCartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(price * quantity);
        cartItem.setOriginalPrice(price * quantity);
        cartItem.setUnitPrice(price);
        cartItem.setId(product.getID());
        cartItem.setItemId(String.valueOf(getItemIdInCurrentTime()));
        return cartItem;
    }

    @Override
    public CartItem create(Product product, int quantity) {
        return create(product, quantity, product.getFinalPrice());
    }

    @Override
    public CartItem create(Checkout checkout, Product product) throws InstantiationException, IllegalAccessException, ParseException, IOException {
        CartItem cartItem = create(product);
        insert(checkout, cartItem);
        return cartItem;
    }

    @Override
    public CartItem findItem(Checkout checkout, Product product) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        if (checkout == null) return null;

        // Khởi tạo danh sách order cartItem
        List<CartItem> listItems =  checkout.getCartItem();
        if (listItems == null) return null;

        // Kiểm tra xem đã có order item với mặt hàng tương ứng chưa
        CartItem cartItem = null;
        for (CartItem item : listItems) {
            String itemID = item.getProduct().getID();
            if (itemID == null) continue;
            if (itemID.equals(product.getID()))
                return item;
        }

        // chưa có thì tạo mới
        return create(checkout);
    }

    @Override
    public int count(Checkout checkout) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        if (checkout.getCartItem() == null) return 0;
        return checkout.getCartItem().size();
    }

    @Override
    public CartItem retrieve(Checkout checkout, String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return checkout.getCartItem().get(0);
    }

    @Override
    public List<CartItem> retrieve(Checkout checkout, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return checkout.getCartItem();
    }

    @Override
    public List<CartItem> retrieve(Checkout checkout) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return checkout.getCartItem();
    }

    @Override
    public List<CartItem> retrieve(Checkout checkout, String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return checkout.getCartItem();
    }

    @Override
    public boolean update(Checkout checkout, CartItem oldModel, CartItem newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public boolean insert(Checkout checkout, CartItem... childs) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        if (checkout.getCartItem() == null)
            checkout.setCartItem(new ArrayList<CartItem>());
        checkout.getCartItem().add(childs[0]);
        return true;
    }

    @Override
    public void increase(CartItem cartItem) {
        increase(cartItem, cartItem.getProduct().getQuantityIncrement());
    }

    @Override
    public void increase(CartItem cartItem, int quantity) {
        int newQuantity = cartItem.getQuantity() + quantity;
        cartItem.setQuantity(newQuantity > 0 ? newQuantity : cartItem.getProduct().getQuantityIncrement());
    }

    @Override
    public void substract(CartItem cartItem) {
        substract(cartItem, cartItem.getProduct().getQuantityIncrement());
    }

    @Override
    public void substract(CartItem cartItem, int quantity) {
        int newQuantity = cartItem.getQuantity() - quantity;
        cartItem.setQuantity(newQuantity > 0 ? newQuantity : cartItem.getProduct().getQuantityIncrement());
    }

    /**
     * Thêm 1 order item
     * @param product
     * @param quantity
     * @param price
     */
    @Override
    public CartItem insert(Checkout checkout, Product product, int quantity, float price) throws InstantiationException, IllegalAccessException, ParseException, IOException {
        // nếu chưa có đơn hàng, bo qua
        if (checkout == null) return null;

        // Khởi tạo danh sách order cartItem
        List<CartItem> listItems =  checkout.getCartItem();
        if (listItems == null) {
            listItems = new ArrayList<CartItem>();
            checkout.setCartItem(listItems);
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
            cartItem = create(product, quantity, price);
            // Thêm vào danh sách order cartItem
            insert(checkout, cartItem);
        }
        // có rồi thì cập nhật lại số lượng
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            float totalPrice = cartItem.getPrice();
            totalPrice += (price * quantity);
            cartItem.setPrice(totalPrice);
        }
        return cartItem;
    }

    private long getItemIdInCurrentTime() {
        long time = System.currentTimeMillis();
        return time;
    }

    /**
     * Thêm product
     * @param product
     * @param quantity
     */
    @Override
    public CartItem insert(Checkout checkout, Product product, int quantity) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return insert(checkout, product, quantity, product.getPrice());
    }

    /**
     * Xóa 1 mặt hàng trong order item
     * @param position
     */
    @Override
    public boolean delete(Checkout checkout, int position) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // nếu chưa có đơn hàng, bo qua
        if (checkout == null) return false;

        // Khởi tạo danh sách order items
        List<CartItem> listItems =  checkout.getCartItem();
        if (listItems == null) return true;

        // remove order item vị trí thứ n
        listItems.remove(position);
        return true;
    }

    /**
     * Xóa item ra khỏi danh sách
     * @param checkout
     * @param childs
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws ParseException
     * @throws IllegalAccessException
     */
    @Override
    public boolean delete(Checkout checkout, CartItem... childs) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        if (checkout.getCartItem() == null) return false;
        checkout.getCartItem().remove(childs[0]);
        return true;
    }

    /**
     * Trừ số lượng một mặt hàng trên đơn hàng
     * @param product
     * @param subQuantity
     */
    @Override
    public CartItem delete(Checkout checkout, Product product, int subQuantity) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // nếu chưa có đơn hàng, bỏ qua
        if (checkout == null) return null;

        // Khởi tạo danh sách order cartItem
        List<CartItem> listItems =  checkout.getCartItem();
        if (listItems == null) return null;

        // Kiểm tra xem đã có item với mặt hàng tương ứng chưa
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
            int newQuantity = cartItem.getQuantity() - subQuantity;
            if (newQuantity < 0) newQuantity = 1;

            // Cập nhật số lượng mới
            cartItem.setQuantity(newQuantity);
            cartItem.setPrice(newQuantity * product.getPrice());
        }
        return cartItem;
    }

    /**
     * Xóa một mặt hàng trên đơn hàng
     * @param product
     */
    @Override
    public CartItem delete(Checkout checkout, Product product) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // nếu chưa có đơn hàng, bỏ qua
        if (checkout == null) return null;

        // Khởi tạo danh sách order cartItem
        List<CartItem> listItems =  checkout.getCartItem();
        if (listItems == null) return null;

        // Kiểm tra xem đã có item với mặt hàng tương ứng chưa
        CartItem cartItem = null;
        for (CartItem item : listItems) {
            String itemID = item.getProduct().getID();
            if (itemID == null) continue;
            if (itemID.equals(product.getID())) {
                cartItem = item;
                break;
            }
        }

        // xóa khỏi danh sách checkout
        if (cartItem != null) delete(checkout, cartItem);
        return cartItem;
    }
}
