package com.magestore.app.pos.api.m2.catalog;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.connection.http.MagestoreConnection;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.catalog.ProductDataAccess;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListProduct;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Gọi các API của Product
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSProductDataAccess extends POSAbstractDataAccess implements ProductDataAccess {
    public POSProductDataAccess() {
        super();
    }

    /**
     * Trả về list các product
     * @param pageSize Số product trên 1 page
     * @param currentPage Page hiện tại
     * @return Danh sách product
     * @throws ParseException
     * @throws ConnectionException
     * @throws DataAccessException
     * @throws IOException
     */
    @Override
    public List<Product> getProducts(int pageSize, int currentPage) throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_PRODUCT_GET_LISTING);
            statement.setParam(POSAPI.PARAM_CURRENT_PAGE, currentPage);
            statement.setParam(POSAPI.PARAM_PAGE_SIZE, pageSize);
            statement.setParam(POSAPI.PARAM_SESSION_ID, POSDataAccessSession.REST_SESSION_ID);

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
//    public List<Product> getProducts(int pageSize, int currentPage) throws ParseException, ConnectionException, DataAccessException, IOException {
//        Gson2PosListProduct listProduct = (Gson2PosListProduct) doAPI(Gson2PosListProduct.class,
//                POSDataAccessSession.REST_GET_PRODUCT_LISTING,
//                null,
//                POSDataAccessSession.PARAM_CURRENT_PAGE, ""+currentPage,
//                POSDataAccessSession.PARAM_PAGE_SIZE, ""+pageSize,
//                POSDataAccessSession.PARAM_SESSION_ID, POSAPI.REST_DEMO_SESSION_ID
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
