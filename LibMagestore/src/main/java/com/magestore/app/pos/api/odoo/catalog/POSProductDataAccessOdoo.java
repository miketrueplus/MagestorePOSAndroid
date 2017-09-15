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
import com.magestore.app.pos.model.catalog.PosProduct;
import com.magestore.app.pos.model.catalog.PosProductTaxDetailOdoo;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
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
            builder.registerTypeAdapter(new TypeToken<List<PosProduct>>() {
            }
                    .getType(), new ConfigProductConverter());
            return builder.create();
        }

        private String PRODUCT_ID = "id";
        private String PRODUCT_NAME = "name";
        private String PRODUCT_SKU = "default_code";
        private String PRODUCT_TYPE = "type";
        private String PRODUCT_MAX_QUANTITY = "qty_available";
        private String TYPE_PRODUCT = "product";
        private String PRODUCT_DESCRIPTION = "description";
        private String PRODUCT_IMAGE = "image";
        private String PRODUCT_PRICE = "lst_price";
        private String PRODUCT_TAX_ID = "taxes_id";
        private String PRODUCT_TAX_DETAIL = "taxes_detail";
        private String PRODUCT_TAX_DETAIL_ID = "id";
        private String PRODUCT_TAX_DETAIL_NAME = "name";
        private String PRODUCT_TAX_DETAIL_AMOUNT = "amount";

        public class ConfigProductConverter implements JsonDeserializer<List<PosProduct>> {
            @Override
            public List<PosProduct> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                List<PosProduct> listProduct = new ArrayList<>();
                if (json.isJsonArray()) {
                    JsonArray arr_product = json.getAsJsonArray();
                    if (arr_product != null && arr_product.size() > 0) {
                        for (JsonElement el_product : arr_product) {
                            JsonObject obj_product = el_product.getAsJsonObject();
                            PosProduct product = new PosProduct();
                            String id = obj_product.remove(PRODUCT_ID).getAsString();
                            product.setID(id);
                            if (obj_product.has(PRODUCT_NAME)) {
                                String name = obj_product.remove(PRODUCT_NAME).getAsString();
                                product.setName(StringUtil.checkJsonData(name) ? name : "");
                            }
                            String default_code = obj_product.remove(PRODUCT_SKU).getAsString();
                            product.setSKU(StringUtil.checkJsonData(default_code) ? default_code : "");
                            String type = obj_product.remove(PRODUCT_TYPE).getAsString();
                            product.setTypeID(type);
                            float max_qty = obj_product.remove(PRODUCT_MAX_QUANTITY).getAsFloat();
                            if (product.getTypeID().equals(TYPE_PRODUCT)) {
                                if (max_qty > 0) {
                                    product.setInStock(true);
                                } else {
                                    product.setInStock(false);
                                }
                            } else {
                                product.setInStock(true);
                            }
                            if (obj_product.has(PRODUCT_DESCRIPTION)) {
                                String description = obj_product.remove(PRODUCT_DESCRIPTION).getAsString();
                                product.setDescription(StringUtil.checkJsonData(description) ? description : "");
                            }
                            String image = obj_product.remove(PRODUCT_IMAGE).getAsString();
                            product.setImage(image);

                            // TODO:  hiện tại fix tạm giá sản phẩm
                            float price = obj_product.remove(PRODUCT_PRICE).getAsFloat();
                            product.setPrice(price);
                            product.setFinalPrice(price);

                            List<String> listTaxId = new ArrayList<>();
                            JsonArray arr_tax_id = obj_product.getAsJsonArray(PRODUCT_TAX_ID);
                            if (arr_tax_id != null && arr_tax_id.size() > 0) {
                                for (JsonElement el_tax_id : arr_tax_id) {
                                    String tax_id = el_tax_id.getAsString();
                                    listTaxId.add(tax_id);
                                }
                            }
                            product.setTaxId(listTaxId);

                            if (obj_product.has(PRODUCT_TAX_DETAIL)) {
                                List<ProductTaxDetailOdoo> listTaxDetail = new ArrayList<>();
                                JsonArray arr_tax_detail = obj_product.getAsJsonArray(PRODUCT_TAX_DETAIL);
                                float total_tax = 0;
                                for (JsonElement el_tax_detail : arr_tax_detail) {
                                    JsonObject obj_tax_detail = el_tax_detail.getAsJsonObject();
                                    ProductTaxDetailOdoo taxDetailOdoo = new PosProductTaxDetailOdoo();
                                    String tax_detail_id = obj_tax_detail.remove(PRODUCT_TAX_DETAIL_ID).getAsString();
                                    taxDetailOdoo.setID(tax_detail_id);
                                    String tax_detail_name = obj_tax_detail.remove(PRODUCT_TAX_DETAIL_NAME).getAsString();
                                    taxDetailOdoo.setName(StringUtil.checkJsonData(tax_detail_name) ? tax_detail_name : "");
                                    float tax_detail_amount = obj_tax_detail.remove(PRODUCT_TAX_DETAIL_AMOUNT).getAsFloat();
                                    taxDetailOdoo.setAmount(tax_detail_amount);
                                    total_tax += tax_detail_amount;
                                    listTaxDetail.add(taxDetailOdoo);
                                }
                                product.setTotalTax(total_tax);
                                product.setTaxDetail(listTaxDetail);
                            }

                            listProduct.add(product);
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
            rp.setParseImplement(new Gson2PosProductParseModel());
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
            rp.setParseImplement(new Gson2PosProductParseModel());
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
            rp.setParseImplement(new Gson2PosProductParseModel());
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
            rp.setParseImplement(new Gson2PosProductParseModel());
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
            rp.setParseImplement(new Gson2PosProductParseModel());
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
