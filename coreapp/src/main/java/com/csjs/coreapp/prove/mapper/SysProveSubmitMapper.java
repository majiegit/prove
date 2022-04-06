package com.csjs.coreapp.prove.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csjs.coreapp.prove.entity.SysProve;
import com.csjs.coreapp.prove.entity.SysProveSubmit;
import com.csjs.coreapp.prove.vo.SysProveSubmitVo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface SysProveSubmitMapper extends BaseMapper<SysProveSubmit> {

    int updateStatusByIds(@Param("ids") ArrayList<Long> ids, @Param("status") String status);

    List<SysProveSubmit> listByProveId(@Param("proveId") String proveId);

    int updateStatusById(@Param("id") Long id, @Param("status") Integer status);

    SysProve getProveByProveSubmitId(@Param("proveSubmitId") Long proveSubmitId);

    List<SysProveSubmitVo> listProveSubmit(@Param("status") Integer status, @Param("proveId") Long proveId, @Param("userId") String userId);
}
