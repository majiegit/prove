package com.csjs.coreapp.prove.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csjs.coreapp.commen.FinalUtils;
import com.csjs.coreapp.prove.entity.SysConfig;
import com.csjs.coreapp.prove.entity.SysProveMessage;
import com.csjs.coreapp.prove.mapper.SysProveMessageMapper;
import com.csjs.coreapp.prove.service.ISysConfigService;
import com.csjs.coreapp.prove.service.ISysProveMesageService;
import com.csjs.coreapp.prove.utils.DingApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SysProveMessageServiceImpl extends ServiceImpl<SysProveMessageMapper, SysProveMessage> implements ISysProveMesageService {
    @Autowired
    private DingApiUtils dingApiUtils;
    @Autowired
    private ISysConfigService sysConfigService;

    @Override
    public int sendProveMessage(String content, Long proveSubmitId, String noticeUser, String type, boolean isDingMessage, Long submitCheckId) {
        SysProveMessage sysProveMessage = new SysProveMessage();
        sysProveMessage.setContent(content);
        sysProveMessage.setNoticeUser(noticeUser);
        sysProveMessage.setCreateTime(new Date());
        sysProveMessage.setProveSubmitId(proveSubmitId);
        sysProveMessage.setIsRead("N");
        sysProveMessage.setMessageType(type);
        int insert = baseMapper.insert(sysProveMessage);
        if (isDingMessage) {
            String description = content;
            String url = null;
            if (type.equals(FinalUtils.messageTypeCheck)) {
                // 审批消息
                SysConfig proveCheckUrl = sysConfigService.getValueByName(FinalUtils.proveCheckUrl);
                description = "审批地址:" + proveCheckUrl.getValueName() + "&id=" + submitCheckId;
                url = proveCheckUrl.getValueName() + "&id=" + submitCheckId;
                dingApiUtils.sentDingDingCheckTasks(noticeUser, content, description, url);
            }else if(type.equals(FinalUtils.messageTypeApply)){
                // 通知消息
                SysConfig proveApplyUrl = sysConfigService.getValueByName(FinalUtils.proveApplyUrl);
                url =  proveApplyUrl.getValueName() + "&id=" + proveSubmitId;
                dingApiUtils.sentDingDingApplyTasks(noticeUser, content, description, url);
            }

        }
        return insert;
    }

    @Override
    public int removeBySubmitId(Long proveSubmitId) {
        SysProveMessage sysProveMessage = new SysProveMessage();
        sysProveMessage.setProveSubmitId(proveSubmitId);
        QueryWrapper<SysProveMessage> sysProveMessageQueryWrapper = new QueryWrapper<>(sysProveMessage);
        return baseMapper.delete(sysProveMessageQueryWrapper);
    }
}
