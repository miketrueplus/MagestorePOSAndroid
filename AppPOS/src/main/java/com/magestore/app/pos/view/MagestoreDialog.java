package com.magestore.app.pos.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.pos.R;

/**
 * Created by Johan on 1/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class MagestoreDialog extends Dialog {
    private TextView dialog_cancel;
    private TextView dialog_save;
    private TextView dialog_title;
    private FrameLayout root_dialog_content;
    private LinearLayout ll_dialog;
    private RelativeLayout rl_dialog_title;

    private View dialog_content;

    boolean goneButtonSave = false;
    boolean goneButtonCancel = false;
    boolean goneDialogTextTitle = false;
    boolean goneDialogTitle = false;
    boolean full_screen = false;
    boolean transparent = false;

    String dialogTitle;
    String dialogCancel;
    String dialogSave;

    int dialog_width = 0;
    int dialog_height = 0;
    int dialogTitleColor = -1;
    int dialogCancelColor = -1;
    int dialogSaveColor = -1;
    int dialogBackground = -1;
    int dialogTitleBackground = -1;

    public void setGoneDialogTextTitle(boolean goneDialogTextTitle) {
        this.goneDialogTextTitle = goneDialogTextTitle;
    }

    public void setGoneButtonCancel(boolean goneButtonCancel) {
        this.goneButtonCancel = goneButtonCancel;
    }

    public void setGoneButtonSave(boolean goneButtonSave) {
        this.goneButtonSave = goneButtonSave;
    }

    public void setDialogBackground(int dialogBackground) {
        this.dialogBackground = dialogBackground;
    }

    public void setDialogTitleBackground(int dialogTitleBackground) {
        this.dialogTitleBackground = dialogTitleBackground;
    }

    public TextView getButtonSave() {
        return dialog_save;
    }

    public TextView getButtonCancel() {
        return dialog_cancel;
    }

    public TextView getDialogTitle() {
        return dialog_title;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public void setDialogCancel(String dialogCancel) {
        this.dialogCancel = dialogCancel;
    }

    public void setDialogSave(String dialogSave) {
        this.dialogSave = dialogSave;
    }

    public void setDialogTitleColor(int dialogTitleColor) {
        this.dialogTitleColor = dialogTitleColor;
    }

    public void setDialogCancelColor(int dialogCancelColor) {
        this.dialogCancelColor = dialogCancelColor;
    }

    public void setFullScreen(boolean fullScreen){
        full_screen = fullScreen;
    }

    public void setTransparent(boolean transparent){
        this.transparent = transparent;
    }

    public void setDialogWidth(int dialog_width) {
        this.dialog_width = dialog_width;
    }

    public void setDialogHeight(int dialog_height) {
        this.dialog_height = dialog_height;
    }

    public void setDialogSaveColor(int dialogSaveColor) {
        this.dialogSaveColor = dialogSaveColor;
    }

    public void setDialogContent(View dialog_content) {
        this.dialog_content = dialog_content;
    }

    public void setGoneDialogTitle(boolean goneDialogTitle) {
        this.goneDialogTitle = goneDialogTitle;
    }

    public MagestoreDialog(Context context) {
        super(context);
    }

    public MagestoreDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MagestoreDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magestore_dialog);
        ll_dialog = (LinearLayout) findViewById(R.id.ll_dialog);
        if(transparent){
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }else{
            ll_dialog.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dialog_background_color));
        }

        if (full_screen) {
            ViewGroup.LayoutParams params = getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
        } else {
            if (dialog_width > 0 || dialog_height > 0) {
                ViewGroup.LayoutParams params = getWindow().getAttributes();
                if (dialog_width > 0)
                    params.width = dialog_width;
                if (dialog_height > 0)
                    params.height = dialog_height;
                getWindow().setAttributes((WindowManager.LayoutParams) params);
            }
        }

        rl_dialog_title = (RelativeLayout) findViewById(R.id.rl_dialog_title);
        dialog_cancel = (TextView) findViewById(R.id.dialog_cancel);
        dialog_save = (TextView) findViewById(R.id.dialog_save);
        dialog_title = (TextView) findViewById(R.id.dialog_title);

        root_dialog_content = (FrameLayout) findViewById(R.id.dialog_content);

        // add panel vào dialog
        root_dialog_content.addView(dialog_content);

        // ẩn button save, cancel, title
        if (goneButtonSave) {
            dialog_save.setVisibility(View.GONE);
        }

        if (goneButtonCancel) {
            dialog_cancel.setVisibility(View.GONE);
        }

        if (goneDialogTextTitle) {
            dialog_title.setVisibility(View.GONE);
        }

        if (goneDialogTitle) {
            rl_dialog_title.setVisibility(View.GONE);
        }

        // thay đổi tên hiển thị title, cancel, save
        if (!TextUtils.isEmpty(dialogTitle)) {
            dialog_title.setText(dialogTitle);
        } else {
            dialog_title.setText("");
        }

        if (!TextUtils.isEmpty(dialogCancel)) {
            dialog_cancel.setText(dialogCancel);
        } else {
            dialog_cancel.setText(getContext().getString(R.string.dialog_cancel));
        }

        if (!TextUtils.isEmpty(dialogSave)) {
            dialog_save.setText(dialogSave);
        } else {
            dialog_save.setText(getContext().getString(R.string.dialog_save));
        }

        if (dialogBackground != -1) {
            ll_dialog.setBackgroundColor(ContextCompat.getColor(getContext(), dialogBackground));
        }

        if (dialogTitleBackground != -1) {
            rl_dialog_title.setBackgroundColor(ContextCompat.getColor(getContext(), dialogTitleBackground));
        }

        // thay đổi màu hiển thị của title, cancel, save
        if (dialogTitleColor != -1) {
            dialog_title.setTextColor(ContextCompat.getColor(getContext(), dialogTitleColor));
        }

        if (dialogCancelColor != -1) {
            dialog_cancel.setTextColor(ContextCompat.getColor(getContext(), dialogCancelColor));
        }

        if (dialogSaveColor != -1) {
            dialog_save.setTextColor(ContextCompat.getColor(getContext(), dialogSaveColor));
        }

        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
