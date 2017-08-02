package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.lib.view.AbstractSimpleListView;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardStarPrintListPaymentContentBinding;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

/**
 * Created by Johan on 8/1/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class StarPrintListCommentPanel extends AbstractSimpleRecycleView<OrderStatus> {
    public StarPrintListCommentPanel(Context context) {
        super(context);
    }

    public StarPrintListCommentPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StarPrintListCommentPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, OrderStatus item, int position) {
        TextView comment = (TextView) view.findViewById(R.id.comment);
        if (!StringUtil.isNullOrEmpty(item.getComment())) {
            comment.setText(ConfigUtil.formatDateTime(item.getCreatedAt()) + ": " + item.getComment());
        }
    }

    @Override
    protected void onClickItem(View view, OrderStatus item, int position) {

    }
}
