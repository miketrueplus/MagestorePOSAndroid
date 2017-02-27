package com.magestore.app.lib.view.item;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.magestore.app.lib.model.Model;

/**
 * View data nắm giữ và quản lý view cho các ô trong 1 list
 * Created by Mike on 2/21/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class GenericModelView implements ModelView {
    protected ProgressBar mProgressBar = null;
    protected TextView mTxtMsg = null;
    protected Model mModel;
    protected ViewState mViewState = new ViewState();
    protected View mLayoutMainView;
    protected View mLayoutContentView;
    protected int mintMainLayout = -1;

    /**
     * Trả tham chiếu đến layout main view
     * @return
     */
    @Override
    public View getLayoutMainView() {
        return mLayoutMainView;
    }

    @Override
    public void setViewState(ViewState state) {
        mViewState = state;
    }

    /**
     * @return
     */
    @Override
    public ViewState getViewState() {
        return mViewState;
    }

    /**
     * Model do view quản lý
     *
     * @param model
     */
    @Override
    public void setModel(Model model) {
        mModel = model;
    }

    /**
     * Model do view quản lý
     *
     * @return
     */
    @Override
    public Model getModel() {
        return mModel;
    }

    @Override
    public void holdView(View v) {
        mLayoutMainView = v;
    }

    @Override
    public void bindModel() {

    }

    @Override
    public void displayView() {
        // xử lý hiển thị progress bar nếu có
        displayProgressBar();

        // xử lý hiển thị thông báo lỗi nếu có
        displayErrorMsg();

        // xử lý hiển thị content
        displayContent();
    }

    /**
     * Nội dung hiển thị chính
     *
     * @return
     */
    @Override
    public String getDisplayContent() {
        if (mModel == null) return null;
        return mModel.getDisplayContent();
    }

    /**
     * Nội dung hiển thị bổ trợ
     *
     * @return
     */
    @Override
    public String getSubDisplayContent() {
        if (mModel == null) return null;
        return mModel.getSubDisplayContent();
    }

    /**
     * Trả ảnh bitmap
     *
     * @return
     */
    @Override
    public Bitmap getImageBitmap() {
        if (mModel == null) return null;
        return mModel.getDisplayBitmap();
    }

    /**
     * Trả về main layout ID
     * @return
     */
    @Override
    public int getMainLayoutID() {
        return mintMainLayout;
    }

    /**
     * Đặt layout id cho view
     * @param mainLayoutID
     */
    @Override
    public void setMainLayoutID(int mainLayoutID) {
        mintMainLayout = mainLayoutID;
    }


    /**
     * Hiển thị progress bar
     */
    public void displayProgressBar() {
        // không chỉ định progress bar, bỏ qua luôn
        if (mProgressBar == null) return;

        // đối với honey commb
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = (mLayoutMainView != null) ? mLayoutMainView.getResources().getInteger(android.R.integer.config_shortAnimTime) : 17694720;

            // hiển thị progress bar
            mProgressBar.setVisibility(getViewState().isStateLoading() ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(
                    getViewState().isStateLoading() ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(getViewState().isStateLoading() ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // che dấu
            mProgressBar.setVisibility(getViewState().isStateLoading() ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Hiển thị thông báo lỗi
     */
    public void displayErrorMsg() {
        // nếu chưa có text view cho msg thì bỏ qua
        if (mTxtMsg == null) return;

        // hiển thị txt view msg và hiển thị thông báo
        mTxtMsg.setVisibility(getViewState().isStateError() ? View.VISIBLE : View.GONE);
        if (getViewState().getStrMsg() != null) mTxtMsg.setText(getViewState().getStrMsg());
    }

    /**
     * Hiển thị phần content
     */
    public void displayContent() {
        // nếu k0 chỉ định layout content, bỏ qua luôn
        if (mLayoutContentView == null) return;

        // hiển thị animation nếu là honeycomb
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = (mLayoutMainView != null) ? mLayoutMainView.getResources().getInteger(android.R.integer.config_shortAnimTime) : 17694720;

            // che dấu content và thông báo lỗi
                mLayoutContentView.setVisibility(getViewState().isStateNormal() ? View.VISIBLE : View.GONE);
                mLayoutContentView.animate().setDuration(shortAnimTime).alpha(
                        getViewState().isStateNormal() ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLayoutContentView.setVisibility(getViewState().isStateNormal() ? View.VISIBLE : View.GONE);
                    }
                });


        } else {
            // hiển thị content
            mLayoutContentView.setVisibility(getViewState().isStateNormal() ? View.VISIBLE : View.GONE);
        }
    }
}
