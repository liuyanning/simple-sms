package com.drondea.simplesms.util;

import com.drondea.simplesms.bean.Report;
import com.drondea.simplesms.bean.Submit;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Lenovo
 */
public abstract class SMSUtil {

    /**
     * 根据byte数据获取编码字符串
     *
     * @param charSet
     * @return
     */
    public static String getSmppCharsetByByte(byte charSet) {
        String dcs = "ASCII";
        switch (charSet) {
            case 0:
                dcs = "GSM";
                break;
            case 1:
                dcs = "ASCII";
                break;
            case 3:
                dcs = "LATIN1";
                break;
            case 4:
                dcs = "LATIN1";
                break;
            case 8:
                dcs = "UCS2";
                break;
            case 12:
                dcs = "RESERVED";
                break;
            case 15:
                dcs = "RESERVED";
                break;
        }
        return dcs;
    }

    /**
     * 根据byte数据获取编码字符串
     *
     * @param charSet
     * @return
     */
    public static String getCharsetByByte(byte charSet) {

        String dcs = "ASCII";
        switch (charSet) {
            case 0:
                dcs = "ASCII";
                break;
            case 4:
                dcs = "LATIN1";
                break;
            case 8:
                dcs = "UCS2";
                break;
            case 12:
                dcs = "RESERVED";
                break;
            case 15:
                dcs = "GBK";
                break;
        }
        return dcs;
    }

    /**
     * 根据编码字符串获取byte数据
     *
     * @param charSet
     * @return
     */
    public static byte getGeneralDataCodingDcs(String charSet) {
        byte dcs = 0x08;
        if (StringUtils.isEmpty(charSet)) {
            return 0x08;
        }
        switch (charSet) {
            case "ASCII":
                dcs = 0x00;
                break;
            case "LATIN1":
                dcs = 0x04;
                break;
            case "UCS2":
                dcs = 0x08;
                break;
            case "RESERVED":
                dcs = 0x0C;
                break;
            case "GBK":
                dcs = 0x0F;
                break;
        }
        return dcs;
    }

    public static byte getSmppGeneralDataCodingDcs(String charSet) {
        byte dcs = 0x00;
        if (StringUtils.isEmpty(charSet)) {
            return 0x08;
        }
        switch (charSet) {
            case "GSM":
                dcs = 0x00;
                break;
            case "ASCII":
                dcs = 0x00;//ASCII也采用GSM编码格式，支持160字节。20220514
                break;
            case "LATIN1":
                dcs = 0x03;
                break;
            case "UCS2":
                dcs = 0x08;
                break;
            case "RESERVED":
                dcs = 0x0F;
                break;
        }
        return dcs;
    }

    public static String getSubmitKey(String channelNo, String msgId) {
        return channelNo + ":" + msgId;
    }


    public static Report buildReport(Submit submit, String mobile) {
        Report report = new Report();
        //对象拷贝
        CopyUtil.SUBMIT_REPORT_COPIER.copy(submit, report, null);
        report.setMobile(mobile);
        report.setStatusCode("unknown");
        return report;
    }

}
