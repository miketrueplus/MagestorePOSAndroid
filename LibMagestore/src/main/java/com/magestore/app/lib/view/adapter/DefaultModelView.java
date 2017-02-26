package com.magestore.app.lib.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.magestore.app.lib.R;

/**
 * Model view mặc định với 2 dòng text và 1 image
 * Created by Mike on 2/21/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class DefaultModelView extends GenericModelView {
    TextView mTxtView1 = null;
    TextView mTxtView2 = null;
    ImageView mImageView = null;
    boolean mblnUseTxtView1 = true;
    boolean mblnUseTxtView2 = true;
    boolean mblnUseImage = true;

    /**
     * Không sử dụng text view 2
     */
    public void notUseTextView2() {
        mblnUseTxtView2 = false;
    }

    /**
     * Không sử dụng image
     */
    public void notUseImage() {
        mblnUseImage = false;
    }

    /**
     * Hold view để tránh việc dùng findview id quá nhiều
     * @param v
     */
    @Override
    public void holdView(View v) {
        super.holdView(v);
        mTxtView1 = ((TextView) v.findViewById(R.id.id_modelview_default_item_text1));
        mTxtView2 = ((TextView) v.findViewById(R.id.id_modelview_default_item_text2));
        mImageView = ((ImageView) v.findViewById(R.id.id_modelview_default_item_image));
    }

    /**
     * Hiển thị các giá trị trên view
     */
    @Override
    public void bindModel() {
        super.bindModel();
        if (mTxtView1 != null && mblnUseTxtView1) mTxtView1.setText(getDisplayContent());
        if (mTxtView2 != null && mblnUseTxtView2) mTxtView2.setText(getSubDisplayContent());
        if (mImageView != null && mblnUseImage) {
            mImageView.setImageBitmap(getImageBitmap());
            mImageView.setVisibility(getImageBitmap() == null ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Mặc định sử dụng main layout luôn
     * @return
     */
    @Override
    public int getMainLayoutID() {
        return R.layout.layout_modelview_default_item_main;
    }
}