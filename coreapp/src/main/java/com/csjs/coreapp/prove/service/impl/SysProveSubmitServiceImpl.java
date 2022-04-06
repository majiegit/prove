package com.csjs.coreapp.prove.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csjs.coreapp.commen.FinalUtils;
import com.csjs.coreapp.prove.entity.SysProve;
import com.csjs.coreapp.prove.entity.SysProveSubmit;
import com.csjs.coreapp.prove.entity.SysSignet;
import com.csjs.coreapp.prove.mapper.SysProveSubmitMapper;
import com.csjs.coreapp.prove.service.ISysProveSubmitService;
import com.csjs.coreapp.prove.service.ISysSignetService;
import com.csjs.coreapp.prove.utils.proveTask.ProveCheckThread;
import com.csjs.coreapp.prove.vo.SysProveSubmitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysProveSubmitServiceImpl extends ServiceImpl<SysProveSubmitMapper, SysProveSubmit> implements ISysProveSubmitService {
    @Resource
    private ProveCheckThread proveCheckThread;
    @Autowired
    private ISysSignetService sysSignetService;
    @Autowired
    private ISysProveSubmitService sysProveSubmitService;

    @Override
    public int updateStatusByIds(ArrayList<Long> ids, String status) {
        return baseMapper.updateStatusByIds(ids, status);
    }

    @Override
    public int updateStatusById(Long id, Integer status) {
        return baseMapper.updateStatusById(id, status);
    }

    @Override
    public List<SysProveSubmit> listByProveId(String proveId) {
        return baseMapper.listByProveId(proveId);
    }

    @Override
    public void proveSignetHander(Long proveSubmitId) {
        SysProveSubmit sysProveSubmit = baseMapper.selectById(proveSubmitId);
        SysProve sysProve= baseMapper.getProveByProveSubmitId(proveSubmitId);
        String signetMode = sysProve.getSignetMode();
        if(FinalUtils.signetMode_1.equals(signetMode)){
            /**
             * E 签宝盖章方式
             */
            proveCheckThread.proveSubmitCheckBySignetESignetTreasureTask(sysProveSubmit, sysProve, sysProveSubmitService);
        }else if (FinalUtils.signetMode_2.equals(signetMode)){
            SysSignet sysSignet = sysSignetService.getById(sysProve.getSignetId());
            /**
             * 本地签章方式
             */
            proveCheckThread.proveSubmitCheckBySignetLocalhostTask(sysProveSubmit.getProvePath(), sysSignet.getSignetPath(), sysProve.getCoordX().floatValue(), sysProve.getCoordY().floatValue(), sysProveSubmit.getId(), sysProveSubmitService);
        }
    }

    @Override
    public List<SysProveSubmitVo> listProveSubmit(SysProveSubmit sysProveSubmit) {

        return baseMapper.listProveSubmit(sysProveSubmit.getStatus(),sysProveSubmit.getProveId(),sysProveSubmit.getUserId());
    }
}
