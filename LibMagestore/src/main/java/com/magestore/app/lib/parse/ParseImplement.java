package com.magestore.app.lib.parse;

import android.renderscript.ScriptGroup;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.entity.Entity;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface ParseImplement {
    void prepareParse(InputStream input, ParseEntity parseEntity) throws ParseException, IOException;
    void prepareParse(InputStream input, Class<ParseEntity> cl) throws ParseException, IOException;
    void prepareParse(InputStream input, Type typeOf) throws ParseException, IOException;
    void doParse() throws ParseException, IOException;
    void close();
    boolean isClosed();
    boolean isOpen();
    ParseEntity getParseEntity();
}