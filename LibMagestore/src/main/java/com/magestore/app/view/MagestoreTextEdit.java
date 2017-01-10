package com.magestore.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * TextEdit với các xử lý đặc thù riêng gồm
 * - Nhận format từ layout, chuyển đổi
 * Created by Mike on 1/6/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class MagestoreTextEdit extends EditText {
    // Quản lý mapp từ attr trong layout sang các thuộc tính của view
    MagestoreViewAttr attr = null;

    /**
     * Khởi tạo
     * @param context
     */
    public MagestoreTextEdit(Context context) {
        super(context);
    }

    /**
     * Khởi tạo với các thuộc tính đặt riêng
     * @param context
     * @param attrs
     */
    public MagestoreTextEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        attr = new MagestoreViewAttr(context, attrs);
    }

    /**
     * Khởi tạo với các thuộc tính đặt riêng
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MagestoreTextEdit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        attr = new MagestoreViewAttr(context, attrs);
    }



    /**
     * Xử lý khi truyền text vào, chuyển đổi format hiển thị cho phù hợp
     * @param text
     * @param type
     */
    @Override
    public void setText(CharSequence text, BufferType type) {
        // Điền ký tự vào
        if (attr != null)
            super.setText(attr.buildText(text), type);
        else super.setText(text, type);
    }

    public void setRawText(float number) {
        if (attr != null)
            setText(attr.buildText(number));
    }

    public void setRawText(int number) {
        if (attr != null)
            setText(attr.buildText(number));
    }

    public void setRawText(String value) {
        if (attr != null)
            setText(attr.buildText(value));
    }
}
