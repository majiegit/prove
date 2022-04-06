package com.csjs.coreapp.prove.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.csjs.coreapp.prove.entity.SysProveSubmitField;
import com.csjs.coreapp.prove.vo.SysProveFieldVo;

import java.util.ArrayList;


public interface ISysProveSubmitFieldService extends IService<SysProveSubmitField> {
    void saveSubmitField(ArrayList<SysProveFieldVo> sysProveFieldVos, Long proveSubmitId);
}
