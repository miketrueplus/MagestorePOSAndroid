package com.magestore.app.lib;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.magestore.app.pos.model.registershift.PosCashTransaction;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        PosCashTransaction cashTransaction = new PosCashTransaction();
        cashTransaction.setCheckOpenShift(true);
        cashTransaction.setBalance(1.0f);

        class MyExclusionStrategy implements ExclusionStrategy {

            private MyExclusionStrategy() {
            }


            public boolean shouldSkipField(FieldAttributes f) {
                return f.getAnnotation(Gson2PosExclude.class) == null;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }

        Gson gson = new GsonBuilder().setExclusionStrategies(new MyExclusionStrategy()).create();
        String str = gson.toJson(cashTransaction);
        return;
    }
}