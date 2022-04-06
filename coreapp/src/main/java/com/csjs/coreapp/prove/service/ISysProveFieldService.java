package com.csjs.coreapp.prove.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.csjs.coreapp.prove.entity.SysProveField;

import java.util.ArrayList;
import java.util.List;

public interface ISysProveFieldService extends IService<SysProveField> {


    ArrayList<SysProveField> listProveFields(Long proveId, String type);
    ArrayList<SysProveField> listProveFields(Long proveId);

    void saveField(List<String> guDing, Long proveId, String type);
}
