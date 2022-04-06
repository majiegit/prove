package com.csjs.coreapp.prove.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csjs.coreapp.commen.FinalUtils;
import com.csjs.coreapp.prove.dto.ProveDto;
import com.csjs.coreapp.prove.entity.SysProveSubmitCheck;
import com.csjs.coreapp.prove.mapper.SysProveSubmitCheckMapper;
import com.csjs.coreapp.prove.service.ISysProveMesageService;
import com.csjs.coreapp.prove.service.ISysProveSubmitCheckService;
import com.csjs.coreapp.prove.vo.ProveSubmitCheckListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class SysProveSubmitCheckServiceImpl extends ServiceImpl<SysProveSubmitCheckMapper, SysProveSubmitCheck> implements ISysProveSubmitCheckService {

    @Autowired
    private ISysProveMesageService proveMessageService;

    @Override
    public List<ProveSubmitCheckListVo> getSubmitListByUserIdStatus(String userId, String status, String proveId) {
        return baseMapper.getSubmitListByUserIdStatus(userId, status, proveId);
    }

    @Override
    public List<SysProveSubmitCheck> listSubmitListCheck(String userId, Integer status, Long proveSubmitId, Integer turn) {
        return baseMapper.listSubmitListCheck(userId, status, proveSubmitId, turn);
    }

    @Override
    public SysProveSubmitCheck getSubmitListCheckBy(String userId, Integer status, String proveSubmitId) {
        return baseMapper.getSubmitListCheckBy(userId, status, proveSubmitId);
    }

    @Override
    public boolean submitCheckTurnIsEnd(Long proveSubmitId, Integer turn) {
        List<SysProveSubmitCheck> list = baseMapper.listSubmitListByProveSubmitId(proveSubmitId);
        SysProveSubmitCheck sysProveSubmitCheck = list.get(0);
        if (turn == sysProveSubmitCheck.getTurn()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        return baseMapper.updateStatus(id, status);
    }

    @Override
    public int updateStatusByTurn(Long proveSubmitId, Integer turn, Integer status) {
        return baseMapper.updateStatusByTurn(proveSubmitId, turn, status);
    }

    @Override
    public boolean turnStatusIsYes(Long proveSubmitId, Integer turn) {
        List<SysProveSubmitCheck> list = baseMapper.listSubmitListCheck(null, null, proveSubmitId, turn);
        boolean isYes = true;
        for (SysProveSubmitCheck item : list) {
            if (!FinalUtils.PROVE_STATUS_CHEACK_YES.equals(item.getStatus())) {
                isYes = false;
                break;
            }
        }
        return isYes;
    }

    @Override
    public boolean turnStatusIsNo(Long proveSubmitId, Integer turn) {
        List<SysProveSubmitCheck> list = baseMapper.listSubmitListCheck(null, null, proveSubmitId, turn);
        boolean isYes = true;
        for (SysProveSubmitCheck item : list) {
            if (!FinalUtils.PROVE_STATUS_CHEACK_NO.equals(item.getStatus())) {
                isYes = false;
            }
        }
        return isYes;
    }

    @Override
    public void sendMessageCheck(Long proveSubmitId, Integer turn, Integer status) {
        List<SysProveSubmitCheck> list = baseMapper.listSubmitListCheck(null, status, proveSubmitId, turn);
        ProveDto proveDto = baseMapper.getProveDtoByProveSubmitId(proveSubmitId);
        String mesBody = proveDto.getApplyUser() + "提交的" + proveDto.getProveName() + ", 待审批！";
        for (SysProveSubmitCheck sysProveSubmitCheck : list) {
            proveMessageService.sendProveMessage(mesBody, proveSubmitId, sysProveSubmitCheck.getCheckUserId(), "check", true,sysProveSubmitCheck.getId());
        }
    }

    @Override
    public void sendMessageApplyOk(Long proveSubmitId) {
        ProveDto proveDto = baseMapper.getProveDtoByProveSubmitId(proveSubmitId);
        String mesgBody = "恭喜您，" + proveDto.getApplyUser() + "申请的" + proveDto.getProveName() + "审批已完成," + "请注意查收!";
        proveMessageService.sendProveMessage(mesgBody, proveSubmitId, proveDto.getApplyUserId(), "apply", true,null);
    }

    @Override
    public void sendMessageApplyError(Long proveSubmitId) {
        ProveDto proveDto = baseMapper.getProveDtoByProveSubmitId(proveSubmitId);
        String mesgBody = "非常抱歉，" + proveDto.getApplyUser() + "申请的" + proveDto.getProveName() + "审批未通过," + "请联系审批人!";
        proveMessageService.sendProveMessage(mesgBody, proveSubmitId, proveDto.getApplyUserId(), "apply", true,null);
    }

    @Override
    public int updateStatusAndOpinion(Long id, Integer status, String checkOpinion) {
        Date updateTime = new Date();
        return baseMapper.updateStatusAndOpinion(id, status, checkOpinion, updateTime);
    }

    @Override
    public int removeBySubmitId(Long id) {
        SysProveSubmitCheck sysProveSubmitCheck = new SysProveSubmitCheck();
        sysProveSubmitCheck.setProveSubmitId(id);
        QueryWrapper<SysProveSubmitCheck> sysProveSubmitCheckQueryWrapper = new QueryWrapper<>(sysProveSubmitCheck);
        int delete = baseMapper.delete(sysProveSubmitCheckQueryWrapper);
        return delete;
    }
}
