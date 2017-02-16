package com.magestore.app.lib.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.R;
import com.magestore.app.lib.model.config.Config;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ListRecycleView extends RecyclerView {
    private int mintLayoutProgress;
    private int mintLayoutMsg;
    private int mintLayoutContent;

    public ListRecycleView(Context context) {
        super(context);
    }

    public ListRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
    }

    public ListRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        loadAttrs(context, attrs);
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.magestore_view);
        mintLayoutProgress = a.getResourceId(R.styleable.magestore_view_layout_item_progress, -1);
        mintLayoutContent = a.getResourceId(R.styleable.magestore_view_layout_item_content, -1);
        mintLayoutMsg = a.getResourceId(R.styleable.magestore_view_layout_item_msg, -1);
        a.recycle();
    }

    public void showProgress(View v, final boolean show) {
        // tránh null
        if ((mintLayoutProgress < 0) || (mintLayoutContent < 0)) return;
        final View vLayoutProgress = v.findViewById(mintLayoutProgress);
        final View vLayoutContent = v.findViewById(mintLayoutContent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            vLayoutContent.setVisibility(show ? View.GONE : View.VISIBLE);
            vLayoutContent.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    vLayoutContent.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            vLayoutProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            vLayoutProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    vLayoutProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            vLayoutProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            vLayoutContent.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
