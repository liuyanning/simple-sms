package com.drondea.simplesms.cache;

import com.drondea.simplesms.bean.Submit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提交缓存用户匹配回执
 * @author liuyanning
 */
public class SubmitCache {

    private static final Map<String, Submit> SUBMIT_CACHE = new ConcurrentHashMap<>();

    public static void saveSubmit(String key, Submit submit) {
        SUBMIT_CACHE.put(key, submit);
    }

    public static Submit getSubmit(String key) {
        return SUBMIT_CACHE.get(key);
    }

    public static void removeSubmit(String key) {
        SUBMIT_CACHE.remove(key);
    }
}
