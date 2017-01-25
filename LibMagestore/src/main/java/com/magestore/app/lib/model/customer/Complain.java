package com.magestore.app.lib.model.customer;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 1/6/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface Complain extends Model {
    String getCustomerID();
    String getComplainID();
    String getContent();
    String getCreateAt();
    Customer getCustomer();

    void setCustomerID(String cusID);
    void setCustomer(Customer customer);
    void setContent(String content);
    void setCreateAt(String createAt);
    void setComplainID(String complainID);

    String getCustomerEmail();
    void setCustomerEmail(String strEmail);
}
