package com.magestore.app.pos.model.magento.checkout;

import com.magestore.app.lib.model.PlaceOrderIntegrationExtension;
import com.magestore.app.lib.model.checkout.PlaceOrderIntegrationOrderData;
import com.magestore.app.lib.model.checkout.PlaceOrderIntegrationParam;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 5/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosPlaceOrderIntegrationParam extends PosAbstractModel implements PlaceOrderIntegrationParam {
    String module;
    String event_name;
    List<PlaceOrderIntegrationOrderData> order_data;
    List<PlaceOrderIntegrationExtension> extension_data;

    @Override
    public void setModule(String strModule) {
        module = strModule;
    }

    @Override
    public void setEventName(String strEventName) {
        event_name = strEventName;
    }

    @Override
    public void setOrderData(List<PlaceOrderIntegrationOrderData> listOrderData) {
        order_data = listOrderData;
    }

    @Override
    public void setExtensionData(List<PlaceOrderIntegrationExtension> listExtensionData) {
        extension_data = listExtensionData;
    }
}
