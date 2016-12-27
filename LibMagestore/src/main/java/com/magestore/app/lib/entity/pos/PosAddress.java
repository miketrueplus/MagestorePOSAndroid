package com.magestore.app.lib.entity.pos;

import com.magestore.app.lib.entity.Address;

import java.util.List;

/**
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class PosAddress extends AbstractEntity implements Address {
    String customer_id;
    String region_id;
    String country_id;
    List<String> street;
    String telephone;
    String postcode;
    String city;
    String firstname;
    String lastname;
    String default_shipping;
    String default_billing;
}
