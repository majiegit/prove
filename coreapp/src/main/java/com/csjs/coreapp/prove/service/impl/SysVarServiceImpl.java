package com.csjs.coreapp.prove.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csjs.coreapp.prove.entity.SysVar;
import com.csjs.coreapp.prove.mapper.SysVarMapper;
import com.csjs.coreapp.prove.service.ISysVarService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysVarServiceImpl extends ServiceImpl<SysVarMapper, SysVar> implements ISysVarService {

    @Override
    public List<SysVar> getParamFieldByTpId(Long tpId) {

        return baseMapper.getParamFieldByTpId(tpId);
    }
}
