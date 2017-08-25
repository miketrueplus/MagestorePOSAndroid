package com.magestore.app.pos.model.magento.catalog;

/**
 * Created by folio on 3/3/2017.
 */

public class PosProductOptionPriceConfig {
    String productId;
    String idSuffix;
    Object[] tierPrices;
    PosProductOptionPriceConfigPriceFormat priceFormat;

    public class PosProductOptionPriceConfigPrice {
        PosProductOptionPriceConfigPriceAmount oldPrice;
        PosProductOptionPriceConfigPriceAmount basePrice;
        PosProductOptionPriceConfigPriceAmount finalPrice;
    }
    PosProductOptionPriceConfigPrice prices;
}
