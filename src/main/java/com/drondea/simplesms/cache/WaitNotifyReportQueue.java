package com.drondea.simplesms.cache;

import com.drondea.simplesms.bean.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 等待发送队列
 * @author liuyanning
 */
public class WaitNotifyReportQueue {
    private static final Logger logger = LoggerFactory.getLogger(WaitNotifyReportQueue.class);

    private static Map<String, Queue<Report>> USER_REPORT_WAIT_SEND = new ConcurrentHashMap<>();

    public static void offerReport(Report report) {
        Queue<Report> userReportQueue = getUserReportQueue(report.getUserId());
        userReportQueue.offer(report);
    }

    public static Report pullReport(String userId) {
        Queue<Report> userReportQueue = getUserReportQueue(userId);
        return userReportQueue.poll();
    }


    private static Queue<Report> getUserReportQueue(String key) {
        Queue<Report> object = USER_REPORT_WAIT_SEND.get(key);
        if (object != null) {
            return object;
        }
        try {
            object = new ConcurrentLinkedQueue<>();
        } catch (Exception e) {
            logger.error("init object failed" + e.getStackTrace());
        }
        Queue<Report> preObject = USER_REPORT_WAIT_SEND.putIfAbsent(key, object);
        if (preObject != null) {
            object = preObject;
        }
        return object;
    }
}
