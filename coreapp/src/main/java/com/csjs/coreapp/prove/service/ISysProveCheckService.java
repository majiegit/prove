package com.csjs.coreapp.prove.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.csjs.coreapp.prove.entity.SysProveCheck;
import com.csjs.coreapp.prove.vo.ProveCheckVo;

import java.util.ArrayList;
import java.util.List;


public interface ISysProveCheckService extends IService<SysProveCheck> {

    List<SysProveCheck> listByProveId(Long id);
    List<SysProveCheck> listByProveIdTurn(Long id, Integer turn);
    ArrayList<ProveCheckVo> listByProveIdGroupTurn(Long id);
    /**
     * 保存审批参数信息
     */
    void saveProveCheck(String checkParam , Long proveId);


    /**
     * 保存审批参数信息
     */
    void deleteProveCheck(Long proveId);

    /**
     * 根据userId 查询
     */
    List<SysProveCheck> listByUserId(String userId);
}
