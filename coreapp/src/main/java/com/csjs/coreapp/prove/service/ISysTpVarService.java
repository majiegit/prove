package com.csjs.coreapp.prove.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.csjs.coreapp.prove.entity.SysTpVar;

import java.util.List;

public interface ISysTpVarService extends IService<SysTpVar> {


    List<SysTpVar> listByTpId(Long tpId);

    int removeByTpId(Long tpId);
}
