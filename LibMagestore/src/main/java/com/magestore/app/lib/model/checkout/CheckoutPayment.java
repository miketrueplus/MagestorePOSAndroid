package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.magento.checkout.PosCheckoutPayment;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface CheckoutPayment extends Model {
    void setCode(String strCode);

    String getCode();

    void setTitle(String strTitle);

    String getTitle();

    void setBaseAmount(float amount);

    float getBaseAmount();

    float getAmount();

    void setAmount(float amount);

    float getBaseRealAmount();

    void setBaseRealAmount(float amount);

    float getRealAmount();

    void setRealAmount(float amount);

    void setPaylater(String paylater);

    String isPaylater();

    String getIsDefault();

    void setIsDefault(String strIsDefault);

    String getIsReferenceNumber();

    void setIsReferenceNumber(String strIsReferenceNumber);

    String getType();

    void setType(String strType);

    String getInformation();

    void setInformation(String strInformation);

    void setShiftID(String shiftID);

    String getShiftID();

    void setCCOwner(String owner);

    String getCCOwner();

    void setUserCVV(String userCVV);

    String getUserCVV();


    void setCCType(String type);

    String getCCType();

    void setCCNumber(String number);

    String getCCNumber();

    void setCCExpMonth(String month);

    String getCCExpMonth();

    void setCCExpYear(String year);

    String getCCExpYear();

    void setCID(String cid);

    String getCID();

    boolean checkReference();

    boolean checkIsPayLater();

    boolean checkIsPayLaterSuggest();

    String getReferenceNumber();
    void setReferenceNumber(String strReferenceNumber);

    float getCurrentValue();
    void setCurrentValue(float fCurrentValue);

    PosCheckoutPayment.AdditionalData createAdditionalData();
    PosCheckoutPayment.AdditionalData getAdditionalData();
    void setAdditionalData(PosCheckoutPayment.AdditionalData additionalData);

    boolean IsNotEnableEditValue();
    void setIsNotEnableEditValue(boolean bIsNotEnableValue);

    // paypal
    String getClientId();
    void setClientId(String strClientId);
    String getIsSandbox();

    // paypal here
    String getAccessToken();
    void setAccessToken(String strAccessToken);

    // Stripe payment
    String getPublishKeyStripe();
    String getStripeToken();
    void setStripeToken(String strStripeToken);

    // Authorize
    String getApiLogin();
    void setApiLogin(String strApiLogin);
    String getAuthorizeToken();
    void setAuthorizeToken(String strAuthorizeToken);
}
