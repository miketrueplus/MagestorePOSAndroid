package com.magestore.app.pos.service.checkout;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.PaymentMethod;
import com.magestore.app.lib.model.checkout.Shipping;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.sales.CheckoutDataAccess;
import com.magestore.app.lib.service.checkout.CheckoutService;
import com.magestore.app.pos.model.checkout.PosCheckout;
import com.magestore.app.pos.model.checkout.PosPaymentMethod;
import com.magestore.app.pos.model.checkout.PosShipping;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCheckoutService extends AbstractService implements CheckoutService {
    @Override
    public boolean insert(Checkout... checkouts) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.insert(checkouts);
    }

    @Override
    public boolean delete(Checkout... checkouts) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 1;
    }

    @Override
    public Checkout create() {
        Checkout checkout = new PosCheckout();
        List<CartItem> cartItemList = new ArrayList<CartItem>();
        checkout.setCartItem(cartItemList);
        return checkout;
    }

    @Override
    public Checkout retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<Checkout> retrieve(int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        List<Checkout> checkoutList = new ArrayList<Checkout>();
        checkoutList.add(create());
        return checkoutList;
    }

    @Override
    public List<Checkout> retrieve() throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return retrieve(1, 1);
    }

    @Override
    public List<Checkout> retrieve(String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return retrieve(1, 1);
    }

    @Override
    public boolean update(Checkout oldModel, Checkout newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public PaymentMethod createPaymentMethod() {
        return new PosPaymentMethod();
    }

    @Override
    public Shipping createShipping() {
        return new PosShipping();
    }
}