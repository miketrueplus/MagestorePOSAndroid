package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderShippingAddress;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Quản lý order shipping address
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderShippingAddress extends PosAbstractModel implements OrderShippingAddress {
    String address_type;
    String city;
    String country_id;
    String customer_address_id;
    String email;
    String entity_id;
    String firstname;
    String lastname;
    String parent_id;
    String postcode;
    List<String> street;
    String region;
    String region_code;
    String region_id;
    String telephone;

    @Override
    public String getID() {
        return entity_id;
    }

    @Override
    public String getAddressType() {
        return address_type;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
