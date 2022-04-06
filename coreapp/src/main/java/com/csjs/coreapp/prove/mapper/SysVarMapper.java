package com.csjs.coreapp.prove.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csjs.coreapp.prove.entity.SysVar;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysVarMapper extends BaseMapper<SysVar> {

    List<SysVar> getParamFieldByTpId(@Param("tpId") Long tpId);
}
