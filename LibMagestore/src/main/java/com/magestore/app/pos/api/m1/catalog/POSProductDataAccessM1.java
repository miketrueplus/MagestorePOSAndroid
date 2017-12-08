package com.magestore.app.pos.api.m1.catalog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.connection.BitmapFileCacheConnection;
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
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.pos.model.catalog.PosProductOption;
import com.magestore.app.pos.model.catalog.PosProductOptionBundleItem;
import com.magestore.app.pos.model.catalog.PosProductOptionConfigOption;
import com.magestore.app.pos.model.catalog.PosProductOptionJsonConfigOptionPrice;
import com.magestore.app.pos.model.inventory.PosStock;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListProduct;
import com.magestore.app.util.ImageUtil;
import com.magestore.app.util.SecurityUtil;
import com.magestore.app.util.StringUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 8/4/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSProductDataAccessM1 extends POSAbstractDataAccessM1 implements ProductDataAccess {

    public class Gson2PosOptionParseModel extends Gson2PosAbstractParseImplement {
        @Override
        protected Gson createGson() {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization();
            builder.registerTypeAdapter(new TypeToken<PosProductOptionJsonConfigOptionPrice>() {
            }
                    .getType(), new ProductConfigOptionPriceConverter());
            builder.registerTypeAdapter(new TypeToken<Map<String, PosProductOptionConfigOption>>() {
            }
                    .getType(), new ConfigOptionConverter());
            builder.registerTypeAdapter(new TypeToken<List<PosStock>>() {
            }
                    .getType(), new GroupOptionConverter());
            builder.registerTypeAdapter(new TypeToken<List<PosProductOptionBundleItem>>() {
            }
                    .getType(), new BundleOptionConverter());
            return builder.create();
        }

        private static final String JSON_OPTION_ID = "optionId";
        private static final String JSON_OPTION_LABEL = "optionLabel";

        public class ConfigOptionConverter implements JsonDeserializer<Map<String, PosProductOptionConfigOption>> {
            @Override
            public Map<String, PosProductOptionConfigOption> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonArray arr_config = json.getAsJsonArray();
                Map<String, PosProductOptionConfigOption> mapConfigOption = new HashMap<>();
                if (arr_config != null && arr_config.size() > 0) {
                    JsonObject json_color = arr_config.get(0).getAsJsonObject();
                    // khởi tạo product config
                    PosProductOptionConfigOption configOptionColor = new PosProductOptionConfigOption();
                    configOptionColor.optionId = json_color.remove(JSON_OPTION_ID).getAsString();
                    configOptionColor.optionLabel = json_color.remove(JSON_OPTION_LABEL).getAsString();
                    // gán cho option Values
                    configOptionColor.optionValues = new HashMap<String, String>();
                    for (Map.Entry<String, JsonElement> item : json_color.entrySet())
                        configOptionColor.optionValues.put(item.getKey(), item.getValue().getAsString());
                    mapConfigOption.put("color", configOptionColor);
                    if (arr_config.size() > 1) {
                        JsonObject json_size = arr_config.get(1).getAsJsonObject();
                        // khởi tạo product config
                        PosProductOptionConfigOption configOptionSize = new PosProductOptionConfigOption();
                        configOptionSize.optionId = json_size.remove(JSON_OPTION_ID).getAsString();
                        configOptionSize.optionLabel = json_size.remove(JSON_OPTION_LABEL).getAsString();
                        // gán cho option Values
                        configOptionSize.optionValues = new HashMap<String, String>();
                        for (Map.Entry<String, JsonElement> item : json_size.entrySet())
                            configOptionSize.optionValues.put(item.getKey(), item.getValue().getAsString());

                        mapConfigOption.put("size", configOptionSize);
                    }
                }

                return mapConfigOption;
            }
        }

        /**
         * Convert đoạn json
         * {"oldPrice":{"amount":"52"},"basePrice":{"amount":"48.036950501155"},"finalPrice":{"amount":"52"}}
         * Sang PosProductConfigOptionPrice
         */
        private static final String PRICE_BASE = "basePrice";
        private static final String PRICE_FINAL = "finalPrice";
        private static final String PRICE_AMOUNT = "amount";
        private static final String PRICE_OLD = "oldPrice";

        public class ProductConfigOptionPriceConverter implements JsonDeserializer<PosProductOptionJsonConfigOptionPrice> {
            public PosProductOptionJsonConfigOptionPrice deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
                PosProductOptionJsonConfigOptionPrice optionPrice = new PosProductOptionJsonConfigOptionPrice();
                if (json.getAsJsonObject().get(PRICE_BASE).getAsJsonObject().has(PRICE_AMOUNT)) {
                    String price = json.getAsJsonObject().get(PRICE_BASE).getAsJsonObject().get(PRICE_AMOUNT).toString();
                    price = price.replaceAll("\"", "");
                    optionPrice.setBasePrice(price == null || price.trim().equals("") ? 0 : Float.parseFloat(price));
                }
                if (json.getAsJsonObject().get(PRICE_FINAL).getAsJsonObject().has(PRICE_AMOUNT)) {
                    String price = json.getAsJsonObject().get(PRICE_FINAL).getAsJsonObject().get(PRICE_AMOUNT).toString();
                    price = price.replaceAll("\"", "");
                    optionPrice.setFinalPrice(price == null || price.trim().equals("") ? 0 : Float.parseFloat(price));
                }
                if (json.getAsJsonObject().get(PRICE_OLD).getAsJsonObject().has(PRICE_AMOUNT)) {
                    String price = json.getAsJsonObject().get(PRICE_OLD).getAsJsonObject().get(PRICE_AMOUNT).toString();
                    price = price.replaceAll("\"", "");
                    optionPrice.setOldPrice(price == null || price.trim().equals("") ? 0 : Float.parseFloat(price));
                }
                return optionPrice;
            }
        }

        public class GroupOptionConverter implements JsonDeserializer<List<PosStock>> {
            @Override
            public List<PosStock> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                List<PosStock> listStock = new ArrayList<>();
                JsonArray arr_group = json.getAsJsonArray();
                if (arr_group != null && arr_group.size() > 0) {
                    for (JsonElement el_stock : arr_group) {
                        if (el_stock.isJsonObject()) {
                            JsonObject obj_stock = el_stock.getAsJsonObject();
                            PosStock stock = gson.fromJson(obj_stock, PosStock.class);
                            listStock.add(stock);
                        }
                    }
                    return listStock;
                }
                return null;
            }
        }

        public class BundleOptionConverter implements JsonDeserializer<List<PosProductOptionBundleItem>> {
            @Override
            public List<PosProductOptionBundleItem> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                List<PosProductOptionBundleItem> listItem = new ArrayList<>();
                JsonObject object = json.getAsJsonObject();
                for (Map.Entry<String, JsonElement> item : object.entrySet()) {
                    JsonObject obj_item = item.getValue().getAsJsonObject();
                    PosProductOptionBundleItem bundleItem = gson.fromJson(obj_item, PosProductOptionBundleItem.class);
                    listItem.add(bundleItem);
                }
                return listItem;
            }
        }
    }

    @Override
    public Bitmap retrieveBitmap(Product product, int sizeWidth, int sizeHeight) throws IOException {
        // chuẩn bị cache
        BitmapFileCacheConnection cacheConnection = new BitmapFileCacheConnection();
        cacheConnection.setCacheName(product.getID() + SecurityUtil.getHash(product.getImage()));
        cacheConnection.setReloadCacheLater(false);
        cacheConnection.setBmpURL(product.getImage());

        // chuẩn bị input stream và bitmap option
        InputStream inputStream = null;
        Rect pad = new Rect();
        BitmapFactory.Options options = new BitmapFactory.Options();

        // load kích thước ảnh trước
        try {
            inputStream = new BufferedInputStream(cacheConnection.execute());
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, pad, options);
            options.inSampleSize = ImageUtil.calculateInSampleSize(options.outWidth, options.outHeight, sizeWidth, sizeHeight);
            options.inJustDecodeBounds = false;

        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            inputStream = null;
        }

        // Nếu ảnh quá to thì lấy kích thước bé hơn, đặt lại sample size
        try {
            inputStream = new BufferedInputStream(cacheConnection.execute());
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(inputStream, pad, options);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            inputStream = null;
        }
        return null;
    }

    @Override
    public ProductOption loadProductOption(Product product) throws IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_PRODUCT_GET_OPTION);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setParam("id", product.getID())
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành json
            rp = statement.execute();
            String json = rp.readResult2String();
            json = StringUtil.truncateJsonOption(json);

            // tạo json implement
            Gson2PosOptionParseModel implement = new Gson2PosOptionParseModel();
            Gson gson = implement.createGson();
            PosProductOption productOption = gson.fromJson(json, PosProductOption.class);

            // return
            product.setProductOption(productOption);
            return productOption;
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
    public List<Product> retrieve(String categoryId, String searchString, int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_PRODUCT_GET_LISTING);

            if (!StringUtil.isNullOrEmpty(searchString)) {
                String finalSearchString = "%" + searchString + "%";
                statement.getParamBuilder().setFilterOrLike("name", finalSearchString);
                statement.getParamBuilder().setFilterOrLike("sku", finalSearchString);
            } else {
                // TODO: tạm thời để search all
                statement.getParamBuilder().setFilterEqual("category_ids", categoryId);
            }
            paramBuilder = statement.getParamBuilder()
                    .setPage(currentPage)
                    .setPageSize(pageSize)
                    .setSortOrderASC("name")
                    .setParam("show_out_stock", "1")
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

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
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_PRODUCT_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1)
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_PRODUCT_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderASC("name")
                    .setParam("show_out_stock", "1")
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();
            List<Product> list = (List<Product>) (List<?>) (listProduct.items);

            // đọc nốt thông tin về product option
//            loadProductOption(list);

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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_PRODUCT_GET_LISTING);


            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setFilterOrLike("name", finalSearchString)
                    .setFilterOrLike("sku", finalSearchString)
                    .setSortOrderASC("name")
                    .setParam("show_out_stock", "1")
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();
            List<Product> list = (List<Product>) (List<?>) (listProduct.items);

            // đọc nốt thông tin về product option
//            loadProductOption(list);

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
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_PRODUCT_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1)
                    .setSortOrderASC("name")
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID)
                    .setFilterEqual("entity_id", strID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();
            List<Product> list = (List<Product>) (List<?>) (listProduct.items);

            // return
            if (list.size() >= 1)
                return list.get(0);
            else
                return null;
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_PRODUCT_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID)
                    .setParam("show_out_stock", "1");

            String listProductId = "";
            for (String id : Ids) {
                listProductId += id + ",";
            }
            paramBuilder.setFilterIn("entity_id", listProductId);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();
            List<Product> list = (List<Product>) (List<?>) (listProduct.items);

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
    public List<Product> getAvailableQty(String Id) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
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
}
