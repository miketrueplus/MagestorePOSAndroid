package com.magestore.app.pos.service.checkout;

import android.util.ArraySet;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.lib.model.catalog.ProductOptionCustomValue;
import com.magestore.app.lib.model.checkout.Cart;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.config.ConfigCustomerGroup;
import com.magestore.app.lib.model.config.ConfigTaxRates;
import com.magestore.app.lib.model.config.ConfigTaxRules;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCartItem;
import com.magestore.app.lib.model.sales.OrderCustomSalesInfo;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.catalog.ProductDataAccess;
import com.magestore.app.lib.resourcemodel.sales.CartDataAccess;
import com.magestore.app.lib.service.ServiceException;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.pos.model.catalog.PosProduct;
import com.magestore.app.pos.model.catalog.PosProductOptionConfigOption;
import com.magestore.app.pos.model.catalog.PosProductOptionJsonConfigAttributes;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.model.customer.PosCustomerAddress;
import com.magestore.app.pos.model.sales.PosOrderCartItem;
import com.magestore.app.pos.model.sales.PosOrderCustomSalesInfo;
import com.magestore.app.pos.service.AbstractService;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Thực hiện các nghiệp vụ, service của cart
 * Created by Mike on 1/12/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCartService extends AbstractService implements CartService {

    /**
     * Tính toán tổng giá trị sub total của đơn hàng
     *
     * @return float
     */
    @Override
    public synchronized float calculateSubTotal(Checkout checkout) {
        // Khởi tạo danh sách order items
        if (checkout == null) return 0;
        List<CartItem> listItems = checkout.getCartItem();
        if (listItems == null) return 0;

        float tax_total = 0;
        float row_total = 0;
        float total_save_cart = 0;
        for (CartItem item : listItems) {
            if (item.getIsSaveCart()) {
                total_save_cart += (item.getPriceShowView() * item.getQuantity());
                row_total = (item.getPriceShowView() * item.getQuantity());
            } else {
                total_save_cart += item.getPrice();
                row_total = item.getPrice();
            }
            float tax_percent = item.getTaxPercent() / 100;
            tax_total += row_total * tax_percent;
        }
        if (!ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_ODOO)) {
            checkout.setTaxTotal(tax_total);
        }
//        checkout.setSubTotalSaveCart(total);
        checkout.setSubTotal(total_save_cart);
        return total_save_cart;
    }

    /**
     * Tính toán thuế
     *
     * @return float
     */
    @Override
    public synchronized float calculateTaxTotal(Checkout checkout) {
        float sub_total = calculateSubTotal(checkout);
//        float tax_total = sub_total * (float) 0.1;
        float tax_total = 0.0f;
//        if (!ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_ODOO)) {
//            tax_total = checkout.getSubTotal() - checkout.getSubTotalSaveCart();
//            checkout.setTaxTotal(tax_total);
//        } else {
        checkout.setTaxTotal(checkout.getTaxTotal());
//        }
        return checkout.getTaxTotal();
    }

    /**
     * Tính toán tổng discount
     *
     * @return float
     */
    @Override
    public synchronized float calculateDiscountTotal(Checkout checkout) {
        checkout.setDiscountTotal(checkout.getDiscountTotal());
        return checkout.getDiscountTotal();
    }

    /**
     * Tính toán tổng giá trị cuối cùng của đơn hàng
     *
     * @return float
     */
    @Override
    public synchronized float calculateLastTotal(Checkout checkout) {
        float last_total = calculateSubTotal(checkout) + calculateTaxTotal(checkout) + calculateDiscountTotal(checkout);
        checkout.setGrandTotal(last_total);
        return last_total;
    }

    @Override
    public CartItem createCartItem() {
        return new PosCartItem();
    }

    @Override
    public CartItem create(Checkout checkout) throws InstantiationException, IllegalAccessException, ParseException, IOException {
        CartItem cartItem = new PosCartItem();
        cartItem.setTypeNormal();

        // Thêm vào danh sách order cartItem
        insert(checkout, cartItem);
        return cartItem;
    }


    /**
     * Khởi tạo 1 cústom sale
     *
     * @return
     */
    @Override
    public CartItem createCustomSale() {
        CartItem cartItem = new PosCartItem();
        Product product = new PosProduct();
        cartItem.setTypeCustom();
        cartItem.setProduct(product);
        cartItem.setQuantity(product.getQuantityIncrement());
        cartItem.setPrice(0);
        cartItem.setOriginalPrice(0);
        cartItem.setUnitPrice(0);
        cartItem.setCustomPrice(0);
        cartItem.setCustomPriceTypeFixed();
        cartItem.setItemId(String.valueOf(getItemIdInCurrentTime()));
        cartItem.setID("customsale");
        cartItem.getProduct().setCustomSale();
        cartItem.setShipunable();
        return cartItem;
    }

    /**
     * Khởi tạo 1 cart item với product
     *
     * @param product
     * @return
     */
    @Override
    public CartItem create(Product product) {
        return create(product, product.getQuantityIncrement(), product.getFinalPrice());
    }

    /**
     * Khởi tạo cart item với product
     *
     * @param product
     * @param quantity
     * @param price
     * @return
     */
    @Override
    public CartItem create(Product product, float quantity, float price) {
        CartItem cartItem = new PosCartItem();
        cartItem.setTypeNormal();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(price * quantity);
        cartItem.setOriginalPrice(price);
        cartItem.setUnitPrice(price);
        cartItem.setCustomPrice(price);
        cartItem.setDefaultCustomPrice(price);
        cartItem.setPriceShowView(price);
        cartItem.setCustomPriceTypeFixed();
        cartItem.setDiscount(StringUtil.STRING_ZERO);
        cartItem.setDiscountTypeFixed();
        cartItem.setId(product.getID());
        cartItem.setItemId(String.valueOf(getItemIdInCurrentTime()));
        return cartItem;
    }

    @Override
    public CartItem create(Product product, float quantity) {
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
        List<CartItem> listItems = checkout.getCartItem();
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

        // tìm cart có rồi thì thôi, vì cart đã được ăn theo bind từ giao diện
        for (CartItem item : checkout.getCartItem())
            if (item == childs[0]) return true;

        // kiểm tra trước với số lượng thêm như vậy có đủ điều kiện cho vào kho hay khônh
//        if (!validateStock(checkout, childs[0].getProduct(), childs[0].getQuantity())) throw new ServiceException("Not enought quantity");

        // chèn vào cart item
        checkout.getCartItem().add(childs[0]);
        return true;
    }

    /**
     * @param checkout
     * @param cartItem
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws ParseException
     * @throws IllegalAccessException
     */
    @Override
    public CartItem insertWithCustomSale(Checkout checkout, CartItem cartItem) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // insert thêm thông tin như option
        cartItem.getProduct().setCustomSale();
        cartItem.insertOption("name", cartItem.getProduct().getName());
        cartItem.insertOption("is_virtual", cartItem.isShipable() ? StringUtil.STRING_ZERO : StringUtil.STRING_ONE);
        cartItem.insertOption("price", Float.toString(cartItem.getUnitPrice()));
        cartItem.insertOption("tax_class_id", cartItem.getTaxClassId());
        cartItem.getProduct().setTaxClassId(cartItem.getTaxClassId());
        cartItem.getProduct().setID(cartItem.getProduct().getName() + "_" + Float.toString(cartItem.getUnitPrice()));
        return insertWithOption(checkout, cartItem);
    }

    @Override
    public CartItem insertWithOption(Checkout checkout, CartItem cartItem) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        if (checkout.getCartItem() == null)
            checkout.setCartItem(new ArrayList<CartItem>());

        // tìm cart có rồi thì thôi, vì cart đã được ăn theo bind từ giao diện
        for (CartItem itemInList : checkout.getCartItem())
            if (itemInList == cartItem) return itemInList;

        // tìm xem có cart item trùng option không
        CartItem itemInList = findCartItem(checkout, cartItem);

        // nếu không trùng option
        if (itemInList == null) {
            addTaxToCartItem(checkout, cartItem, false);
            // thêm cart item thẳng vào danh sách
            checkout.getCartItem().add(cartItem);
            return cartItem;
        }
        // nếu trùng option, chỉ cập nhật tăng thêm số lượng
        else {
            itemInList.setQuantity(itemInList.getQuantity() + cartItem.getQuantity());
            addTaxToCartItem(checkout, itemInList, true);
            return itemInList;
        }
    }

    /**
     * Tìm xem có cart item nào tương tự k0,
     * Tương tự là cùng product id và cùng product option
     *
     * @param checkout
     * @param findItem
     * @return
     */
    @Override
    public CartItem findCartItem(Checkout checkout, CartItem findItem) {
        // nếu danh sách rỗng
        if (checkout.getCartItem() == null) return null;

        // tìm cart trong danh sách
        for (CartItem item : checkout.getCartItem()) {
            if (compareCartItem(item, findItem)) return item;
        }

        // không thấy trả về null
        return null;
    }

    /**
     * So sánh xem liệu 2 item có giống nhau hay không
     * Nếu cùng tham chiếu, cùng product id hoặc cùng product option id
     *
     * @param itemOld
     * @param itemNew
     * @return
     */
    @Override
    public boolean compareCartItem(CartItem itemOld, CartItem itemNew) {
        // nếu cùng tham chiếu
        if (itemOld == itemNew) return true;

        // nếu không có product option
        if (itemOld.getProduct().getProductOption() == null && itemOld.getProduct().getID() != null)
            if (itemOld.getProduct().getID().equals(itemNew.getProduct().getID())) return true;

        // nếu có product option, 2 item trùng nhau nếu có product id trùng nhau và các cặp code value trong option trùng nhau
        if (itemOld.getProduct().getProductOption() != null || (itemOld.getProduct().isCustomSale() && itemNew.getProduct().isCustomSale())) {
            if (itemOld.getProduct().getID() != null && !itemOld.getProduct().getID().equals(itemNew.getProduct().getID()))
                return false;

            // so sánh từng cặp code value với custom option
            // nếu 2 danh sách không cùng độ dài thì return false
            int lenOld = itemOld.getOptions() == null ? 0 : itemOld.getOptions().size();
            int lenNew = itemNew.getOptions() == null ? 0 : itemNew.getOptions().size();
            if (lenOld != lenNew) return false;
            // còn cùng độ dài thì so sánh từng phần tử
            if (itemOld.getOptions() != null)
                for (PosCartItem.OptionsValue optionOldValue : itemOld.getOptions()) {
                    boolean blnMatchThis = false;
                    if (itemNew.getOptions() != null)
                        for (PosCartItem.OptionsValue optionNewValue : itemNew.getOptions()) {
                            // nếu tìm thấy cặp tương ứng thì thì break ra luôn
                            if (optionOldValue.code.equals(optionNewValue.code) && optionOldValue.value.equals(optionNewValue.value)) {
                                blnMatchThis = true;
                                break;
                            }
                        }
                    if (!blnMatchThis)
                        return false;
                }

            // so sánh các cặp bundle options
            // nếu 2 danh sách không cùng độ dài thì return false
            lenOld = itemOld.getBundleOption() == null ? 0 : itemOld.getBundleOption().size();
            lenNew = itemNew.getBundleOption() == null ? 0 : itemNew.getBundleOption().size();
            // ngược lại thì so sánh từng phần tử
            if (lenOld != lenNew) return false;
            if (itemOld.getBundleOption() != null)
                for (PosCartItem.OptionsValue optionOldValue : itemOld.getBundleOption()) {
                    boolean blnMatchThis = false;
                    if (itemNew.getBundleOption() != null)
                        for (PosCartItem.OptionsValue optionNewValue : itemNew.getBundleOption()) {
                            // nếu tìm thấy cặp tương ứng thì thì break ra luôn
                            if (optionOldValue.code.equals(optionNewValue.code) && optionOldValue.value.equals(optionNewValue.value)) {
                                blnMatchThis = true;
                                break;
                            }
                        }
                    if (!blnMatchThis)
                        return false;
                }

            // so sánh các cặp bundle options về khối lượng
            // nếu 2 danh sách không cùng độ dài thì return false
            lenOld = itemOld.getBundleOptionQuantity() == null ? 0 : itemOld.getBundleOptionQuantity().size();
            lenNew = itemNew.getBundleOptionQuantity() == null ? 0 : itemNew.getBundleOptionQuantity().size();
            if (lenOld != lenNew) return false;
            // ngược lại thì so sánh từng phần tử
            if (itemOld.getBundleOptionQuantity() != null)
                for (PosCartItem.OptionsValue optionOldValue : itemOld.getBundleOptionQuantity()) {
                    boolean blnMatchThis = false;
                    if (itemNew.getBundleOptionQuantity() != null)
                        for (PosCartItem.OptionsValue optionNewValue : itemNew.getBundleOptionQuantity()) {
                            // nếu tìm thấy cặp tương ứng thì thì break ra luôn
                            if (optionOldValue.code.equals(optionNewValue.code) && optionOldValue.value.equals(optionNewValue.value)) {
                                blnMatchThis = true;
                                break;
                            }
                        }
                    if (!blnMatchThis)
                        return false;
                }

            // so sánh các cặp bundle options config
            // nếu 2 danh sách không cùng độ dài thì return false
            lenOld = itemOld.getSuperAttribute() == null ? 0 : itemOld.getSuperAttribute().size();
            lenNew = itemNew.getSuperAttribute() == null ? 0 : itemNew.getSuperAttribute().size();
            if (lenOld != lenNew) return false;
            // ngược lại thì so sánh từng phần tử
            if (itemOld.getSuperAttribute() != null)
                for (PosCartItem.OptionsValue optionOldValue : itemOld.getSuperAttribute()) {
                    boolean blnMatchThis = false;
                    if (itemNew.getSuperAttribute() != null)
                        for (PosCartItem.OptionsValue optionNewValue : itemNew.getSuperAttribute()) {
                            // nếu tìm thấy cặp tương ứng thì thì break ra luôn
                            if (optionOldValue.code.equals(optionNewValue.code) && optionOldValue.value.equals(optionNewValue.value)) {
                                blnMatchThis = true;
                                break;
                            }
                        }
                    if (!blnMatchThis)
                        return false;
                }
            return true;
        }

        // false không tìm thấy
        return false;
    }

    @Override
    public void increase(CartItem cartItem) {
        increase(cartItem, cartItem.getProduct().getQuantityIncrement());
    }

    @Override
    public void increase(CartItem cartItem, float quantity) {
        float newQuantity = cartItem.getQuantity() + quantity;
        newQuantity = newQuantity > 0 ? newQuantity : cartItem.getProduct().getQuantityIncrement();
        float newPrice = cartItem.getUnitPrice() * newQuantity;
        cartItem.setQuantity(newQuantity);
        cartItem.setPrice(newPrice);
    }

    @Override
    public void substract(CartItem cartItem) {
        substract(cartItem, cartItem.getProduct().getQuantityIncrement());
    }

    @Override
    public void substract(CartItem cartItem, float quantity) {
        float newQuantity = cartItem.getQuantity() - quantity;
        newQuantity = newQuantity > 0 ? newQuantity : cartItem.getProduct().getQuantityIncrement();
        float newPrice = cartItem.getUnitPrice() * newQuantity;
        cartItem.setQuantity(newQuantity);
        cartItem.setPrice(newPrice);
    }

    @Override
    public CartItem insert(Checkout checkout, String productID, String productName, float quantity, float price, boolean inStock, String image) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // nếu chưa có đơn hàng, bo qua
        if (checkout == null) return null;

        // khởi tạo product
        Product product = new PosProduct();
        product.setID(productID);
        product.setName(productName);
        product.setInStock(inStock);
        if (!StringUtil.isNullOrEmpty(image)) {
            product.setImage(image);
        }

        // kiểm tra trước xem trong checkout đã có sẵn product chưa
        if (checkout.getCartItem() != null)
            for (CartItem item : checkout.getCartItem()) {
                if (item.getProduct().getID().equals(productID)) product = item.getProduct();
            }

        // khởi tạo cart và add vào list
        return insert(checkout, product, quantity, price);
    }

    /**
     * Thêm 1 order item
     *
     * @param product
     * @param quantity
     * @param price
     */
    @Override
    public CartItem insert(Checkout checkout, Product product, float quantity, float price) throws InstantiationException, IllegalAccessException, ParseException, IOException, ServiceException {
        // nếu chưa có đơn hàng, bo qua
        if (checkout == null) return null;

        // Khởi tạo danh sách order cartItem
        List<CartItem> listItems = checkout.getCartItem();
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
//            if (!validateStock(checkout, product, quantity))
//                throw new ServiceException(ServiceException.EXCEPTION_QUANTITY_NOT_ENOUGH, "");

            // kiểm tra số lượng còn trong kho không đã
            quantity = calculateValidStock(checkout, product, quantity);

            // Khởi tạo product order item
            cartItem = create(product, quantity, price);
            addTaxToCartItem(checkout, cartItem, false);
            // Thêm vào danh sách order cartItem
            insert(checkout, cartItem);
        }
        // có rồi thì cập nhật lại số lượng
        else {
            // tính toán số lượng mới
//            int newQuantity = cartItem.getQuantity() + quantity;
            // kiểm tra số lượng trước có đủ trong kho không
//            validateStock(checkout, cartItem, quantity);
//                throw new ServiceException("Not enough quantity");

            // kiểm tra số lượng còn trong kho không đã
            quantity = calculateValidStock(checkout, cartItem, quantity);

            // cập nhật số lượng
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            float unitPrice = cartItem.getUnitPrice();
            float totalPrice = (unitPrice * cartItem.getQuantity());
            cartItem.setPrice(totalPrice);
            addTaxToCartItem(checkout, cartItem, false);
        }
        return cartItem;
    }

    /**
     * Sinh ID tương ứng theo thời gian
     *
     * @return
     */
    private long getItemIdInCurrentTime() {
        long time = System.currentTimeMillis();
        return time;
    }

    /**
     * Thêm product
     *
     * @param product
     * @param quantity
     */
    @Override
    public CartItem insert(Checkout checkout, Product product, float quantity) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return insert(checkout, product, quantity, product.getFinalPrice());
    }

    /**
     * Xóa 1 mặt hàng trong order item
     *
     * @param position
     */
    @Override
    public boolean delete(Checkout checkout, int position) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // nếu chưa có đơn hàng, bo qua
        if (checkout == null) return false;

        // Khởi tạo danh sách order items
        List<CartItem> listItems = checkout.getCartItem();
        if (listItems == null) return true;

        // remove order item vị trí thứ n
        listItems.remove(position);
        return true;
    }

    /**
     * Xóa item ra khỏi danh sách
     *
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
        CartItem cartItem = childs[0];
        if (cartItem.getIsSaveCart()) {
            DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
            CartDataAccess cartDataAccess = factory.generateCartDataAccess();
            boolean isRemove = cartDataAccess.delete(checkout, cartItem);
            if (isRemove) {
                checkout.getCartItem().remove(cartItem);
            }
            return isRemove;
        } else {
            checkout.getCartItem().remove(cartItem);
        }
        return true;
    }

    /**
     * Trừ số lượng một mặt hàng trên đơn hàng
     *
     * @param product
     * @param subQuantity
     */
    @Override
    public CartItem delete(Checkout checkout, Product product, float subQuantity) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // nếu chưa có đơn hàng, bỏ qua
        if (checkout == null) return null;

        // Khởi tạo danh sách order cartItem
        List<CartItem> listItems = checkout.getCartItem();
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
            float newQuantity = cartItem.getQuantity() - subQuantity;
            if (newQuantity < 0) newQuantity = 1;

            // Cập nhật số lượng mới
            cartItem.setQuantity(newQuantity);
            cartItem.setPrice(newQuantity * cartItem.getUnitPrice());
        }
        return cartItem;
    }

    /**
     * Xóa một mặt hàng trên đơn hàng
     *
     * @param product
     */
    @Override
    public CartItem delete(Checkout checkout, Product product) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // nếu chưa có đơn hàng, bỏ qua
        if (checkout == null) return null;

        // Khởi tạo danh sách order cartItem
        List<CartItem> listItems = checkout.getCartItem();
        if (listItems == null) return null;
        CartItem cartItem = null;
        // kiểm tra xem có phải item online ko?
        if (product.getIsSaveCart()) {
            DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
            CartDataAccess cartDataAccess = factory.generateCartDataAccess();
//            cartItem = cartDataAccess.delete(checkout, product);
            // xóa khỏi danh sách checkout
            if (cartItem != null) delete(checkout, cartItem);
        } else {
            // Kiểm tra xem đã có item với mặt hàng tương ứng chưa
            for (CartItem item : listItems) {
//                if (item.isTypeCustom()) {
//                    cartItem = item;
//                } else {
                String itemID = item.getProduct().getID();
                if (itemID == null) continue;
                if (itemID.equals(product.getID())) {
                    cartItem = item;
                    break;
                }
//                }
            }

            // xóa khỏi danh sách checkout
            if (cartItem != null) delete(checkout, cartItem);
        }
        return cartItem;
    }

    /**
     * Cập nhật lại đơn giá cho cart item
     *
     * @param cartItem
     */
    @Override
    public void updatePrice(CartItem cartItem) {
//        float price = cartItem.getProduct().getFinalPrice();
//        float basePrice = cartItem.getProduct().getFinalPrice();
//
//        // sử dụng để tính toán đối với config option
//        List<String> products = new ArrayList<>();
//        String configOptionProductId = null;
//
//        // duyệt từng custome option
//        for (ProductOptionCustom productOptionCustom : cartItem.getChooseProductOptions().keySet()) {
//            if (cartItem.getChooseProductOptions().get(productOptionCustom) == null) continue;
//
//            for (ProductOptionCustomValue productOptionCustomValue : cartItem.getChooseProductOptions().get(productOptionCustom).productOptionCustomValueList) {
//                if (productOptionCustom.isConfigOption()) {
//                    List<PosProductOptionJsonConfigAttributes.Option> configOption = cartItem.getProduct().getProductOption().getJsonConfig().attributes.get(productOptionCustom.getID()).options;
//                    for (PosProductOptionJsonConfigAttributes.Option option : cartItem.getProduct().getProductOption().getJsonConfig().attributes.get(productOptionCustom.getID()).options)
//                        if (option.id.equals(productOptionCustomValue.getID())) {
//                            if (products.size() <= 0) products.addAll(option.products);
//                            else
//                                products.retainAll(option.products);
//                        }
//                    configOptionProductId = (products.size() == 1) ? products.get(0) : null;
//                } else {
//                    price += (productOptionCustom.isPriceTypePercent() ? basePrice : 1) * Float.parseFloat(productOptionCustomValue.getPrice());
//                }
//            }
//        }
//
//        // tính giá theo config option
//        if (configOptionProductId != null) price = cartItem.getProduct().getProductOption().getJsonConfig().optionPrices.get(configOptionProductId).getFinalPrice();
//
//        // cập nhật đơn giá
//        cartItem.setUnitPrice(price);
    }

    /**
     * Kiểm tra số lượng trong kho đủ để bán không
     *
     * @param checkout
     * @param product
     * @param quantity
     * @return
     */
    @Override
    public boolean validateStock(Checkout checkout, Product product, float quantity) {
        if (!product.isInStock()) return false;
//        if (quantity < product.getAllowMinQty()) return false;
        if (quantity > product.getAllowMaxQty() && (product.getAllowMaxQty() > 0))
            return false;
        return true;
    }

    /**
     * Kiểm tra số lượng trong kho đủ để bán không
     * Nếu đủ, trả lại số lượng khả dĩ cho phép có thể add vào cart
     *
     * @param checkout
     * @param quantity
     * @return
     */
    @Override
    public boolean validateStock(Checkout checkout, CartItem item, float quantity) throws ServiceException {
        float newQuantity = item.getQuantity() + quantity;
        Product product = item.getProduct();

        if (!item.getProduct().isInStock())
            throw new ServiceException(ServiceException.EXCEPTION_QUANTITY_OUT_OF_STOCK);

        // nếu không đặt backorder thì quan tâm quantity phải nằm trong khoản qty ... max_quantity
        if (!product.isBackOrders() && newQuantity > product.getAllowMaxQty() && (product.getAllowMaxQty() > 0))
            throw new ServiceException(ServiceException.EXCEPTION_QUANTITY_REACH_MAXIMUM, Float.toString(product.getAllowMaxQty()));
        // nếu không đật backorder thì quantity phải nằm trong khoản zero đến maximum quantity
        if (product.isBackOrders() && newQuantity > product.getMaximumQty() && (product.getMaximumQty() > 0))
            throw new ServiceException(ServiceException.EXCEPTION_QUANTITY_REACH_MAXIMUM, Float.toString(product.getMaximumQty()));

//        if (newQuantity < product.getAllowMinQty()) throw new ServiceException(ServiceException.EXCEPTION_QUANTITY_REACH_MINIMUM, "");
        return true;
    }

    /**
     * Trả lại số lượng phù hợp khi add 1 item vào cart
     *
     * @param checkout
     * @param item
     * @param quantity
     * @return
     * @throws ServiceException
     */
    @Override
    public float calculateValidStock(Checkout checkout, CartItem item, float quantity) throws ServiceException {
        float newQuantity = item.getQuantity() + quantity;
        Product product = item.getProduct();

        if (!item.getProduct().isInStock())
            throw new ServiceException(ServiceException.EXCEPTION_QUANTITY_OUT_OF_STOCK);
        if (!product.isBackOrders() && newQuantity > product.getAllowMaxQty() && ((int) product.getAllowMaxQty() > 0))
            throw new ServiceException(ServiceException.EXCEPTION_QUANTITY_REACH_MAXIMUM, Float.toString(product.getAllowMaxQty()));
        if (product.isBackOrders() && newQuantity > product.getMaximumQty() && (product.getMaximumQty() > 0))
            throw new ServiceException(ServiceException.EXCEPTION_QUANTITY_REACH_MAXIMUM, Float.toString(product.getMaximumQty()));

        if (newQuantity < product.getAllowMinQty()) {
            newQuantity = product.getAllowMinQty() > product.getQuantityIncrement() ? product.getAllowMinQty() : product.getQuantityIncrement();
            quantity = newQuantity - quantity;
            if (quantity <= 0) quantity = 1;
        }
        return quantity;
    }

    /**
     * Trả lại số lượng phù hợp khi add 1 item vào cart
     *
     * @param checkout
     * @param quantity
     * @return
     * @throws ServiceException
     */
    @Override
    public float calculateValidStock(Checkout checkout, Product product, float quantity) throws ServiceException {
        if (!product.isInStock())
            throw new ServiceException(ServiceException.EXCEPTION_QUANTITY_OUT_OF_STOCK);
        if (!product.isBackOrders() && quantity > product.getAllowMaxQty() && (product.getAllowMaxQty() > 0))
            throw new ServiceException(ServiceException.EXCEPTION_QUANTITY_REACH_MAXIMUM, Float.toString(product.getAllowMaxQty()));
        if (product.isBackOrders() && quantity > product.getMaximumQty() && (product.getMaximumQty() > 0))
            throw new ServiceException(ServiceException.EXCEPTION_QUANTITY_REACH_MAXIMUM, Float.toString(product.getMaximumQty()));

        if (quantity < product.getAllowMinQty()) {
            quantity = product.getAllowMinQty() > product.getQuantityIncrement() ? product.getAllowMinQty() : product.getQuantityIncrement();
            if (quantity <= 0) quantity = 1;
        }
        return quantity;
    }

    /**
     * Thực hiện re-order, tạo lại các cart item tương ứng đối với checkout mới
     *
     * @param checkout
     * @param order
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws ParseException
     * @throws IllegalAccessException
     */
    @Override
    public List<CartItem> reOrder(Checkout checkout, Order order) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ProductDataAccess productDataAccess = factory.generateProductDataAccess();
        List<Product> listProduct = new ArrayList<>();
        List<String> listId = new ArrayList<>();
        for (OrderCartItem orderitem : order.getItemsInfoBuy().getListOrderCartItems()) {
            if (orderitem.getCustomSalesInfo() == null) {
                listId.add(orderitem.getID());
            }
        }
        if (listId.size() > 0) {
            listProduct = productDataAccess.retrieve(listId);
        }

        // xử lý từng item trong order
        for (OrderCartItem orderitem : order.getItemsInfoBuy().getListOrderCartItems()) {
            // nếu không phải custome sales
            if (orderitem.getCustomSalesInfo() == null) {
                // fill thông tin product vào
                String id = orderitem.getID();
                Product product = null;
                if (listProduct.size() > 0) {
                    for (Product productRespone : listProduct) {
                        if (productRespone.getID().equals(id)) {
                            product = productRespone;
                        }
                    }
                }
                if (product != null) {
                    if (product.haveProductOption()) {
                        product.setProductOption(productDataAccess.loadProductOption(product));
                    }
                    if (product.isInStock()) {
                        // tạo item theo product truy xuất được
                        CartItem newItem = create(product, orderitem.getQty(), orderitem.getUnitPrice());
                        newItem.setOriginalPrice(orderitem.getOriginalPrice());
                        newItem.setOptions(mapOptionValueID2Code(orderitem.getOptions()));
                        newItem.setBundleOption(mapOptionValueID2Code(orderitem.getBundleOption()));
                        newItem.setBundleOptionQty(mapOptionValueID2Code(orderitem.getBundleOptionQty()));
                        newItem.setSuperAttribute(mapOptionValueID2Code(orderitem.getSuperAttribute()));

                        // xử lý xong thì insert lại vào checkout
                        insert(checkout, newItem);
                    }
                }
            }
            // nếu là custom sales
            else {
                if (orderitem.getCustomSalesInfo().size() <= 0) continue;
                OrderCustomSalesInfo customSalesInfo = orderitem.getCustomSalesInfo().get(0);
                CartItem newItem = createCustomSale();
                newItem.setQuantity(customSalesInfo.getQty());
                newItem.setTypeCustom();
                newItem.setPrice(customSalesInfo.getUnitPrice());
                newItem.setUnitPrice(customSalesInfo.getUnitPrice());
                newItem.setCustomPrice(customSalesInfo.getUnitPrice());
                newItem.setOriginalPrice(customSalesInfo.getUnitPrice());
                newItem.getProduct().setName(customSalesInfo.getProductName());
                newItem.setShipable(!customSalesInfo.isVirtual());

                // xử lý xong thì insert lại vào checkout
                insertWithCustomSale(checkout, newItem);
            }
        }
        return checkout.getCartItem();
    }

    @Override
    public void changeTax(Checkout checkout) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CartDataAccess cartDataAccess = factory.generateCartDataAccess();
        cartDataAccess.changeTax(checkout);
    }

    @Override
    public void changeCustomerOffline(Checkout checkout) {
        List<CartItem> mListCartItems = checkout.getCartItem();
        if (mListCartItems != null && mListCartItems.size() > 0) {
            for (CartItem cartItem : mListCartItems) {
                float taxPercent = getTaxPercentWithProduct(cartItem.getProduct(), checkout);
                float qty = cartItem.getQuantity();
                float basePrice = cartItem.getUnitPrice();
                float price = ConfigUtil.convertToPrice(basePrice);

                float unitTaxAmount = price * taxPercent / 100;
                float baseUnitTaxAmount = basePrice * basePrice / 100;

                float priceInclTax = price + unitTaxAmount;
                float basePriceInclTax = basePrice + baseUnitTaxAmount;

                float taxAmount = unitTaxAmount * qty;
                float baseTaxAmount = baseUnitTaxAmount * qty;

                cartItem.setBasePriceInclTax(basePriceInclTax);
                cartItem.setUnitTaxAmount(unitTaxAmount);
                cartItem.setBaseUnitTaxAmount(baseUnitTaxAmount);
                cartItem.setTaxAmount(taxAmount);
                cartItem.setBaseTaxAmount(baseTaxAmount);
                cartItem.setTaxPercent(taxPercent);
            }
        }
    }

    /**
     * Map ID sang Code đối với mỗi option value khi re order
     *
     * @param options
     * @return
     */
    private List<PosCartItem.OptionsValue> mapOptionValueID2Code(List<PosCartItem.OptionsValue> options) {
        if (options == null) return options;
        for (PosCartItem.OptionsValue option : options) {
            option.code = option.id;
        }
        return options;
    }

    private void addTaxToCartItem(Checkout checkout, CartItem cartItem, boolean isAddCart) {
        if (isAddCart) {
            cartItem.setTaxAmount(cartItem.getUnitTaxAmount() * cartItem.getQuantity());
            cartItem.setBaseTaxAmount(cartItem.getBaseUnitTaxAmount() * cartItem.getQuantity());
        } else {
            float taxPercent = 0;
            taxPercent = getTaxPercentWithProduct(cartItem.getProduct(), checkout);
            float base_price = cartItem.getUnitPrice();
            float price = ConfigUtil.convertToPrice(cartItem.getUnitPrice());
            float unitTaxAmount = 0;
            float baseUnitTaxAmount = 0;
            if (ConfigUtil.isTaxCalculationPriceIncludesTax()) {
                float basePriceExclTax = (base_price / (100 + taxPercent)) * 100;
                float priceExclTax = ConfigUtil.convertToPrice(basePriceExclTax);
                cartItem.setBasePriceInclTax(basePriceExclTax);
                unitTaxAmount = price - priceExclTax;
                baseUnitTaxAmount = base_price - basePriceExclTax;
            } else {
                float basePriceExclTax = base_price + base_price * taxPercent / 100;
                float priceExclTax = ConfigUtil.convertToPrice(basePriceExclTax);
                cartItem.setBasePriceInclTax(basePriceExclTax);
                unitTaxAmount = priceExclTax - price;
                baseUnitTaxAmount = basePriceExclTax - base_price;
            }
            float taxAmount = unitTaxAmount * cartItem.getQuantity();
            float baseTaxAmount = unitTaxAmount * cartItem.getQuantity();
            cartItem.setUnitTaxAmount(unitTaxAmount);
            cartItem.setBaseUnitTaxAmount(baseUnitTaxAmount);
            cartItem.setTaxAmount(taxAmount);
            cartItem.setBaseTaxAmount(baseTaxAmount);
            cartItem.setTaxPercent(taxPercent);
        }
    }

    private float getTaxPercentWithProduct(Product mProduct, Checkout mCheckout) {
        String taxClassId = mProduct.getTaxClassId();
        List<StoreTaxRate> store_tax_rates = getProductTaxRate(taxClassId, ConfigUtil.getDefaultCustomerGroup(), ConfigUtil.getAddressOrigin());
        CustomerAddress addressCalTax = new PosCustomerAddress();
        String calculateTaxBaseOn = ConfigUtil.getTaxCalculationBasedOn();

        Customer guestCustomer = ConfigUtil.getCustomerGuest();
        List<CustomerAddress> customerAddressList = mCheckout.getCustomer().getAddress();

        CustomerAddress shippingAddress;
        CustomerAddress billingAddress;
        if (mCheckout.getCustomer().getUseOneAddress()) {
            shippingAddress = customerAddressList.get(0);
            billingAddress = customerAddressList.get(0);
        } else {
            shippingAddress = customerAddressList.get(0);
            if (customerAddressList.size() == 1) {
                billingAddress = customerAddressList.get(0);
            } else {
                billingAddress = customerAddressList.get(1);
            }
        }

        if (calculateTaxBaseOn.equals("shipping")) {
            addressCalTax.setCountry(shippingAddress.getCountry());
            addressCalTax.setRegionID(shippingAddress.getRegionID());
            addressCalTax.setPostCode(shippingAddress.getPostCode());
        } else if (calculateTaxBaseOn.equals("billing")) {
            addressCalTax.setCountry(billingAddress.getCountry());
            addressCalTax.setRegionID(billingAddress.getRegionID());
            addressCalTax.setPostCode(billingAddress.getPostCode());
        }

        List<StoreTaxRate> taxRates = getProductTaxRate(taxClassId, ConfigUtil.getDefaultCustomerGroup(), addressCalTax);
        float taxPercent = 0;
        for (StoreTaxRate taxRate : taxRates) {
            taxPercent += taxRate.rate;
        }
        return taxPercent;
    }

    private List<StoreTaxRate> getProductTaxRate(String tax_class_id, String group_id, CustomerAddress address) {
        String customerTaxClassId = getCustomerTaxClassId(group_id);
        List<StoreTaxRate> rate = getRateWithTaxClassId(tax_class_id, customerTaxClassId, address);
        return rate;
    }

    private String getCustomerTaxClassId(String group_id) {
        String customerTaxClassId = "";
        if (!StringUtil.isNullOrEmpty(group_id) && ConfigUtil.getConfigCustomerGroup() != null && ConfigUtil.getConfigCustomerGroup().size() > 0) {
            for (ConfigCustomerGroup customerGroup : ConfigUtil.getConfigCustomerGroup()) {
                if (customerGroup.getID().equals(group_id)) {
                    customerTaxClassId = customerGroup.getTaxClassId();
                }
            }
        }
        return customerTaxClassId;
    }

    private List<StoreTaxRate> getRateWithTaxClassId(String productTaxClassId, String customerTaxClassId, CustomerAddress address) {
        List<String> rateIds = new ArrayList<>();
        List<StoreTaxRate> rates = new ArrayList<>();
        List<StoreTaxRate> tempRates = new ArrayList<>();
        List<ConfigTaxRules> rules = ConfigUtil.getConfigTaxRules();

        if (!StringUtil.isNullOrEmpty(customerTaxClassId)) {
            if (rules != null && rules.size() > 0) {
                for (ConfigTaxRules rule : rules) {
                    List<String> customerTcIds = rule.getCustomerTcIds();
                    List<String> productTcIds = rule.getProductTcIds();
                    int customerIndex = customerTcIds.indexOf(customerTaxClassId);
                    int productIndex = productTcIds.indexOf(productTaxClassId);
                    if (customerIndex != -1 && productIndex != -1) {
                        rateIds = new ArrayList<>();
                        rateIds.addAll(rule.getRatesIds());

                        float tempRate = getRateValueWithRateIds(rateIds, address);
                        if (tempRate > 0) {
                            String priority = "";
                            if (!StringUtil.isNullOrEmpty(rule.getPriority())) {
                                priority = rule.getPriority();
                            }
                            boolean isNew = true;

                            if (tempRates.size() > 0) {
                                for (StoreTaxRate rate : tempRates) {
                                    String ratePriority = "";
                                    if (!StringUtil.isNullOrEmpty(rate.priority)) {
                                        ratePriority = rate.priority;
                                    }
                                    if (priority.equals(ratePriority)) {
                                        isNew = false;
                                        float newRate = rate.rate + tempRate;
                                        rate.rate = newRate;
                                    }
                                }
                            }

                            if (isNew) {
                                StoreTaxRate rate = new StoreTaxRate();
                                rate.priority = priority;
                                rate.rate = tempRate;
                                tempRates.add(rate);
                            }
                        }
                    }
                }
            }
        } else {
            if (rules != null && rules.size() > 0) {
                for (ConfigTaxRules rule : rules) {
                    List<String> productTcIds = rule.getProductTcIds();
                    int productIndex = productTcIds.indexOf(productTaxClassId);
                    if (productIndex != -1) {
                        rateIds = new ArrayList<>();
                        rateIds.addAll(rule.getRatesIds());

                        float tempRate = getRateValueWithRateIds(rateIds, address);
                        if (tempRate > 0) {
                            String priority = "";
                            if (!StringUtil.isNullOrEmpty(rule.getPriority())) {
                                priority = rule.getPriority();
                            }
                            boolean isNew = true;

                            if (tempRates.size() > 0) {
                                for (StoreTaxRate rate : tempRates) {
                                    String ratePriority = "";
                                    if (!StringUtil.isNullOrEmpty(rate.priority)) {
                                        ratePriority = rate.priority;
                                    }
                                    if (priority.equals(ratePriority)) {
                                        isNew = false;
                                        float newRate = rate.rate + tempRate;
                                        rate.rate = newRate;
                                    }
                                }
                            }

                            if (isNew) {
                                StoreTaxRate rate = new StoreTaxRate();
                                rate.priority = priority;
                                rate.rate = tempRate;
                                tempRates.add(rate);
                            }
                        }
                    }
                }
            }
        }

        Collections.sort(tempRates, new Comparator<StoreTaxRate>() {
            @Override
            public int compare(StoreTaxRate storeTaxRate, StoreTaxRate t1) {
                return storeTaxRate.priority.compareToIgnoreCase(t1.priority);
            }
        });
        rates.addAll(tempRates);
        return rates;
    }

    private float getRateValueWithRateIds(List<String> rateIds, CustomerAddress address) {
        float rate = 0;

        for (String rateId : rateIds) {
            List<ConfigTaxRates> taxRates = ConfigUtil.getConfigTaxRates();
            for (ConfigTaxRates taxRate : taxRates) {
                String taxRateId = vadilateString(taxRate.getID());
                if (taxRateId.equals(rateId)) {
                    String taxRateCountry = vadilateString(taxRate.getCountry());
                    String taxRateRegionId = vadilateString(taxRate.getRegionId());
                    String taxRatePostCode = vadilateString(taxRate.getPostCode());

                    String addressCountry = vadilateString(address.getCountry());
                    String addressRegionId = vadilateString(address.getRegionID());
                    String addressPostCode = vadilateString(address.getPostCode());

                    if ((taxRateCountry.equals("*") || taxRateCountry.equals(addressCountry))
                            && (taxRateRegionId.equals("*") || taxRateRegionId.equals(addressRegionId) || taxRateRegionId.equals("0"))
                            && (taxRatePostCode.equals("*") || taxRatePostCode.equals(addressPostCode))) {
                        if (taxRate.getRate() > rate) {
                            rate = taxRate.getRate();
                        }
                    }
                }
            }
        }

        return rate;
    }

    private String vadilateString(String data) {
        if (!StringUtil.isNullOrEmpty(data)) {
            return data;
        }
        return "";
    }

    private class StoreTaxRate {
        public String priority;
        public float rate;
    }
}