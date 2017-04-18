package com.magestore.app.pos.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.pos.R;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.util.List;

/**
 * Created by Johan on 4/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PrintUtil {
    private static WebView mWebView;

    public static void doPint(final Context context, Order order) {
        // Create a WebView object specifically for printing
        final WebView webView = new WebView(context);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                createWebPrintJob(context, view);
                mWebView = null;
            }
        });

        // Generate an HTML document on the fly:
        String htmlDocument = createLayoutOrder(context, order);
        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void createWebPrintJob(Context context, WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = context.getString(R.string.app_name) + " Print Order";
        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    private static String createLayoutOrder(Context context, Order order) {
        String title_invoice = context.getString(R.string.order_detail_bottom_btn_invoice);
        String body_header_title = "<div align=\"center\" style=\"font-weight: bold; font-size: 30px; display: block; font-family: monospace;\">" + title_invoice + "</div>" + "<div align=\"center\" style=\"font-size: 16px; font-weight: 400; display: block; font-family: monospace;\">**** ****</div>";
        // header content
        String body_header_content = "";
        if (!StringUtil.isNullOrEmpty(ConfigUtil.getConfigPrint().getHeaderText())) {
            body_header_content = "<div align=\"center\" style=\"font-size: 13px; display: block; font-family: monospace;\">" + ConfigUtil.getConfigPrint().getHeaderText() + "</div>";
        }
        // invoice id
        String body_header_info_invoice = "<div align=\"center\"><br><span><span style=\"font-size: 20px; font-family: monospace;\">" + "#" + order.getIncrementId() + "</span><br><span style=\"font-size: 13px; font-family: monospace;\">" + ConfigUtil.formatDateTime(order.getCreatedAt()) + "</span><br></div>";
        // cashier name
        String cashier_title = context.getString(R.string.print_header_cashier);
        String body_header_info_cashier_name = "";
        if (ConfigUtil.getConfigPrint().getShowCashierName().equals("1") && !StringUtil.isNullOrEmpty(order.getWebposStaffName())) {
            body_header_info_cashier_name = "<div align=\"center\" style=\"font-size: 13px; display: block; font-family: monospace;\"><p style=\"text-transform: uppercase; margin-bottom: 0px;\"><span style=\"font-size: 13px; font-family: monospace;\">" + cashier_title + "</span><span style=\"font-size: 13px; font-family: monospace;\">" + order.getWebposStaffName() + "</span></p></div>";
        }
        // customer name
        String customer_title = context.getString(R.string.print_header_customer);
        String body_header_info_customer_name = "";
        if (order.getBillingAddress() != null && !StringUtil.isNullOrEmpty(order.getBillingAddress().getName())) {
            if (!order.getBillingAddress().getName().equals(ConfigUtil.getCustomerGuest().getName())) {
                String customer_name = order.getBillingAddress().getName();
                body_header_info_customer_name = "<div align=\"center\" style=\"font-size: 13px; display: block; font-family: monospace;\"><p style=\"text-transform: uppercase; margin-bottom: 0px;\"><span style=\"font-size: 13px; font-family: monospace;\">" + customer_title + "</span><span style=\"font-size: 13px; font-family: monospace;\">" + customer_name + "</span></p></div>";
            }
        }
        // shipping
        String shipping_title = context.getString(R.string.print_header_shipping);
        String body_content_shipping_method = "";
        if (!StringUtil.isNullOrEmpty(order.getShippingDescription())) {
            body_content_shipping_method = "<div><span style=\"font-size: 13px; font-family: monospace;\">" + shipping_title + "</span><span style=\"font-size: 13px; font-family: monospace;\">" + order.getShippingDescription() + "</span></div>";
        }
        // items
        String body_content_item = "";
        String body_content_items = "";
        List<CartItem> items = order.getOrderItems();
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                String body_content_item_name = "<td style=\"font-family: monospace; text-align: left; padding: 5px 0;\"><span style=\"font-family: monospace;\">" + item.getName() + "</span><span style=\"font-family: monospace; display: block;\">" + item.getSku() + "</span></td>";
                String body_content_item_qty = "<td style=\"font-family: monospace; text-align: right; padding: 5px 0;\">" + item.getQtyOrdered() + "</td>";
                String body_content_item_price = "<td style=\"font-family: monospace; text-align: right; padding: 5px 0;\">" + ConfigUtil.formatPrice(item.getPrice()) + "</td>";
                String body_content_item_subtotal = "<td style=\"font-family: monospace; text-align: right; padding: 5px 0;\">" + ConfigUtil.formatPrice(item.getSubtotal()) + "</td>";
                body_content_item += "<tr style=\"display: table-row;\">" + body_content_item_name + body_content_item_qty + body_content_item_price + body_content_item_subtotal + "</tr>";
            }
            body_content_items = "<div style=\"padding: 3px 0; margin-bottom: 7px\"><table style=\"width: 100%;\"><thead><tr style=\"display: table-row;\"><th style=\"font-weight: bold; font-family: monospace; text-align: left;\">ITEM</th><th style=\"font-weight: bold; font-family: monospace; text-align: right;\">Qty</th><th style=\"font-weight: bold; font-family: monospace; text-align: right;\">PRICE</th><th style=\"font-weight: bold; font-family: monospace; text-align: right;\">SUBTOTAL</th></tr></thead><tbody>" + body_content_item + "</tbody></table></div>";
        }
        // totals
        String title_sutotal = context.getString(R.string.order_detail_bottom_tb_subtotal).toUpperCase();
        String title_shipping = context.getString(R.string.order_detail_bottom_tb_shipping).toUpperCase();
        String title_tax = context.getString(R.string.order_detail_bottom_tb_tax).toUpperCase();
        String title_discount = context.getString(R.string.order_detail_bottom_tb_discount).toUpperCase();
        String title_grandtotal = context.getString(R.string.order_detail_bottom_tb_grand_total).toUpperCase();
        String title_total_paid = context.getString(R.string.order_detail_bottom_tb_total_paid).toUpperCase();
        String title_total_due = context.getString(R.string.order_detail_bottom_tb_total_due).toUpperCase();
        String body_content_total_subtotal = "<tr><td style=\"font-family: monospace;\">" + title_sutotal + "</td><td align=\"right\" style=\"font-family: monospace;\"><strong style=\"font-family: monospace;\">" + ConfigUtil.formatPrice(order.getOrderHistorySubtotal()) + "</strong></td></tr>";
        String body_content_total_shipping = "<tr><td style=\"font-family: monospace;\">" + title_shipping + "</td><td align=\"right\" style=\"font-family: monospace;\"><strong style=\"font-family: monospace;\">" + ConfigUtil.formatPrice(order.getShippingAmount()) + "</strong></td></tr>";
        String body_content_total_tax = "<tr><td style=\"font-family: monospace;\">" + title_tax + "</td><td align=\"right\" style=\"font-family: monospace;\"><strong style=\"font-family: monospace;\">" + ConfigUtil.formatPrice(order.getTaxAmount()) + "</strong></td></tr>";
        String body_content_total_discount = "<tr><td style=\"font-family: monospace;\">" + title_discount + "</td><td align=\"right\" style=\"font-family: monospace;\"><strong style=\"font-family: monospace;\">" + ConfigUtil.formatPrice(order.getDiscountAmount()) + "</strong></td></tr>";
        String body_content_grandtotal = "<tr><td style=\"font-family: monospace;\">" + title_grandtotal + "</td><td align=\"right\" style=\"font-family: monospace;\"><strong style=\"font-family: monospace;\">" + ConfigUtil.formatPrice(order.getGrandTotal()) + "</strong></td></tr>";
        String body_content_total_paid = "<tr><td style=\"font-family: monospace;\">" + title_total_paid + "</td><td align=\"right\" style=\"font-family: monospace;\"><strong style=\"font-family: monospace;\">" + ConfigUtil.formatPrice(order.getTotalPaid()) + "</strong></td></tr>";
        String body_content_total_due = "<tr><td style=\"font-family: monospace;\">" + title_total_due + "</td><td align=\"right\" style=\"font-family: monospace;\"><strong style=\"font-family: monospace;\">" + ConfigUtil.formatPrice(order.getTotalDue()) + "</strong></td></tr>";
        String body_content_line = "<tr><td style=\"border-top: dashed 1px #000;padding:2px 0px; font-family: monospace;\"></td><td style=\"border-top: dashed 1px #000;padding:2px 0px; font-family: monospace;\"></td></tr>";
        // payment
        String body_content_payment = "";
        List<OrderWebposPayment> listPayment = order.getWebposOrderPayments();
        if (listPayment != null && listPayment.size() > 0) {
            for (OrderWebposPayment payment : listPayment) {
                body_content_payment += "<tr><td style=\"font-family: monospace;\">" + payment.getMethodTitle() + "</td><td align=\"right\" style=\"font-family: monospace;\"><strong style=\"font-family: monospace;\">" + ConfigUtil.formatPrice(payment.getDisplayAmount()) + "</strong></td></tr>";
            }
        }
        // change
        String body_content_change = "";
        String title_total_change = context.getString(R.string.order_detail_bottom_tb_total_change).toUpperCase();
        float total_change = order.getTotalPaid() - order.getGrandTotal();
        if (total_change > 0) {
            body_content_change = "<tr><td style=\"font-family: monospace;\">" + title_total_change + "</td><td align=\"right\" style=\"font-family: monospace;\"><strong style=\"font-family: monospace;\">" + ConfigUtil.formatPrice(total_change) + "</strong></td></tr>";
        }
        String body_content_total = "<tbody style=\"display: table-row-group;\">" + body_content_line + body_content_total_subtotal + body_content_total_shipping + body_content_total_tax + body_content_total_discount + body_content_grandtotal + body_content_total_paid + body_content_total_due + body_content_line + body_content_payment + body_content_change + "</tbody>";
        String body_content_totals = "<div style=\"font-size: 13px; display: block; font-family: monospace; margin-bottom: 7px;\"><table style=\"width: 100%;\">" + body_content_total + "</table></div>";
        // comments
        String title_comment = context.getString(R.string.order_add_comment_title);
        List<OrderStatus> listComment = order.getOrderStatus();
        String body_content_comments = "";
        String body_content_comment = "";
        if (ConfigUtil.getConfigPrint().getShowComment().equals("1") && listComment != null && listComment.size() > 0) {
            for (OrderStatus comment : listComment) {
                if (!StringUtil.isNullOrEmpty(comment.getComment())) {
                    body_content_comments += "<div style=\"font-size: 13px; display: block; font-family: monospace;\">" + ConfigUtil.formatDateTime(comment.getCreatedAt()) + ": " + comment.getComment() + "</div>";
                }
            }
            body_content_comment = "<div style=\"font-size: 13px; display: block; font-family: monospace;\"><label style=\"font-size: 13px; display: block; font-family: monospace;\">" + title_comment + body_content_comments + "</div>";
        }
        // footer
        String body_content_footer_line = "<div align=\"center\" style=\"font-size: 16px; display: block; font-family: monospace; font-weight: 400;\">-------- **** --------</div>";
        String body_content_footer = "";
        if (!StringUtil.isNullOrEmpty(ConfigUtil.getConfigPrint().getFooterText())) {
            body_content_footer = "<div align=\"center\" style=\"font-size: 13px; display: block; font-family: monospace;\">" + ConfigUtil.getConfigPrint().getFooterText() + "</div>";
        } else {
            body_content_footer = "<div align=\"center\" style=\"font-size: 13px; display: block; font-family: monospace;\">" + context.getString(R.string.print_footer_default) + "</div>";
        }
        String body = body_header_title + body_header_content + body_header_info_invoice + body_header_info_cashier_name + body_header_info_customer_name + body_content_shipping_method + body_content_items + body_content_totals + body_content_comment + body_content_footer_line + body_content_footer;
        String layout_order = "<html><body>" + body + "</body></html>";
        if (!StringUtil.isNullOrEmpty(ConfigUtil.getConfigPrint().getFontType())) {
            layout_order = layout_order.replaceAll("monospace", ConfigUtil.getConfigPrint().getFontType());
        }
        return layout_order;
    }
}
