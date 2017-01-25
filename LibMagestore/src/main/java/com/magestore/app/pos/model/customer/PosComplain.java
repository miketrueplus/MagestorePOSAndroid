package com.magestore.app.pos.model.customer;

import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Mike on 1/18/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class PosComplain extends PosAbstractModel implements Complain {
    Customer ref_customer;
    String complain_id;
    String customer_id;
    String content;
    String create_at;
    String customer_email;

    @Override
    public String getCustomerID() {
        return customer_id;
    }

    @Override
    public String getComplainID() {
        return complain_id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getCreateAt() {
        return create_at;
    }

    @Override
    public Customer getCustomer() {
        return ref_customer;
    }

    @Override
    public void setCustomerID(String cusID) {
        this.customer_id = cusID;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.ref_customer = customer;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setCreateAt(String createAt) {
        this.create_at = createAt;
    }

    @Override
    public void setComplainID(String complainID) {
        this.complain_id = complainID;
    }

    @Override
    public String getID() {
        return this.complain_id;
    }

    @Override
    public String getCustomerEmail() {
        return customer_email;
    }

    @Override
    public void setCustomerEmail(String strEmail) {
        customer_email = strEmail;
    }
}
