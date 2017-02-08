package com.magestore.app.pos.service.checkout;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.service.checkout.CheckoutShippingService;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 2/8/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCheckoutShippingService extends AbstractService implements CheckoutShippingService {
    @Override
    public CheckoutShipping create(Checkout checkout) {
        return null;
    }

    @Override
    public int count(Checkout checkout) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 0;
    }

    @Override
    public CheckoutShipping retrieve(Checkout checkout, String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<CheckoutShipping> retrieve(Checkout checkout, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public List<CheckoutShipping> retrieve(Checkout checkout) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public List<CheckoutShipping> retrieve(Checkout checkout, String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public boolean update(Checkout checkout, CheckoutShipping oldModel, CheckoutShipping newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public boolean insert(Checkout checkout, CheckoutShipping... childs) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public boolean delete(Checkout checkout, CheckoutShipping... childs) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }
}
