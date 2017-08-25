package com.magestore.app.pos.model.magento.sales;

import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderItemParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderShipmentTrackParams;
import com.magestore.app.pos.model.PosAbstractModel;
import java.util.List;

/**
 * Created by Johan on 1/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderShipmentParams extends PosAbstractModel implements OrderShipmentParams {
    String orderId;
    String emailSent;
    List<OrderItemParams> items;
    List<OrderShipmentTrackParams> tracks;
    List<OrderCommentParams> comments;

    @Override
    public void setOrderId(String strOrderId) {
        orderId = strOrderId;
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void setEmailSent(String strEmailSent) {
        emailSent = strEmailSent;
    }

    @Override
    public String getEmailSent() {
        return emailSent;
    }

    @Override
    public List<OrderItemParams> getItems() {
        return items;
    }

    @Override
    public void setItems(List<OrderItemParams> items) {
        this.items = items;
    }

    @Override
    public List<OrderShipmentTrackParams> getTracks() {
        return (List<OrderShipmentTrackParams>) (List<?>) tracks;
    }

    @Override
    public void setTracks(List<OrderShipmentTrackParams> tracks) {
        this.tracks = tracks;
    }

    @Override
    public List<OrderCommentParams> getComments() {
        return (List<OrderCommentParams>) (List<?>) comments;
    }

    @Override
    public void setComments(List<OrderCommentParams> comments) {
        this.comments = comments;
    }
}
