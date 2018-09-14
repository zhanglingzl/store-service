package com.rxr.store.wechat.util;

import com.google.common.base.Joiner;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.Charset;
import java.util.*;

public final class HashUtils {
    private static final HashFunction SHA_1 = Hashing.sha1();
    private static final HashFunction MD5 = Hashing.md5();

    public enum SignType {
        MD5, HMACSHA256, SHA1
    }

    public static String getSHA1(String str) {
        return SHA_1.hashString(str, Charset.forName("UTF-8")).toString();
    }

    public static String getHmacSha256(String str, String key) {
        return Hashing.hmacSha256(key.getBytes()).hashString(str,Charset.forName("UTF-8")).toString();
    }

    public static String getMD5(String str) {
        return MD5.hashString(str, Charset.forName("UTF-8")).toString();
    }

    public static String getSign(Map<String, String> elementMap, String payKey, SignType signType) {
        List<String> keys = new ArrayList<>(elementMap.keySet());
        Map<String, String> sortMap = new LinkedHashMap<>(16);
        Collections.sort(keys);
        keys.forEach(key -> sortMap.put(key, elementMap.get(key)));
        sortMap.put("key", payKey);
        String sign = Joiner.on("&").withKeyValueSeparator("=").join(sortMap);
        switch (signType) {
            case MD5:
                return getMD5(sign).toUpperCase();
            case SHA1:
                return getSHA1(sign);
            case HMACSHA256:
                return getHmacSha256(sign, sortMap.get("key"));
                default:
                    return "";
        }
    }

    public static void main(String[] args) {
        System.out.println(RandomStringUtils.random(32, true, true));
    }

}
