package com.csjs.coreapp.prove.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csjs.coreapp.prove.entity.SysProve;
import com.csjs.coreapp.prove.entity.SysProveCheck;
import com.google.gson.annotations.SerializedName;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

public interface SysProveCheckMapper extends BaseMapper<SysProveCheck> {
    @Select("SELECT  turn  FROM sys_prove_check  where PROVE_ID = #{id} GROUP BY turn")
    ArrayList<Integer> proveCheckListGroupTurn(@Param("id") Long id);

    @Select("SELECT * from SYS_PROVE_CHECK WHERE CHECK_USER_ID = #{userId}")
    List<SysProveCheck> listByUserId(@Param("userId") String userId);
}
