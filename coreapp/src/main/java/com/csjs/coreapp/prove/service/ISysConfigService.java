package com.csjs.coreapp.prove.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.csjs.coreapp.prove.entity.SysConfig;

public interface ISysConfigService extends IService<SysConfig> {

    SysConfig getValueByName(String key);

    int updateByKeyName(String keyName, SysConfig sysConfig);

    boolean deleteByKeyName(String keyName);

    String getSql(String userId);
}
