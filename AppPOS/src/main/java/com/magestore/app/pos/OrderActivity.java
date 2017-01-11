package com.magestore.app.pos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magestore.app.pos.panel.CustomerDetailPanel;
import com.magestore.app.pos.panel.CustomerListPanel;
import com.magestore.app.pos.panel.OrderDetailPanel;
import com.magestore.app.pos.panel.OrderListPanel;
import com.magestore.app.pos.ui.AbstractActivity;
import com.magestore.app.pos.ui.dummy.DummyContent;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link OrderDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class OrderActivity extends AbstractActivity {

    private OrderListPanel mOrderListPanel = null;
    private OrderDetailPanel mOrderDetailPanel = null;

    // Toolbar ứng dụng
    private Toolbar mToolbar;

    // xác định loại màn hình 1 pane hay 2 pane
    private boolean mblnTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_menu);

        initLayout();
        initModel();
        initValue();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle(getTitle());
//        initToolbarMenu(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        View recyclerView = findViewById(R.id.order_list);
//        assert recyclerView != null;
//        setupRecyclerView((RecyclerView) recyclerView);
//
//        if (findViewById(R.id.order_detail_container) != null) {
//            // The detail container view will be present only in the
//            // large-screen layouts (res/values-w900dp).
//            // If this view is present, then the
//            // activity should be in two-pane mode.
//            mTwoPane = true;
//        }
    }

    @Override
    protected void initLayout() {
        // chuẩn bị tool bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());
        initToolbarMenu(mToolbar);

        // chuẩn bị panel danh sách đơn hàng
        mOrderListPanel = (OrderListPanel) findViewById(R.id.order_list_panel);
//        mOrderListPanel.setListener(this);

        // chuẩn bị panel thông tin đơn hàng chi tiết
//        mOrderDetailPanel = (CustomerDetailPanel) findViewById(R.id.order_detail_panel);
//        if (mOrderDetailPanel != null)
//            mOrderDetailPanel.setListener(this);

        // xem giao diện 2 pane hay 1 pane
        mblnTwoPane = findViewById(R.id.two_pane) != null;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

//            holder.mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mTwoPane) {
//                        Bundle arguments = new Bundle();
//                        arguments.putString(OrderDetailFragment.ARG_ITEM_ID, holder.mItem.id);
//                        OrderDetailFragment fragment = new OrderDetailFragment();
//                        fragment.setArguments(arguments);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.order_detail_container, fragment)
//                                .commit();
//                    } else {
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, OrderDetailActivity.class);
//                        intent.putExtra(OrderDetailFragment.ARG_ITEM_ID, holder.mItem.id);
//
//                        context.startActivity(intent);
//                    }
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
