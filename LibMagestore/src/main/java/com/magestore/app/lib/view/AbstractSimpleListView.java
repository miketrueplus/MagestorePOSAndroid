package com.magestore.app.lib.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.magestore.app.lib.R;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.view.adapter.DefaultModelView;
import com.magestore.app.lib.view.item.ModelView;

import java.util.List;

/**
 * Một listview đơn giản
 * Created by Mike on 1/18/2017.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class AbstractSimpleListView<TModel extends Model>
        extends ListView
        implements MagestoreView<ListController<TModel>> {
    // ID Layout của rowview
    int mListLayout;
    boolean mNoScroll;

    // Task điều khiển danh sách trong list
    protected ListController<TModel> mController;

    // Model chứa data danh sách
    protected List<TModel> mList;
    private int mintProgresLayout;
    private View mProgressView;

    public AbstractSimpleListView(Context context) {
        super(context);
        initLayout();
    }

    public AbstractSimpleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        initLayout();
    }

    public AbstractSimpleListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        loadAttrs(context, attrs);
        initLayout();
    }

    @Override
    public void setController(ListController<TModel> controller) {
        mController = controller;
        initLayout();
    }

    @Override
    public ListController<TModel> getController() {
        return mController;
    }

    @Override
    public void showErrMsgDialog(String strMsg) {

    }

    @Override
    public void showErrMsgDialog(Exception exp) {

    }

    /**
     * Đọc các thuộc tính của layout
     * @param context
     * @param attrs
     */
    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.magestore_view);
        mListLayout = a.getResourceId(R.styleable.magestore_view_layout_item, -1);
        if (mListLayout == -1)
            mListLayout = a.getResourceId(R.styleable.magestore_view_layout_row, -1);
        mNoScroll = a.getBoolean(R.styleable.magestore_view_layout_no_scroll, true);
        mintProgresLayout = a.getResourceId(R.styleable.magestore_view_layout_progress, -1);
        a.recycle();

        if (mintProgresLayout > -1) {
            mProgressView = findViewById(mintProgresLayout);
        }
    }

    public void initLayout() {


    }

    public void initModel() {
    }

    public void initValue() {
    }

    /**
     * Gán danh sách và cập nhật view
     *
     * @param list
     */
    public void bindList(List<TModel> list) {
        mList = list;
        if (mList != null) {
            this.setAdapter(new AbstractSimpleListView<TModel>.SimpleListAdapter(getContext(), mListLayout, mList));
        }
    }

    /**
     * Map mỗi item tương ứng với view trên danh sách
     *
     * @param view
     * @param item
     */
    protected void bindItem(View view, TModel item, int position) {

    }

    /**
     * Hiển thị tiến trình
     *
     * @param show
     */
    public void showProgress(final boolean show) {
        if (mProgressView == null) return;
// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void setListLayout(int listLayout) {
        mListLayout = listLayout;
    }

    /**
     * Hiển thị thông báo lỗi
     *
     * @param strMsg
     */
    public void showErrorMsg(String strMsg) {
        new AlertDialog.Builder(getContext())
                .setMessage(strMsg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void showErrorMsg(Exception exp) {
        new AlertDialog.Builder(getContext())
                .setMessage(exp.getMessage())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Thay đổi dữ liệu, refresh lại
     */
    public void notifyDataSetChanged() {
        if (getAdapter() == null) return;
        if (getAdapter() instanceof ArrayAdapter)
            ((ArrayAdapter) getAdapter()).notifyDataSetChanged();;
    }

    public class SimpleListAdapter extends ArrayAdapter<TModel> {
        // Danh sách của view
        private List<TModel> mList;

        public SimpleListAdapter(Context context, int resource) {
            super(context, resource);
        }

        public SimpleListAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public SimpleListAdapter(Context context, int resource, TModel[] objects) {
            super(context, resource, objects);
        }

        public SimpleListAdapter(Context context, int resource, int textViewResourceId, TModel[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public SimpleListAdapter(Context context, int resource, List<TModel> objects) {
            super(context, resource, objects);
            mList = objects;
        }

        public SimpleListAdapter(Context context, int resource, int textViewResourceId, List<TModel> objects) {
            super(context, resource, textViewResourceId, objects);
        }


        @Override
        public View getView(final int position, final View convertView,
                            final ViewGroup parent) {
            int viewType = getItemViewType(position);
            // Xác định item được chọn trong danh sách
            final TModel item = mList.get(position);

            // first check to see if the view is null. if so, we have to inflate it.
            // to inflate it basically means to render, or show, the view.
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(mListLayout, null);
            }
//
//            // map dữ liệu từ model sang item trong danh sách
            bindItem(v, item, position);
            return v;
        }
    }

    /**
     * Xuwr
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Nếu k0 tự động thay đổi chiều cao
        if (!mNoScroll) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }

    @Override
    public void hideAllProgressBar() {

    }

    @Override
    public void hideErrorMsg(Exception exp) {

    }

    @Override
    public void showWarning(String strMsg) {

    }

    @Override
    public void hideWarning() {

    }

    @Override
    public void showProgressLoadRefresh(boolean show) {

    }

    @Override
    public void showProgressLoadingMore(boolean show) {

    }

    @Override
    public ModelView createModelView(Model model) {
        ModelView modelView = new DefaultModelView();
        modelView.setModel(model);
        modelView.getViewState().setStateNormal();
        return modelView;
    }
}