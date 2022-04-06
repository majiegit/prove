package com.csjs.coreapp.prove.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.csjs.coreapp.prove.entity.SysProveSubmit;
import com.csjs.coreapp.prove.vo.SysProveSubmitVo;

import java.util.ArrayList;
import java.util.List;

public interface ISysProveSubmitService extends IService<SysProveSubmit> {


    int updateStatusByIds(ArrayList<Long> ids, String status);

    int updateStatusById(Long id, Integer status);

    List<SysProveSubmit> listByProveId(String proveId);

    /**
     * 证明签章处理
     * @param proveSubmitId
     */
    void proveSignetHander(Long proveSubmitId);

    List<SysProveSubmitVo> listProveSubmit(SysProveSubmit sysProveSubmit);
}
