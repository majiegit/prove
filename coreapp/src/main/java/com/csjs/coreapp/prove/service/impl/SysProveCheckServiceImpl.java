package com.csjs.coreapp.prove.service.impl;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csjs.coreapp.prove.entity.SysProveCheck;
import com.csjs.coreapp.prove.mapper.SysProveCheckMapper;
import com.csjs.coreapp.prove.service.ISysProveCheckService;
import com.csjs.coreapp.prove.vo.ProveCheckVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysProveCheckServiceImpl extends ServiceImpl<SysProveCheckMapper, SysProveCheck> implements ISysProveCheckService {


    @Override
    public List<SysProveCheck> listByProveId(Long id) {
        SysProveCheck sysProveCheck = new SysProveCheck();
        sysProveCheck.setProveId(id);
        QueryWrapper<SysProveCheck> sysProveCheckQueryWrapper = new QueryWrapper<>(sysProveCheck);
        List<SysProveCheck> sysProveChecks = baseMapper.selectList(sysProveCheckQueryWrapper);
        return sysProveChecks;
    }

    @Override
    public List<SysProveCheck> listByProveIdTurn(Long id, Integer turn) {
        SysProveCheck sysProveCheck = new SysProveCheck();
        sysProveCheck.setProveId(id);
        sysProveCheck.setTurn(turn);
        QueryWrapper<SysProveCheck> sysProveCheckQueryWrapper = new QueryWrapper<>(sysProveCheck);
        List<SysProveCheck> sysProveChecks = baseMapper.selectList(sysProveCheckQueryWrapper);
        return sysProveChecks;
    }

    @Override
    public  ArrayList<ProveCheckVo> listByProveIdGroupTurn(Long id) {
        ArrayList<ProveCheckVo> objects = new ArrayList<>();
        ArrayList<Integer> turns = baseMapper.proveCheckListGroupTurn(id);
        turns.forEach(turn -> {
            List<SysProveCheck> sysProveChecks = listByProveIdTurn(id, turn);
            ArrayList<String> checkUserIds = new ArrayList<>();
            ArrayList<String> checkUserNames = new ArrayList<>();
            sysProveChecks.forEach(item -> {
                checkUserIds.add(item.getCheckUserId());
                checkUserNames.add(item.getCheckUserName());
            });
            ProveCheckVo proveCheckVo = new ProveCheckVo();
            proveCheckVo.setEditStatus(false);
            proveCheckVo.setTurn(turn);
            proveCheckVo.setCheckMode(sysProveChecks.get(0).getCheckMode());
            proveCheckVo.setCheckUserId(checkUserIds);
            proveCheckVo.setCheckUserName(checkUserNames);
            objects.add(proveCheckVo);
        });

        return objects;
    }

    @Override
    public void saveProveCheck(String checkParam, Long proveId) {
        // 审批参数
        JSONArray checks = JSONUtil.parseArray(checkParam);
        checks.forEach(item -> {
            JSONObject check = JSONUtil.parseObj(item);
            JSONArray checkUserIds = JSONUtil.parseArray(check.get("checkUserId"));
            JSONArray checkUserNames = JSONUtil.parseArray(check.get("checkUserName"));
            Integer turn = Integer.valueOf(check.get("turn").toString());
            String checkMode = check.get("checkMode").toString();
            for (int i = 0; i < checkUserIds.size(); i++) {
                String checkUserId = checkUserIds.get(i).toString();
                String checkUserName = checkUserNames.get(i).toString();
                SysProveCheck sysProveCheck = new SysProveCheck();
                sysProveCheck.setProveId(proveId);
                sysProveCheck.setCheckUserId(checkUserId);
                sysProveCheck.setCheckUserName(checkUserName);
                sysProveCheck.setTurn(turn);
                sysProveCheck.setCheckMode(checkMode);
                sysProveCheck.setCreateTime(new Date());
                baseMapper.insert(sysProveCheck);
            }
        });
    }

    @Override
    public void deleteProveCheck(Long proveId) {
        SysProveCheck sysProveCheck = new SysProveCheck();
        sysProveCheck.setProveId(proveId);
        QueryWrapper<SysProveCheck> sysProveCheckQueryWrapper = new QueryWrapper<>(sysProveCheck);
        baseMapper.delete(sysProveCheckQueryWrapper);
    }

    @Override
    public List<SysProveCheck> listByUserId(String userId) {

        return baseMapper.listByUserId(userId);
    }
}
