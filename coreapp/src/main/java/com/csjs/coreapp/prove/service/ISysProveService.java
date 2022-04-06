package com.csjs.coreapp.prove.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.csjs.coreapp.prove.entity.SysProve;
import com.csjs.coreapp.prove.entity.SysSignet;

import java.util.List;

public interface ISysProveService extends IService<SysProve> {


    List<SysProve> listBySignetId(String signetId);
}
