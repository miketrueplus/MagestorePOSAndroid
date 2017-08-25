package com.magestore.app.pos.model.magento.catalog;

import com.magestore.app.pos.model.PosAbstractModel;

import java.util.Map;

/**
 * Created by folio on 3/3/2017.
 */

public class PosProductOptionConfigOption extends PosAbstractModel {
    public String optionId;
    public String optionLabel;
    public Map<String, String> optionValues;

    @Override
    public String getID() {
        return optionId;
    }

    @Override
    public String getDisplayContent() {
        return optionLabel;
    }
}
