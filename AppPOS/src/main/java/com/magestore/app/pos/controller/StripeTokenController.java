package com.magestore.app.pos.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

/**
 * Created by Johan on 6/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class StripeTokenController {
    private Context mContext;
    private String mPublishableKey;
    private CheckoutListController mCheckoutListController;
    private CheckoutPayment mPayment;

    public StripeTokenController (
            @NonNull Context context,
            @NonNull String publishableKey,
            CheckoutListController checkoutListController,
            CheckoutPayment payment){
        mContext = context;
        mPublishableKey = publishableKey;
        mCheckoutListController = checkoutListController;
        mPayment = payment;
        saveCard();
    }

    private void saveCard() {
        String cc_number = mPayment.getCCNumber();
        int cc_month = Integer.parseInt(mPayment.getCCExpMonth());
        int cc_year = Integer.parseInt(mPayment.getCCExpYear());
        String cc_cvv = mPayment.getCID();
        Card card = new Card(cc_number, cc_month, cc_year, cc_cvv);
        card.setName(mPayment.getCCOwner());
        new Stripe(mContext).createToken(
                card,
                mPublishableKey,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        String stripeToken = token.getId();
                        mPayment.setStripeToken(stripeToken);
                        mCheckoutListController.doInputApprovedStripe(mPayment);
                    }
                    public void onError(Exception error) {
                        mCheckoutListController.showDialogError(error.getLocalizedMessage());
                        mCheckoutListController.isShowLoadingDetail(false);
                    }
                });
    }
}
