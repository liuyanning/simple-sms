package com.drondea.simplesms.util;

import com.drondea.simplesms.bean.Input;
import com.drondea.simplesms.bean.Report;
import com.drondea.simplesms.bean.Submit;
import org.springframework.cglib.beans.BeanCopier;

/**
 * @version V3.0.0
 * @description: 拷贝全局缓存类
 * @author: 刘彦宁
 * @date: 2020年11月19日11:31
 **/
public class CopyUtil {
    public static final BeanCopier SUBMIT_COPIER = BeanCopier.create(Submit.class, Submit.class, false);
    public static final BeanCopier INPUT_SUBMIT_COPIER = BeanCopier.create(Input.class, Submit.class, false);
    public static final BeanCopier SUBMIT_REPORT_COPIER = BeanCopier.create(Submit.class, Report.class, false);
}
