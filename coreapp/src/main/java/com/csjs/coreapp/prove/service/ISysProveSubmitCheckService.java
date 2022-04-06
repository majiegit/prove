package com.csjs.coreapp.prove.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.csjs.coreapp.prove.entity.SysProveSubmitCheck;
import com.csjs.coreapp.prove.vo.ProveSubmitCheckListVo;

import java.util.List;


public interface ISysProveSubmitCheckService extends IService<SysProveSubmitCheck> {

    List<ProveSubmitCheckListVo> getSubmitListByUserIdStatus(String userId, String status, String proveId);

    List<SysProveSubmitCheck> listSubmitListCheck(String userId, Integer status, Long proveSubmitId, Integer turn);

    SysProveSubmitCheck getSubmitListCheckBy(String userId, Integer status, String proveSubmitId);

    boolean submitCheckTurnIsEnd(Long proveSubmitId, Integer turn);

    int updateStatus(Long id, Integer status);

    int updateStatusByTurn(Long proveSubmitId, Integer turn, Integer status);

    boolean turnStatusIsYes(Long proveSubmitId, Integer turn);

    boolean turnStatusIsNo(Long proveSubmitId, Integer turn);

    void sendMessageCheck(Long proveSubmitId, Integer turn , Integer status);

    void sendMessageApplyOk(Long proveSubmitId);

    void sendMessageApplyError(Long proveSubmitId);


    int updateStatusAndOpinion(Long id, Integer status, String checkOpinion);

    int removeBySubmitId(Long id);
}
