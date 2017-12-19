package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 4/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface ConfigPrint extends Model {
    String getAutoPrint();
    void setAutoPrint(String strAutoPrint);
    String getFontType();
    void setFontType(String strFontType);
    String getHeaderText();
    void setHeaderText(String strHeaderText);
    String getFooterText();
    void setFooterText(String strFooterText);
    String getShowReceiptLogo();
    void setShowReceiptLogo(String strShowReceiptLogo);
    String getPathLogo();
    void setPathLogo(String strPathLogo);
    String getShowCashierName();
    void setShowCashierName(String strShowCashierName);
    String getShowComment();
    void setShowComment(String strShowComment);
    String getReceiptTitle();
    void setReceiptTitle(String strReceiptTitle);
}
