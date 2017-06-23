package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.payment.CreditCard;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.model.checkout.PosCheckoutPayment;
import com.magestore.app.pos.util.EditTextUtil;
import com.magestore.app.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 3/9/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutPaymentCreditCardPanel extends AbstractDetailPanel<CheckoutPayment> {
    CheckoutListController mCheckoutListController;
    SimpleSpinner s_card_type, s_card_month, s_card_year;
    EditText card_name, card_number, card_cvv;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    public CheckoutPaymentCreditCardPanel(Context context) {
        super(context);
    }

    public CheckoutPaymentCreditCardPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutPaymentCreditCardPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        card_name = (EditText) findViewById(R.id.card_name);
        card_number = (EditText) findViewById(R.id.card_number);
        card_cvv = (EditText) findViewById(R.id.card_cvv);
        s_card_type = (SimpleSpinner) findViewById(R.id.s_card_type);
        s_card_month = (SimpleSpinner) findViewById(R.id.s_card_month);
        s_card_year = (SimpleSpinner) findViewById(R.id.s_card_year);
    }

    @Override
    public void initValue() {

    }

    public void setCardTypeDataSet(Map<String, String> listCardType) {
        s_card_type.bind(listCardType);
        if (listCardType.size() > 0) {
            for (String key : listCardType.keySet()) {
                if (key.equals("VI")) {
                    s_card_type.setSelection(key);
                    break;
                }
            }
        }
    }

    public void setCardMonthDataSet(List<String> listCardMonth) {
        s_card_month.bind(listCardMonth.toArray(new String[0]));
    }

    public void setCardYearDataSet(Map<String, String> listCardYear) {
        s_card_year.bind(listCardYear);
    }

    @Override
    public CheckoutPayment bind2Item() {
        CheckoutPayment checkoutPayment = mCheckoutListController.createPaymentMethod();
        PosCheckoutPayment.AdditionalData additionalData = checkoutPayment.createAdditionalData();
        checkoutPayment.setAdditionalData(additionalData);
        String ccOwner = card_name.getText().toString();
        String ccType = s_card_type.getSelection();
        String ccNumber = card_number.getText().toString();
        String ccMonth = s_card_month.getSelection();
        String ccYear = s_card_year.getSelection();
        String ccCvv = card_cvv.getText().toString();
        checkoutPayment.setCCOwner(ccOwner);
        checkoutPayment.setCCType(ccType);
        checkoutPayment.setCCNumber(ccNumber);
        checkoutPayment.setCCExpMonth(ccMonth);
        checkoutPayment.setCCExpYear(ccYear);
        checkoutPayment.setCID(ccCvv);
        return checkoutPayment;
    }

    public void clearDataForm() {
        card_name.setText("");
        card_number.setText("");
        card_cvv.setText("");
    }

    public boolean checkRequiedCreditCardType() {
        boolean checkCreditCardType = false;
        ArrayList<CreditCard> listOfPattern = new ArrayList<CreditCard>();
        CreditCard creditCardVI = mCheckoutListController.createCreditCard();
        String ptVisa = "^4[0-9]{6,}$";
        creditCardVI.setCode("VI");
        creditCardVI.setRegular(ptVisa);
        listOfPattern.add(creditCardVI);
        CreditCard creditCardMC = mCheckoutListController.createCreditCard();
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        creditCardMC.setCode("MC");
        creditCardMC.setRegular(ptMasterCard);
        listOfPattern.add(creditCardMC);
        CreditCard creditCardAE = mCheckoutListController.createCreditCard();
        String ptAmeExp = "^3[47][0-9]{5,}$";
        creditCardAE.setCode("AE");
        creditCardAE.setRegular(ptAmeExp);
        listOfPattern.add(creditCardAE);
        CreditCard creditCardDC = mCheckoutListController.createCreditCard();
        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        creditCardDC.setRegular(ptDinClb);
        creditCardDC.setCode("DC");
        listOfPattern.add(creditCardDC);
        CreditCard creditCardDI = mCheckoutListController.createCreditCard();
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        creditCardDI.setCode("DI");
        creditCardDI.setRegular(ptDiscover);
        listOfPattern.add(creditCardDI);
        CreditCard creditCardJC = mCheckoutListController.createCreditCard();
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        creditCardJC.setCode("JC");
        creditCardJC.setRegular(ptJcb);
        listOfPattern.add(creditCardJC);
        String ccNumber = card_number.getText().toString();
        String ccType = s_card_type.getSelection();
        for (CreditCard creditCard : listOfPattern) {
            if (ccNumber.matches(creditCard.getRegular())) {
                if (creditCard.getCode().equals(ccType)) {
                    return true;
                }
                break;
            }
        }
        return checkCreditCardType;
    }

    public boolean checkRequiedCard(boolean checkCVV) {
//        if (!isRequied(card_name)) {
//            return false;
//        }
        if (!isRequied(card_number)) {
            return false;
        }
        if (!checkCVV) {
            if (!isRequied(card_cvv)) {
                return false;
            }
        }
        return true;
    }

    public boolean isRequied(EditText editText) {
        return EditTextUtil.checkRequied(getContext(), editText);
    }

    public void showErrorCreditCardType(){
        String message = getContext().getString(R.string.err_credit_type);
        // Tạo dialog và hiển thị
        DialogUtil.confirm(getContext(), message, R.string.done);
    }
}
