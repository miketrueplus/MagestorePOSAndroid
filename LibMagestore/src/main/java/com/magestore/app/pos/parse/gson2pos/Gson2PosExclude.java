package com.magestore.app.pos.parse.gson2pos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Mike on 12/19/2016.
 * Magestore
 * mike@trueplus.vn
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Gson2PosExclude {

}
