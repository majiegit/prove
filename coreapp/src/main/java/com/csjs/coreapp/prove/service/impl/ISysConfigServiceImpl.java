package com.csjs.coreapp.prove.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csjs.coreapp.commen.FinalUtils;
import com.csjs.coreapp.prove.entity.SysConfig;
import com.csjs.coreapp.prove.mapper.SysConfigMapper;
import com.csjs.coreapp.prove.service.ISysConfigService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ISysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    @Override
    public SysConfig getValueByName(String key) {
        SysConfig sysDingConfig = baseMapper.getValueByName(key);
        return sysDingConfig;
    }

    @Override
    public int updateByKeyName(String keyName, SysConfig sysConfig) {
        String valueName = sysConfig.getValueName();
        String remark = sysConfig.getRemark();
        Date updateDate = sysConfig.getUpdateDate();
        return  baseMapper.updateByKeyName(keyName,valueName,remark, updateDate);
    }

    @Override
    public boolean deleteByKeyName(String keyName) {
        return baseMapper.deleteByKeyName(keyName);
    }

    @Override
    public String getSql(String userId) {
        String sqlContent = baseMapper.getValueByName(FinalUtils.proveTpSql).getValueName();
        String fieldName = baseMapper.getValueByName(FinalUtils.proveTpSqlReplace).getValueName();
        String sql = sqlContent.replace(fieldName, userId);
        return sql;
    }
}
