package com.magestore.app.pos.api.m2.sales;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.connection.http.MagestoreStatementAction;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartDeleteItemParam;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.resourcemodel.sales.CartDataAccess;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.checkout.PosCheckout;
import com.magestore.app.pos.model.checkout.cart.PosCartDeleteItemParam;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Johan on 3/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCartDataAccess extends POSAbstractDataAccess implements CartDataAccess {
    @Override
    public boolean delete(Checkout checkout, CartItem cartItem) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.setAction(MagestoreStatementAction.ACTION_DELETE);
            statement.prepareQuery(POSAPI.REST_CART_DELETE_ITEM);
            statement.setParam(POSAPI.CART_QUOTE_ID, checkout.getQuoteId());
            statement.setParam(POSAPI.CART_ITEM_ID, cartItem.getItemId());

            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);


//            CartDeleteItemParam cartDeleteItemParam = new PosCartDeleteItemParam();
//            cartDeleteItemParam.setQuoteId(checkout.getQuoteId());
//            cartDeleteItemParam.setItemId(product.getItemId());
            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosCheckout.class);

            Checkout checkoutRespone = (Checkout) rp.doParse();
            CartItem cartItemResponse = null;
            if (checkoutRespone != null) {
                checkout.setTotals(checkoutRespone.getTotals());
                return true;
            } else {
                return false;
            }
        } catch (ConnectionException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }
}
