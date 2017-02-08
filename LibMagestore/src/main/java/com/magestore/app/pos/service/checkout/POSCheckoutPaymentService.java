package com.magestore.app.pos.service.checkout;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.service.checkout.CheckoutPaymentService;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 2/8/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCheckoutPaymentService extends AbstractService implements CheckoutPaymentService {
    @Override
    public CheckoutPayment create(Checkout checkout) {
        return null;
    }

    @Override
    public int count(Checkout checkout) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 0;
    }

    @Override
    public CheckoutPayment retrieve(Checkout checkout, String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<CheckoutPayment> retrieve(Checkout checkout, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public List<CheckoutPayment> retrieve(Checkout checkout) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public List<CheckoutPayment> retrieve(Checkout checkout, String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public boolean update(Checkout checkout, CheckoutPayment oldModel, CheckoutPayment newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public boolean insert(Checkout checkout, CheckoutPayment... childs) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public boolean delete(Checkout checkout, CheckoutPayment... childs) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }
}
