package com.csjs.coreapp.prove.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.csjs.coreapp.commen.exception.BaseException;
import com.csjs.coreapp.prove.service.ISysDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@DS("slave")
public class SysDataServiceImpl implements ISysDataService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map getUserInfo(String sql) {
        return jdbcTemplate.queryForMap(sql);
    }

    @Override
    public List<Map<String, Object>> getSmUserList() {
        String sql = "select CUSERID,USER_CODE,USER_NAME from sm_user sm";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }

    @Override
    public List<Map<String, Object>> getUserList(String checkUserId) {
        String sql = "";
        if (StrUtil.isNotEmpty(checkUserId)) {
            StringBuffer stringBuffer = new StringBuffer();
            String[] split = checkUserId.split(",");
            for (int i = 0; i < split.length; i++) {
                stringBuffer.append("'" + split[i] + "'");
                if (i != split.length - 1) {
                    stringBuffer.append(",");
                }
            }
            String params = stringBuffer.toString();
            sql = "select CUSERID,USER_CODE,USER_NAME from sm_user where CUSERID in " +
                    "(" + params + ")";
        } else {
            return null;
        }
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }

    @Override
    public List<String> getSqlField(String sql) {
        System.out.println(sql);
        List<String> columnNames = new ArrayList<>();
        try {
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
            SqlRowSetMetaData metaData = sqlRowSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnNames;
    }

    @Override
    public String getMobileByUserId(String userId) {
        String sql = "SELECT mobile from bd_psndoc d  " +
                "INNER JOIN sm_user sm ON sm.pk_psndoc  = d.pk_psndoc  AND sm.cuserid = '" + userId + "'";
        Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap(sql);
        return stringObjectMap.get("mobile").toString();
    }

    @Override
    public Map<String, Object> getShouldSalary(String userId) {
        Map<String, Object> stringObjectMap = null;
        try {
            String getPkdoc = "select PK_PSNDOC from sm_user where CUSERID = '" + userId + "'";
            Map<String, Object> pkMap = jdbcTemplate.queryForMap(getPkdoc);
            String pk_psndoc = pkMap.get("pk_psndoc").toString();

            String sql = "SELECT round(sum(t_2.f_1)/count(t_1.qijian),2) f_1,count(t_1.qijian) qijian, t_2.pk_psndoc pk_psndoc FROM (SELECT * FROM wa_data) t_2 INNER JOIN (SELECT wa_period.cyear || wa_period.cperiod qijian, wa_periodstate.accountmark accountmark, wa_waclass.pk_wa_class pk_wa_class, wa_waclass.code code, wa_waclass.name name, wa_period.pk_wa_period pk_wa_period, wa_period.pk_periodscheme pk_periodscheme, wa_period.cyear cyear, wa_period.cperiod cperiod FROM wa_periodstate wa_periodstate INNER JOIN wa_waclass wa_waclass ON wa_periodstate.pk_wa_class = wa_waclass.pk_wa_class INNER JOIN wa_period wa_period ON wa_periodstate.pk_wa_period = wa_period.pk_wa_period) t_1 ON (t_1.cyear = t_2.cyear AND t_2.cperiod = t_1.cperiod AND t_1.pk_wa_class = t_2.pk_wa_class) WHERE t_1.accountmark = 'Y'and t_1.cyear||'-'||t_1.cperiod||'-01' > (SELECT to_char(add_months(to_date(t_2.qijian || '-01','yyyy-mm-dd hh24:mi:ss'),-12),'yyyy-mm-dd') begindate FROM (SELECT max(wa_period.cyear || '-' || wa_period.cperiod) qijian FROM (SELECT * FROM wa_periodstate WHERE accountmark = 'Y') wa_periodstate INNER JOIN wa_waclass wa_waclass ON wa_periodstate.pk_wa_class = wa_waclass.pk_wa_class INNER JOIN wa_period wa_period ON wa_periodstate.pk_wa_period = wa_period.pk_wa_period) t_2) and t_1.cyear||'-'||t_1.cperiod||'-01' <= (SELECT t_2.qijian || '-01' enddate FROM (SELECT max(wa_period.cyear || '-' || wa_period.cperiod) qijian FROM (SELECT * FROM wa_periodstate WHERE accountmark = 'Y') wa_periodstate INNER JOIN wa_waclass wa_waclass ON wa_periodstate.pk_wa_class = wa_waclass.pk_wa_class INNER JOIN wa_period wa_period ON wa_periodstate.pk_wa_period = wa_period.pk_wa_period) t_2) group by t_2.pk_psndoc";
            List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
            Stream<Map<String, Object>> shouldSalaryDtoStream = maps.stream().filter(item -> pk_psndoc.equals(item.get("pk_psndoc").toString()));
            List<Map<String, Object>> collect = shouldSalaryDtoStream.collect(Collectors.toList());
            stringObjectMap = collect.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringObjectMap;

    }
}
