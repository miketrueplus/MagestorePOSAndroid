package com.magestore.app.lib.panel;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.view.MagestoreView;

/**
 * Created by folio on 3/3/2017.
 */

public abstract class AbstractPanel<TController extends Controller> extends FrameLayout implements MagestoreView<TController> {
    // tham chiếu view của layout
    View mView;

    // tham chiếu của cả panel
    private int mintPanelLayout;

    public AbstractPanel(@NonNull Context context) {
        super(context);
    }

    public AbstractPanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractPanel(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AbstractPanel(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setController(TController controller) {

    }

    @Override
    public TController getController() {
        return null;
    }

    @Override
    public void showErrorMsg(String msg) {

    }

    @Override
    public void showErrorMsg(Exception exp) {

    }

    @Override
    public void showProgress(boolean blnShow) {

    }

    @Override
    public void hideAllProgressBar() {

    }

    protected void initLayout() {

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

    protected View getView() {
        return mView;
    }
}
