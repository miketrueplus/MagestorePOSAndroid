package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSession();
            }
        });
    }

    @Override
    protected void bindItem(View view, RegisterShift item, int position) {
        CardRegisterShiftListContentBinding binding = DataBindingUtil.bind(view);
        binding.setRegisterShift(item);
    }

    public void openSession() {
        openSessionPanel = new RegisterOpenSessionPanel(getContext());
        openSessionPanel.setRegisterShiftListController((RegisterShiftListController) getController());
        openSessionPanel.initModel();
        dialogOpenSession = DialogUtil.dialog(getContext(), getContext().getString(R.string.open_session_title), openSessionPanel);
        dialogOpenSession.setFullScreen(true);
        dialogOpenSession.setTransparent(true);
        dialogOpenSession.setGoneDialogTitle(true);
        dialogOpenSession.show();
    }

    public void isShowButtonOpenSession(boolean isShow){
        fab.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void dismissDialogOpenSession(){
        dialogOpenSession.dismiss();
    }

    public void updateFloatAmount(float total) {
        openSessionPanel.updateFloatAmount(total);
    }
}
