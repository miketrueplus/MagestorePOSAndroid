package com.magestore.app.pos.api.odoo.catalog;

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
import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.model.catalog.DataCategory;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.catalog.CategoryDataAccess;
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.pos.api.odoo.POSDataAccessSessionOdoo;
import com.magestore.app.pos.model.catalog.PosCategory;
import com.magestore.app.pos.model.catalog.PosDataCategory;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListCategory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 8/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCategoryDataAccessOdoo extends POSAbstractDataAccessOdoo implements CategoryDataAccess {
    public static List<Category> mListCategory;
    public static List<Category> mListDefaultCategory;

    public class Gson2PosCategoryParseModel extends Gson2PosAbstractParseImplement {
        @Override
        protected Gson createGson() {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization();
            builder.registerTypeAdapter(new TypeToken<List<PosCategory>>() {
            }
                    .getType(), new ConfigCategoryConverter());
            return builder.create();
        }

        private String CATEGORY_ID = "id";
        private String CATEGORY_NAME = "name";
        private String CATEGORY_PARENT_ID = "parent_id";
        private String CATEGORY_CHILD_ID = "child_id";

        public class ConfigCategoryConverter implements JsonDeserializer<List<PosCategory>> {
            @Override
            public List<PosCategory> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                List<PosCategory> listCategory = new ArrayList<>();
                if (json.isJsonArray()) {
                    JsonArray arr_category = json.getAsJsonArray();
                    if (arr_category != null && arr_category.size() > 0) {
                        for (JsonElement el_category : arr_category) {
                            JsonObject obj_category = el_category.getAsJsonObject();
                            PosCategory category = new PosCategory();
                            String id = obj_category.remove(CATEGORY_ID).getAsString();
                            category.setId(id);
                            String name = obj_category.remove(CATEGORY_NAME).getAsString();
                            category.setName(name);
                            JsonElement el_parent_id = obj_category.get(CATEGORY_PARENT_ID);
                            if (el_parent_id.isJsonArray()) {
                                JsonArray arr_parent_id = el_parent_id.getAsJsonArray();
                                if (arr_parent_id != null && arr_parent_id.size() > 0) {
                                    int parent_id = arr_parent_id.get(0).getAsInt();
                                    category.setParentId(parent_id);
                                } else {
                                    category.setParentId(-1);
                                }
                            } else {
                                category.setParentId(-1);
                            }
                            JsonElement el_child_id = obj_category.get(CATEGORY_CHILD_ID);
                            if (el_child_id.isJsonArray()) {
                                List<String> listChildId = new ArrayList<>();
                                JsonArray arr_child_id = el_child_id.getAsJsonArray();
                                if (arr_child_id != null && arr_child_id.size() > 0) {
                                    for (JsonElement el_child : arr_child_id) {
                                        String child_id = el_child.getAsString();
                                        listChildId.add(child_id);
                                    }
                                    category.setChildren(listChildId);
                                } else {
                                    category.setChildren(null);
                                }
                            } else {
                                category.setChildren(null);
                            }
                            listCategory.add(category);
                        }
                    }
                }
                return listCategory;
            }
        }
    }

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 0;
    }

    @Override
    public List<Category> retrieve() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<Category> retrieve(int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_GET_CATEGORY_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosCategoryParseModel());
            rp.setParseModel(PosDataCategory.class);
            DataCategory categoryEntity = (DataCategory) rp.doParse();
            mListCategory = categoryEntity.getItems();
            return getListCategory(null);
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
    public List<Category> retrieve(String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public Category retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public boolean update(Category oldModel, Category newModel) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean insert(Category... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean delete(Category... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public List<Category> getListCategory(Category category) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mListCategory == null) mListCategory = new ArrayList<Category>();
        if (mListDefaultCategory == null) mListDefaultCategory = new ArrayList<Category>();
        PosCategory cateFirst = new PosCategory();
        cateFirst.setName("All Products");
        cateFirst.setLevel(0);
        mListDefaultCategory.add(cateFirst);
        for (Category c : mListCategory) {
            if (c.getParentID() == -1) {
                c.setLevel(1);
                mListDefaultCategory.add(c);
                if (c.getChildren() != null) {
                    for (String IdChild : c.getChildren()) {
                        mListDefaultCategory = findChildLv2(mListDefaultCategory, IdChild, mListCategory, 2, c.getSubCategory());
                    }
                }
            }
        }
        return mListDefaultCategory;
    }

    private List<Category> findChildLv2(List<Category> resultList, String idChild, List<Category> categories, int level, List<Category> sub_category) {
        for (Category category : categories) {
            if (category.getID().equals(idChild)) {
                category.setLevel(level);
                sub_category.add(category);
                if (category.getChildren() != null) {
                    resultList.add(category);
                    level++;
                    for (String idSubChild : category.getChildren()) {
                        resultList = findChildLv2(resultList, idSubChild, categories, level, category.getSubCategory());
                    }
                } else {
                    resultList.add(category);
                }
            }
        }
        return resultList;
    }
}
