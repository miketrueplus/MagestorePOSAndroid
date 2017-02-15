package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.ArrayList;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCheckoutShipping extends PosAbstractModel implements CheckoutShipping {
    String method;
    String title;
    String description;
    String error_message;
    String price_type;
    float price;
    String datetime;
    ArrayList<Model> tracks;
    CustomerAddress address;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getErrorMessage() {
        return error_message;
    }

    @Override
    public String getPriceType() {
        return price_type;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public void setMethod(String strMethod) {
        this.method = strMethod;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public void setTrack(ArrayList<Model> tracks) {
        this.tracks = tracks;
    }

    @Override
    public ArrayList<Model> getTracks() {
        return tracks;
    }

    @Override
    public String getDatetime() {
        return this.datetime;
    }

    @Override
    public void setDatetime(String strDatetime) {
        this.datetime = strDatetime;
    }

    @Override
    public void setAddress(CustomerAddress address) {
        this.address = address;
    }

    @Override
    public CustomerAddress getAddress() {
        return this.address;
    }
}
