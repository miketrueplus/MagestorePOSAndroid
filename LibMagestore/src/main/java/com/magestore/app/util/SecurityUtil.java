package com.magestore.app.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Mike on 2/4/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class SecurityUtil {
    /**
     * Sinh nhanh chuỗi HASH bằng MD5
     * @param strSource
     * @return
     */
    public static String getHash(String strSource) {
        MessageDigest m= null;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(strSource.getBytes(), 0, strSource.length());
            return new String(new BigInteger(1,m.digest()).toString(16));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return strSource;
    }
}
