package com.magestore.app.lib.usecase.pos;

import com.magestore.app.lib.entity.Customer;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.gateway.CustomerGateway;
import com.magestore.app.lib.gateway.GatewayFactory;
import com.magestore.app.lib.gateway.ProductGateway;
import com.magestore.app.lib.gateway.pos.POSGatewayFactory;
import com.magestore.app.lib.usecase.CustomerUseCase;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class POSCustomerUseCase extends AbstractUseCase implements CustomerUseCase {
    Customer mCustomer;
    @Override
    public void setCustomer(Customer customer) {
        mCustomer = customer;
    }

    @Override
    public Customer getCustomer() {
        return mCustomer;
    }

    @Override
    public List<Customer> retrieveCustomerList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Khởi tạo customer gateway factory
        GatewayFactory factory = GatewayFactory.getFactory(POSGatewayFactory.class);
        CustomerGateway customerGateway = factory.generateCustomerGateway();

        // Lấy list 30 products đầu tiên
        return customerGateway.getCustomers(size, 1);
    }
}
