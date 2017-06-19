package com.magestore.app.pos.sdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.pos.R;
import com.magestore.app.util.ConfigUtil;
import com.paypal.merchant.sdk.TransactionManager;
import com.paypal.merchant.sdk.domain.DomainFactory;
import com.paypal.merchant.sdk.domain.Invoice;
import com.paypal.merchant.sdk.domain.InvoiceItem;
import com.paypal.merchant.sdk.domain.TransactionRecord;

import java.math.BigDecimal;

public class ChargeActivity extends ActionBarActivity {
    private static final String LOG_TAG = ChargeActivity.class.getSimpleName();

    private boolean mIsInMiddleOfTakingPayment = false;
    private TransactionRecord mTransactionRecord = null;
    private TextView txt_amount;
    private RelativeLayout mChargeLayout;
    private RelativeLayout mPaymentSuccessfulLayout;
    private RelativeLayout mPaymentFailureLayout;
    private RelativeLayout mRefundSuccessfulLayout;
    private RelativeLayout mRefundFailureLayout;
    private float amount;
    private String quote_id = "";
    private static int TYPE_SUCCESS = 0;
    private static int TYPE_ERROR = 1;
    private static int TYPE_CANCEL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        amount = getIntent().getFloatExtra("amount", amount);
        quote_id = getIntent().getStringExtra("quote_id");

