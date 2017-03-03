package com.magestore.app.pos.api.m2.catalog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.google.gson.Gson;
import com.magestore.app.lib.connection.BitmapFileCacheConnection;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.catalog.ProductDataAccess;
import com.magestore.app.pos.model.catalog.PosProductOption;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListProduct;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.parse.gson2pos.Gson2PosProductOptionParseImplement;
import com.magestore.app.util.ImageUtil;
import com.magestore.app.util.SecurityUtil;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @Override
    public ProductOption loadProductOption(Product product) throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_PRODUCT_GET_OPTION);
            statement.setParam(POSAPI.PARAM_PRODUCT_ID, product.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
//                    .setPage(page)
//                    .setPageSize(pageSize)
//                .setFilterLike("name", "%Backyard%")
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
//                .setFilterEqual("name", "Joust Duffle Bag");

            // thực thi truy vấn và parse kết quả thành json
            rp = statement.execute();
            String json = rp.readResult2String()
                    .replace("\\", "")
//                    .replace("\\\\\"", "\\\"")
//                    .replace("\\\"", "\"")
                    .replace("\"{\"", "{\"")
                    .replace("}\"", "}");

            // tạo json implement
            Gson2PosProductOptionParseImplement implement = new Gson2PosProductOptionParseImplement();
            Gson gson = implement.createGson();
            PosProductOption productOption = gson.fromJson(json, PosProductOption.class);

            // return
            product.setProductOption(productOption);
            return productOption;
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

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null)statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public void loadProductOption(List<Product> listProduct) {
//        Gson2PosProductParseImplement implement = new Gson2PosProductParseImplement();
//        Gson gson = implement.createGson();
//        for (Product product: listProduct) {
//            if (product.getJsonConfigOption() == null || "".equals(product.getJsonConfigOption().trim())) continue;
//            PosProductOption productOption = gson.fromJson(product.getJsonConfigOption(), PosProductOption.class);
//            product.setProductOption((ProductOption) productOption);
//        }
    }

    /**
     * Sắp xếp lại các biến trong product option để thuận tiện truy cập hơn
     */
    public void arrangeProductOption(PosProductOption productOption) {
        // duỵet từng nhóm thuộc tính
//        for (PosProductConfigOptionAttributes attribute : productOption.attributes.values()) {
//            // duyệt từng option trong thuộc tính
//            for (PosProductConfigOptionAttributes.Attributes option: attribute.options ) {
//                option.images = new ArrayList<PosProductOptionJsonConfigOptionImage>();
//                option.images.add(attribute.images.get())
//            }
//        }
    }

    /**
     * Trả về list các product
     * @param pageSize Số product trên 1 page
     * @param page Page hiện tại
     * @return Danh sách product
     * @throws ParseException
     * @throws ConnectionException
     * @throws DataAccessException
     * @throws IOException
     */
    @Override
    public List<Product> retrieve(int page, int pageSize) throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_PRODUCT_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                .setPage(page)
                .setPageSize(pageSize)
//                .setFilterLike("name", "%Backyard%")
                .setSessionID(POSDataAccessSession.REST_SESSION_ID);
//                .setFilterEqual("name", "Joust Duffle Bag");

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();
            List<Product> list = (List<Product>)(List<?>) (listProduct.items);

            // đọc nốt thông tin về product option
//            loadProductOption(list);

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

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null)statement.close();
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_PRODUCT_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1)
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
//                .setFilterEqual("name", "Joust Duffle Bag");

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();

            // return
            return listProduct.total_count;
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

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null)statement.close();
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
    public Product retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_PRODUCT_GET);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1)
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID)
                    .setFilterEqual("product_id", strID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();
            List<Product> list = (List<Product>)(List<?>) (listProduct.items);

            // đọc nốt thông tin về product option
            loadProductOption(list);

            // return
            if (list.size() >= 1)
                return list.get(0);
            else
                return null;
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

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null)statement.close();
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_PRODUCT_GET_LISTING);


                paramBuilder = statement.getParamBuilder()
                        .setPage(page)
                        .setPageSize(pageSize)
                        .setFilterLike("name", finalSearchString)
                        .setFilterLike("sku", finalSearchString)

//                        .setFilterLike("sku", finalSearchString)
                        .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();
            List<Product> list = (List<Product>)(List<?>) (listProduct.items);

            // đọc nốt thông tin về product option
//            loadProductOption(list);

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

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null)statement.close();
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_PRODUCT_GET_LISTING);


                paramBuilder = statement.getParamBuilder()
                        .setPage(currentPage)
                        .setPageSize(pageSize)
                        .setFilterEqual("category_id", categoryId)
                        .setSessionID(POSDataAccessSession.REST_SESSION_ID);


            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();
            List<Product> list = (List<Product>)(List<?>) (listProduct.items);

            // đọc nốt thông tin về product option
            loadProductOption(list);

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

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null)statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
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
}
