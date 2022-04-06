package com.csjs.coreapp.prove.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csjs.coreapp.prove.entity.SysProve;
import com.csjs.coreapp.prove.entity.SysSignet;
import com.csjs.coreapp.prove.mapper.SysProveMapper;
import com.csjs.coreapp.prove.service.ISysProveService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysProveServiceImpl extends ServiceImpl<SysProveMapper, SysProve> implements ISysProveService {

    @Override
    public List<SysProve> listBySignetId(String signetId) {

        return baseMapper.listBySignetId(signetId);
    }
}