        setContentView(R.layout.activity_charge);
        txt_amount = (TextView) findViewById(R.id.txt_amount);
        mChargeLayout = (RelativeLayout) findViewById(R.id.id_charge_layout);
        mPaymentSuccessfulLayout = (RelativeLayout) findViewById(R.id.id_payment_successful_layout);
        mPaymentFailureLayout = (RelativeLayout) findViewById(R.id.id_payment_failure_layout);
        mRefundSuccessfulLayout = (RelativeLayout) findViewById(R.id.id_refund_successful_layout);
        mRefundFailureLayout = (RelativeLayout) findViewById(R.id.id_refund_failure_layout);
        txt_amount.setText(getString(R.string.amount) + ": " + ConfigUtil.formatPrice(amount));
        showChargeLayout();
        inVoiceTakePayment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
        PayPalHereSDKWrapper.getInstance().registerCardReaderEventListener();
        setListener();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(LOG_TAG, "onConfigurationChanged");
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "onBackPressed");

        //Lets go back to PaymentOptionsActivity instead of going back to ReaderConnectionActivity
        goToSalesActivity(TYPE_CANCEL, "");
        PayPalHereSDKWrapper.getInstance().unregisterCardReaderEventListener();
    }

    /*
     * This function will be called when Take Payment button is clicked on Charge Screen
     */
    public void onTakePaymentOptionSelected(View view) {

    }

    public void inVoiceTakePayment(){
        Log.d(LOG_TAG, "takePayment");

        Invoice invoice = DomainFactory.newEmptyInvoice();
        InvoiceItem invoiceItem = DomainFactory.newInvoiceItem("Pay to Retailer POS", quote_id, new BigDecimal(amount));
        invoice.addItem(invoiceItem, new BigDecimal(1));

        /**
         * STEP 1: Step 1 of SDK integration is calling beginPayment of the PayPalHereSDK's TransactionManager
         */
        PayPalHereSDKWrapper.getInstance().beginPayment(invoice);
        takePayment();
    }

    public void onRefundOptionSelected(View view) {
        Log.d(LOG_TAG, "onRefundOptionSelected");
        PayPalHereSDKWrapper.getInstance().doRefund(mTransactionRecord, mTransactionRecord.getInvoice().getGrandTotal(), new PayPalHereSDKWrapperCallbacks() {
            @Override
            public void onRefundFailure(TransactionManager.PaymentErrors errors) {
                Log.d(LOG_TAG, "onRefundFailure error: " + errors);
                showRefundFailureLayout();
            }

            @Override
            public void onRefundSuccess(TransactionManager.PaymentResponse responseObject) {
                Log.d(LOG_TAG, "onRefundSuccess");
                showRefundSuccessfulLayout();
            }
        });
    }

    /*
     * Show the charge layout and hide all other layouts
     */
    private void showChargeLayout() {
        mChargeLayout.setVisibility(View.VISIBLE);
        mPaymentSuccessfulLayout.setVisibility(View.GONE);
        mPaymentFailureLayout.setVisibility(View.GONE);
        mRefundSuccessfulLayout.setVisibility(View.GONE);
        mRefundFailureLayout.setVisibility(View.GONE);
    }

    /*
     * Show the payment successful layout and hide all other layouts
     */
    private void showPaymentSuccessfulLayout() {
        mChargeLayout.setVisibility(View.GONE);
        mPaymentSuccessfulLayout.setVisibility(View.VISIBLE);
        mPaymentFailureLayout.setVisibility(View.GONE);
        mRefundSuccessfulLayout.setVisibility(View.GONE);
        mRefundFailureLayout.setVisibility(View.GONE);
    }

    /*
     * Show the payment failure layout and hide all other layouts
     */
    private void showPaymentFailureLayout() {
        mChargeLayout.setVisibility(View.GONE);
        mPaymentSuccessfulLayout.setVisibility(View.GONE);
        mPaymentFailureLayout.setVisibility(View.VISIBLE);
        mRefundSuccessfulLayout.setVisibility(View.GONE);
        mRefundFailureLayout.setVisibility(View.GONE);
    }

    /*
     * Show the refund success layout and hide all other layouts
     */
    private void showRefundSuccessfulLayout() {
        mChargeLayout.setVisibility(View.GONE);
        mPaymentSuccessfulLayout.setVisibility(View.GONE);
        mPaymentFailureLayout.setVisibility(View.GONE);
        mRefundSuccessfulLayout.setVisibility(View.VISIBLE);
        mRefundFailureLayout.setVisibility(View.GONE);
    }

    /*
     * Show the refund failure layout and hide all other layouts
     */
    private void showRefundFailureLayout() {
        mChargeLayout.setVisibility(View.GONE);
        mPaymentSuccessfulLayout.setVisibility(View.GONE);
        mPaymentFailureLayout.setVisibility(View.GONE);
        mRefundSuccessfulLayout.setVisibility(View.GONE);
        mRefundFailureLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Listener to PayPalHere SDK Wrapper for all the events.
     */
    private void setListener() {
        PayPalHereSDKWrapper.getInstance().setListener(this, new PayPalHereSDKWrapperCallbacks() {
            @Override
            public void onSuccessfulCardRead() {
                Log.d(LOG_TAG, "onSuccessfulCardRead");
                takePayment();
            }

            @Override
            public void onMagstripeReaderDisconnected() {
                Log.d(LOG_TAG, "onMagstripeReaderDisconnected");
                //If reader got disconnected and if we are not in the middle of taking payment
                // go back to reader connect layout.
                if (!mIsInMiddleOfTakingPayment) {
                    finish();
                }
            }

            @Override
            public void onEMVReaderDisconnected() {
                Log.d(LOG_TAG, "onEMVReaderDisconnected");
                //If reader got disconnected and if we are not in the middle of taking payment
                // go back to reader connect layout.
                if (!mIsInMiddleOfTakingPayment) {
                    finish();
                }
            }
        });
    }

    private void takePayment() {
        Log.d(LOG_TAG, "takePayment");
        mIsInMiddleOfTakingPayment = true;
        /**
         * STEP 2: Step 2 is calling processPayment of PayPalHere SDK's Transaction Manager.
         */
        PayPalHereSDKWrapper.getInstance().takePayment(new PayPalHereSDKWrapperCallbacks() {
            @Override
            public void onPaymentFailure(TransactionManager.PaymentErrors errors) {
                Log.d(LOG_TAG, "takePayment onPaymentFailure");
                mIsInMiddleOfTakingPayment = false;
                Log.d(LOG_TAG, "Transaction errors: " + errors.toString());
                goToSalesActivity(TYPE_ERROR, "");
            }

            @Override
            public void onPaymentSuccess(TransactionManager.PaymentResponse responseObject) {
                Log.d(LOG_TAG, "takePayment onPaymentSuccess");
                mIsInMiddleOfTakingPayment = false;
                mTransactionRecord = responseObject.getTransactionRecord();
                Log.d(LOG_TAG, "TransactionID: " + mTransactionRecord.getTransactionId());
                goToSalesActivity(TYPE_SUCCESS, mTransactionRecord.getTransactionId());
            }
        });
    }

    public void goToSalesActivity(int type, String transaction_id) {
        Intent returnIntent = new Intent();
        if (type == TYPE_SUCCESS) {
            returnIntent.putExtra("transaction_id", transaction_id);
            setResult(Activity.RESULT_OK, returnIntent);
        } else if (type == TYPE_ERROR) {
            setResult(MultiReaderConnectionActivity.TRANSACTION_ERRORS, returnIntent);
        } else {
            setResult(Activity.RESULT_CANCELED, returnIntent);
        }
        finish();
    }
}
