package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.databinding.CardOrderHistoryListContentBinding;
import com.magestore.app.util.StringUtil;

import java.util.List;

/**
 * Panel hiển thị danh sách đơn hàng chi tiết
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class OrderListPanel extends AbstractListPanel<Order> implements View.OnClickListener {
    // Danh sách đơn hàng
    List<Order> mListOrder;
    LinearLayout ll_status_pending, ll_status_processing, ll_status_complete, ll_status_cancelled, ll_status_closed, ll_status_not_sync;
    ImageView im_status_pending, im_status_processing, im_status_complete, im_status_cancelled, im_status_closed, im_status_not_sync;
    TextView tv_status_pending, tv_status_processing, tv_status_complete, tv_status_cancelled, tv_status_closed, tv_status_not_sync;
    boolean isPending, isProcessing, isComplete, isCancelled, isClosed, isNotSync;

    /**
     * Khởi tạo
     *
     * @param context
     */
    public OrderListPanel(Context context) {
        super(context);
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     */
    public OrderListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public OrderListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        ll_status_pending = (LinearLayout) findViewById(R.id.ll_status_pending);
        im_status_pending = (ImageView) findViewById(R.id.im_status_pending);
        tv_status_pending = (TextView) findViewById(R.id.tv_status_pending);
        ll_status_processing = (LinearLayout) findViewById(R.id.ll_status_processing);
        im_status_processing = (ImageView) findViewById(R.id.im_status_processing);
        tv_status_processing = (TextView) findViewById(R.id.tv_status_processing);
        ll_status_complete = (LinearLayout) findViewById(R.id.ll_status_complete);
        im_status_complete = (ImageView) findViewById(R.id.im_status_complete);
        tv_status_complete = (TextView) findViewById(R.id.tv_status_complete);
        ll_status_cancelled = (LinearLayout) findViewById(R.id.ll_status_cancelled);
        im_status_cancelled = (ImageView) findViewById(R.id.im_status_cancelled);
        tv_status_cancelled = (TextView) findViewById(R.id.tv_status_cancelled);
        ll_status_closed = (LinearLayout) findViewById(R.id.ll_status_closed);
        im_status_closed = (ImageView) findViewById(R.id.im_status_closed);
        tv_status_closed = (TextView) findViewById(R.id.tv_status_closed);
        ll_status_not_sync = (LinearLayout) findViewById(R.id.ll_status_not_sync);
        im_status_not_sync = (ImageView) findViewById(R.id.im_status_not_sync);
        tv_status_not_sync = (TextView) findViewById(R.id.tv_status_not_sync);
        initValue();
    }

    /**
     * Chuẩn bị layout giá trị
     */
    @Override
    public void initValue() {
        setColorStatus(R.id.im_status_pending, im_status_pending);
        setColorStatus(R.id.im_status_processing, im_status_processing);
        setColorStatus(R.id.im_status_complete, im_status_complete);
        setColorStatus(R.id.im_status_cancelled, im_status_cancelled);
        setColorStatus(R.id.im_status_closed, im_status_closed);
        setColorStatus(R.id.im_status_not_sync, im_status_not_sync);
        ll_status_pending.setOnClickListener(this);
        ll_status_processing.setOnClickListener(this);
        ll_status_complete.setOnClickListener(this);
        ll_status_cancelled.setOnClickListener(this);
        ll_status_closed.setOnClickListener(this);
        ll_status_not_sync.setOnClickListener(this);
    }

    @Override
    protected void bindItem(View view, Order item, int position) {
        CardOrderHistoryListContentBinding biding = DataBindingUtil.bind(view);
        biding.setOrder(item);

        ImageView im_status = (ImageView) view.findViewById(R.id.im_status);
        im_status.setImageResource(R.drawable.ic_order_status);

        String status = item.getStatus().toLowerCase();
        changeColorStatusOrder(status, im_status);
    }

    private void changeColorStatusOrder(String status, ImageView im_status) {
        switch (status) {
            case "pending":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_pending));
                break;
            case "processing":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_processing));
                break;
            case "complete":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_complete));
                break;
            case "canceled":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_cancelled));
                break;
            case "closed":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_closed));
                break;
            case "not_sync":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_notsync));
                break;
        }
    }

    private void setColorStatus(int id, ImageView im_status) {
        im_status.setImageResource(R.drawable.ic_order_status);
        switch (id) {
            case R.id.im_status_pending:
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_pending));
                break;
            case R.id.im_status_processing:
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_processing));
                break;
            case R.id.im_status_complete:
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_complete));
                break;
            case R.id.im_status_cancelled:
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_cancelled));
                break;
            case R.id.im_status_closed:
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_closed));
                break;
            case R.id.im_status_not_sync:
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_notsync));
                break;
        }
    }

    private void changeColorBackGroundStatus(int id, boolean isStatus, LinearLayout ll_status, ImageView im_status, TextView tv_status) {
        switch (id) {
            case R.id.ll_status_pending:
                if (isPending) {
                    isPending = false;
                } else {
                    isPending = true;
                }
                ll_status.setBackgroundColor(isPending ? ContextCompat.getColor(getContext(), R.color.order_status_pending) : ContextCompat.getColor(getContext(), R.color.white));
                im_status.setColorFilter(isPending ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.order_status_pending));
                tv_status.setTextColor(isPending ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.text_color));
                break;
            case R.id.ll_status_processing:
                if (isProcessing) {
                    isProcessing = false;
                } else {
                    isProcessing = true;
                }
                ll_status.setBackgroundColor(isProcessing ? ContextCompat.getColor(getContext(), R.color.order_status_processing) : ContextCompat.getColor(getContext(), R.color.white));
                im_status.setColorFilter(isProcessing ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.order_status_processing));
                tv_status.setTextColor(isProcessing ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.text_color));
                break;
            case R.id.ll_status_complete:
                if (isComplete) {
                    isComplete = false;
                } else {
                    isComplete = true;
                }
                ll_status.setBackgroundColor(isComplete ? ContextCompat.getColor(getContext(), R.color.order_status_complete) : ContextCompat.getColor(getContext(), R.color.white));
                im_status.setColorFilter(isComplete ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.order_status_complete));
                tv_status.setTextColor(isComplete ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.text_color));
                break;
            case R.id.ll_status_cancelled:
                if (isCancelled) {
                    isCancelled = false;
                } else {
                    isCancelled = true;
                }
                ll_status.setBackgroundColor(isCancelled ? ContextCompat.getColor(getContext(), R.color.order_status_cancelled) : ContextCompat.getColor(getContext(), R.color.white));
                im_status.setColorFilter(isCancelled ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.order_status_cancelled));
                tv_status.setTextColor(isCancelled ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.text_color));
                break;
            case R.id.ll_status_closed:
                if (isClosed) {
                    isClosed = false;
                } else {
                    isClosed = true;
                }
                ll_status.setBackgroundColor(isClosed ? ContextCompat.getColor(getContext(), R.color.order_status_closed) : ContextCompat.getColor(getContext(), R.color.white));
                im_status.setColorFilter(isClosed ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.order_status_closed));
                tv_status.setTextColor(isClosed ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.text_color));
                break;
            case R.id.ll_status_not_sync:
                if (isNotSync) {
                    isNotSync = false;
                } else {
                    isNotSync = true;
                }
                ll_status.setBackgroundColor(isNotSync ? ContextCompat.getColor(getContext(), R.color.order_status_notsync) : ContextCompat.getColor(getContext(), R.color.white));
                im_status.setColorFilter(isNotSync ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.order_status_notsync));
                tv_status.setTextColor(isNotSync ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.text_color));
                break;
        }
    }

    @Override
    public void initModel() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_status_pending:
                changeColorBackGroundStatus(R.id.ll_status_pending, isPending, ll_status_pending, im_status_pending, tv_status_pending);
                break;
            case R.id.ll_status_processing:
                changeColorBackGroundStatus(R.id.ll_status_processing, isProcessing, ll_status_processing, im_status_processing, tv_status_processing);
                break;
            case R.id.ll_status_complete:
                changeColorBackGroundStatus(R.id.ll_status_complete, isComplete, ll_status_complete, im_status_complete, tv_status_complete);
                break;
            case R.id.ll_status_cancelled:
                changeColorBackGroundStatus(R.id.ll_status_cancelled, isCancelled, ll_status_cancelled, im_status_cancelled, tv_status_cancelled);
                break;
            case R.id.ll_status_closed:
                changeColorBackGroundStatus(R.id.ll_status_closed, isClosed, ll_status_closed, im_status_closed, tv_status_closed);
                break;
            case R.id.ll_status_not_sync:
                changeColorBackGroundStatus(R.id.ll_status_not_sync, isNotSync, ll_status_not_sync, im_status_not_sync, tv_status_not_sync);
                break;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = StringUtil.addStringElement(stringBuilder, isPending, StringUtil.STATUS_PENDING);
        stringBuilder = StringUtil.addStringElement(stringBuilder, isProcessing, StringUtil.STATUS_PROCESSING);
        stringBuilder = StringUtil.addStringElement(stringBuilder, isComplete, StringUtil.STATUS_COMPLETE);
        stringBuilder = StringUtil.addStringElement(stringBuilder, isCancelled, StringUtil.STATUS_CANCELLED);
        stringBuilder = StringUtil.addStringElement(stringBuilder, isClosed, StringUtil.STATUS_CLOSED);
        String status = stringBuilder.toString();
        if (getController() instanceof OrderHistoryListController) {
            ((OrderHistoryListController) getController()).doSearchStatus(status);
        }
//        stringBuilder = StringUtil.addStringElement(stringBuilder, isNotSync, StringUtil.STATUS_PENDING);

    }
}
