package com.csjs.coreapp.prove.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.csjs.coreapp.prove.entity.SysVar;

import java.util.List;

public interface ISysVarService extends IService<SysVar> {


    List<SysVar> getParamFieldByTpId(Long id);
}
