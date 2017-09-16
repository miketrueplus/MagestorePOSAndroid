package com.magestore.app.pos.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.pos.R;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.ICommandBuilder.AlignmentPosition;
import com.starmicronics.starioextension.ICommandBuilder.BarcodeSymbology;
import com.starmicronics.starioextension.ICommandBuilder.BarcodeWidth;
import com.starmicronics.starioextension.ICommandBuilder.CodePageType;
import com.starmicronics.starioextension.ICommandBuilder.InternationalType;
import com.starmicronics.starioextension.StarIoExt.CharacterCode;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EnglishReceiptsImpl extends ILocalizeReceipts {

    public EnglishReceiptsImpl() {
        mLanguageCode = "En";

        mCharacterCode = CharacterCode.Standard;
    }

    @Override
    public void appendTextCustomReceiptData(ICommandBuilder builder, boolean utf8, Context context, Order mOrder, int length) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");
            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");
            builder.appendCodePage(CodePageType.CP998);
        }
        builder.appendInternational(InternationalType.USA);
        builder.appendCharacterSpace(0);
        builder.appendAlignment(AlignmentPosition.Center);

        // Start Header
        String invoice_title = context.getString(R.string.order_detail_bottom_btn_invoice).toUpperCase();
        builder.appendMultiple((invoice_title + "\n").getBytes(encoding), 2, 2);
        builder.appendMultiple(("**** ****\n").getBytes(encoding), 1, 1);
        if (!StringUtil.isNullOrEmpty(ConfigUtil.getConfigPrint().getHeaderText())) {
            builder.append((ConfigUtil.getConfigPrint().getHeaderText() + "\n").getBytes(encoding));
        }
        String increment_id = context.getString(R.string.checkout_order_id, mOrder.getIncrementId());
        builder.append((increment_id + "\n").getBytes(encoding));
        String create_date = ConfigUtil.formatDateTime(mOrder.getCreatedAt());
        builder.append((create_date + "\n").getBytes(encoding));
        if (!StringUtil.isNullOrEmpty(ConfigUtil.getConfigPrint().getShowCashierName())) {
            if (ConfigUtil.getConfigPrint().getShowCashierName().equals("1") && !StringUtil.isNullOrEmpty(mOrder.getWebposStaffName())) {
                String cashier_title = context.getString(R.string.print_header_cashier) + " " + mOrder.getWebposStaffName().toUpperCase();
                builder.append((cashier_title + "\n").getBytes(encoding));
            }
        }
        if (mOrder.getBillingAddress() != null && !StringUtil.isNullOrEmpty(mOrder.getBillingAddress().getName())) {
            if (!mOrder.getBillingAddress().getName().equals(ConfigUtil.getCustomerGuest().getName())) {
                String customer_name = context.getString(R.string.print_header_customer) + " " + mOrder.getBillingAddress().getName();
                builder.append((customer_name + "\n").getBytes(encoding));
            }
        }
        // End Header
        builder.appendLineFeed();

        // Start Items
        String title_item = context.getString(R.string.items);
        String title_qty = context.getString(R.string.order_detail_content_item_qty);
        String title_price = context.getString(R.string.price);
        String title_sutotal = context.getString(R.string.order_detail_content_item_subtotal);
        String line = lineWith(title_item, title_qty, title_price, title_sutotal, length);
        builder.append((line).getBytes(encoding));
        if (mOrder.getOrderItems() != null && mOrder.getOrderItems().size() > 0) {
            List<CartItem> listOrder = new ArrayList<>();
            for (CartItem item : mOrder.getOrderItems()) {
                if (item.getOrderParentItem() == null) {
                    listOrder.add(item);
                }
            }
            if (listOrder != null && listOrder.size() > 0) {
                for (CartItem item : listOrder) {
                    String line_item = lineWith(item.getName(), ConfigUtil.formatQuantity(item.getQtyOrdered()), ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(item.getBasePrice())), ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(item.getBaseSubTotal())), length);
                    builder.append((line_item).getBytes(encoding));
                }
            }
        }
        // End Items
        builder.appendLineFeed();

        // Start Total
        if (mOrder.getBaseSubtotal() > 0) {
            String subtotal = ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(mOrder.getBaseSubtotal()));
            String title_subtotal = context.getString(R.string.order_detail_bottom_tb_subtotal);
            String space = stringSpaceWith(length - title_subtotal.length() - subtotal.length());
            builder.append((title_subtotal + space + subtotal + "\n").getBytes(encoding));
        }
        if (mOrder.getRewardPointsEarn() != 0) {
            String reward_point_earn = ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(mOrder.getRewardPointsEarn()));
            String title_earn = context.getString(R.string.plugin_order_detail_bottom_reward_earn);
            String space = stringSpaceWith(length - title_earn.length() - reward_point_earn.length());
            builder.append((title_earn + space + reward_point_earn + "\n").getBytes(encoding));
        }
        if (mOrder.getRewardPointsSpent() != 0) {
            String reward_point_spent = ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(mOrder.getRewardPointsSpent()));
            String title_spent = context.getString(R.string.plugin_order_detail_bottom_reward_spend);
            String space = stringSpaceWith(length - title_spent.length() - reward_point_spent.length());
            builder.append((title_spent + space + reward_point_spent + "\n").getBytes(encoding));
        }
        if (mOrder.getBaseShippingAmount() != 0) {
            String shipping = ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(mOrder.getBaseShippingAmount()));
            String title_shipping = context.getString(R.string.order_detail_bottom_tb_shipping);
            String space = stringSpaceWith(length - title_shipping.length() - shipping.length());
            builder.append((title_shipping + space + shipping + "\n").getBytes(encoding));
        }
        if (mOrder.getBaseTaxAmount() != 0) {
            String tax = ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(mOrder.getBaseTaxAmount()));
            String title_tax = context.getString(R.string.order_detail_bottom_tb_tax);
            String space = stringSpaceWith(length - title_tax.length() - tax.length());
            builder.append((title_tax + space + tax + "\n").getBytes(encoding));
        }
        if (mOrder.getBaseDiscountAmount() != 0) {
            String discount = ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(mOrder.getBaseDiscountAmount()));
            String title_discount = context.getString(R.string.order_detail_bottom_tb_discount);
            String space = stringSpaceWith(length - title_discount.length() - discount.length());
            builder.append((title_discount + space + discount + "\n").getBytes(encoding));
        }
        if (mOrder.getBaseGiftVoucherDiscount() != 0) {
            String giftcard_discount = ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(mOrder.getBaseGiftVoucherDiscount()));
            String title_giftcard_discount = context.getString(R.string.plugin_order_detail_bottom_giftcard_discount);
            String space = stringSpaceWith(length - title_giftcard_discount.length() - giftcard_discount.length());
            builder.append((title_giftcard_discount + space + giftcard_discount + "\n").getBytes(encoding));
        }
        if (mOrder.getRewardPointsBaseDiscount() != 0) {
            String reward_discount = ConfigUtil.formatDecimalQuantity(ConfigUtil.convertToPrice(mOrder.getRewardPointsBaseDiscount()));
            String title_reward_discount = context.getString(R.string.plugin_order_detail_bottom_reward_discount);
            String space = stringSpaceWith(length - title_reward_discount.length() - reward_discount.length());
            builder.append((title_reward_discount + space + reward_discount + "\n").getBytes(encoding));
        }
        // grand total
        builder.append(stringDashLine(length).getBytes(encoding));
        String grand_total = ConfigUtil.formatPrice(ConfigUtil.convertToPrice(mOrder.getBaseGrandTotal()));
        String title_grand_total = context.getString(R.string.order_detail_bottom_tb_grand_total);
        String line_grand_total = title_grand_total + stringSpaceWith(length - title_grand_total.length() - (2 * grand_total.length()));
        builder.append((line_grand_total).getBytes(encoding));
        builder.appendMultiple((grand_total).getBytes(encoding), 2, 2);
        builder.append(("\n").getBytes(encoding));
        builder.append(stringDashLine(length).getBytes(encoding));
        // total paid
        if (mOrder.getWebposBaseChange() != 0) {
            String change = ConfigUtil.formatPrice(ConfigUtil.convertToPrice(mOrder.getWebposBaseChange()));
            String title_change = context.getString(R.string.order_detail_bottom_tb_total_paid);
            String space = stringSpaceWith(length - title_change.length() - change.length());
            builder.append((title_change + space + change + "\n").getBytes(encoding));
        }
        // total change
        if (mOrder.getBaseTotalPaid() != 0) {
            String paid = ConfigUtil.formatPrice(ConfigUtil.convertToPrice(mOrder.getBaseTotalPaid()));
            String title_paid = context.getString(R.string.order_detail_bottom_tb_total_change);
            String space = stringSpaceWith(length - title_paid.length() - paid.length());
            builder.append((title_paid + space + paid + "\n").getBytes(encoding));
        }
        // End Total
        builder.appendLineFeed();

        // start comment
        builder.appendAlignment(AlignmentPosition.Left);
        List<OrderStatus> listComment = mOrder.getOrderStatus();
        if (!StringUtil.isNullOrEmpty(ConfigUtil.getConfigPrint().getShowComment())) {
            if (ConfigUtil.getConfigPrint().getShowComment().equals("1") && listComment != null && listComment.size() > 0) {
                String title_comment = context.getString(R.string.order_add_comment_title);
                String line_total = title_comment + stringSpaceWith(length - title_comment.length());
                builder.append((line_total + "\n").getBytes(encoding));

                for (OrderStatus status : listComment) {
                    String comment = !StringUtil.isNullOrEmpty(status.getComment()) ? !StringUtil.isNullOrEmpty(status.getCreatedAt()) ? (status.getCreatedAt() + ": ") : "" + status.getComment() : "";
                    if (!StringUtil.isNullOrEmpty(comment)) {
                        String commentFinal = lineCommentWith(comment, length);
                        builder.append((commentFinal).getBytes(encoding));
                    }
                }
            }
        }
        // end comment

        // start footer
        builder.appendAlignment(AlignmentPosition.Center);
        builder.append(("-------- **** --------\n").getBytes(encoding));
        if (!StringUtil.isNullOrEmpty(ConfigUtil.getConfigPrint().getFooterText())) {
            builder.append((ConfigUtil.getConfigPrint().getFooterText()).getBytes(encoding));
        } else {
            builder.append((context.getString(R.string.print_footer_default)).getBytes(encoding));
        }
        // end footer
    }

    private String lineWith(String item, String qty, String price, String subtotal, int length) {
        int lengthItemQty = (int) length * 3 / 5;
        int lengthPriceSubtotal = length - lengthItemQty;

        int lengthQty = 6;
        int lengthItem = lengthItemQty - lengthQty;

        int numSpace0 = lengthItem - item.length();
        int numSpace1 = lengthQty - qty.length();
        int numSpace2 = lengthPriceSubtotal - price.length() - subtotal.length();

        StringBuilder strReturn = new StringBuilder();

        if (item.length() >= lengthItem) {
            String[] arrayItem = item.split(" ");
            boolean haveFirstRow = false;
            String subName = "";
            for (int i = 0; i < arrayItem.length; i++) {
                if (subName.equals("")) {
                    subName = arrayItem[i];
                } else {
                    subName = subName + " " + arrayItem[i];
                }

                if (i != arrayItem.length - 1) {
                    if ((subName.length() + arrayItem[i + 1].length() + 1) >= lengthItem) {
                        if (haveFirstRow) {
                            strReturn.append(" ").append(subName).append(stringSpaceWith(length - subName.length() - 1)).append("\n");
                        } else {
                            strReturn.append(subName).append(stringSpaceWith(lengthItem - subName.length())).append(qty).append(stringSpaceWith(numSpace1)).append(price).append(stringSpaceWith(numSpace2)).append(subtotal).append("\n");
                            haveFirstRow = true;
                        }
                        subName = "";
                    }
                } else {
                    strReturn.append(" ").append(subName).append(stringSpaceWith(length - subName.length() - 1)).append("\n");
                }
            }
        } else {
            strReturn.append(item).append(stringSpaceWith(numSpace0)).append(qty).append(stringSpaceWith(numSpace1)).append(price).append(stringSpaceWith(numSpace2)).append(subtotal).append("\n");
        }

        return strReturn.toString();
    }

    private String lineCommentWith(String comment, int length) {
        StringBuilder strReturn = new StringBuilder();
        if (comment.length() > (length - 1)) {
            String[] arrayItem = comment.split(" ");
            boolean haveFirstRow = false;
            String subName = "";
            for (int i = 0; i < arrayItem.length; i++) {
                if (subName.equals("")) {
                    subName = arrayItem[i];
                } else {
                    subName = subName + " " + arrayItem[i];
                }

                if (i != arrayItem.length - 1) {
                    if ((subName.length() + arrayItem[i + 1].length() + 1) > (length - 2)) {
                        if (haveFirstRow) {
                            strReturn.append(" ").append(subName).append(stringSpaceWith(length - subName.length() - 2)).append("\n");
                        } else {
                            strReturn.append("-").append(subName).append(stringSpaceWith(length - subName.length() - 1)).append("\n");
                            haveFirstRow = true;
                        }
                        subName = "";
                    }
                } else {
                    strReturn.append(" ").append(subName).append(stringSpaceWith(length - subName.length() - 2)).append("\n");
                }
            }
        } else {
            strReturn.append("-").append(comment).append(stringSpaceWith(length - comment.length() - 1)).append("\n");
        }
        return strReturn.toString();
    }

    private String stringSpaceWith(int length) {
        String line = "";
        for (int i = 0; i < length; i++) {
            line += " ";
        }
        return line;
    }

    private String stringDashLine(int length) {
        String line = "";
        for (int i = 0; i < length; i++) {
            line += "-";
        }
        return line;
    }

    @Override
    public void append2inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.appendAlignment(AlignmentPosition.Center);

        builder.append((
                "Star Clothing Boutique\n" +
                        "123 Star Road\n" +
                        "City, State 12345\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Left);

        builder.append((
                "Date:MM/DD/YYYY    Time:HH:MM PM\n" +
                        "--------------------------------\n" +
                        "\n").getBytes(encoding));

        builder.append((
                "SKU         Description    Total\n" +
                        "300678566   PLAIN T-SHIRT  10.99\n" +
                        "300692003   BLACK DENIM    29.99\n" +
                        "300651148   BLUE DENIM     29.99\n" +
                        "300642980   STRIPED DRESS  49.99\n" +
                        "300638471   BLACK BOOTS    35.99\n" +
                        "\n" +
                        "Subtotal                  156.95\n" +
                        "Tax                         0.00\n" +
                        "--------------------------------\n").getBytes(encoding));

        builder.append(("Total     ").getBytes(encoding));

        builder.appendMultiple(("   $156.95\n").getBytes(encoding), 2, 2);

        builder.append((
                "--------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "\n").getBytes(encoding));

        builder.appendInvert(("Refunds and Exchanges\n").getBytes(encoding));

        builder.append(("Within ").getBytes(encoding));

        builder.appendUnderLine(("30 days").getBytes(encoding));

        builder.append((" with receipt\n").getBytes(encoding));

        builder.append((
                "And tags attached\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Center);

        builder.appendBarcode(("{BStar.").getBytes(Charset.forName("US-ASCII")), BarcodeSymbology.Code128, BarcodeWidth.Mode2, 40, true);
    }

    @Override
    public void append3inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.appendAlignment(AlignmentPosition.Center);

        builder.append((
                "Star Clothing Boutique\n" +
                        "123 Star Road\n" +
                        "City, State 12345\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Left);

        builder.append((
                "Date:MM/DD/YYYY                    Time:HH:MM PM\n" +
                        "------------------------------------------------\n" +
                        "\n").getBytes(encoding));

        builder.appendEmphasis(("SALE\n").getBytes(encoding));

        builder.append((
                "SKU               Description              Total\n" +
                        "300678566         PLAIN T-SHIRT            10.99\n" +
                        "300692003         BLACK DENIM              29.99\n" +
                        "300651148         BLUE DENIM               29.99\n" +
                        "300642980         STRIPED DRESS            49.99\n" +
                        "300638471         BLACK BOOTS              35.99\n" +
                        "\n" +
                        "Subtotal                                  156.95\n" +
                        "Tax                                         0.00\n" +
                        "------------------------------------------------\n").getBytes(encoding));

        builder.append(("Total                       ").getBytes(encoding));

        builder.appendMultiple(("   $156.95\n").getBytes(encoding), 2, 2);

        builder.append((
                "------------------------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "\n").getBytes(encoding));

        builder.appendInvert(("Refunds and Exchanges\n").getBytes(encoding));

        builder.append(("Within ").getBytes(encoding));

        builder.appendUnderLine(("30 days").getBytes(encoding));

        builder.append((" with receipt\n").getBytes(encoding));

        builder.append((
                "And tags attached\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Center);

        builder.appendBarcode(("{BStar.").getBytes(Charset.forName("US-ASCII")), BarcodeSymbology.Code128, BarcodeWidth.Mode2, 40, true);
    }

    @Override
    public void append4inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.appendAlignment(AlignmentPosition.Center);

        builder.append((
                "Star Clothing Boutique\n" +
                        "123 Star Road\n" +
                        "City, State 12345\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Left);

        builder.append((
                "Date:MM/DD/YYYY                                         Time:HH:MM PM\n" +
                        "---------------------------------------------------------------------\n" +
                        "\n").getBytes(encoding));

        builder.appendEmphasis(("SALE\n").getBytes(encoding));

        builder.append((
                "SKU                        Description                          Total\n" +
                        "300678566                  PLAIN T-SHIRT                        10.99\n" +
                        "300692003                  BLACK DENIM                          29.99\n" +
                        "300651148                  BLUE DENIM                           29.99\n" +
                        "300642980                  STRIPED DRESS                        49.99\n" +
                        "300638471                  BLACK BOOTS                          35.99\n" +
                        "\n" +
                        "Subtotal                                                       156.95\n" +
                        "Tax                                                              0.00\n" +
                        "---------------------------------------------------------------------\n").getBytes(encoding));

        builder.append(("Total                                            ").getBytes(encoding));

        builder.appendMultiple(("   $156.95\n").getBytes(encoding), 2, 2);

        builder.append((
                "---------------------------------------------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "\n").getBytes(encoding));

        builder.appendInvert(("Refunds and Exchanges\n").getBytes(encoding));

        builder.append(("Within ").getBytes(encoding));

        builder.appendUnderLine(("30 days").getBytes(encoding));

        builder.append((" with receipt\n").getBytes(encoding));

        builder.append((
                "And tags attached\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Center);

        builder.appendBarcode(("{BStar.").getBytes(Charset.forName("US-ASCII")), BarcodeSymbology.Code128, BarcodeWidth.Mode2, 40, true);
    }

    @Override
    public Bitmap create2inchRasterReceiptImage() {
        String textToPrint =
                "   Star Clothing Boutique\n" +
                        "        123 Star Road\n" +
                        "      City, State 12345\n" +
                        "\n" +
                        "Date:MM/DD/YYYY Time:HH:MM PM\n" +
                        "-----------------------------\n" +
                        "SALE\n" +
                        "SKU       Description   Total\n" +
                        "300678566 PLAIN T-SHIRT 10.99\n" +
                        "300692003 BLACK DENIM   29.99\n" +
                        "300651148 BLUE DENIM    29.99\n" +
                        "300642980 STRIPED DRESS 49.99\n" +
                        "30063847  BLACK BOOTS   35.99\n" +
                        "\n" +
                        "Subtotal               156.95\n" +
                        "Tax                      0.00\n" +
                        "-----------------------------\n" +
                        "Total                 $156.95\n" +
                        "-----------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "Refunds and Exchanges\n" +
                        "Within 30 days with receipt\n" +
                        "And tags attached\n";

        int textSize = 22;
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);

        return createBitmapFromText(textToPrint, textSize, PrinterSetting.PAPER_SIZE_TWO_INCH, typeface);
    }

    @Override
    public Bitmap create3inchRasterReceiptImage() {
        String textToPrint =
                "        Star Clothing Boutique\n" +
                        "             123 Star Road\n" +
                        "           City, State 12345\n" +
                        "\n" +
                        "Date:MM/DD/YYYY          Time:HH:MM PM\n" +
                        "--------------------------------------\n" +
                        "SALE\n" +
                        "SKU            Description       Total\n" +
                        "300678566      PLAIN T-SHIRT     10.99\n" +
                        "300692003      BLACK DENIM       29.99\n" +
                        "300651148      BLUE DENIM        29.99\n" +
                        "300642980      STRIPED DRESS     49.99\n" +
                        "30063847       BLACK BOOTS       35.99\n" +
                        "\n" +
                        "Subtotal                        156.95\n" +
                        "Tax                               0.00\n" +
                        "--------------------------------------\n" +
                        "Total                          $156.95\n" +
                        "--------------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "Refunds and Exchanges\n" +
                        "Within 30 days with receipt\n" +
                        "And tags attached\n";

        int textSize = 25;
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);

        return createBitmapFromText(textToPrint, textSize, PrinterSetting.PAPER_SIZE_THREE_INCH, typeface);
    }

    @Override
    public Bitmap create4inchRasterReceiptImage() {
        String textToPrint =
                "                   Star Clothing Boutique\n" +
                        "                        123 Star Road\n" +
                        "                      City, State 12345\n" +
                        "\n" +
                        "Date:MM/DD/YYYY                             Time:HH:MM PM\n" +
                        "---------------------------------------------------------\n" +
                        "SALE\n" +
                        "SKU                     Description                 Total\n" +
                        "300678566               PLAIN T-SHIRT               10.99\n" +
                        "300692003               BLACK DENIM                 29.99\n" +
                        "300651148               BLUE DENIM                  29.99\n" +
                        "300642980               STRIPED DRESS               49.99\n" +
                        "300638471               BLACK BOOTS                 35.99\n" +
                        "\n" +
                        "Subtotal                                           156.95\n" +
                        "Tax                                                  0.00\n" +
                        "---------------------------------------------------------\n" +
                        "Total                                             $156.95\n" +
                        "---------------------------------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "Refunds and Exchanges\n" +
                        "Within 30 days with receipt\n" +
                        "And tags attached\n";

        int textSize = 23;
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);

        return createBitmapFromText(textToPrint, textSize, PrinterSetting.PAPER_SIZE_FOUR_INCH, typeface);
    }

    @Override
    public Bitmap createCouponImage(Resources resources) {
        return BitmapFactory.decodeResource(resources, R.drawable.ic_add_new);
    }

    @Override
    public Bitmap createEscPos3inchRasterReceiptImage() {
        String textToPrint =
                "\n" +
                        "      Star Clothing Boutique\n" +
                        "           123 Star Road\n" +
                        "         City, State 12345\n" +
                        "\n" +
                        "Date:MM/DD/YYYY       Time:HH:MM PM\n" +
                        "-----------------------------------\n" +
                        "SALE\n" +
                        "SKU          Description      Total\n" +
                        "300678566    PLAIN T-SHIRT    10.99\n" +
                        "300692003    BLACK DENIM      29.99\n" +
                        "300651148    BLUE DENIM       29.99\n" +
                        "300642980    STRIPED DRESS    49.99\n" +
                        "30063847     BLACK BOOTS      35.99\n" +
                        "\n" +
                        "Subtotal                     156.95\n" +
                        "Tax                            0.00\n" +
                        "-----------------------------------\n" +
                        "Total                       $156.95\n" +
                        "-----------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "Refunds and Exchanges\n" +
                        "Within 30 days with receipt\n" +
                        "And tags attached\n";

        int textSize = 24;
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);

        return createBitmapFromText(textToPrint, textSize, PrinterSetting.PAPER_SIZE_ESCPOS_THREE_INCH, typeface);
    }

    @Override
    public void appendEscPos3inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.appendAlignment(AlignmentPosition.Center);

        builder.append((
                "\n" +
                        "Star Clothing Boutique\n" +
                        "123 Star Road\n" +
                        "City, State 12345\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Left);

        builder.append((
                "Date:MM/DD/YYYY              Time:HH:MM PM\n" +
                        "------------------------------------------\n" +
                        "\n").getBytes(encoding));

        builder.appendEmphasis(("SALE \n").getBytes(encoding));

        builder.append((
                "SKU            Description           Total\n" +
                        "300678566      PLAIN T-SHIRT         10.99\n" +
                        "300692003      BLACK DENIM           29.99\n" +
                        "300651148      BLUE DENIM            29.99\n" +
                        "300642980      STRIPED DRESS         49.99\n" +
                        "300638471      BLACK BOOTS           35.99\n" +
                        "\n" +
                        "Subtotal                            156.95\n" +
                        "Tax                                   0.00\n" +
                        "------------------------------------------\n").getBytes(encoding));

        builder.append(("Total                 ").getBytes(encoding));

        builder.appendMultiple(("   $156.95\n").getBytes(encoding), 2, 2);

        builder.append((
                "------------------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "\n").getBytes(encoding));

        builder.appendInvert(("Refunds and Exchanges\n").getBytes(encoding));

        builder.append(("Within ").getBytes(encoding));

        builder.appendUnderLine(("30 days").getBytes(encoding));

        builder.append((" with receipt\n").getBytes(encoding));

        builder.append((
                "And tags attached\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Center);

        builder.appendBarcode(("{BStar.").getBytes(Charset.forName("US-ASCII")), BarcodeSymbology.Code128, BarcodeWidth.Mode2, 40, true);
    }

    @Override
    public void appendDotImpact3inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.appendAlignment(AlignmentPosition.Center);

        builder.append((
                "Star Clothing Boutique\n" +
                        "123 Star Road\n" +
                        "City, State 12345\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Left);

        builder.append((
                "Date:MM/DD/YYYY              Time:HH:MM PM\n" +
                        "------------------------------------------\n" +
                        "\n").getBytes(encoding));

        builder.appendEmphasis(("SALE \n").getBytes(encoding));

        builder.append((
                "SKU             Description          Total\n" +
                        "300678566       PLAIN T-SHIRT        10.99\n" +
                        "300692003       BLACK DENIM          29.99\n" +
                        "300651148       BLUE DENIM           29.99\n" +
                        "300642980       STRIPED DRESS        49.99\n" +
                        "300638471       BLACK BOOTS          35.99\n" +
                        "\n" +
                        "Subtotal                            156.95\n" +
                        "Tax                                   0.00\n" +
                        "------------------------------------------\n" +
                        "Total                              $156.95\n" +
                        "------------------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "\n").getBytes(encoding));

        builder.appendInvert(("Refunds and Exchanges\n").getBytes(encoding));

        builder.append(("Within ").getBytes(encoding));

        builder.appendUnderLine(("30 days").getBytes(encoding));

        builder.append((" with receipt\n").getBytes(encoding));
    }

    @Override
    public void appendTextLabelData(ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.appendUnitFeed(20 * 2);

        builder.appendMultipleHeight(2);

        builder.append(("Star Micronics America, Inc.").getBytes(encoding));

        builder.appendUnitFeed(64);

        builder.append(("65 Clyde Road Suite G").getBytes(encoding));

        builder.appendUnitFeed(64);

        builder.append(("Somerset, NJ 08873-9997 U.S.A").getBytes(encoding));

        builder.appendUnitFeed(64);

        builder.appendMultipleHeight(1);
    }

    @Override
    public String createPasteTextLabelString() {
        return "Star Micronics America, Inc.\n" +
                "65 Clyde Road Suite G\n" +
                "Somerset, NJ 08873-9997 U.S.A";
    }

    @Override
    public void appendPasteTextLabelData(ICommandBuilder builder, String pasteText, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.append(pasteText.getBytes(encoding));
    }
}
