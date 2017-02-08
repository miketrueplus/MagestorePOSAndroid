package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface PaymentMethod extends Model {
    void setCode(String strCode);

    String getCode();

    void setTitle(String strTitle);

    String getTitle();

    void setBaseAmount(int amount);

    int getBaseAmount();

    int getAmount();

    void setAmount(int amount);

    int getBaseRealAmount();

    void setBaseRealAmount(int amount);

    int getRealAmount();

    void setRealAmount(int amount);

    void setPaylater(int paylater);

    int isPaylater();

    void setShiftID(String shiftID);

    String getShiftID();

    void setCCOwner(String owner);

    String getCCOwner();

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
}
