package com.magestore.app.lib.panel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.magestore.app.lib.R;
import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.view.MagestoreView;

/**
 * Created by folio on 3/3/2017.
 */

public abstract class AbstractPanel<TController extends Controller> extends FrameLayout implements MagestoreView<TController> {
    // controller của view
    protected TController mController;

    // text view hiện thông báo lỗi
    private TextView mTxtErrorMsg;

    // tham chiếu id của thông báo lỗi
    private int mintTxtErrMsg;

    // tham chiếu view của layout
    View mView;

    // tham chiếu của cả panel
    private int mintPanelLayout;

    // tham chiếu layout của progress
    private ProgressBar mProgressBar;

    // tham chiếu id của progress
    private int mintIdProgresBar;

    // progress bar ở top panel
    private ProgressBar mProgressBarTop;
    private View mProgressBarBottom;

    // tham chiếu id layout của progress
    private int mintIdProgresBarTop;
    private int mintIdProgresBarBottom;

    public AbstractPanel(@NonNull Context context) {
        super(context);
        initLayout();
    }

    public AbstractPanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        initLayout();
    }

    public AbstractPanel(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttrs(context, attrs);
        initLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AbstractPanel(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadAttrs(context, attrs);
        initLayout();
    }

    @Override
    public void setController(TController controller) {
        mController = controller;
    }

    @Override
    public TController getController() {
        return mController;
    }

    /**
     * Hiển thị thông báo lỗi
     *
     * @param strMsg
     */
    public void showErrorMsg(String strMsg) {
        if (mTxtErrorMsg != null) {
            mTxtErrorMsg.setText(strMsg);
            mTxtErrorMsg.setVisibility(VISIBLE);
        } else {
            new AlertDialog.Builder(getContext())
                    .setMessage(strMsg)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    /**
     * Hiện thông báo lỗi cho exception
     * @param exp
     */
    @Override
    public void showErrorMsg(Exception exp) {
        exp.printStackTrace();
        showErrorMsg(exp.getMessage());
    }

    /**
     * Ẩn thông báo lỗi
     * @param exp
     */
    @Override
    public void hideErrorMsg(Exception exp) {
        hideWarning();
    }

    @Override
    public void showProgress(boolean blnShow) {
        if (mProgressBar != null) mProgressBar.setVisibility(blnShow ? View.VISIBLE : View.GONE);
    }

    /**
     * Hiển thị progress bar top của panel khi loading dữ liệu lazy
     *
     * @param show
     */
    @Override
    public void showProgressLoadRefresh(final boolean show) {
        if (mProgressBarTop != null) mProgressBarTop.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Hiển thị progress bar bottom của panel khi loading dữ liệu lazy
     *
     * @param show
     */
    @Override
    public void showProgressLoadingMore(final boolean show) {
        if (mProgressBarBottom == null) return;
        mProgressBarBottom.setVisibility(show ? View.VISIBLE : View.GONE);
        if (mProgressBarBottom instanceof com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar) {
            ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
            animation.setDuration(500);
            animation.setFillAfter(true);
            ((com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar) mProgressBarBottom).startAnimation(animation);
        }
    }

    /**
     * Ẩn tất cả progressbar
     */
    @Override
    public void hideAllProgressBar() {
        if (mProgressBar != null) mProgressBar.setVisibility(View.GONE);
        if (mProgressBarTop != null) mProgressBarTop.setVisibility(View.GONE);
        if (mProgressBarBottom != null) mProgressBarBottom.setVisibility(View.GONE);
    }

    /**
     * Đọc các thuộc tính từ XML của layout
     * @param context
     * @param attrs
     */
    protected void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.magestore_view);

        // layoyt id của chính panel
        mintPanelLayout = a.getResourceId(R.styleable.magestore_view_layout_panel, -1);

        // layout id của progress hiển thị quá trình loading
        mintIdProgresBar = a.getResourceId(R.styleable.magestore_view_layout_progress, R.id.id_modelview_default_progressbar);
        mintIdProgresBarTop = a.getResourceId(R.styleable.magestore_view_layout_progress_top, R.id.id_modelview_default_progressbar_top);
        mintIdProgresBarBottom = a.getResourceId(R.styleable.magestore_view_layout_progress_bottom, R.id.id_modelview_default_progressbar_bottom);

        // layout id của progress hiển thị quá trình loading
        mintTxtErrMsg = a.getResourceId(R.styleable.magestore_view_layout_msg, R.id.id_modelview_default_textmsg);

        a.recycle();
    }
    protected void initLayout() {
        // tham chiêu file layout của panel
        if (mintPanelLayout > 0) setLayoutPanel(mintPanelLayout);

        // tham chiếu layout của progressbar
        mProgressBar = (ProgressBar) findViewById(mintIdProgresBar);

        // layut progress barr
        mProgressBarTop = (ProgressBar) findViewById(mintIdProgresBarTop);
        mProgressBarBottom = (View) findViewById(mintIdProgresBarBottom);

        // tham chiếu layout của thông báo lỗi
        if (mintTxtErrMsg > -1) {
            mTxtErrorMsg = (TextView) findViewById(mintTxtErrMsg);
        }
    }

    @Override
    public void initModel() {

    }

    @Override
    public void initValue() {

    }

    /**
     * Thiết lập layout cho panel
     *
     * @param layoutPanel
     */
    public void setLayoutPanel(int layoutPanel) {
        mintPanelLayout = layoutPanel;
        mView = inflate(getContext(), mintPanelLayout, null);
        addView(mView);
    }

    /**
     * Trả lại view
     * @return
     */
    protected View getView() {
        return mView;
    }

    /**
     * Hiển thị thông tin cảnh báo
     * @param strMsg
     */
    @Override
    public void showWarning(String strMsg) {
        if (mTxtErrorMsg != null) {
            mTxtErrorMsg.setText(strMsg);
            mTxtErrorMsg.setVisibility(VISIBLE);
        }
    }

    /**
     * Ẩn thông tin cảnh báo
     */
    @Override
    public void hideWarning() {
        if (mTxtErrorMsg != null)
            mTxtErrorMsg.setVisibility(GONE);
    }
}
