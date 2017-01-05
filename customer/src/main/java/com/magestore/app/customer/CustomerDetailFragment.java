package com.magestore.app.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magestore.app.customer.dummy.DummyContent;

import java.util.List;

/**
 * A fragment representing a single Customer detail screen.
 * This fragment is either contained in a {@link CustomerListActivity}
 * in two-pane mode (on tablets) or a {@link CustomerDetailActivity}
 * on handsets.
 */
public class CustomerDetailFragment extends Fragment {
    /**
     * Customer được chọn ở CustomerListActivity
     */
    public static Customer mCustomer;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CustomerDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mCustomer != null) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null & mCustomer != null) {
                appBarLayout.setTitle(mCustomer.getFirstName() + " " + mCustomer.getLastName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_detail, container, false);

        // Điền thông tin khách hàng vào phần customer information
        if (mCustomer != null) {
            TextView txtView = (TextView) rootView.findViewById(R.id.txt_first_name);
            txtView.setText(mCustomer.getFirstName());

            txtView = (TextView) rootView.findViewById(R.id.txt_last_name);
            txtView.setText(mCustomer.getLastName());

            txtView = (TextView) rootView.findViewById(R.id.txt_email);
            txtView.setText(mCustomer.getLastName());

            txtView = (TextView) rootView.findViewById(R.id.txt_telephone);
            txtView.setText(mCustomer.getLastName());
        }

        // Tham chiếu đến view danh sách addres
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.customer_address);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        if (mCustomer != null)
            recyclerView.setAdapter(new CustomerDetailFragment.ListAddressRecyclerViewAdapter(mCustomer.getAddress()));
        return rootView;
    }

    /**
     * Adapter chuyển đổi List các đối tượng address hiện thị lên activity
     */
    public class ListAddressRecyclerViewAdapter
            extends RecyclerView.Adapter<CustomerDetailFragment.ListAddressRecyclerViewAdapter.ViewHolder> {

        private final List<Address> mValues;

        public ListAddressRecyclerViewAdapter(List<Address> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Đọc layout card_customer_address_content
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_customer_address_content, parent, false);
            return new CustomerDetailFragment.ListAddressRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CustomerDetailFragment.ListAddressRecyclerViewAdapter.ViewHolder holder, int position) {
            // Lấy tên, địa chỉ và telephone của adress hiển thị lên view
            holder.mItem = mValues.get(position);
            holder.mNameView.setText(mValues.get(position).getName());
            holder.mAddressView.setText(mValues.get(position).getFullAddress());
            holder.mPostCodeView.setText(mValues.get(position).getPostCode());
            holder.mTelephoneView.setText(mValues.get(position).getTelephone());
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView mNameView;
            public final TextView mAddressView;
            public final TextView mPostCodeView;
            public final TextView mTelephoneView;

            public View mView;
            public Address mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                // Tham chiếu sang các textview trong layout
                mNameView = (TextView) view.findViewById(R.id.txt_adrress_name);
                mAddressView = (TextView) view.findViewById(R.id.txt_adrress_full);
                mPostCodeView = (TextView) view.findViewById(R.id.txt_adrress_postcode);
                mTelephoneView = (TextView) view.findViewById(R.id.txt_adrress_telephone);
            }
        }
    }
}
