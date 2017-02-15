package com.magestore.app.lib.panel;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.magestore.app.lib.R;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.view.SearchAutoCompleteTextView;
import com.magestore.app.lib.view.adapter.SearchAutoCompleteAdapter;


/**
 * Panel chứa auto complete text để search kèm theo các nút tương tác như progress, close
 * Created by Mike on 2/15/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class SearchAutoCompletePanel extends FrameLayout {
    SearchAutoCompleteTextView mAutoTextView;
    ListController mController;

    // view của layout
    // tham chiếu view của layout
    View mViewLayout;

    ImageButton mCloseButton;

    // tham chiếu của cả panel
    private int mintPanelLayout;

    // tham chiếu layout của mỗi item trong list
    private int mintItemLayout;

    // tham chiếu id layout của progress
    private int mintProgresLayout;

    // tham chiếu id layout của close
    private int mintCloseLayout;

    public SearchAutoCompletePanel(Context context) {
        super(context);
        initLayout();
    }

    public SearchAutoCompletePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        initLayout();
    }

    public SearchAutoCompletePanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        loadAttrs(context, attrs);
        initLayout();
    }

    /**
     * Load các tham số từ attributes của panel
     * @param context
     * @param attrs
     */
    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.magestore_view);

        // layoyt id của chính panel
        mintPanelLayout = a.getResourceId(R.styleable.magestore_view_layout_panel, -1);
        // layout id của progress hiển thị quá trình loading
        mintProgresLayout = a.getResourceId(R.styleable.magestore_view_layout_progress, -1);
        // layout của complete search
        mintItemLayout = a.getResourceId(R.styleable.magestore_view_layout_item, -1);
        // layout của close
        mintCloseLayout = a.getResourceId(R.styleable.magestore_view_layout_close, -1);

        a.recycle();
    }

    /**
     * Chuẩn bị các layout
     */
    private void initLayout() {
        // load layout
        if (mintPanelLayout > 0) {
            mViewLayout = inflate(getContext(), mintPanelLayout, null);
            addView(mViewLayout);
        }

        // load complete view chính
        if (mintItemLayout > 0) {
            mAutoTextView = (SearchAutoCompleteTextView) mViewLayout.findViewById(mintItemLayout);
            mAutoTextView.setThreshold(3);
        }

        // load progress bar
        if (mintProgresLayout > 0) {
            if (mViewLayout.findViewById(mintProgresLayout) instanceof ProgressBar)
                mAutoTextView.setLoadingIndicator((ProgressBar) mViewLayout.findViewById(mintProgresLayout));
        }

        // load close
        if (mintCloseLayout > 0) {
            mCloseButton = (ImageButton) mViewLayout.findViewById(mintCloseLayout);
        }

        mAutoTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Model model = (Model) adapterView.getItemAtPosition(position);
                mAutoTextView.setText(model.getDisplayContent());
                mController.displaySearch(model);
                if (mCloseButton != null) mCloseButton.setVisibility(VISIBLE);
            }
        });

        if (mCloseButton != null)
            mCloseButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAutoTextView.setText("");
                    mCloseButton.setVisibility(GONE);
                    if (mCloseButton != null) mController.hideSearch();
                }
            });
    }

    /**
     * Đặt controller quản lý
     * @param controller
     */
    public void setListController(ListController controller) {
        mController = controller;
        mAutoTextView.setAdapter(new SearchAutoCompleteAdapter<Product>(getContext(), mController.getListService()));
    }
}
