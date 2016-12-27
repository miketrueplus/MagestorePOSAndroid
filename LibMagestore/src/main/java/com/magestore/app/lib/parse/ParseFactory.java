package com.magestore.app.lib.parse;

import com.magestore.app.lib.entity.Entity;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.entity.pos.PosProduct;

import org.apache.olingo.client.api.edm.xml.EntityType;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.text.*;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class ParseFactory {
    public static ParseImplement generateParseImplement(Class cl) throws java.text.ParseException {
        try {
            return (ParseImplement) cl.newInstance();
        } catch (InstantiationException e) {
            throw new ParseException(e);
        } catch (IllegalAccessException e) {
            throw new ParseException(e);
        }
    }

    public static ParseEntity generateParseEntity(Class cl) throws IllegalAccessException, InstantiationException {
        return (ParseEntity) cl.newInstance();
    }
}
