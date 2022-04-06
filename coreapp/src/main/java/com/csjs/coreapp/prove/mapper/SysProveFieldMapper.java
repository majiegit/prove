package com.csjs.coreapp.prove.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csjs.coreapp.prove.entity.SysProveField;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

public interface SysProveFieldMapper extends BaseMapper<SysProveField> {

    @Select("select * from sys_prove_field where prove_id = #{proveId}")
    ArrayList<SysProveField> listProveFields(Long proveId);
    ArrayList<SysProveField> listProveFieldsByType(@Param("proveId") Long proveId, @Param("type") String type);
}
