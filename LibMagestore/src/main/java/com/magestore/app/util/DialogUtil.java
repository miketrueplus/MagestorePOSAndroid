package com.magestore.app.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Các tiện ích với dialog để gọi cho nhanh
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class DialogUtil {
    /**
     * Dialog confirm trước khi xóa, thêm, sửa
     * @param context
     * @param intTitle
     * @param intMsg
     * @param intYes
     * @param intNo
     * @param listener
     */
    public static void confirm(Context context, int intTitle, int intMsg, int intYes, int intNo, DialogInterface.OnClickListener listener) {
        AlertDialog confirmDialogBox = new AlertDialog.Builder(context)
                .setTitle(intTitle)
                .setMessage(intMsg)
                .setPositiveButton(intYes, listener)
                .setNegativeButton(intNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        confirmDialogBox.show();
    }

    /**
     * Dialog confirm trước khi xóa, thêm, sửa
     * @param context
     * @param intTitle
     * @param strMsgDetail
     * @param intYes
     * @param intNo
     * @param listener
     */
    public static void confirm(Context context, int intTitle, String strMsgDetail, int intYes, int intNo, DialogInterface.OnClickListener listener) {
        AlertDialog confirmDialogBox = new AlertDialog.Builder(context)
                .setTitle(intTitle)
                .setMessage(strMsgDetail)
                .setPositiveButton(intYes, listener)
                .setNegativeButton(intNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        confirmDialogBox.show();
    }
}
