package com.magestore.app.pos.model.odoo.config;

import com.magestore.app.lib.model.config.ConfigPrint;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 4/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosConfigPrint extends PosAbstractModel implements ConfigPrint {
    String auto_print;
    String font_type;
    String header_text;
    String footer_text;
    String show_receipt_logo;
    String path_logo;
    String show_cashier_name;
    String show_comment;

    @Override
    public String getAutoPrint() {
        return auto_print;
    }

    @Override
    public void setAutoPrint(String strAutoPrint) {
        auto_print = strAutoPrint;
    }

    @Override
    public String getFontType() {
        return font_type;
    }

    @Override
    public void setFontType(String strFontType) {
        font_type = strFontType;
    }

    @Override
    public String getHeaderText() {
        return header_text;
    }

    @Override
    public void setHeaderText(String strHeaderText) {
        header_text = strHeaderText;
    }

    @Override
    public String getFooterText() {
        return footer_text;
    }

    @Override
    public void setFooterText(String strFooterText) {
        footer_text = strFooterText;
    }

    @Override
    public String getShowReceiptLogo() {
        return show_receipt_logo;
    }

    @Override
    public void setShowReceiptLogo(String strShowReceiptLogo) {
        show_receipt_logo = strShowReceiptLogo;
    }

    @Override
    public String getPathLogo() {
        return path_logo;
    }

    @Override
    public void setPathLogo(String strPathLogo) {
        path_logo = strPathLogo;
    }

    @Override
    public String getShowCashierName() {
        return show_cashier_name;
    }

    @Override
    public void setShowCashierName(String strShowCashierName) {
        show_cashier_name = strShowCashierName;
    }

    @Override
    public String getShowComment() {
        return show_comment;
    }

    @Override
    public void setShowComment(String strShowComment) {
        show_comment = strShowComment;
    }
}
