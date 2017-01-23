package com.magestore.app.pos.util;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import com.magestore.app.pos.R;
import com.magestore.app.pos.view.MagestoreDialog;

/**
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class DialogUtil {
    /**
     * Confirm delete
     *
     * @param context
     * @param strMsgDetail
     * @param listener
     */
    public static void confirmDelete(Context context, String strMsgDetail, DialogInterface.OnClickListener listener) {
        com.magestore.app.util.DialogUtil.confirm(context,
                R.string.title_confirm_delete,
                context.getString(R.string.ask_are_you_sure_to_delete) + "\n" + strMsgDetail,
                R.string.yes,
                R.string.no,
                listener);
    }

    /**
     * Confirm save
     *
     * @param context
     * @param strMsgDetail
     * @param listener
     */
    public static void confirmSave(Context context, String strMsgDetail, DialogInterface.OnClickListener listener) {
        com.magestore.app.util.DialogUtil.confirm(context,
                R.string.title_confirm_save,
                context.getString(R.string.ask_are_you_sure_to_save) + "\n" + strMsgDetail,
                R.string.yes,
                R.string.no,
                listener);
    }

    /**
     * Confirm update
     *
     * @param context
     * @param strMsgDetail
     * @param listener
     */
    public static void confirmUpdate(Context context, String strMsgDetail, DialogInterface.OnClickListener listener) {
        com.magestore.app.util.DialogUtil.confirm(context,
                R.string.title_confirm_update,
                context.getString(R.string.ask_are_you_sure_to_update) + "\n" + strMsgDetail,
                R.string.yes,
                R.string.no,
                listener);
    }

    /**
     * Confirm remove
     *
     * @param context
     * @param strMsgDetail
     * @param listener
     */
    public static void confirmRemove(Context context, String strMsgDetail, DialogInterface.OnClickListener listener) {
        com.magestore.app.util.DialogUtil.confirm(context,
                R.string.title_confirm_remove,
                context.getString(R.string.ask_are_you_sure_to_remove) + "\n" + strMsgDetail,
                R.string.yes,
                R.string.no,
                listener);
    }

    /**
     * Confirm cancel
     *
     * @param context
     * @param strMsgDetail
     * @param listener
     */
    public static void confirmCancel(Context context, String strMsgDetail, DialogInterface.OnClickListener listener) {
        com.magestore.app.util.DialogUtil.confirm(context,
                R.string.title_confirm_cancel,
                context.getString(R.string.ask_are_you_sure_to_cancel) + "\n" + strMsgDetail,
                R.string.yes,
                R.string.no,
                listener);
    }

    public static MagestoreDialog dialog(Context context, String title, View panel) {
        MagestoreDialog dialog = new MagestoreDialog(context);
        dialog.setDialogTitle(title);
        dialog.setDialogContent(panel);
        return dialog;
    }
}

