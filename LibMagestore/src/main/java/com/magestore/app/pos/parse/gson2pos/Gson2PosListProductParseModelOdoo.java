package com.magestore.app.pos.parse.gson2pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.model.catalog.ProductTaxDetailOdoo;
import com.magestore.app.pos.model.catalog.PosProduct;
import com.magestore.app.pos.model.catalog.PosProductTaxDetailOdoo;
import com.magestore.app.util.StringUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 9/21/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class Gson2PosListProductParseModelOdoo extends Gson2PosAbstractParseImplement {
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
