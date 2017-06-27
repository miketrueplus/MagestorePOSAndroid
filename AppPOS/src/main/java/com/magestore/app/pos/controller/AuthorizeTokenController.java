package com.magestore.app.pos.controller;

import android.content.Context;
import android.util.Log;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.pos.R;

import net.authorize.acceptsdk.AcceptSDKApiClient;
import net.authorize.acceptsdk.datamodel.merchant.ClientKeyBasedMerchantAuthentication;
import net.authorize.acceptsdk.datamodel.transaction.CardData;
import net.authorize.acceptsdk.datamodel.transaction.EncryptTransactionObject;
import net.authorize.acceptsdk.datamodel.transaction.TransactionObject;
import net.authorize.acceptsdk.datamodel.transaction.TransactionType;
import net.authorize.acceptsdk.datamodel.transaction.callbacks.EncryptTransactionCallback;
import net.authorize.acceptsdk.datamodel.transaction.response.EncryptTransactionResponse;
import net.authorize.acceptsdk.datamodel.transaction.response.ErrorTransactionResponse;

/**
 * Created by Johan on 6/27/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class AuthorizeTokenController implements EncryptTransactionCallback {
    private Context mContext;
    private CheckoutListController mCheckoutListController;
    private CheckoutPayment mPayment;
    private AcceptSDKApiClient apiClient;

    public AuthorizeTokenController(Context context, CheckoutListController checkoutListController, CheckoutPayment payment) {
        mContext = context;
        mCheckoutListController = checkoutListController;
        mPayment = payment;
        try {
            if(payment.getIsSandbox().equals("1")){
                apiClient = new AcceptSDKApiClient.Builder(context,
                        AcceptSDKApiClient.Environment.SANDBOX).connectionTimeout(
                        4000) // optional connection time out in milliseconds
                        .build();
            }else{
                apiClient = new AcceptSDKApiClient.Builder(context,
                        AcceptSDKApiClient.Environment.PRODUCTION).connectionTimeout(
                        4000) // optional connection time out in milliseconds
                        .build();
            }
        } catch (NullPointerException e) {
            mCheckoutListController.showDialogError(mContext.getString(R.string.authorize_cancel_payment));
            mCheckoutListController.isShowLoadingDetail(false);
            e.printStackTrace();
        }

        getToken();
    }

    @Override
    public void onErrorReceived(ErrorTransactionResponse error) {
        Log.d(AuthorizeTokenController.class.getName(), error.getFirstErrorMessage().getMessageText());
        mCheckoutListController.showDialogError(error.getFirstErrorMessage().getMessageText());
        mCheckoutListController.isShowLoadingDetail(false);
    }

    @Override
    public void onEncryptionFinished(EncryptTransactionResponse response) {
        String token = response.getDataValue();
        mPayment.setAuthorizeToken(token);
        mCheckoutListController.doInputApprovedAuthorizenetIntergration(mPayment);
    }

    private void getToken() {
        try {
            EncryptTransactionObject transactionObject = prepareTransactionObject();
              /*
                Make a call to get Token API
                parameters:
                  1) EncryptTransactionObject - The transactionObject for the current transaction
                  2) callback - callback of transaction
               */
            apiClient.getTokenWithRequest(transactionObject, this);
        } catch (NullPointerException e) {
            // Handle exception transactionObject or callback is null.
            mCheckoutListController.showDialogError(mContext.getString(R.string.authorize_cancel_payment));
            mCheckoutListController.isShowLoadingDetail(false);
            e.printStackTrace();
        }
    }

    /**
     * prepares a transaction object with dummy data to be used with the Gateway transactions
     */
    private EncryptTransactionObject prepareTransactionObject() {
        ClientKeyBasedMerchantAuthentication merchantAuthentication =
                ClientKeyBasedMerchantAuthentication.
                        createMerchantAuthentication(mPayment.getApiLogin(), mPayment.getClientId());

        // create a transaction object by calling the predefined api for creation
        return TransactionObject.
                createTransactionObject(
                        TransactionType.SDK_TRANSACTION_ENCRYPTION) // type of transaction object
                .cardData(prepareCardDataFromFields()) // card data to get Token
                .merchantAuthentication(merchantAuthentication).build();
    }

    private CardData prepareCardDataFromFields() {
        return new CardData.Builder("", mPayment.getCCExpMonth(), mPayment.getCCExpYear()).cvvCode(mPayment.getCID()) //CVV Code is optional
                .cardHolderName(mPayment.getCCOwner()).build();
    }
}
