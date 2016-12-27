package com.magestore.app.lib.gateway.pos;

import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.connection.http.MagestoreConnection;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.entity.pos.PosProduct;
import com.magestore.app.lib.gateway.GatewayException;
import com.magestore.app.lib.gateway.ProductGateway;
import com.magestore.app.lib.parse.ParseEntity;
import com.magestore.app.lib.parse.ParseFactory;
import com.magestore.app.lib.parse.ParseImplement;
import com.magestore.app.lib.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.lib.parse.gson2pos.Gson2PosListParseEntity;
import com.magestore.app.lib.parse.gson2pos.Gson2PosListProduct;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.List;

/**
 * Gọi các API của Product
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSProductGateway extends POSAbstractGateway implements ProductGateway {
    protected POSProductGateway() {
        super();
    }

    /**
     * Trả về list các product
     * @param pageSize Số product trên 1 page
     * @param currentPage Page hiện tại
     * @return Danh sách product
     * @throws ParseException
     * @throws ConnectionException
     * @throws GatewayException
     * @throws IOException
     */
    @Override
    public List<Product> getProducts(int pageSize, int currentPage) throws ParseException, ConnectionException, GatewayException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = MagestoreConnection.getConnection(POSGatewaySession.REST_BASE_URL, POSGatewaySession.REST_USER_NAME, POSGatewaySession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_GET_PRODUCT_LISTING);
            statement.setParam(POSAPI.PARAM_CURRENT_PAGE, currentPage);
            statement.setParam(POSAPI.PARAM_PAGE_SIZE, pageSize);
            statement.setParam(POSAPI.PARAM_SESSION_ID, POSGatewaySession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
//            String str = rp.readResult2String();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseEntity(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();
            List<Product> list = (List<Product>)(List<?>) (listProduct.items);

            // return
            return list;
        }
        catch (ConnectionException ex) {
            throw ex;
        }
        catch (IOException ex) {
            throw ex;
        }
        finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            // đóng statement
            if (statement != null)statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

//    @Override
//    public List<Product> getProducts(int pageSize, int currentPage) throws ParseException, ConnectionException, GatewayException, IOException {
//        Gson2PosListProduct listProduct = (Gson2PosListProduct) doAPI(Gson2PosListProduct.class,
//                POSGatewaySession.REST_GET_PRODUCT_LISTING,
//                null,
//                POSGatewaySession.PARAM_CURRENT_PAGE, ""+currentPage,
//                POSGatewaySession.PARAM_PAGE_SIZE, ""+pageSize,
//                POSGatewaySession.PARAM_SESSION_ID, POSAPI.REST_DEMO_SESSION_ID
//        );
//        List<Product> list = (List<Product>)(List<?>) (listProduct.items);
//        return list;
//    }

    @Override
    public void getProducts(Product product) {

    }

    @Override
    public void updateProduct(Product product) {

    }
}
