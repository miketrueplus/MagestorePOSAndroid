package com.magestore.app.lib.panel;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.magestore.app.lib.R;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.customer.PlaceAutoComplete;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.lib.view.SearchAutoCompleteTextView;
import com.magestore.app.lib.view.adapter.SearchPlaceAutoCompleteAdapter;
import com.magestore.app.util.StringUtil;

/**
 * Created by Johan on 12/1/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class SearchPlaceAutoCompletePanel extends FrameLayout {
    private final int MODE_FULL = 0;
    private final int MODE_FILTER = 1;
    private final int MODE_SUGGEST = 2;
    private int mintMode;

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

    /**
     * Khởi tạo
     *
     * @param context
     */

    Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
    Runnable workRunnable;

    public SearchPlaceAutoCompletePanel(Context context) {
        super(context);
        mintMode = MODE_FULL;
        initLayout();
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     */
    public SearchPlaceAutoCompletePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        mintMode = MODE_FULL;
        initLayout();
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SearchPlaceAutoCompletePanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        loadAttrs(context, attrs);
        mintMode = MODE_FULL;
        initLayout();
    }

    /**
     * Load các tham số từ attributes của panel
     *
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

        // giải phóng attributes
        a.recycle();
    }

    /**
     * Chuẩn bị các layout
     */
    boolean checkScan = true;

    private void initLayout() {
        checkScan = true;
        // load layout
        if (mintPanelLayout > 0) {
            mViewLayout = inflate(getContext(), mintPanelLayout, null);
            addView(mViewLayout);
        }

        // load complete view chính
        if (mintItemLayout > 0) {
            mAutoTextView = (SearchAutoCompleteTextView) mViewLayout.findViewById(mintItemLayout);
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

        // sự kiện chọn 1 item trên suggest
        mAutoTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // xác định model được chọn trên suggest
                Model model = (Model) adapterView.getItemAtPosition(position);

                // fill giá trị vào text search
                mAutoTextView.setText(model.getDisplayContent());

                // hiển thị kết quả
                mController.displayPlaceSearch((PlaceAutoComplete) model);

                // hiển thị nút close
                if (mCloseButton != null) mCloseButton.setVisibility(VISIBLE);

                // xác định mode là sugest
                mintMode = MODE_SUGGEST;
            }
        });

        // sự kiện close kết quả tìm kiếm, quay về với list ban đầu
        if (mCloseButton != null)
            mCloseButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // nếu đang thao tác với suggesst
                    if (mintMode == MODE_SUGGEST) {
                        // giấu mục tìm kiếm
                        if (mCloseButton != null) mController.hideSearch();

                        // ô text search được đặt lại giá trị cũ
                        // gán chuỗi tìm kiếm về null trong controller
                        boolean haveSearchString = (mController.getSearchString() != null) && !(StringUtil.STRING_EMPTY.equals(mController.getSearchString()));
                        mAutoTextView.setText(mController.getSearchString());

                        // ẩn nút close tìm kiếm đi nếu trước đó k0 có nội dung search
                        if (!haveSearchString) {
                            mCloseButton.setVisibility(GONE);
                            mintMode = MODE_FULL;
                        } else
                            mintMode = MODE_FILTER;
                    }
                    // nếu đang thao tác với kết quả filter
                    else if (mintMode == MODE_FILTER) {
                        if (mintProgresLayout > 0) {
                            if (mViewLayout.findViewById(mintProgresLayout) instanceof ProgressBar)
                                mViewLayout.findViewById(mintProgresLayout).setVisibility(GONE);
                        }
                        mAutoTextView.setText(StringUtil.STRING_EMPTY);
                        mController.setSearchString(null);
                        mController.reload();
                        mCloseButton.setVisibility(GONE);
                        mintMode = MODE_FULL;
                    }
                }
            });

    }

    /**
     * áp dụng search
     */
    public void applySearch() {
        // dừng search gợi ý lại
        mAutoTextView.dimissSuggest();

        // đặt chuỗi search
        mController.setSearchString(mAutoTextView.getText().toString().trim());

        // reload lại, đồng thời clear list
        mController.reload();

        // hiển thị nút để close kết quả tìm kiếm
        if (mCloseButton != null) mCloseButton.setVisibility(VISIBLE);
    }

    /**
     * áp dụng search
     */
    public void actionSearch() {
        // dừng search gợi ý lại
        mAutoTextView.dimissSuggest();

        // đặt chuỗi search
        mController.setSearchString(mAutoTextView.getText().toString().trim());

        // reload lại, đồng thời clear list
        mController.reload();

        // hiển thị nút để close kết quả tìm kiếm
        if (mCloseButton != null) mCloseButton.setVisibility(VISIBLE);

        mintMode = MODE_FILTER;
        checkScan = true;
    }

    /**
     * Đặt controller quản lý
     *
     * @param controller
     */
    public void setListController(ListController controller, CustomerService mCustomerService) {
        mController = controller;
        mAutoTextView.setAdapter(new SearchPlaceAutoCompleteAdapter<PlaceAutoComplete>(getContext(), mCustomerService));
    }


    public SearchAutoCompleteTextView getAutoTextView() {
        return mAutoTextView;
    }
}
