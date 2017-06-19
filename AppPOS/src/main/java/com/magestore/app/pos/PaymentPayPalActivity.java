package com.magestore.app.pos;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.LinearLayout;

import com.magestore.app.pos.ui.AbstractActivity;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DialogUtil;
import com.magestore.app.util.StringUtil;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by Johan on 4/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PaymentPayPalActivity extends AbstractActivity {
    // TODO: Lấy từ config theo sanbox hay live, client_id là gì
    private static String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    private static String CONFIG_CLIENT_ID = "";
    private static final int REQUEST_CODE_PAYMENT = 1;
    float mTotal;
    String mIsSandbox;
    public static String SEND_PAYMENT_ID_TO_SALE_ACTIVITY = "com.magestore.app.pos.paymentpaypalactivity.paymentid";
    public static String SEND_ERROR_TO_SALE_ACTIVITY = "com.magestore.app.pos.paymentpaypalactivity.error";

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        setContentView(layout);

        if (getIntent() != null) {
            mTotal = getIntent().getFloatExtra("total", 0);
            CONFIG_CLIENT_ID = getIntent().getStringExtra("client_id");
            mIsSandbox = getIntent().getStringExtra("sandbox");
            if(mIsSandbox.equals("1")){
                CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
            }
        }

        PayPalConfiguration config = new PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(CONFIG_CLIENT_ID)
                // TODO: set merchant name
                .merchantName("Magestore")
                .rememberUser(false)
                .merchantPrivacyPolicyUri(null)
                .merchantUserAgreementUri(null);

        Intent intent_service = new Intent(this, PayPalService.class);
        intent_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent_service);

        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(PaymentPayPalActivity.this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        String currency = "USD";
        if (StringUtil.isNullOrEmpty(ConfigUtil.getBaseCurrencyCode())) {
            currency = ConfigUtil.getBaseCurrencyCode();
        }
        return new PayPalPayment(new BigDecimal(mTotal), currency, "paypal",
                paymentIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.d("PaymentPayPalActivity", confirm.toJSONObject().toString(4));
                        JSONObject response = confirm.toJSONObject().getJSONObject("response");
                        String payment_id = response.getString("id");
                        Intent intent = new Intent();
                        intent.setAction(SEND_PAYMENT_ID_TO_SALE_ACTIVITY);
                        intent.putExtra("payment_id", payment_id);
                        sendBroadcast(intent);
                        finish();
                    } catch (JSONException e) {
                        Log.d("PaymentPayPalActivity", "an extremely unlikely failure occurred: ", e);
                        finish();
                    }
                } else {
                    finish();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                String message = getString(R.string.paypal_cancel_order);
                Log.d("PaymentPayPalActivity", message);
                Intent intent = new Intent();
                intent.setAction(SEND_ERROR_TO_SALE_ACTIVITY);
                intent.putExtra("message", message);
                sendBroadcast(intent);
                finish();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                String message = getString(R.string.paypal_error);
                Log.d("PaymentPayPalActivity", message);
                Intent intent = new Intent();
                intent.setAction(SEND_ERROR_TO_SALE_ACTIVITY);
                intent.putExtra("message", message);
                sendBroadcast(intent);
                finish();
            }
        }
    }
}
