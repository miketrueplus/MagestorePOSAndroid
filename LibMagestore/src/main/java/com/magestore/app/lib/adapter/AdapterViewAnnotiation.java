package com.magestore.app.lib.adapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation để map từ entity sang view
 * Created by Mike on 1/5/2017.
 * Magestore
 * mike@trueplus.vn
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdapterViewAnnotiation {
    public enum MethodType {
        SET,
        GET
    }

    String resName();
    MethodType methodType();
}