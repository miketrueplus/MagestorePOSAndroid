package com.magestore.app.pos.panel;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.RegisterShiftListController;
import com.magestore.app.pos.databinding.CardRegisterShiftListContentBinding;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.view.MagestoreDialog;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftListPanel extends AbstractListPanel<RegisterShift> {
    RegisterOpenSessionPanel openSessionPanel;
    MagestoreDialog dialogOpenSession;
    FloatingActionButton fab;
    RelativeLayout rl_open_session;
    Toolbar mToolbar;
    LinearLayout content_other, content_register_shift_list_card_view;
    TextView tv_other;

    public void setToolbar(Toolbar mToolbar) {
        this.mToolbar = mToolbar;
    }

    public RegisterShiftListPanel(Context context) {
        super(context);
    }

    public RegisterShiftListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterShiftListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initLayout() {
        super.initLayout();

        // Xử lý sự kiện floating action bar
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public void initModel() {
        rl_open_session = (RelativeLayout) mToolbar.findViewById(R.id.rl_open_session);
        rl_open_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSession();
            }
        });
    }

    @Override
    protected void bindItem(View view, RegisterShift item, int position) {
        content_other = (LinearLayout) view.findViewById(R.id.content_other);
        tv_other = (TextView) view.findViewById(R.id.tv_other);
        content_register_shift_list_card_view = (LinearLayout) view.findViewById(R.id.content_register_shift_list_card_view);
        if (item.getLastSevenDay()) {
            content_register_shift_list_card_view.setVisibility(GONE);
            content_other.setVisibility(VISIBLE);
            tv_other.setText(getContext().getString(R.string.register_shift_last_seven_days));
        } else if (item.getLessSevenDay()) {
            content_register_shift_list_card_view.setVisibility(GONE);
            content_other.setVisibility(VISIBLE);
            tv_other.setText(getContext().getString(R.string.other));
        } else {
            content_register_shift_list_card_view.setVisibility(VISIBLE);
            content_other.setVisibility(GONE);
            CardRegisterShiftListContentBinding binding = DataBindingUtil.bind(view);
            binding.setRegisterShift(item);
        }
    }

    public void openSession() {
        openSessionPanel = new RegisterOpenSessionPanel(getContext());
        ((RegisterShiftListController) getController()).setOpenSessionPanel(openSessionPanel);
        openSessionPanel.setRegisterShiftListController((RegisterShiftListController) getController());
        openSessionPanel.initModel();
        dialogOpenSession = DialogUtil.dialog(getContext(), getContext().getString(R.string.open_session_title), openSessionPanel);
        dialogOpenSession.setDialogWidth(getContext().getResources().getDimensionPixelSize(R.dimen.order_dialog_refund_width));
        dialogOpenSession.setGoneDialogTitle(true);
        dialogOpenSession.setDialogCancel(getContext().getString(R.string.cancel));
        ((RegisterShiftListController) getController()).clearListValueOpen();
        dialogOpenSession.show();
    }

    public void isShowButtonOpenSession(boolean isShow) {
        rl_open_session.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void dismissDialogOpenSession() {
        dialogOpenSession.dismiss();
    }

    public void updateFloatAmount(float total) {
        openSessionPanel.updateFloatAmount(total);
    }

    public void showDialogContinueCheckout() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.ask_are_you_sure_go_to_checkout)
                .setPositiveButton(R.string.register_shift_continue, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((RegisterShiftListController) getController()).getMagestoreContext().getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    public void setSelectPosition() {
        setMintSelectedPos(1);
        notifyDataSetChanged();
    }
}
