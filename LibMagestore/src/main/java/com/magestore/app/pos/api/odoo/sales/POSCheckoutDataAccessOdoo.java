package com.magestore.app.pos.api.odoo.sales;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductTaxDetailOdoo;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.PlaceOrderParams;
import com.magestore.app.lib.model.checkout.Quote;
import com.magestore.app.lib.model.checkout.QuoteAddCouponParam;
import com.magestore.app.lib.model.checkout.SaveQuoteParam;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.sales.CheckoutDataAccess;
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.pos.api.odoo.POSDataAccessSessionOdoo;
import com.magestore.app.pos.model.catalog.PosProduct;
import com.magestore.app.pos.model.checkout.PosCheckoutPayment;
import com.magestore.app.util.ConfigUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 9/11/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCheckoutDataAccessOdoo extends POSAbstractDataAccessOdoo implements CheckoutDataAccess {
    private class PlaceOrderEntity {
        float amount_paid;
        List<PaymentEntity> statement_ids;
        String creation_date;
        String partner_id;
        float amount_total;
        String pos_session_id;
        List<ProductEntity> lines;
        float amount_tax;
        float amount_return;
        boolean is_create_invoice;
    }

    private class PaymentEntity {
        float amount;
        String name;
        String journal_id;
    }

    private class ProductEntity {
        String product_id;
        float price_unit;
        float qty;
        float discount;
        List<SerialNumberEntity> pack_lot_ids;
        List<String> tax_ids;
    }

    private class SerialNumberEntity {
        String lot_name;
    }

    @Override
    public boolean insert(Checkout... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public Checkout saveCart(Checkout checkout, Quote quote) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        float total_tax = 0;
        float grand_total = 0;
        List<CartItem> listCartItem = checkout.getCartItem();
        for (CartItem cartItem : listCartItem) {
            cartItem.setRowTotal(cartItem.getProduct().getFinalPrice());
            float tax = 0;
            List<ProductTaxDetailOdoo> listTaxDetail = cartItem.getProduct().getTaxDetail();
            if (listTaxDetail != null && listTaxDetail.size() > 0) {
                for (ProductTaxDetailOdoo taxDetail : listTaxDetail) {
                    tax += taxDetail.getAmount();
                }
            }
            total_tax += tax;
        }
        checkout.setTaxTotal(ConfigUtil.convertToBasePrice(total_tax));
        checkout.setSubTotalView(ConfigUtil.convertToBasePrice(checkout.getSubTotal()));
        grand_total = checkout.getSubTotal() + total_tax - checkout.getDiscountTotal();
        checkout.setGrandTotal(ConfigUtil.convertToBasePrice(grand_total));
        quote.setID(String.valueOf(ConfigUtil.getItemIdInCurrentTime()));
        checkout.setQuote(quote);

        // TODO: fake payment
        List<CheckoutPayment> listPayment = new ArrayList<>();
        CheckoutPayment payment = new PosCheckoutPayment();
        payment.setID("61");
        payment.setType("0");
        payment.setTitle("TH-Cash (VND)");
        payment.setIsDefault("1");
        payment.setCode("cashforpos");
        payment.setPaylater("0");
        payment.setIsReferenceNumber("1");
        listPayment.add(payment);
        checkout.setCheckoutPayment(listPayment);
        return checkout;
    }

    @Override
    public Checkout saveQuote(Checkout checkout, SaveQuoteParam quoteParam) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        float discount_percent;
        float discount_amount;
        if (quoteParam.getDiscountType().equals("%")) {
            discount_percent = quoteParam.getDiscountValue();
            discount_amount = (checkout.getGrandTotal() * quoteParam.getDiscountValue()) / 100;
        } else {
            discount_percent = (checkout.getGrandTotal() / quoteParam.getDiscountValue()) * 100;
            discount_amount = quoteParam.getDiscountValue();
        }
        checkout.setDiscountOffline(0 - discount_percent);
        checkout.setDiscountTotal(0 - discount_amount);
        return checkout;
    }

    @Override
    public Checkout addCouponToQuote(Checkout checkout, QuoteAddCouponParam quoteAddCouponParam) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public Checkout saveShipping(Checkout checkout, String quoteId, String shippingCode) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return checkout;
    }

    @Override
    public Checkout savePayment(String quoteId, String paymentCode) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public void updateCartItemWithServerRespone(Checkout oldCheckout, Checkout newCheckout) throws ParseException, InstantiationException, IllegalAccessException, IOException {

    }

    @Override
    public Model placeOrder(Checkout checkout, PlaceOrderParams placeOrderParams, List<CheckoutPayment> listCheckoutPayment) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_CHECK_OUT_PLACE_ORDER);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // set data
            List<PaymentEntity> listPaymentEntity = new ArrayList<>();
            List<ProductEntity> listProductEntity = new ArrayList<>();
            PlaceOrderEntity placeOrderEntity = new PlaceOrderEntity();
            float amount_paid;
            float amount_return;
            if (checkout.getExchangeMoney() > 0) {
                amount_paid = checkout.getGrandTotal() + checkout.getExchangeMoney();
                amount_return = checkout.getExchangeMoney();
            } else if (checkout.getRemainMoney() > 0) {
                amount_paid = checkout.getGrandTotal() - checkout.getRemainMoney();
                amount_return = checkout.getRemainMoney();
            } else {
                amount_paid = checkout.getGrandTotal();
                amount_return = 0;
            }
            placeOrderEntity.amount_paid = amount_paid;
            placeOrderEntity.creation_date = ConfigUtil.getCurrentDateTime();
            if (listCheckoutPayment != null && listCheckoutPayment.size() > 0) {
                for (CheckoutPayment payment : listCheckoutPayment) {
                    PaymentEntity paymentEntity = new PaymentEntity();
                    paymentEntity.amount = payment.getRealAmount();
                    paymentEntity.journal_id = payment.getID();
                    paymentEntity.name = ConfigUtil.getCurrentDateTime();
                    listPaymentEntity.add(paymentEntity);
                }
            }
            placeOrderEntity.statement_ids = listPaymentEntity;
            placeOrderEntity.amount_total = checkout.getGrandTotal();
            for (CartItem cartItem : checkout.getCartItem()) {
                Product product = cartItem.getProduct();
                ProductEntity productEntity = new ProductEntity();
                productEntity.product_id = product.getID();
                productEntity.qty = cartItem.getQuantity();
                productEntity.tax_ids = product.getTaxId();
                if (cartItem.isCustomPrice()) {
                    productEntity.price_unit = cartItem.getCustomPrice();
                    productEntity.discount = (cartItem.getCustomPrice() / cartItem.getDiscountAmount()) * 100;
                } else {
                    productEntity.price_unit = product.getFinalPrice();
                }
                listProductEntity.add(productEntity);
            }
            if (checkout.getDiscountTotal() > 0) {
                ProductEntity productDiscount = new ProductEntity();
                // TODO: fake data phải lấy discount_product_id theo pos
                productDiscount.product_id = "26";
                productDiscount.qty = 1;
                productDiscount.price_unit = checkout.getDiscountTotal();
                listProductEntity.add(productDiscount);
            }
            placeOrderEntity.partner_id = checkout.getCustomerID();
            placeOrderEntity.lines = listProductEntity;
            placeOrderEntity.amount_tax = checkout.getTaxTotal();
            placeOrderEntity.amount_return = amount_return;
            placeOrderEntity.is_create_invoice = checkout.getCreateInvoice().equals("1") ? true : false;

            // TODO: fake pos_id = 144 open
            placeOrderEntity.pos_session_id = "144";

            rp = statement.execute(placeOrderEntity);
            String data = rp.readResult2String();
            return null;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public boolean addOrderToListCheckout(Checkout checkout) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean removeOrderToListCheckout(Checkout checkout) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public String sendEmail(String email, String increment_id) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public boolean approvedAuthorizenet(String url, String params) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean invoicesPaymentAuthozire(String orderID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean cancelPaymentAuthozire(String orderID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public String approvedPaymentPayPal(String payment_id) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public String approvedAuthorizeIntergration(String quote_id, String token, float amount) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public String approvedStripe(String token, float amount) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public String getAccessTokenPaypalHere() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }
}
