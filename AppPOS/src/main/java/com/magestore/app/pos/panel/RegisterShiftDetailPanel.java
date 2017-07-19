package com.magestore.app.pos.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.PrintDialogActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.RegisterShiftCashListController;
import com.magestore.app.pos.controller.RegisterShiftListController;
import com.magestore.app.pos.controller.RegisterShiftSaleListController;
import com.magestore.app.pos.databinding.PanelRegisterShiftDetailBinding;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.util.PrintUtil;
import com.magestore.app.pos.view.MagestoreDialog;
import com.magestore.app.util.ConfigUtil;

import java.io.File;

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

        ((Button) v.findViewById(R.id.close_shift)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showCloseShift(item);
            }
        });

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

    public void isShowLoadingDetail(boolean isShow) {
        register_shift_background_loading.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void showDialogMakeAdjusment(boolean isType) {
        dialogCloseSession.dismiss();
        showMakeAdjustment(registerShift, isType);
    }

    private void actionPrint(RegisterShift registerShift) {
        final Dialog dialogPrint = new Dialog(getContext());
        dialogPrint.setCancelable(true);
        dialogPrint.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPrint.setFeatureDrawableAlpha(1, 1);
        dialogPrint.setContentView(R.layout.print_register_shift_preview);
        WebView dialogWebView = (WebView) dialogPrint.findViewById(R.id.webview);
        TextView bt_print = (TextView) dialogPrint.findViewById(R.id.dialog_save);
        bt_print.setText(getContext().getString(R.string.print));
        TextView dialog_cancel = (TextView) dialogPrint.findViewById(R.id.dialog_cancel);
        dialog_cancel.setText(getContext().getString(R.string.cancel));
        TextView dialog_title = (TextView) dialogPrint.findViewById(R.id.dialog_title);
        dialogWebView.getSettings().setJavaScriptEnabled(true);
        dialogWebView.getSettings().setLoadsImagesAutomatically(true);
        dialogWebView.setDrawingCacheEnabled(true);
        dialogWebView.buildDrawingCache();
        dialogWebView.capturePicture();
        PrintUtil.doPrintRegisterShift(getContext(), registerShift, dialogWebView);
        dialogPrint.show();

        dialog_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPrint.dismiss();
            }
        });

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
    }
}
