package com.magestore.app.pos.api.odoo.catalog;

import android.graphics.Bitmap;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.catalog.DataProduct;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.model.catalog.ProductTaxDetailOdoo;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.catalog.ProductDataAccess;
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.pos.api.odoo.POSDataAccessSessionOdoo;
import com.magestore.app.pos.model.catalog.PosDataProduct;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListProductParseModelOdoo;
import com.magestore.app.util.StringUtil;
import java.io.IOException;
import java.util.List;

/**
 * Created by Johan on 9/6/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSProductDataAccessOdoo extends POSAbstractDataAccessOdoo implements ProductDataAccess {

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_PRODUCT_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListProductParseModelOdoo());
            rp.setParseModel(PosDataProduct.class);
            DataProduct dataProduct = (DataProduct) rp.doParse();
            // return
            return dataProduct.getTotalCount();
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
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

    @Override
    public List<Product> retrieve() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return retrieve(1, 500);
    }

    @Override
    public List<Product> retrieve(int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_PRODUCT_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderASC("name");

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListProductParseModelOdoo());
            rp.setParseModel(PosDataProduct.class);
            DataProduct dataProduct = (DataProduct) rp.doParse();
            List<Product> list = dataProduct.getItems();

            // return
            return list;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
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

    @Override
    public List<Product> retrieve(String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        String finalSearchString = "%" + searchString + "%";
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_PRODUCT_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setFilterOrLike("name", finalSearchString)
                    .setFilterOrLike("barcode", finalSearchString)
                    .setFilterOrLike("default_code", finalSearchString)
                    .setSortOrderASC("name");

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListProductParseModelOdoo());
            rp.setParseModel(PosDataProduct.class);
            DataProduct dataProduct = (DataProduct) rp.doParse();
            List<Product> list = dataProduct.getItems();

            // return
            return list;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
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

    @Override
    public Product retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public boolean update(Product oldModel, Product newModel) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean insert(Product... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean delete(Product... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public Bitmap retrieveBitmap(Product product, int sizeWidth, int sizeHeight) throws IOException {
        return null;
    }

    @Override
    public ProductOption loadProductOption(Product product) throws IOException {
        return null;
    }

    @Override
    public List<Product> retrieve(String categoryId, String searchString, int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_PRODUCT_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            if (!StringUtil.isNullOrEmpty(searchString)) {
                String finalSearchString = "%" + searchString + "%";
                statement.getParamBuilder().setFilterOrLike("name", finalSearchString);
                statement.getParamBuilder().setFilterOrLike("barcode", finalSearchString);
                statement.getParamBuilder().setFilterOrLike("default_code", finalSearchString);
            } else {
                // TODO: tạm thời để search all
                statement.getParamBuilder().setFilterOrEqual("pos_categ_id", categoryId);
            }
            paramBuilder = statement.getParamBuilder()
                    .setPage(currentPage)
                    .setPageSize(pageSize)
                    .setSortOrderASC("name");

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListProductParseModelOdoo());
            rp.setParseModel(PosDataProduct.class);
            DataProduct dataProduct = (DataProduct) rp.doParse();
            List<Product> list = dataProduct.getItems();

            // return
            return list;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
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

    @Override
    public List<Product> retrieve(List<String> Ids) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_PRODUCT_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            String listProductId = "";
            for (String id : Ids) {
                listProductId += id + ",";
            }
            if (listProductId.length() > 0) {
                listProductId = listProductId.substring(0, listProductId.length() - 1);
            }
            paramBuilder = statement.getParamBuilder()
                    .setFilterIn("id", listProductId);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListProductParseModelOdoo());
            rp.setParseModel(PosDataProduct.class);
            DataProduct dataProduct = (DataProduct) rp.doParse();
            List<Product> list = dataProduct.getItems();

            // return
            return list;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
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
