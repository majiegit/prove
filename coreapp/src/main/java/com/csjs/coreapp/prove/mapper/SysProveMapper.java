package com.csjs.coreapp.prove.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csjs.coreapp.prove.entity.SysProve;
import com.csjs.coreapp.prove.entity.SysSignet;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysProveMapper extends BaseMapper<SysProve> {

    List<SysProve> listBySignetId(@Param("signetId") String signetId);
}
