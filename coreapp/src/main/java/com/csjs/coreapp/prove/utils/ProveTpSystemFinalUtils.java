package com.csjs.coreapp.prove.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.csjs.coreapp.prove.service.ISysDataService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Component
public class ProveTpSystemFinalUtils {
    private static final String systemDate = "日期";
    private static final String systemYear = "年";
    private static final String systemMonth = "月";
    private static final String systemDay = "日";
    private static final String shouldSalaySmall = "应发工资小写";
    private static final String shouldSalayBig = "应发工资大写";
    @Resource
    private ISysDataService sysDataService;

    public String getValue(String name, String userId) {
        String value = " ";
        switch (name) {
            case systemDate:
                value = DateUtil.format(new Date(), "yyyy-MM-dd");
                break;
            case systemYear:
                value = DateUtil.format(new Date(), "yyyy");
                break;
            case systemMonth:
                value = DateUtil.format(new Date(), "MM");
                break;
            case systemDay:
                value = DateUtil.format(new Date(), "dd");
                break;
            case shouldSalaySmall:
                Map<String, Object> shouldSalary = sysDataService.getShouldSalary(userId);
                if (ObjectUtil.isNotEmpty(shouldSalary)) {
                    value = shouldSalary.get("f_1").toString();
                }
                break;
            case shouldSalayBig:
                Map<String, Object> shouldSalary2 = sysDataService.getShouldSalary(userId);
                if (ObjectUtil.isNotEmpty(shouldSalary2)) {
                    value = Number2Money.format(shouldSalary2.get("f_1").toString());
                }
                break;
        }
        return value;
    }
}
