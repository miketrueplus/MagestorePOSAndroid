package com.magestore.app.pos.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.registershift.CashBox;
import com.magestore.app.lib.model.registershift.OpenSessionValue;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SaleSummary;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.PrintDialogActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.RegisterShiftCashListController;
import com.magestore.app.pos.controller.RegisterShiftListController;
import com.magestore.app.pos.controller.RegisterShiftSaleListController;
import com.magestore.app.pos.databinding.PanelRegisterShiftDetailBinding;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.util.PrintUtil;
import com.magestore.app.pos.util.StarPrintUtil;
import com.magestore.app.pos.view.MagestoreDialog;
import com.magestore.app.util.ConfigUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftDetailPanel extends AbstractDetailPanel<RegisterShift> {
    View v;
    PanelRegisterShiftDetailBinding mBinding;
    RegisterShiftSaleListPanel mRegisterShiftSaleListPanel;
    RegisterShiftSaleListController mRegisterShiftSaleListController;
    RegisterShiftCashListPanel mRegisterShiftCashListPanel;
    RegisterShiftCashListController mRegisterShiftCashListController;
    TextView tv_staff_name;
    TextView tv_location;
    CloseSessionPanel panelCloseSessionPanel;
    RelativeLayout register_shift_background_loading;
    MagestoreDialog dialogCloseSession;
    RegisterShift registerShift;

    public RegisterShiftDetailPanel(Context context) {
        super(context);
    }

    public RegisterShiftDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterShiftDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        // Load layout view danh sách register shift
        v = inflate(getContext(), R.layout.panel_register_shift_detail, null);
        addView(v);
        mBinding = DataBindingUtil.bind(v);

        tv_staff_name = (TextView) v.findViewById(R.id.tv_staff_name);
        tv_location = (TextView) v.findViewById(R.id.tv_location);

        // chuẩn bị panel view danh sách payment
        mRegisterShiftSaleListPanel = (RegisterShiftSaleListPanel) findViewById(R.id.register_shift_sales);

        // chuẩn bị panel view danh sách cash transaction
        mRegisterShiftCashListPanel = (RegisterShiftCashListPanel) findViewById(R.id.register_shift_cash);

        register_shift_background_loading = (RelativeLayout) findViewById(R.id.register_shift_background_loading);
    }

    @Override
    public void initModel() {
        // Lấy lại customer service từ controller của panel
        Controller controller = getController();

        // Controller Payment
        mRegisterShiftSaleListController = new RegisterShiftSaleListController();
        mRegisterShiftSaleListController.setView(mRegisterShiftSaleListPanel);
        mRegisterShiftSaleListController.setMagestoreContext(controller.getMagestoreContext());

        mRegisterShiftCashListController = new RegisterShiftCashListController();
        mRegisterShiftCashListController.setView(mRegisterShiftCashListPanel);
        mRegisterShiftCashListController.setMagestoreContext(controller.getMagestoreContext());

        if (controller instanceof RegisterShiftListController) {
            mRegisterShiftSaleListController.setRegisterShiftService(((RegisterShiftListController) controller).getRegisterShiftService());
            mRegisterShiftCashListController.setRegisterShiftService(((RegisterShiftListController) controller).getRegisterShiftService());
        }
    }

    @Override
    public void bindItem(final RegisterShift item) {
        super.bindItem(item);
        registerShift = item;
        mBinding.setRegisterShift(item);
        tv_staff_name.setText(ConfigUtil.getStaff().getStaffName());
        tv_location.setText(item.getPosName());
        mRegisterShiftSaleListController.doSelectRegisterShift(item);
        mRegisterShiftCashListController.doSelectRegisterShift(item);
        mRegisterShiftCashListPanel.setRegisterShift(item);

        Button make_adjustment_put_money = (Button) v.findViewById(R.id.make_adjustment_put_money);
        make_adjustment_put_money.setVisibility(ConfigUtil.isManagerShiftAdjustment() ? VISIBLE : GONE);
        Button make_adjustment_take_money = (Button) v.findViewById(R.id.make_adjustment_take_money);
        make_adjustment_take_money.setVisibility(ConfigUtil.isManagerShiftAdjustment() ? VISIBLE : GONE);
        ((View) v.findViewById(R.id.fr_button_1)).setVisibility(ConfigUtil.isManagerShiftAdjustment() ? GONE : VISIBLE);
        ((View) v.findViewById(R.id.fr_button_2)).setVisibility(ConfigUtil.isManagerShiftAdjustment() ? GONE : VISIBLE);
        make_adjustment_put_money.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showMakeAdjustment(item, true);
            }
        });
        make_adjustment_take_money.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showMakeAdjustment(item, false);
            }
        });

        ((Button) v.findViewById(R.id.close_shift)).setVisibility(ConfigUtil.isCloseShift() ? VISIBLE : GONE);
        ((Button) v.findViewById(R.id.close_shift)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showCloseShift(item);
            }
        });

        ((Button) v.findViewById(R.id.print)).setVisibility(ConfigUtil.isPrintSession() ? VISIBLE : GONE);
        ((Button) v.findViewById(R.id.print)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionPrint(item);
            }
        });
    }

    private void showMakeAdjustment(RegisterShift item, boolean isType) {
        final RegisterShiftMakeAdjustmentPanel panelMakeAdjustment = new RegisterShiftMakeAdjustmentPanel(getContext());
        panelMakeAdjustment.setType(isType);
        panelMakeAdjustment.bindItem(item);
        panelMakeAdjustment.setController(mController);
        final MagestoreDialog dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.register_shift_dialog_make_adjustment_title), panelMakeAdjustment);
        dialog.setDialogTitle(isType ? getContext().getString(R.string.register_shift_bottom_put_money_in) : getContext().getString(R.string.register_shift_bottom_take_money_out));
        dialog.setDialogWidth(getContext().getResources().getDimensionPixelSize(R.dimen.dialog_width));
        dialog.setGoneButtonSave(true);
        dialog.show();

        Button bt_set_adjustment = (Button) dialog.findViewById(R.id.bt_set_adjustment);
        bt_set_adjustment.setText(isType ? getContext().getString(R.string.register_shift_bottom_put_money_in) : getContext().getString(R.string.register_shift_bottom_take_money_out));
        bt_set_adjustment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterShift registerShift = panelMakeAdjustment.bind2Item();
                if (panelMakeAdjustment.getSelectMakeAdjustment().equals(RegisterShiftMakeAdjustmentPanel.ADD_MAKE_ADJUSTMENT)) {
                    dialog.dismiss();
                    ((RegisterShiftListController) mController).doInputMakeAdjustment(registerShift);
                } else {
                    if (registerShift.getParamCash().getValue() > 0 && registerShift.getParamCash().getValue() <= ConfigUtil.convertToPrice(registerShift.getBaseBalance())) {
                        dialog.dismiss();
                        ((RegisterShiftListController) mController).doInputMakeAdjustment(registerShift);
                    } else if (registerShift.getParamCash().getValue() == 0) {
                        panelMakeAdjustment.showErrorAmountGreat();
                    } else {
                        panelMakeAdjustment.showErrorAmountLess();
                    }
                }
            }
        });
    }

    public void showCloseShift(RegisterShift item) {
        panelCloseSessionPanel = new CloseSessionPanel(getContext());
        ((RegisterShiftListController) mController).setPanelCloseSessionPanel(panelCloseSessionPanel);
        panelCloseSessionPanel.setController(mController);
        panelCloseSessionPanel.initValue();
        panelCloseSessionPanel.initModel();
        panelCloseSessionPanel.bindItem(item);
        dialogCloseSession = DialogUtil.dialog(getContext(), getContext().getString(R.string.register_shift_dialog_close_session_title), panelCloseSessionPanel);
        dialogCloseSession.setDialogWidth(getContext().getResources().getDimensionPixelSize(R.dimen.order_dialog_refund_width));
        dialogCloseSession.setGoneDialogTitle(true);
        dialogCloseSession.setCancelBack(true);
        dialogCloseSession.setCanceledOnTouchOutside(false);
        ((RegisterShiftListController) mController).clearListValueClose();
        dialogCloseSession.show();
    }

    public void dismissDialogCloseSession() {
        dialogCloseSession.dismiss();
    }

    public void bindItemCloseSessionPanel(RegisterShift item) {
        panelCloseSessionPanel.bindItem(item);
    }

    public void updateFloatAmount(float total) {
        panelCloseSessionPanel.updateFloatAmount(total);
    }

    public void updateCashBoxClose(HashMap<OpenSessionValue, CashBox> mCashBox) {
        panelCloseSessionPanel.updateCashBoxClose(mCashBox);
    }

    public void isShowLoadingDetail(boolean isShow) {
        register_shift_background_loading.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void showDialogMakeAdjusment(RegisterShift item, boolean isType) {
        dialogCloseSession.dismiss();
        showMakeAdjustment(item, isType);
    }

    private void actionPrint(RegisterShift registerShift) {
        final Dialog dialogPrint = new Dialog(getContext());
        dialogPrint.setCancelable(true);
        dialogPrint.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPrint.setFeatureDrawableAlpha(1, 1);

        if (ConfigUtil.getTypePrint().equals(getContext().getString(R.string.print_type_star_print))) {
            dialogPrint.setContentView(R.layout.star_print_session_layout);
            ViewGroup.LayoutParams params = dialogPrint.getWindow().getAttributes();
            if (ConfigUtil.getStarPrintArea() == 384) {
                params.width = ConfigUtil.getStarPrintArea() + 150;
            } else {
                params.width = ConfigUtil.getStarPrintArea() + 50;
            }
            dialogPrint.getWindow().setAttributes((WindowManager.LayoutParams) params);
        } else {
            dialogPrint.setContentView(R.layout.print_register_shift_preview);
        }

        TextView bt_print = (TextView) dialogPrint.findViewById(R.id.dialog_save);
        bt_print.setText(getContext().getString(R.string.print));
        TextView dialog_cancel = (TextView) dialogPrint.findViewById(R.id.dialog_cancel);
        dialog_cancel.setText(getContext().getString(R.string.cancel));
        TextView dialog_title = (TextView) dialogPrint.findViewById(R.id.dialog_title);

        if (!ConfigUtil.getTypePrint().equals(getContext().getString(R.string.print_type_star_print))) {
            WebView dialogWebView = (WebView) dialogPrint.findViewById(R.id.webview);
            dialogWebView.getSettings().setJavaScriptEnabled(true);
            dialogWebView.getSettings().setLoadsImagesAutomatically(true);
            dialogWebView.setDrawingCacheEnabled(true);
            dialogWebView.buildDrawingCache();
            dialogWebView.capturePicture();
            PrintUtil.doPrintRegisterShift(getContext(), registerShift, dialogWebView);

            bt_print.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/RetailerPOS/PrintZReport.pdf");
                    Intent printIntent = new Intent(getContext(),
                            PrintDialogActivity.class);
                    printIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
                    printIntent.putExtra("title", "");
                    getContext().startActivity(printIntent);
                }
            });
        } else {
            final LinearLayout rl = (LinearLayout) dialogPrint.findViewById(R.id.ll_content);
            rl.setDrawingCacheEnabled(true);

            TextView tv_draw_number = (TextView) dialogPrint.findViewById(R.id.tv_draw_number);
            tv_draw_number.setText(getContext().getString(R.string.shift) + "#" + registerShift.getShiftId() + "#" + ConfigUtil.getStaff().getStaffName());

            TextView tv_opened = (TextView) dialogPrint.findViewById(R.id.tv_opened);
            tv_opened.setText(ConfigUtil.formatDateTime(registerShift.getOpenedAt()));

            TextView tv_closed = (TextView) dialogPrint.findViewById(R.id.tv_closed);
            tv_closed.setText(ConfigUtil.formatDateTime(registerShift.getClosedAt()));

            TextView tv_title_transaction = (TextView) dialogPrint.findViewById(R.id.tv_title_transaction);
            tv_title_transaction.setText("#" + getContext().getString(R.string.close_session_transaction_title));

            TextView tv_open_amount = (TextView) dialogPrint.findViewById(R.id.tv_open_amount);
            tv_open_amount.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(registerShift.getBaseFloatAmount())));

            TextView tv_close_amount = (TextView) dialogPrint.findViewById(R.id.tv_close_amount);
            tv_close_amount.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(registerShift.getBaseClosedAmount())));

            TextView tv_cash_sales = (TextView) dialogPrint.findViewById(R.id.tv_cash_sales);
            tv_cash_sales.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(registerShift.getBaseCashSales())));

            TextView tv_cash_add = (TextView) dialogPrint.findViewById(R.id.tv_cash_add);
            tv_cash_add.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(registerShift.getBaseCashAdded())));

            TextView tv_cash_remove = (TextView) dialogPrint.findViewById(R.id.tv_cash_remove);
            tv_cash_remove.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(registerShift.getBaseCashRemoved())));

            TextView tv_title_sales = (TextView) dialogPrint.findViewById(R.id.tv_title_sales);
            tv_title_sales.setText("#" + getContext().getString(R.string.sales));

            TextView tv_total_sales = (TextView) dialogPrint.findViewById(R.id.tv_total_sales);
            tv_total_sales.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(registerShift.getBaseTotalSales())));

            TextView tv_discount = (TextView) dialogPrint.findViewById(R.id.tv_discount);
            tv_discount.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(registerShift.getZrepostSalesSummary().getDiscountAmount())));

            LinearLayout ll_gift_voucher_discount = (LinearLayout) dialogPrint.findViewById(R.id.ll_gift_voucher_discount);
            View line_gift_voucher_discount = (View) dialogPrint.findViewById(R.id.line_gift_voucher_discount);
            ll_gift_voucher_discount.setVisibility(registerShift.getZrepostSalesSummary().getGiftvoucherDiscount() > 0 ? VISIBLE : GONE);
            line_gift_voucher_discount.setVisibility(registerShift.getZrepostSalesSummary().getGiftvoucherDiscount() > 0 ? VISIBLE : GONE);
            TextView tv_gift_voucher_discount = (TextView) dialogPrint.findViewById(R.id.tv_gift_voucher_discount);
            tv_gift_voucher_discount.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(registerShift.getZrepostSalesSummary().getGiftvoucherDiscount())));

            LinearLayout ll_point_discount = (LinearLayout) dialogPrint.findViewById(R.id.ll_point_discount);
            View line_point_discount = (View) dialogPrint.findViewById(R.id.line_point_discount);
            ll_point_discount.setVisibility(registerShift.getZrepostSalesSummary().getRewardpointsDiscount() > 0 ? VISIBLE : GONE);
            line_point_discount.setVisibility(registerShift.getZrepostSalesSummary().getRewardpointsDiscount() > 0 ? VISIBLE : GONE);
            TextView tv_point_discount = (TextView) dialogPrint.findViewById(R.id.tv_point_discount);
            tv_point_discount.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(registerShift.getZrepostSalesSummary().getRewardpointsDiscount())));

            TextView tv_refund = (TextView) dialogPrint.findViewById(R.id.tv_refund);
            tv_refund.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(registerShift.getZrepostSalesSummary().getTotalRefunded())));

            TextView tv_title_payment_method = (TextView) dialogPrint.findViewById(R.id.tv_title_payment_method);
            tv_title_payment_method.setText("#" + getContext().getString(R.string.register_shift_report_payment_title));

            StarPrinSessiontListPaymentPanel listPaymentPanel = (StarPrinSessiontListPaymentPanel) dialogPrint.findViewById(R.id.session_item_payment_panel);
            List<SaleSummary> listPayment = registerShift.getSalesSummary();
            if (listPayment != null && listPayment.size() > 0) {
                tv_title_payment_method.setVisibility(VISIBLE);
                listPaymentPanel.setVisibility(VISIBLE);
                listPaymentPanel.bindList(listPayment);
            } else {
                tv_title_payment_method.setVisibility(GONE);
                listPaymentPanel.setVisibility(GONE);
            }

            bt_print.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bitmap bitmap = rl.getDrawingCache();
                    StarPrintUtil.showSearchPrint(getContext(), ((RegisterShiftListController) mController).getMagestoreContext().getActivity(), bitmap);
                }
            });
        }

        dialogPrint.show();

        dialog_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPrint.dismiss();
            }
        });
    }
}
