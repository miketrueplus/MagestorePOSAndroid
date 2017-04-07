package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardOrderHistoryListContentBinding;
import java.util.List;

/**
 * Panel hiển thị danh sách đơn hàng chi tiết
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class OrderListPanel extends AbstractListPanel<Order> {
    // Danh sách đơn hàng
    List<Order> mListOrder;

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

    /**
     * Chuẩn bị layout giá trị
     */
    @Override
    public void initValue() {

    }

    @Override
    public void initModel() {

    }
}
