package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.payment.Authorizenet;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;

import java.util.Map;

/**
 * Created by Johan on 4/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutPaymentWebviewPanel extends AbstractDetailPanel<Checkout> {
    CheckoutListController mCheckoutListController;
    Authorizenet mAuthorizenet;
    WebView payment_webview;
    String mUrl = "";

    public void setAuthorizenet(Authorizenet mAuthorizenet) {
        this.mAuthorizenet = mAuthorizenet;
    }

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    public CheckoutPaymentWebviewPanel(Context context) {
        super(context);
    }

    public CheckoutPaymentWebviewPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutPaymentWebviewPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        payment_webview = (WebView) findViewById(R.id.payment_webview);
        payment_webview.getSettings().setJavaScriptEnabled(true);
        payment_webview.getSettings().setLoadsImagesAutomatically(true);
    }

    @Override
    public void initValue() {
        String url = mAuthorizenet.getPaymentInformation().getUrl();
        Map<String, String> params = mAuthorizenet.getPaymentInformation().getParams();
        String url_params = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            url_params += key + "=" + value + "&";
        }
        url_params = url_params.substring(0, (url_params.length() - 1));
        Log.e("CheckoutWebviewPanel", "Params: " + url_params);
        mUrl = url + "?" + url_params;
        Log.e("CheckoutWebviewPanel", "Url: " + mUrl);
        payment_webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                viewx.loadUrl(urlx);
                Log.e("CheckoutWebviewPanel", urlx);
                return true;
            }
        });
    }
}
