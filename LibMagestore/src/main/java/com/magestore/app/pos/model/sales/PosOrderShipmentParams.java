package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.checkout.cart.Items;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderShipmentItemParams;
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
    List<OrderShipmentItemParams> items;
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
    public List<OrderShipmentItemParams> getItems() {
        return items;
    }

    @Override
    public void setItems(List<OrderShipmentItemParams> items) {
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
