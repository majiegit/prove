package com.csjs.coreapp.prove.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csjs.coreapp.prove.entity.SysTpVar;
import com.csjs.coreapp.prove.mapper.SysTpVarMapper;
import com.csjs.coreapp.prove.service.ISysTpVarService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysTpVarServiceImpl extends ServiceImpl<SysTpVarMapper, SysTpVar> implements ISysTpVarService {

    @Override
    public List<SysTpVar> listByTpId(Long tpId) {

        return baseMapper.listByTpId(tpId);
    }

    @Override
    public int removeByTpId(Long tpId) {

        return baseMapper.removeByTpId(tpId);
    }
}
