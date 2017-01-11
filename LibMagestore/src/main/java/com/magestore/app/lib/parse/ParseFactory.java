package com.magestore.app.lib.parse;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;

/**
 * Tạo các object parse implement và entity để thực hiện chuyển đổi dữ liệu sang các entity
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class ParseFactory {
    /**
     * Khởi tạo parse implement
     * @param context
     * @return
     * @throws java.text.ParseException
     */
    public static ParseImplement generateParseImplement(MagestoreContext context) throws java.text.ParseException {
        return generateParseImplement(Gson2PosAbstractParseImplement.class);
    }

    /**
     * Khởi tạo entity
     * @param cl
     * @return
     * @throws java.text.ParseException
     */
    public static ParseImplement generateParseImplement(Class cl) throws java.text.ParseException {
        try {
            return (ParseImplement) cl.newInstance();
        } catch (InstantiationException e) {
            throw new ParseException(e);
        } catch (IllegalAccessException e) {
            throw new ParseException(e);
        }
    }

    public static ParseModel generateParseEntity(Class cl) throws IllegalAccessException, InstantiationException {
        return (ParseModel) cl.newInstance();
    }
}
