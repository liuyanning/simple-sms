package com.drondea.simplesms.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户msgid缓存，用来推送回执
 * @author liuyanning
 */
public class UserMsgIdCache {

    private static final Map<String, String> MSG_ID_MAP = new ConcurrentHashMap<>();

    public static String getKey(String batchNo, int index) {
        return batchNo + "_" + index;
    }

    public static void saveMsgId(String batchNo, int index, String msgId) {
        MSG_ID_MAP.put(getKey(batchNo, index), msgId);
    }

    public static String getMsgId(String batchNo, int index) {
        return MSG_ID_MAP.get(getKey(batchNo, index));
    }
}
