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
        String email_validation_match = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        String email = editText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            editText.setError(context.getString(R.string.err_field_required));
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editText.setError(context.getString(R.string.err_field_email_required));
            return false;
        } else {
            if (!email.matches(email_validation_match)) {
                editText.setError(context.getString(R.string.err_field_email_required));
                return false;
            }
        }

        return true;
    }
}
