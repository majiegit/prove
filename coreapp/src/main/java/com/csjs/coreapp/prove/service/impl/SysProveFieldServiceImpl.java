package com.csjs.coreapp.prove.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csjs.coreapp.commen.FinalUtils;
import com.csjs.coreapp.prove.entity.SysProveField;
import com.csjs.coreapp.prove.mapper.SysProveFieldMapper;
import com.csjs.coreapp.prove.service.ISysProveFieldService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysProveFieldServiceImpl extends ServiceImpl<SysProveFieldMapper, SysProveField> implements ISysProveFieldService {

    @Override
    public ArrayList<SysProveField> listProveFields(Long proveId, String type) {
        return baseMapper.listProveFieldsByType(proveId, type);
    }

    @Override
    public ArrayList<SysProveField> listProveFields(Long proveId) {
        return baseMapper.listProveFields(proveId);
    }

    @Override
    public void saveField(List<String> guDing, Long proveId, String type) {
        for (int i = 0; i < guDing.size(); i++) {
            SysProveField sysProveField = new SysProveField();
            sysProveField.setFieldName(guDing.get(i));
            sysProveField.setFieldKey(sysProveField.getId() + "_" + type);
            sysProveField.setProveId(proveId);
            sysProveField.setFieldType(type);
            baseMapper.insert(sysProveField);
        }
    }
}
