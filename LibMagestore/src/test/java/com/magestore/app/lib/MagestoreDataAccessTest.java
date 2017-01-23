package com.magestore.app.lib;

import android.util.Log;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.user.User;
import com.magestore.app.lib.resourcemodel.config.ConfigDataAccess;
import com.magestore.app.lib.resourcemodel.customer.CustomerDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.catalog.ProductDataAccess;
import com.magestore.app.lib.resourcemodel.registershift.RegisterShiftDataAccess;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.lib.resourcemodel.user.UserDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.user.PosUser;
import com.magestore.app.pos.service.user.POSUserService;

import org.junit.Test;

import java.util.List;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class MagestoreDataAccessTest {
    @Test
    public void test_product_gateway_isCorrect() throws Exception {
        User user = new PosUser();
        user.setUserName("demo");
        user.setPasswords("demo123");

        // Khởi tạo product gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(null);
        ProductDataAccess product = factory.generateProductDataAccess();
        UserDataAccess userDA = factory.generateUserDataAccess();

        // Lấy list 30 products đầu tiên
        String strSession = userDA.login("http://demo-magento2.magestore.com/webpos", user);
        POSUserService.session = new POSDataAccessSession();
        POSUserService.session.REST_SESSION_ID = strSession.trim().replace("\"", "");
        List<Product> list = product.getProducts(1, 1);
        return;
    }

    @Test
    public void test_customer_gateway_isCorrect() throws Exception {
        User user = new PosUser();
        user.setUserName("demo");
        user.setPasswords("demo123");

        // Khởi tạo product gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(new MagestoreContext());
        CustomerDataAccess customerGateway = factory.generateCustomerDataAccess();
        UserDataAccess userDA = factory.generateUserDataAccess();

        // Lấy list 30 customer đầu tiên
        String strSession = userDA.login("http://demo-magento2.magestore.com/webpos", user);
        POSUserService.session = new POSDataAccessSession();
        POSUserService.session.REST_SESSION_ID = strSession.trim().replace("\"", "");
        List<Customer> list = customerGateway.retrieveCustomers(20, 1);
        assert (list != null);
        assert (list.size() == 20);
        return;
    }

    @Test
    public void test_order_gateway_isCorrect() throws Exception {
        User user = new PosUser();
        user.setUserName("demo");
        user.setPasswords("demo123");

        // Khởi tạo order gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(new MagestoreContext());
        OrderDataAccess orderResourceModel = factory.generateOrderDataAccess();
        UserDataAccess userDA = factory.generateUserDataAccess();

        // Lấy list 30 order đầu tiên
        String strSession = userDA.login("http://demo-magento2.magestore.com/webpos", user);
        POSUserService.session = new POSDataAccessSession();
        POSUserService.session.REST_SESSION_ID = strSession.trim().replace("\"", "");

        List<Order> list = orderResourceModel.getOrders(20, 1);
        assert (list != null);
        assert (list.size() == 20);
        return;
    }

//    @Test
//    public void test_config_gateway_isCorrect() throws Exception {
        // Khởi tạo order gateway factory
//        DataAccessFactory factory = DataAccessFactory.getFactory(new MagestoreContext());
//        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();

//        UserDataAccess user = factory.generateUserDataAccess();

        // Lấy list 30 order đầu tiên
//        String strSession = user.login("http://demo-magento2.magestore.com/webpos", "demo", "demo123");
//        POSUserService.session = new POSDataAccessSession();
//        POSUserService.session.REST_SESSION_ID = strSession.trim().replace("\"", "");

//        Config config = configDataAccess.getConfig();
//        assert (config != null);
//        return;
//    }

    @Test
    public void test_register_shift_gateway_isCorrect() throws Exception {
        User user = new PosUser();
        user.setUserName("demo");
        user.setPasswords("demo123");

        // Khởi tạo order gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(new MagestoreContext());
        RegisterShiftDataAccess registerShiftDataAccess = factory.generateRegisterShiftDataAccess();
        UserDataAccess userDA = factory.generateUserDataAccess();

        // Lấy list 30 order đầu tiên
        String strSession = userDA.login("http://demo-magento2.magestore.com/webpos", user);
        POSUserService.session = new POSDataAccessSession();
        POSUserService.session.REST_SESSION_ID = strSession.trim().replace("\"", "");

        List<RegisterShift> list = registerShiftDataAccess.getRegisterShifts();
        assert (list != null);
        return;
    }

    @Test
    public void test_complain_listing_is_correct() throws Exception {
        User user = new PosUser();
        user.setUserName("demo");
        user.setPasswords("demo123");

        // Khởi tạo order gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(new MagestoreContext());
        CustomerDataAccess customerDataAccess = factory.generateCustomerDataAccess();
        UserDataAccess userDA = factory.generateUserDataAccess();

        // Lấy list 30 order đầu tiên
        String strSession = userDA.login("http://demo-magento2.magestore.com/webpos", user);
        POSUserService.session = new POSDataAccessSession();
        POSUserService.session.REST_SESSION_ID = strSession.trim().replace("\"", "");

        List<Complain> list = customerDataAccess.retrieveCustomerComplain(10, 1);
        assert (list != null);
        return;
    }

    @Test
    public void test_complain_listing_customer_is_correct() throws Exception {
        User user = new PosUser();
        user.setUserName("demo");
        user.setPasswords("demo123");

        // Khởi tạo order gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(new MagestoreContext());
        CustomerDataAccess customerDataAccess = factory.generateCustomerDataAccess();
        UserDataAccess userDA = factory.generateUserDataAccess();

        // Lấy list 30 order đầu tiên
        String strSession = userDA.login("http://demo-magento2.magestore.com/webpos", user);
        POSUserService.session = new POSDataAccessSession();
        POSUserService.session.REST_SESSION_ID = strSession.trim().replace("\"", "");

        List<Complain> list = customerDataAccess.retrieveCustomerComplain("1");
        assert (list != null);
        return;
    }
}
