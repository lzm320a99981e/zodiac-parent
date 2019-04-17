package com.github.lzm320a99981e.zodiac.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * 签名工具类
 */
public abstract class HashUtils {
    /**
     * 使用MD5算法获取hash码
     *
     * @param value
     * @return
     */
    public static String md5Hash(Object value) {
        return hash(Hashing.md5().newHasher(), value);
    }

    /**
     * 使用SHA-256算法获取hash码
     *
     * @param value
     * @return
     */
    public static String sha256Hash(Object value) {
        return hash(Hashing.sha256().newHasher(), value);
    }

    /**
     * 使用SHA-512算法获取hash码
     *
     * @param value
     * @return
     */
    public static String sha512Hash(Object value) {
        return hash(Hashing.sha512().newHasher(), value);
    }

    /**
     * 使用murmur3_32算法获取hash码
     *
     * @param value
     * @return
     */
    public static String murmur3_32Hash(Object value) {
        return hash(Hashing.murmur3_32().newHasher(), value);
    }

    /**
     * 使用murmur3_128算法获取hash码
     *
     * @param value
     * @return
     */
    public static String murmur3_128Hash(Object value) {
        return hash(Hashing.murmur3_128().newHasher(), value);
    }

    private static String hash(Hasher hasher, Object value) {
        Preconditions.checkNotNull(value);
        if (CharSequence.class.isAssignableFrom(value.getClass())) {
            return hasher.putString((CharSequence) value, StandardCharsets.UTF_8).hash().toString();
        }
        // 对需要进行hash的数据进行排序
        return hasher.putString(JSON.toJSONString(value, SerializerFeature.SortField), StandardCharsets.UTF_8).hash().toString();
    }

}
