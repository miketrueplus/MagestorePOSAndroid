package com.magestore.app.pos.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.magestore.app.pos.R;

/**
 * Created by Johan on 2/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class EditTextUtil {
    public static boolean checkRequied(Context context, EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            editText.setError(context.getString(R.string.err_field_required));
            return false;
        }

        return true;
    }

    public static boolean checkRequiedEmail(Context context, EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            editText.setError(context.getString(R.string.err_field_required));
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches()) {
            editText.setError(context.getString(R.string.err_field_email_required));
            return false;
        }

        return true;
    }
}
