package com.magestore.app.pos.panel;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.controller.ControllerListener;
import com.magestore.app.lib.entity.Customer;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.LoadCustomerController;

import java.util.List;

/**
 * Panel giao diện quản lý danh sách khách hàng
 * Created by Mike on 12/29/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerListPanel extends FrameLayout {
    // View quản lý danh sách khách hàng
    RecyclerView mCustomerListRecyclerView;

    // Task load danh sách khách hàng
    LoadCustomerController mLoadCustomerTask;

    // Data Danh sách khách hàng
    List<Customer> mListCustomer;

    // Bắt các sự kiện
    CustomerListPanelListener mCustomerListPanelListener;

    public CustomerListPanel(Context context) {
        super(context);
        init();
    }

    public CustomerListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomerListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(CustomerListPanelListener customerListPanelListener) {
        mCustomerListPanelListener = customerListPanelListener;
    }

    private void init() {
        initControlLayout();
        initControlValue();
        initTask();
    }

    public void loadCustomerList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
        {
            mLoadCustomerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else // Below Api Level 13
        {
            mLoadCustomerTask.execute();
        }
    }

    private void initControlValue() {

    }

    private void initControlLayout() {
//        super.initControlLayout();

        // Load layout view danh sách khách hàng
        View v = inflate(getContext(), R.layout.panel_customer_list, null);
        addView(v);

        // Xử lý sự kiện floating action bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Chuẩn bị list danh sách khách hàng
        mCustomerListRecyclerView = (RecyclerView) findViewById(R.id.customer_list);
        mCustomerListRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
    }


    /**
     * Chuẩn bị layout
     */
//    @Override
    private void initTask() {
//        super.initTask();
        mLoadCustomerTask = new LoadCustomerController(new CustomerListPanel.LoadCustomerListener());
    }

    /**
     * Hiển thị tiến trình
     *
     * @param show
     */
    public void showProgress(boolean show) {

    }

    /**
     * Trả lại danh sách khách hàng
     *
     * @return
     */
    public List<Customer> getCustomerList() {
        return mListCustomer;
    }

    /**
     * Đặt danh sách khách hàng
     *
     * @param listCustomer
     */
    public void setCustomerList(List<Customer> listCustomer) {
        mListCustomer = listCustomer;
    }

    /**
     * Xử lý các sự kiện khi load danh sách customer
     */
    public class LoadCustomerListener implements ControllerListener<Void, Void, List<Customer>> {
        @Override
        public void onPreController(Controller controller) {
            showProgress(true);
        }

        @Override
        public void onPostController(Controller controller, List<Customer> customersList) {
            // Tất progress đi
            mLoadCustomerTask = null;
            showProgress(false);

            // Hiển thị danh sách customer trên recycle view
            setCustomerList(customersList);
            if (mListCustomer != null) {
                mCustomerListRecyclerView.setAdapter(new CustomerListPanel.CustomerListRecyclerViewAdapter(mListCustomer));
                if (mCustomerListPanelListener != null)
                    mCustomerListPanelListener.onSuccessLoadCustomer(mListCustomer);
            }
        }

        @Override
        public void onCancelController(Controller controller, Exception exp) {
            mLoadCustomerTask = null;
            showProgress(false);
        }

        @Override
        public void onProgressController(Controller controller, Void... progress) {

        }
    }

    /**
     * Adapter map CustomerList sang view
     */
    public class CustomerListRecyclerViewAdapter
            extends RecyclerView.Adapter<CustomerListPanel.CustomerListRecyclerViewAdapter.CustomerListViewHolder> {

        // đánh dấu vị trí đã chọn
        private int selectedPos = 0;

        // Danh sách khách hàng của view
        private final List<Customer> mCustomerList;

        public CustomerListRecyclerViewAdapter(List<Customer> customerList) {
            mCustomerList = customerList;
        }

        /**
         * Khởi tạo danh sách khách hàng
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public CustomerListPanel.CustomerListRecyclerViewAdapter.CustomerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.customer_list_content, parent, false);
            return new CustomerListPanel.CustomerListRecyclerViewAdapter.CustomerListViewHolder(view);
        }

        /**
         * Map dataset sang view
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(final CustomerListPanel.CustomerListRecyclerViewAdapter.CustomerListViewHolder holder, final int position) {
            Customer customer = mCustomerList.get(position);
            holder.mItem = customer;

            // Đặt các trường text vào danh sách
            holder.mNameView.setText(customer.getName());
            holder.mEmailView.setText(customer.getEmail());
            holder.mTelephoneView.setText(customer.getTelephone());

            // highlight vị trí đã chọn
            holder.itemView.setSelected(selectedPos == position);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update highlight khách hàng đã chọn
                    notifyItemChanged(selectedPos);
                    selectedPos = position;
                    notifyItemChanged(selectedPos);

                    // Thông báo sự kiện khi đã chọn customer
                    if (mCustomerListPanelListener != null)
                        mCustomerListPanelListener.onSelectCustomer(mCustomerList.get(position));
//                    if (mTwoPane) {
//                        Bundle arguments = new Bundle();
//                        arguments.putString(OrderDetailFragment.ARG_ITEM_ID, "2223");
//                        CustomerDetailFragment fragment = new CustomerDetailFragment();
//                        fragment.setArguments(arguments);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.customer_detail_container, fragment)
//                                .commit();
//                    } else {
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, CustomerDetailActivity.class);
//                        intent.putExtra(CustomerDetailFragment.ARG_ITEM_ID, "2223");
//
//                        context.startActivity(intent);
//                    }
                }
            });
        }

        /**
         * Đếm số bản ghi trong view
         *
         * @return
         */
        @Override
        public int getItemCount() {
            return mCustomerList.size();
        }

        /**
         * Nắm giữ từng view con trong list
         */
        public class CustomerListViewHolder extends RecyclerView.ViewHolder {
            public final TextView mNameView;
            public final TextView mEmailView;
            public final TextView mTelephoneView;
            public Customer mItem;
            public View mView;

            public CustomerListViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (TextView) view.findViewById(R.id.customer_name);
                mEmailView = (TextView) view.findViewById(R.id.customer_email);
                mTelephoneView = (TextView) view.findViewById(R.id.customer_telephone);
            }
        }
    }
}
