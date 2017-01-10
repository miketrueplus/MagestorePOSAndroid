package com.magestore.app.lib.adapterview.adapter2pos;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.magestore.app.lib.adapterview.AdapterView;
import com.magestore.app.lib.adapterview.AdapterViewAnnotiation;
import com.magestore.app.lib.model.Model;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.NotFoundObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class AdapterView2Model implements AdapterView {
    private static final int PARSE_FROM_VIEW = 1;
    private static final int PARSE_TO_VIEW = 2;
    Model mModel;
    Class mModelClazz;
    View mView;
    int mintParseFromView;

    // Map giữa annotitaion và field hoặc method
    HashMap<String, Field> mAnnotiationField = new HashMap<String, Field>();
    HashMap<String, Method> mAnnotiationMethodGet = new HashMap<String, Method>();
    HashMap<String, Method> mAnnotiationMethodSet = new HashMap<String, Method>();

    public void setModelClass(Class clazz) {
        mModelClazz = clazz;
        buildAnnotiation(mModelClazz);
    }

    public void setModel(Model model) {
        mModel = model;
    }

    public void setView(View view) {
        mView = view;
    }

    /**
     * Map dữ liệu từ view vào engity
     */
    @Override
    public void fromView() throws InvocationTargetException, IllegalAccessException {
        mintParseFromView = PARSE_FROM_VIEW;
        debugViewIds(mView);
    }

    /**
     * Map dữ liệu tử entity vào view
     */
    @Override
    public void toView() throws InvocationTargetException, IllegalAccessException {
        mintParseFromView = PARSE_TO_VIEW;
        debugViewIds(mView);
    }

    /**
     * Xây dựng sẵn các annotiation nếu có
     * @param clazz
     */
    private void buildAnnotiation(Class clazz) {
        // Quét annotiation của class
//        Class clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getDeclaredMethods();

        // Quét field của class
        for (Field field : fields) {
            AdapterViewAnnotiation annotiation = field.getAnnotation(AdapterViewAnnotiation.class);
            if (annotiation== null) continue;
            mAnnotiationField.put(annotiation.resName(), field);
        }

        // Quét method của class
        for (Method method : methods) {
            AdapterViewAnnotiation annotiation = method.getAnnotation(AdapterViewAnnotiation.class);
            if (annotiation== null) continue;
            if (annotiation.methodType() == AdapterViewAnnotiation.MethodType.SET)
                mAnnotiationMethodSet.put(annotiation.resName(), method);
            else
                mAnnotiationMethodGet.put(annotiation.resName(), method);
        }
    }

    /**
     * Trả về giá trị resName tương ứng theo annotiation
     * @param resName
     * @return
     */
    private Object getAnnotiationValue(String resName) throws IllegalAccessException, InvocationTargetException {
        // Check trước trong field
        Field field = mAnnotiationField.get(resName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(this);
        }

        // Check trước trong method
        Method method = mAnnotiationMethodGet.get(resName);
        if (method != null) {
            method.setAccessible(true);
            return method.invoke(mModel);
        }

        return NotFoundObject.getInstance();
    }

    /**
     * Đặt giá trị theo resName tương ứng theo annotiation
     * @param resName
     */
    private boolean setAnnotiationValue(String resName, Object value) throws IllegalAccessException, InvocationTargetException {
        // Check trước trong field
        Field field = mAnnotiationField.get(resName);
        if (field != null) {
            field.setAccessible(true);
            field.set(this, value);
            return true;
        }

        // Check trước trong method
        Method method = mAnnotiationMethodSet.get(resName);
        if (method != null) {
            method.setAccessible(true);
            method.invoke(mModel, value);
            return true;
        }
        return false;
    }

    /**
     * Duyệt toàn bộ view
     * @param view
     */
    private View debugViewIds(View view) throws InvocationTargetException, IllegalAccessException {
        int resID = view.getId();
        if (resID != -1) {
            parseEntity(view, mModel, view.getResources().getResourceEntryName(resID), mintParseFromView);
        }
        if (view.getParent() != null && (view.getParent() instanceof ViewGroup)) {
            return debugViewIds((View)view.getParent());
        }
        else {
            debugChildViewIds(view);
            return view;
        }
    }

    /**
     * Duyệt toàn bộ child view
     * @param view
     */
    private void debugChildViewIds(View view) throws InvocationTargetException, IllegalAccessException {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                int resID = child.getId();
                if (resID != -1) {
                    parseEntity(child, mModel, child.getResources().getResourceEntryName(resID), mintParseFromView);
                }
                debugChildViewIds(child);
            }
        }
    }

    private void parseEntity(View view, Model model, String resName, int fromOrToView) throws InvocationTargetException, IllegalAccessException {
        if (fromOrToView == PARSE_TO_VIEW) parseToView(view, resName);
        else parseFromView(view, resName);
    }

    private void parseToView(View view, String resName) throws InvocationTargetException, IllegalAccessException {
        // Kiểm tra trước trong annotiatation, nếu có thực hiện trước
        Object object = getAnnotiationValue(resName);
        if (!(object instanceof NotFoundObject)) {
            ((TextView) view).setText(object.toString());
            return;
        }

        // Nếu không có trong annotiation thì thực hiện theo field
        object = mModel.getValue(resName);
        if (object instanceof NotFoundObject) return;
        if (object == null) return;
        if (view instanceof TextView)
            ((TextView) view).setText(object.toString());

    }

    private void parseFromView(View view, String resName) throws InvocationTargetException, IllegalAccessException {
        // Lấy giá trị trong view
        if (!(view instanceof EditText)) return;
        String value = ((EditText) view).getText().toString();

        // Kiểm tra trước trong annotiatation, nếu có thực hiện trước
        if (setAnnotiationValue(resName, value)) return;

        // Nếu không có trong annotiation thì thực hiện theo field
        Object object = mModel.getValue(resName);
        if (object instanceof NotFoundObject) return;
        mModel.setValue(resName, value);
        return;
    }
}
