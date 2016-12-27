package com.magestore.app.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.magestore.app.lib.entity.Customer;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.entity.pos.PosProduct;
import com.magestore.app.lib.gateway.CustomerGateway;
import com.magestore.app.lib.gateway.GatewayFactory;
import com.magestore.app.lib.gateway.ProductGateway;
import com.magestore.app.lib.gateway.UserGateway;
import com.magestore.app.lib.gateway.pos.POSCustomerGateway;
import com.magestore.app.lib.gateway.pos.POSGatewayFactory;
import com.magestore.app.lib.gateway.pos.POSGatewaySession;
import com.magestore.app.lib.parse.gson2pos.Gson2PosListProduct;
import com.magestore.app.lib.usecase.pos.POSUserUseCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class MagestoreGatewayTest {
    @Test
    public void gateway_isCorrect() throws Exception {
        // Khởi tạo product gateway factory
        GatewayFactory factory = GatewayFactory.getFactory(POSGatewayFactory.class);
        ProductGateway product = factory.generateProductGateway();
        UserGateway user = factory.generateUserGateway();

        // Lấy list 30 products đầu tiên
        String strSession = user.login("http://demo-magento2.magestore.com/webpos", "demo", "demo123");
        POSUserUseCase.session = new POSGatewaySession();
        POSUserUseCase.session.REST_SESSION_ID = strSession.trim().replace("\"", "");
        List<Product> list = product.getProducts(1, 1);
        return;
    }

    @Test
    public void test_customer_gateway_isCorrect() throws Exception {
        // Khởi tạo product gateway factory
        GatewayFactory factory = GatewayFactory.getFactory(POSGatewayFactory.class);
        CustomerGateway customer = factory.generateCustomerGateway();
        UserGateway user = factory.generateUserGateway();

        // Lấy list 30 customer đầu tiên
        String strSession = user.login("http://demo-magento2.magestore.com/webpos", "demo", "demo123");
        POSUserUseCase.session = new POSGatewaySession();
        POSUserUseCase.session.REST_SESSION_ID = strSession.trim().replace("\"", "");
        List<Customer> list = customer.getCustomers(20, 1);
        return;
    }
}
