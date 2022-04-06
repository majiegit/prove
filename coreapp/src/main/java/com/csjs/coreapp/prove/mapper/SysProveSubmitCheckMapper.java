package com.csjs.coreapp.prove.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csjs.coreapp.prove.dto.ProveDto;
import com.csjs.coreapp.prove.entity.SysProveSubmitCheck;
import com.csjs.coreapp.prove.vo.ProveSubmitCheckListVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

public interface SysProveSubmitCheckMapper extends BaseMapper<SysProveSubmitCheck> {
    List<ProveSubmitCheckListVo> getSubmitListByUserIdStatus(@Param("userId") String userId, @Param("status") String status, @Param("proveId") String proveId);

    List<SysProveSubmitCheck> listSubmitListCheck(@Param("userId") String userId, @Param("status") Integer status, @Param("proveSubmitId") Long proveSubmitId, @Param("turn") Integer turn);

    SysProveSubmitCheck getSubmitListCheckBy(@Param("userId") String userId, @Param("status") Integer status, @Param("proveSubmitId") String proveSubmitId);

    List<SysProveSubmitCheck> listSubmitListByProveSubmitId(@Param("proveSubmitId") Long proveSubmitId);

    @Update("update SYS_PROVE_SUBMIT_CHECK SET STATUS = #{status} WHERE ID = #{id} ")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("update SYS_PROVE_SUBMIT_CHECK SET STATUS = #{status} WHERE PROVE_SUBMIT_ID = #{proveSubmitId} AND TURN = #{turn} ")
    int updateStatusByTurn(@Param("proveSubmitId") Long proveSubmitId, @Param("turn") Integer turn, @Param("status") Integer status);

    ProveDto getProveDtoByProveSubmitId(@Param("proveSubmitId") Long proveSubmitId);


    @Update("update SYS_PROVE_SUBMIT_CHECK SET STATUS = #{status},CHECK_OPINION = #{checkOpinion},UPDATE_TIME = #{updateTime} WHERE ID = #{id} ")
    int updateStatusAndOpinion(@Param("id") Long id, @Param("status") Integer status, @Param("checkOpinion") String checkOpinion, @Param("updateTime") Date updateTime);

}
