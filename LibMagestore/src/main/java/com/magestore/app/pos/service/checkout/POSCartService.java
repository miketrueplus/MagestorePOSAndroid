package com.magestore.app.pos.service.checkout;

import android.util.ArraySet;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.lib.model.catalog.ProductOptionCustomValue;
import com.magestore.app.lib.model.checkout.Cart;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.sales.CartDataAccess;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.pos.model.catalog.PosProduct;
import com.magestore.app.pos.model.catalog.PosProductOptionConfigOption;
import com.magestore.app.pos.model.catalog.PosProductOptionJsonConfigAttributes;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.service.AbstractService;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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
     * @return
     */
    @Override
    public synchronized float calculateSubTotal(Checkout checkout) {
        // Khởi tạo danh sách order items
        if (checkout == null) return 0;
        List<CartItem> listItems = checkout.getCartItem();
        if (listItems == null) return 0;

        float total = 0;
        for (CartItem item : listItems) {
            total += item.getPrice();
        }
        checkout.setSubTotal(total);
        return total;
    }

    /**
     * Tính toán thuế
     *
     * @return
     */
    @Override
    public synchronized float calculateTaxTotal(Checkout checkout) {
        float sub_total = calculateSubTotal(checkout);
//        float tax_total = sub_total * (float) 0.1;
        float tax_total = 0.0f;
        checkout.setTaxTotal(tax_total);
        return tax_total;
    }

    /**
     * Tính toán tổng discount
     *
     * @return
     */
    @Override
    public synchronized float calculateDiscountTotal(Checkout checkout) {
        checkout.setDiscountTotal(0);
        return 0;
    }

    /**
     * Tính toán tổng giá trị cuối cùng của đơn hàng
     *
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
    public CartItem create(Product product, int quantity, float price) {
        CartItem cartItem = new PosCartItem();
        cartItem.setTypeNormal();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(price * quantity);
        cartItem.setOriginalPrice(price);
        cartItem.setUnitPrice(price);
        cartItem.setCustomPrice(price);
        cartItem.setCustomPriceTypeFixed();
        cartItem.setDiscount(StringUtil.STRING_ZERO);
        cartItem.setDiscountTypeFixed();
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

        checkout.getCartItem().add(childs[0]);
        return true;
    }

    /**
     *
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
        cartItem.insertOption("is_virtual", StringUtil.STRING_ONE);
        cartItem.insertOption("price", Float.toString(cartItem.getUnitPrice()));
        cartItem.insertOption("tax_class_id", StringUtil.STRING_ZERO);

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
            // thêm cart item thẳng vào danh sách
            checkout.getCartItem().add(cartItem);
            return cartItem;
        }
        // nếu trùng option, chỉ cập nhật tăng thêm số lượng
        else {
            itemInList.setQuantity(itemInList.getQuantity() + cartItem.getQuantity());
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
            if (itemOld.getProduct().getID() != null && !itemOld.getProduct().getID().equals(itemNew.getProduct().getID())) return false;

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
    public void increase(CartItem cartItem, int quantity) {
        int newQuantity = cartItem.getQuantity() + quantity;
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
    public void substract(CartItem cartItem, int quantity) {
        int newQuantity = cartItem.getQuantity() - quantity;
        newQuantity = newQuantity > 0 ? newQuantity : cartItem.getProduct().getQuantityIncrement();
        float newPrice = cartItem.getUnitPrice() * newQuantity;
        cartItem.setQuantity(newQuantity);
        cartItem.setPrice(newPrice);
    }

    @Override
    public CartItem insert(Checkout checkout, String productID, String productName, int quantity, float price) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // nếu chưa có đơn hàng, bo qua
        if (checkout == null) return null;

        // khởi tạo product
        Product product = new PosProduct();
        product.setID(productID);
        product.setName(productName);

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
    public CartItem insert(Checkout checkout, Product product, int quantity, float price) throws InstantiationException, IllegalAccessException, ParseException, IOException {
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
            // Khởi tạo product order item
            cartItem = create(product, quantity, price);
            // Thêm vào danh sách order cartItem
            insert(checkout, cartItem);
        }
        // có rồi thì cập nhật lại số lượng
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            float unitPrice = cartItem.getUnitPrice();
            float totalPrice = (unitPrice * cartItem.getQuantity());
            cartItem.setPrice(totalPrice);
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
    public CartItem insert(Checkout checkout, Product product, int quantity) throws IOException, InstantiationException, ParseException, IllegalAccessException {
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
        checkout.getCartItem().remove(childs[0]);
        return true;
    }

    /**
     * Trừ số lượng một mặt hàng trên đơn hàng
     *
     * @param product
     * @param subQuantity
     */
    @Override
    public CartItem delete(Checkout checkout, Product product, int subQuantity) throws IOException, InstantiationException, ParseException, IllegalAccessException {
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
            int newQuantity = cartItem.getQuantity() - subQuantity;
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
            cartItem = cartDataAccess.delete(checkout, product);
            // xóa khỏi danh sách checkout
            if (cartItem != null) delete(checkout, cartItem);
        } else {
            // Kiểm tra xem đã có item với mặt hàng tương ứng chưa
            for (CartItem item : listItems) {
                if (item.isTypeCustom()) {
                    cartItem = item;
                } else {
                    String itemID = item.getProduct().getID();
                    if (itemID == null) continue;
                    if (itemID.equals(product.getID())) {
                        cartItem = item;
                        break;
                    }
                }
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
}
