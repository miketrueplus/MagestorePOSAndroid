package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.util.ConfigUtil;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Johan on 3/30/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutDeliveryPanel extends AbstractDetailPanel<Checkout> {
    DatePicker delivery_date;
    TimePicker delivery_time;
    String date_delivery = "";
    String time_delivery = "";
    String current_date_time = "";
    int mYear, mMonth, mDay, hour, minute, second;

    public CheckoutDeliveryPanel(Context context) {
        super(context);
    }

    public CheckoutDeliveryPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutDeliveryPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_checkout_delivery, null);
        addView(view);

        delivery_date = (DatePicker) view.findViewById(R.id.delivery_date);
        delivery_time = (TimePicker) view.findViewById(R.id.delivery_time);

        initValue();
    }

    @Override
    public void initValue() {
        delivery_time.setIs24HourView(true);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTimeInMillis(System.currentTimeMillis());
        mYear = calendar.get(Calendar.YEAR); // current year
        mMonth = calendar.get(Calendar.MONTH); // current month
        mDay = calendar.get(Calendar.DAY_OF_MONTH); // current day
        hour = calendar.get(Calendar.HOUR_OF_DAY); // current hour
        minute = calendar.get(Calendar.MINUTE); // current minute
        second = calendar.get(Calendar.SECOND); // current second

        delivery_date.setMinDate(calendar.getTimeInMillis());
        date_delivery = mYear + "-" + (mMonth + 1) + "-" + mDay;
        time_delivery = hour + ":" + minute;
        current_date_time = date_delivery + " " + time_delivery;

        delivery_date.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
                if (view.isShown())
                    date_delivery = year + "-" + (month + 1) + "-" + dayOfMonth;
            }
        });

        delivery_time.setCurrentHour(hour);
        delivery_time.setCurrentMinute(minute);
        delivery_time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown())
                    time_delivery = hourOfDay + ":" + minute ;
            }
        });
    }

    public String showData(){
        return date_delivery + " " + time_delivery;
    }

    public String showDataNow(){
        return current_date_time;
    }

    public String bindDateTime() {
        String date_time = date_delivery + " " + time_delivery + ":" + "00";
        String convert_time = ConfigUtil.convertToGMTTime(date_time);
        return convert_time;
    }

    public String bindDateTimeNow() {
        String date_time_now = current_date_time + ":" + second;
        String convert_time_now = ConfigUtil.convertToGMTTime(date_time_now);
        return convert_time_now;
    }
}
