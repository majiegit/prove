package com.csjs.coreapp.prove.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csjs.coreapp.prove.entity.SysProveField;
import com.csjs.coreapp.prove.entity.SysProveSubmitField;
import com.csjs.coreapp.prove.mapper.SysProveSubmitFieldMapper;
import com.csjs.coreapp.prove.service.ISysProveSubmitFieldService;
import com.csjs.coreapp.prove.vo.SysProveFieldVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SysProveSubmitFieldServiceImpl extends ServiceImpl<SysProveSubmitFieldMapper, SysProveSubmitField> implements ISysProveSubmitFieldService {

    @Override
    public void saveSubmitField(ArrayList<SysProveFieldVo> sysProveFieldVos, Long proveSubmitId) {
        sysProveFieldVos.stream().forEach(item -> {
            SysProveSubmitField sysProveSubmitField = new SysProveSubmitField();
            sysProveSubmitField.setFieldId(item.getFieldId());
            sysProveSubmitField.setFieldKey(item.getFieldKey());
            sysProveSubmitField.setFieldName(item.getFieldName());
            sysProveSubmitField.setFieldType(item.getFieldType());
            sysProveSubmitField.setFieldValue(item.getFieldValue());
            sysProveSubmitField.setProveSubmitId(proveSubmitId);
            baseMapper.insert(sysProveSubmitField);
        });
    }
}
