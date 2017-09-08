package com.magestore.app.pos.api.odoo.catalog;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.catalog.ProductDataAccess;
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.pos.api.odoo.POSDataAccessSessionOdoo;
import com.magestore.app.pos.model.catalog.PosProduct;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListProduct;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 9/6/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSProductDataAccessOdoo extends POSAbstractDataAccessOdoo implements ProductDataAccess {

    public class Gson2PosProductParseModel extends Gson2PosAbstractParseImplement {
        @Override
        protected Gson createGson() {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization();
            builder.registerTypeAdapter(new TypeToken<List<Product>>() {
            }
                    .getType(), new ConfigProductConverter());
            return builder.create();
        }

        private String PRODUCT_ID = "product_id";
        private String PRODUCT_NAME = "product_name";
        private String PRODUCT_SKU = "default_code";
        private String PRODUCT_TYPE = "type";
        public class ConfigProductConverter implements JsonDeserializer<List<Product>> {
            @Override
            public List<Product> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                List<Product> listProduct = new ArrayList<>();
                if (json.isJsonArray()) {
                    JsonArray arr_product = json.getAsJsonArray();
                    if (arr_product != null && arr_product.size() > 0) {
                        for (JsonElement el_product : arr_product) {
                            JsonObject obj_product = el_product.getAsJsonObject();
                            Product product = new PosProduct();
                            String id = obj_product.remove(PRODUCT_ID).getAsString();
                            product.setID(id);
                            String name = obj_product.remove(PRODUCT_NAME).getAsString();
                            product.setName(name);
                            String default_code = obj_product.remove(PRODUCT_SKU).getAsString();
                            product.setSKU(default_code);
                            String type = obj_product.remove(PRODUCT_TYPE).getAsString();
                            product.setTypeID(type);
                            
                        }
                    }
                }
                return listProduct;
            }
        }
    }

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
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();

            // return
            return listProduct.total_count;
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
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();
            List<Product> list = (List<Product>) (List<?>) (listProduct.items);

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
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();
            List<Product> list = (List<Product>) (List<?>) (listProduct.items);

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
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();
            List<Product> list = (List<Product>) (List<?>) (listProduct.items);

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
        return null;
    }
}
