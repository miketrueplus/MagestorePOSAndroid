package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.PlaceOrderIntegrationExtension;

import java.util.List;

/**
 * Created by Johan on 5/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PlaceOrderIntegrationParam extends Model {
    void setModule(String strModule);
    void setEventName(String strEventName);
    void setOrderData(List<PlaceOrderIntegrationOrderData> listOrderData);
    void setExtensionData(List<PlaceOrderIntegrationExtension> listExtensionData);
}
