package com.magestore.app.pos.panel;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.dummy.ExpandableListDataPump;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mike on 3/2/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ProductOptionListPanel extends AbstractListPanel<ProductOption> {
    ExpandableListView expandableListView;
    ProductOptionListPanel.CustomExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    public ProductOptionListPanel(Context context) {
        super(context);
    }

    public ProductOptionListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductOptionListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Khởi tạo layout
     */
    @Override
    public void initLayout() {
        // đặt layout chung cả panel
        super.initLayout();
        setLayoutPanel(R.layout.panel_product_option_list);

        // expan list view
        expandableListView = (ExpandableListView) findViewById(R.id.id_product_option_list);

        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new ProductOptionListPanel.CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }

    @Override
    public void bindList(List<ProductOption> list) {
//        super.bindList(list);
        expandableListAdapter.bindList(list);
        expandableListAdapter.notifyDataSetChanged();

    }

    public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
        private List<ProductOption> mProductOptionList;
        private Context context;
        private List<String> expandableListTitle;
        private HashMap<String, List<String>> expandableListDetail;

        public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                           HashMap<String, List<String>> expandableListDetail) {
            this.context = context;
            this.expandableListTitle = expandableListTitle;
            this.expandableListDetail = expandableListDetail;
        }

        public void bindList(List<ProductOption> productOptions) {
            mProductOptionList = productOptions;
        }

        @Override
        public Object getChild(int listPosition, int expandedListPosition) {
//            if (mProductOptionList != null) {
//                return mProductOptionList.get(listPosition).getOptionValueList().get(expandedListPosition).getDisplayContent();
//            }
//            else
                return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                    .get(expandedListPosition);
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
//            if (mProductOptionList != null) {
//                return mProductOptionList.get(listPosition).getOptionValueList().size();
//            }
//            else
                return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
        }

        @Override
        public Object getGroup(int listPosition) {
            if (mProductOptionList != null) return mProductOptionList.get(listPosition).getDisplayContent();
            return this.expandableListTitle.get(listPosition);
        }

        @Override
        public int getGroupCount() {
            if (mProductOptionList != null) return mProductOptionList.size();
            else return this.expandableListTitle.size();
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
