package com.csjs.coreapp.prove.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csjs.coreapp.prove.entity.SysConfig;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

public interface SysConfigMapper extends BaseMapper<SysConfig> {

    @Select("select * from sys_config where key_name = #{key}")
    SysConfig getValueByName(@Param("key") String key);

    @Update("UPDATE SYS_CONFIG SET VALUE_NAME = #{valueName},remark = #{remark},UPDATE_DATE = #{updateDate} WHERE KEY_NAME = #{keyName}")
    int updateByKeyName(@Param("keyName") String keyName, @Param("valueName") String valueName,
                        @Param("remark") String remark, @Param("updateDate") Date updateDate);

    @Delete("delete FROM SYS_CONFIG WHERE KEY_NAME = #{keyName}")
    boolean deleteByKeyName(@Param("keyName") String keyName);
}
