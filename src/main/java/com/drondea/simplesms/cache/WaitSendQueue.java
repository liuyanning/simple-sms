package com.drondea.simplesms.cache;

import com.drondea.simplesms.bean.Input;
import com.drondea.simplesms.bean.Submit;
import com.drondea.simplesms.bean.User;
import com.drondea.simplesms.gateway.service.TcpValidator;
import com.drondea.simplesms.util.CopyUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 等待发送队列
 * @author liuyanning
 */
public class WaitSendQueue {
    private static Queue<Submit> CHINA_MOBILE_WAIT_SEND = new ConcurrentLinkedQueue<>();
    private static Queue<Submit> CHINA_UNICOM_WAIT_SEND = new ConcurrentLinkedQueue<>();
    private static Queue<Submit> CHINA_TELECOM_WAIT_SEND = new ConcurrentLinkedQueue<>();

    public static void route(Input input) {
        Submit submit = new Submit();
        CopyUtil.INPUT_SUBMIT_COPIER.copy(input, submit, null);
        String inputSubCode = input.getSubCode();
        submit.setInputSubCode(inputSubCode);
        User user = TcpValidator.getUser(input.getUserId());
        //去除用户主码号
        if (user != null && StringUtils.isNotEmpty(user.getSpNumber())
                && StringUtils.isNotEmpty(inputSubCode) && inputSubCode.startsWith(user.getSpNumber())) {
            String spNumber = StringUtils.replaceOnce(inputSubCode, user.getSpNumber(), "");
            submit.setSubCode(spNumber);
        }
        //路由到对应的通道
        CHINA_MOBILE_WAIT_SEND.offer(submit);
    }

    public static Submit pullSubmit(String type) {
        if ("chinaMobile".equals(type)) {
            return CHINA_MOBILE_WAIT_SEND.poll();
        } else if ("chinaUnicom".equals(type)) {
            return CHINA_UNICOM_WAIT_SEND.poll();
        } else if ("chinaTelecom".equals(type)) {
            return CHINA_TELECOM_WAIT_SEND.poll();
        }
        return null;
    }
}
