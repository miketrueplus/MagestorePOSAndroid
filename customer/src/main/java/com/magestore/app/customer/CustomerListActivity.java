package com.magestore.app.customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.magestore.app.customer.dummy.DummyContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Customer. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CustomerDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CustomerListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        // Thanh toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


//        setupRecyclerView((RecyclerView) mRecyclerView);

        if (findViewById(R.id.customer_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // Tham chiếu đến view danh sách khách hàng
        mRecyclerView = (RecyclerView) findViewById(R.id.customer_list);

        // Thực hiện truy vấn danh sách khách hàng
        LoadListCustomer task = new LoadListCustomer();
        task.execute();
    }

    public class LoadListCustomer extends AsyncTask<Void, Void, List<Customer>> {
        public Exception mException = null;
        public LoadListCustomer() {
            super();
        }

        @Override
        protected void onPostExecute(List<Customer> customers) {
            // Truy vấn thành công danh sách khách hàng, sử dụng Adapter hiển thị lên màn hình luôn
            if (customers != null)
                mRecyclerView.setAdapter(new ListCustomerRecyclerViewAdapter(customers));

        }

        @Override
        protected void onCancelled() {
            if (mException == null) return;

            // Dừng truy vấn do lỗi
            // Hiển thị thông báo lỗi lên màn hình
            AlertDialog alertDialog = new AlertDialog.Builder(CustomerListActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage(mException.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        @Override
        protected List<Customer> doInBackground(Void... params) {
            try {
                // Gọi API lấy về danh sách khách hàng
                List<Customer> customerList = MagestorePosAPI.getListCustomer();
                return customerList;
            } catch (Exception e) {
                // Nếu có lỗi, dừng thực hiện và hiện thông báo lỗi
                mException = e;
                cancel(true);
                return null;
            }
        }
    }

    /**
     * Adapter chuyển đổi List các đối tượng Customer hiện thị lên activity
     */
    public class ListCustomerRecyclerViewAdapter
            extends RecyclerView.Adapter<ListCustomerRecyclerViewAdapter.ViewHolder> {

        private final List<Customer> mValues;

        public ListCustomerRecyclerViewAdapter(List<Customer> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Đọc layout customer_list_content
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.customer_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // Lấy tên, email và telephone của customer hiển thị lên view
            holder.mItem = mValues.get(position);
            holder.mNameView.setText(mValues.get(position).getName());
            holder.mEmailView.setText(mValues.get(position).getEmail());
            holder.mTelephoneView.setText(mValues.get(position).getTelephone());

            // Bắt sự kiện click vào customer
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomerDetailFragment.mCustomer = holder.mItem;
                    if (mTwoPane) {
                        // Nếu là tablet
                        // Truyền customer được click chọn sang Fragment
                        Bundle arguments = new Bundle();
                        CustomerDetailFragment fragment = new CustomerDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.customer_detail_container, fragment)
                                .commit();
                    } else {
                        // Nếu là điện thoại
                        // Truyền cusotmer được chọn sang CustomerDetailActivity
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CustomerDetailActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView mNameView;
            public final TextView mEmailView;
            public final TextView mTelephoneView;
            public View mView;
            public Customer mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                // Tham chiếu sang các textview trong layout
                mNameView = (TextView) view.findViewById(R.id.customer_name);
                mEmailView = (TextView) view.findViewById(R.id.customer_email);
                mTelephoneView = (TextView) view.findViewById(R.id.customer_telephone);
            }
        }
    }
}
