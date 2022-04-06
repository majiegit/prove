package com.csjs.coreapp.prove.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csjs.coreapp.prove.entity.SysTpVar;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysTpVarMapper extends BaseMapper<SysTpVar> {

    List<SysTpVar> listByTpId(@Param("tpId") Long tpId);

    int removeByTpId(@Param("tpId") Long tpId);
}
