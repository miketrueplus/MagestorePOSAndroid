package com.magestore.app.pos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.controller.ControllerListener;
import com.magestore.app.lib.entity.Customer;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.pos.controller.LoadCustomerController;
import com.magestore.app.pos.controller.LoadProductController;
import com.magestore.app.pos.controller.LoadProductImageController;
import com.magestore.app.pos.dummy.DummyContent;
import com.magestore.app.pos.ui.AbstractActivity;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CustomerDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CustomerListActivity extends AbstractActivity {
    private LoadCustomerController mLoadCustomer = null;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    List<Customer> mListCustomer;
    RecyclerView mCustomerListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_menu);

        initControlLayout();
        initControlValue();
        initTask();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
        {
            mLoadCustomer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else // Below Api Level 13
        {
            mLoadCustomer.execute();
        }
    }

    @Override
    protected void initControlLayout() {
        super.initControlLayout();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        initToolbarMenu(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mCustomerListRecyclerView = (RecyclerView) findViewById(R.id.customer_list);
        assert mCustomerListRecyclerView != null;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        mCustomerListRecyclerView.setLayoutManager(mLayoutManager);

        if (findViewById(R.id.customer_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    protected void initControlValue() {
        super.initControlValue();
    }

    @Override
    protected void initTask() {
        super.initTask();
        mLoadCustomer = new LoadCustomerController(new CustomerListActivity.LoadCustomerListener());
    }

    public List<Customer> getCustomerList() {
        return mListCustomer;
    }

    public void setCustomerList(List<Customer> listCustomer) {
        mListCustomer = listCustomer;
    }

    public class CustomerListRecyclerViewAdapter
            extends RecyclerView.Adapter<CustomerListActivity.CustomerListRecyclerViewAdapter.CustomerListViewHolder> {

        private final List<Customer> mValues;

        public CustomerListRecyclerViewAdapter(List<Customer> items) {
            mValues = items;
        }

        @Override
        public CustomerListActivity.CustomerListRecyclerViewAdapter.CustomerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.customer_list_content, parent, false);
            return new CustomerListActivity.CustomerListRecyclerViewAdapter.CustomerListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CustomerListActivity.CustomerListRecyclerViewAdapter.CustomerListViewHolder holder, final int position) {
            Customer customer = mValues.get(position);
            holder.mItem = customer;

            // Đặt các trường text vào danh sách
            holder.mNameView.setText(customer.getName());
            holder.mEmailView.setText(customer.getEmail());
            holder.mTelephoneView.setText(customer.getTelephone());
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class CustomerListViewHolder extends RecyclerView.ViewHolder {
            public final TextView mNameView;
            public final TextView mEmailView;
            public final TextView mTelephoneView;
            public Customer mItem;
            public View mView;

            public CustomerListViewHolder(View view) {
                super(view);
                mNameView = (TextView) view.findViewById(R.id.customer_name);
                mEmailView = (TextView) view.findViewById(R.id.customer_email);
                mTelephoneView = (TextView) view.findViewById(R.id.customer_telephone);
            }
        }
    }

    /**
     * Xử lý các sự kiện khi load danh sách product lần đầu
     */
    public class LoadCustomerListener implements ControllerListener<Void, Void, List<Customer>> {
        @Override
        public void onPreController(Controller controller) {
            showProgress(true);
        }

        @Override
        public void onPostController(Controller controller, List<Customer> customersList) {
            // Tất progress đi
            mLoadCustomer = null;
            showProgress(false);

            // Hiển thị danh sách product
            setCustomerList(customersList);
            if (mListCustomer != null)
                mCustomerListRecyclerView.setAdapter(new CustomerListActivity.CustomerListRecyclerViewAdapter(mListCustomer));
        }

        @Override
        public void onCancelController(Controller controller, Exception exp) {
            mLoadCustomer = null;
            showProgress(false);
        }

        @Override
        public void onProgressController(Controller controller, Void... progress) {

        }
    }
}
