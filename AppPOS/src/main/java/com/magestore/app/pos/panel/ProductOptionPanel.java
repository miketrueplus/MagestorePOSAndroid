package com.magestore.app.pos.panel;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mike on 3/2/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ProductOptionPanel extends AbstractDetailPanel<Product> {
    ExpandableListView expandableListView;
    ProductOptionPanel.CustomExpandableListAdapter expandableListAdapter;

    public ProductOptionPanel(Context context) {
        super(context);
    }

    public ProductOptionPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductOptionPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Đưa item vào giao diện
     * @param item
     */
    @Override
    public void bindItem(Product item) {
        super.bindItem(item);
        if (expandableListAdapter != null) {
            expandableListAdapter.setProduct(item);
            expandableListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Clear danh sách khi lần đầu hiện form
     */
    public void clearList() {
        if (expandableListAdapter != null) {
            expandableListAdapter.setProduct(null);
            expandableListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Khởi tạo layout
     */
    @Override
    public void initLayout() {
        // đặt layout chung cả panel
//        super.initLayout();
        setLayoutPanel(R.layout.panel_product_option_list);

        // progress bar
        setProgressBar(R.id.id_product_option_progress);
        setTextViewMsg(R.id.id_product_option_msg);

        // expan list view
        expandableListView = (ExpandableListView) findViewById(R.id.id_product_option_list);

        expandableListAdapter = new ProductOptionPanel.CustomExpandableListAdapter(getContext());
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getContext(),
//                        expandableListTitle.get(groupPosition)
//                                + " -> "
//                                + expandableListDetail.get(
//                                expandableListTitle.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT
//                ).show();
                return false;
            }
        });
    }

//    @Override
//    public void bindList(List<ProductOption> list) {
////        super.bindList(list);
//        expandableListAdapter.bindList(list);
//        expandableListAdapter.notifyDataSetChanged();
//
//    }

    public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
//        private List<ProductOption> mProductOptionList;
        private Context context;
        private Product mProduct;
//        private List<String> expandableListTitle;
//        private HashMap<String, List<String>> expandableListDetail;

        public CustomExpandableListAdapter(Context context) {
            this.context = context;
//            mProduct = product;
        }

        public void setProduct(Product product) {
            mProduct = product;
        }

        @Override
        public Object getChild(int listPosition, int expandedListPosition) {
            return mProduct.getProductOption().getCustomOptions().get(listPosition).getOptionValueList().get(expandedListPosition).getDisplayContent()
                    + ": " + mProduct.getProductOption().getCustomOptions().get(listPosition).getOptionValueList().get(expandedListPosition).getPrice();
        }

        @Override
        public long getChildId(int listPosition, int expandedListPosition) {
            return expandedListPosition;
        }

        @Override
        public View getChildView(int listPosition, final int expandedListPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String expandedListText = (String) getChild(listPosition, expandedListPosition);
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.sample_list_item, null);
            }
            TextView expandedListTextView = (TextView) convertView
                    .findViewById(R.id.expandedListItem);
            expandedListTextView.setText(expandedListText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int listPosition) {
            if (mProduct == null || mProduct.getProductOption() == null || mProduct.getProductOption().getCustomOptions() == null)
                return 0;
            if (mProduct.getProductOption().getCustomOptions().get(listPosition).getOptionValueList() == null)
                return 0;
            return mProduct.getProductOption().getCustomOptions().get(listPosition).getOptionValueList().size();
        }

        @Override
        public Object getGroup(int listPosition) {
            return mProduct.getProductOption().getCustomOptions().get(listPosition).getDisplayContent();
//            if (mProductOptionList != null) return mProductOptionList.get(listPosition).getDisplayContent();
//            return this.expandableListTitle.get(listPosition);
        }

        @Override
        public int getGroupCount() {
            if (mProduct == null || mProduct.getProductOption() == null || mProduct.getProductOption().getCustomOptions() == null)
                return 0;
            return mProduct.getProductOption().getCustomOptions().size();
        }

        @Override
        public long getGroupId(int listPosition) {
            return listPosition;
        }

        @Override
        public View getGroupView(int listPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String listTitle = (String) getGroup(listPosition);
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context.
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.sample_list_group, null);
            }
            TextView listTitleTextView = (TextView) convertView
                    .findViewById(R.id.listTitle);
            listTitleTextView.setTypeface(null, Typeface.BOLD);
            listTitleTextView.setText(listTitle);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int listPosition, int expandedListPosition) {
            return true;
        }
    }
}
