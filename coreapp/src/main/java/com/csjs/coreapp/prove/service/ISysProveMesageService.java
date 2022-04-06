package com.csjs.coreapp.prove.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.csjs.coreapp.prove.entity.SysProveMessage;


public interface ISysProveMesageService extends IService<SysProveMessage> {

   int sendProveMessage(String content, Long proveSubmitId, String noticeUser, String type, boolean isDingMessage, Long submitCheckId);


    int removeBySubmitId(Long proveSubmitId);
}
