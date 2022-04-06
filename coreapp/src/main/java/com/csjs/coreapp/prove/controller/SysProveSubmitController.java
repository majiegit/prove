package com.csjs.coreapp.prove.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csjs.coreapp.commen.FinalUtils;
import com.csjs.coreapp.commen.R;
import com.csjs.coreapp.commen.exception.BaseException;
import com.csjs.coreapp.prove.dto.ProveCheckDto;
import com.csjs.coreapp.prove.dto.ProveSubmitDto;
import com.csjs.coreapp.prove.entity.*;
import com.csjs.coreapp.prove.service.*;
import com.csjs.coreapp.prove.utils.FileFtpUtils;
import com.csjs.coreapp.prove.utils.proveTask.ProveCheckThread;
import com.csjs.coreapp.prove.utils.proveTask.ProveSubmitThread;
import com.csjs.coreapp.prove.vo.ProveSubmitCheckListVo;
import com.csjs.coreapp.prove.vo.SysProveFieldVo;
import com.csjs.coreapp.prove.vo.SysProveSubmitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/proveSubmit")
@CrossOrigin
public class SysProveSubmitController {
    @Autowired
    private ISysProveSubmitService sysProveSubmitService;
    @Autowired
    private ISysProveSubmitFieldService sysProveSubmitFieldService;
    @Autowired
    private ISysProveFieldService sysProveFieldService;
    @Autowired
    private ISysProveCheckService sysProveCheckService;
    @Autowired
    private ISysProveSubmitCheckService sysProveSubmitCheckService;
    @Autowired
    private ISysProveService sysProveService;
    @Autowired
    private ISysTpService sysTpService;
    @Autowired
    private ISysTpVarService sysTpVarService;
    @Autowired
    private ISysVarService sysVarService;
    @Autowired
    private ISysDataService sysDataService;
    @Autowired
    private ProveSubmitThread proveSubmitThread;
    @Autowired
    private ISysProveMesageService proveMesageService;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private FileFtpUtils fileFtpUtils;
    @Autowired
    private ProveCheckThread proveCheckThread;

    /**
     * 证明提交
     *
     * @param
     * @return
     */
    @PostMapping("/submit")
//    @Transactional
    public R submit(@RequestBody ProveSubmitDto proveSubmitDto) {
        String userId = proveSubmitDto.getUserId();
        String userName = proveSubmitDto.getUserName();
        String remark = proveSubmitDto.getRemark();
        Long proveId = Long.valueOf(proveSubmitDto.getProveId());
        SysProveSubmit sysProveSubmit = new SysProveSubmit();
        sysProveSubmit.setUserId(userId);
        sysProveSubmit.setUserName(userName);
        sysProveSubmit.setProveId(proveId);
        sysProveSubmit.setRemark(remark);
        sysProveSubmit.setStatus(FinalUtils.PROVE_STATUS_CHECKING);
        // 查询证明模板
        SysProve prove = sysProveService.getById(proveId);
        SysTp sysTp = sysTpService.getById(prove.getTpId());
        String fileName = sysTp.getTpName();
        String proveName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
        String proveWordPath = FinalUtils.PROVE_PROVE_PATH + proveName;
        // 查询模板参数
        List<SysTpVar> sysTpVars = sysTpVarService.listByTpId(sysTp.getId());
        List<SysVar> sysVars = sysVarService.getParamFieldByTpId(sysTp.getId());
        ArrayList<HashMap<String, Object>> hashMaps = new ArrayList<>();
        sysTpVars.stream().forEach(sysTpVar -> {
            SysVar sysVarParam = sysVars.stream().filter(sysVar -> sysTpVar.getTpVarName().equals(sysVar.getName())).findAny().orElse(null);
            HashMap<String, Object> map = new HashMap<>();
            if (ObjectUtil.isNotEmpty(sysVarParam)) {
                map.put("name", sysTpVar.getTpVarName());
                map.put("field", sysVarParam.getField());
            } else {
                map.put("name", sysTpVar.getTpVarName());
                map.put("field", null);
            }
            hashMaps.add(map);
        });
        String sql = sysConfigService.getSql(userId);
        Map userInfo = sysDataService.getUserInfo(sql);
      /*  Map userInfo = null;
        try {
            userInfo = sysDataService.getUserInfo(sql);
        } catch (Exception e) {
            throw new BaseException("后台SQL 配置错误，查询不到用户数据， 无法提交");
        }*/
        sysProveSubmit.setCreateTime(new Date());
        // 保存提交记录
        sysProveSubmitService.save(sysProveSubmit);

        // 保存提交字段值替换模板字段
        ArrayList<SysProveFieldVo> sysProveFieldExpands = proveSubmitDto.getSysProveFieldExpands();
        sysProveSubmitFieldService.saveSubmitField(sysProveFieldExpands, sysProveSubmit.getId());
        ArrayList<SysProveField> sysProveFields = sysProveFieldService.listProveFields(sysProveSubmit.getProveId(), FinalUtils.FIELD_TYPE_SYSTEM);
        for (SysProveField sysProveField : sysProveFields) {
            SysProveFieldVo sysProveFieldVo = new SysProveFieldVo();
            sysProveFieldVo.setFieldType(sysProveField.getFieldType());
            sysProveFieldVo.setFieldName(sysProveField.getFieldName());
            sysProveFieldVo.setFieldKey(sysProveField.getFieldKey());
            sysProveFieldVo.setFieldId(sysProveField.getId());
            sysProveFieldExpands.add(sysProveFieldVo);
        }
        // 生成证明文件
        proveSubmitThread.proveCreateTask(proveWordPath, sysTp.getTpPath(), hashMaps, userInfo, sysProveSubmit, sysProveSubmitService, sysProveFieldExpands);
        /**
         * 获取当前证明审批流，并提交审批记录
         */
        List<SysProveCheck> sysProveChecks = sysProveCheckService.listByProveId(proveId);
        for (SysProveCheck sysProveCheck : sysProveChecks) {
            SysProveSubmitCheck sysProveSubmitCheck = new SysProveSubmitCheck();
            sysProveSubmitCheck.setProveId(proveId);
            sysProveSubmitCheck.setProveSubmitId(sysProveSubmit.getId());
            sysProveSubmitCheck.setCheckUserId(sysProveCheck.getCheckUserId());
            sysProveSubmitCheck.setCheckUsername(sysProveCheck.getCheckUserName());
            sysProveSubmitCheck.setTurn(sysProveCheck.getTurn());
            sysProveSubmitCheck.setCheckMode(sysProveCheck.getCheckMode());
            sysProveSubmitCheck.setCreateTime(new Date());
            // 第一次提交  将第一轮审批状态设为审核中 其他轮次为 未审核
            if (sysProveCheck.getTurn().equals(1)) {
                sysProveSubmitCheck.setStatus(FinalUtils.PROVE_STATUS_CHECKING);
                sysProveSubmitCheckService.save(sysProveSubmitCheck);
                // 第一次提交发送消息给审批人 isDingMessage true 表示发送钉钉消息
                String mesBody = userName + "提交的" + prove.getName() + ", 待审批！";
                proveMesageService.sendProveMessage(mesBody, sysProveSubmit.getId(), sysProveSubmitCheck.getCheckUserId(), "check", true, sysProveSubmitCheck.getId());
//                proveMesageService.sendProveMessage(mesBody, sysProveSubmit.getId(), sysProveSubmitCheck.getCheckUserId(), "check", true);
            } else {
                sysProveSubmitCheck.setStatus(FinalUtils.PROVE_STATUS_CHEACK_NO_APPROVED);
                sysProveSubmitCheckService.save(sysProveSubmitCheck);
            }
        }
        return R.SUCCESS;
    }

    /**
     * 证明提交列表
     *
     * @return
     */
    @PostMapping("/list")
    public R list(@RequestBody ProveSubmitDto proveSubmitDto) {
        String status = proveSubmitDto.getStatus();
        String userId = proveSubmitDto.getUserId();
        String proveId = proveSubmitDto.getProveId();
        SysProveSubmit sysProveSubmit = new SysProveSubmit();
        if (ObjectUtil.isNotEmpty(status)) {
            sysProveSubmit.setStatus(Integer.valueOf(status));
        }
        if (ObjectUtil.isNotEmpty(proveId)) {
            sysProveSubmit.setProveId(Long.valueOf(proveId));
        }
        if (ObjectUtil.isNotEmpty(userId)) {
            sysProveSubmit.setUserId(userId);
        }
        List<SysProveSubmitVo> list = sysProveSubmitService.listProveSubmit(sysProveSubmit);
        return R.SUCCESS.data(list);
    }

    /**
     * 证明对应审核人查询
     *
     * @return
     */
    @GetMapping("/checkUserList")
    public R getCheckUser(@RequestParam("proveSubmitId") String proveSubmitId) {
        SysProveSubmit byId = sysProveSubmitService.getById(proveSubmitId);
        List<SysProveSubmitCheck> list = sysProveSubmitCheckService.listSubmitListCheck(null, null, Long.valueOf(proveSubmitId), null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("submitUser", byId.getUserName());
        map.put("createTime", DateUtil.format(byId.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        map.put("checkData", list);
        return R.SELECT_SUCCESS.data(map);
    }

    /**
     * 根据证明ID 查询
     *
     * @return
     */
    @GetMapping("/byId")
    public R byId(@RequestParam("id") String id) {
        // 根据条件查询
        SysProveSubmit proveSubmit = sysProveSubmitService.getById(id);
        SysProveSubmitVo sysProveSubmitVo = BeanUtil.toBean(proveSubmit, SysProveSubmitVo.class);
        SysProve prove = sysProveService.getById(proveSubmit.getProveId());
        sysProveSubmitVo.setProveName(prove.getName());
        return R.SELECT_SUCCESS.data(sysProveSubmitVo);
    }

    /**
     * 根据证明ID, 审批人ID 查询当前单据是否已审核过
     *
     * @return
     */
    @GetMapping("/byCheckId")
    public R byId(@RequestParam("id") String id, @RequestParam("userId") String userId) {
        ProveSubmitCheckListVo proveSubmitCheckListVo1 = new ProveSubmitCheckListVo();

        List<ProveSubmitCheckListVo> list = sysProveSubmitCheckService.getSubmitListByUserIdStatus(userId,null,null);
        for (ProveSubmitCheckListVo proveSubmitCheckListVo : list) {
            if(id.equals(proveSubmitCheckListVo.getCheckId().toString())) {
                proveSubmitCheckListVo1 = proveSubmitCheckListVo;
                break;
            }
        }
        return R.SELECT_SUCCESS.data(proveSubmitCheckListVo1);
    }

    /**
     * 证明提交删除
     *
     * @return
     */
    @PostMapping("/delete")
    @Transactional
    public R delete(@RequestBody HashMap<String, Object> idMap) {
        String id = idMap.get("id").toString();
        Long proveSubmitId = Long.valueOf(id);
        // 判断当前单据是否已有审核人审核过
        List<SysProveSubmitCheck> list = sysProveSubmitCheckService.listSubmitListCheck(null, null, proveSubmitId, null);
        boolean isCheck = false;
        for (SysProveSubmitCheck sysProveSubmitCheck : list) {

            if (FinalUtils.PROVE_STATUS_CHEACK_YES.equals(sysProveSubmitCheck.getStatus())
                    || FinalUtils.PROVE_STATUS_CHEACK_NO.equals(sysProveSubmitCheck.getStatus())) {
                isCheck = true;
                break;
            }
        }
        if (!isCheck) {
            sysProveSubmitService.removeById(proveSubmitId);
            sysProveSubmitCheckService.removeBySubmitId(proveSubmitId);
            proveMesageService.removeBySubmitId(proveSubmitId);
            return R.DELETE_SUCCESS;
        } else {
            return R.data(506, "当前证明已被审核，无法收回");
        }
    }

    /**
     * 证明审核列表
     *
     * @return
     */
    @PostMapping("/checkList")
    public R checkList(@RequestBody ProveSubmitDto proveSubmitDto) {
        String status = proveSubmitDto.getStatus();
        String userId = proveSubmitDto.getUserId();
        String proveId = proveSubmitDto.getProveId();
        // 是否为审批人
        if (ObjectUtil.isNotEmpty(userId)) {
            List<SysProveCheck> list = sysProveCheckService.listByUserId(userId);
            if (list.size() == 0) {
                ArrayList<SysProveSubmit> sysProveSubmits = new ArrayList<>();
                return R.data(200, "你不是证明审核人", sysProveSubmits);
            }
        }
        // 根据条件查询
        List<ProveSubmitCheckListVo> list = sysProveSubmitCheckService.getSubmitListByUserIdStatus(userId, status, proveId);
        return R.SUCCESS.data(list);
    }

    /**
     * 证明审核
     *
     * @return
     */
    @PostMapping("/check")
    @Transactional
    public R check(@RequestBody ProveCheckDto proveCheckDto) {
        ArrayList<String> proveSubmitIds = proveCheckDto.getIds();
        String status = proveCheckDto.getStatus();
        String checkUserId = proveCheckDto.getCheckUserId();
        String checkOpinion = proveCheckDto.getCheckOpinion();
        // 批量处理
        for (String proveSubmitId1 : proveSubmitIds) {
            // 查询当前单据审批人
            SysProveSubmitCheck submitListCheck = sysProveSubmitCheckService.getSubmitListCheckBy(checkUserId, FinalUtils.PROVE_STATUS_CHECKING, proveSubmitId1);
            // 判断是否最后一轮
            boolean isTurn = sysProveSubmitCheckService.submitCheckTurnIsEnd(submitListCheck.getProveSubmitId(), submitListCheck.getTurn());
            Long proveSubmitId = submitListCheck.getProveSubmitId();
            Integer turn = submitListCheck.getTurn();
            String checkMode = submitListCheck.getCheckMode();
            Long id = submitListCheck.getId();
            if (!isTurn) {
                // 不是最后一轮
                // 判断审批方式
                if (FinalUtils.check_Mode_1.equals(checkMode)) {
                    // 会签
                    // 更新当前审核状态和意见
                    sysProveSubmitCheckService.updateStatusAndOpinion(id, Integer.valueOf(status), checkOpinion);
                    // 判断当前轮次是否为全部审核通过
                    boolean isYes = sysProveSubmitCheckService.turnStatusIsYes(proveSubmitId, turn);
                    if (isYes) {
                        // 更新下一轮状态
                        sysProveSubmitCheckService.updateStatusByTurn(proveSubmitId, turn + 1, FinalUtils.PROVE_STATUS_CHECKING);
                        // 发送消息给下一轮审核人
                        sysProveSubmitCheckService.sendMessageCheck(proveSubmitId, turn + 1, FinalUtils.PROVE_STATUS_CHECKING);
                    } else {
                        // 更新单据状态为未通过
                        sysProveSubmitService.updateStatusById(proveSubmitId, FinalUtils.PROVE_STATUS_CHEACK_NO);

                        // 发送审批不通过消息
                        proveCheckThread.sendMessageCheckNo(proveSubmitId);
                    }
                } else if (FinalUtils.check_Mode_2.equals(checkMode)) {
                    // 或签
                    // 更新当前轮次状态
                    sysProveSubmitCheckService.updateStatusByTurn(proveSubmitId, turn, Integer.valueOf(status));
                    sysProveSubmitCheckService.updateStatusAndOpinion(id, Integer.valueOf(status), checkOpinion);
                    // 通过 则 更新下一轮状态 待审核人
                    if (FinalUtils.PROVE_STATUS_CHEACK_YES.equals(status)) {
                        sysProveSubmitCheckService.updateStatusByTurn(proveSubmitId, turn + 1, FinalUtils.PROVE_STATUS_CHECKING);
                        // 发送消息给下一轮审核人
                        sysProveSubmitCheckService.sendMessageCheck(proveSubmitId, turn + 1, FinalUtils.PROVE_STATUS_CHECKING);
                    }
                }
            } else {
                //  最后一轮
                // 判断审批方式
                if (FinalUtils.check_Mode_1.equals(checkMode)) {
                    // 会签
                    //  更新当前审核状态和意见
                    sysProveSubmitCheckService.updateStatusAndOpinion(id, Integer.valueOf(status), checkOpinion);
                    // 判断当前轮次是否为全部审核通过
                    boolean isYes = sysProveSubmitCheckService.turnStatusIsYes(proveSubmitId, turn);
                    if (isYes) {
                        /**
                         * 执行签章任务、更新单据审核状态、发送审批通过消息
                         */
                        sysProveSubmitService.proveSignetHander(proveSubmitId);
                    } else {
                        // 更新单据状态为未通过
                        sysProveSubmitService.updateStatusById(proveSubmitId, FinalUtils.PROVE_STATUS_CHEACK_NO);

                        // 发送审批不通过消息
                        proveCheckThread.sendMessageCheckNo(proveSubmitId);
                    }
                } else if (FinalUtils.check_Mode_2.equals(checkMode)) {
                    // 或签
                    // 更新当前轮次状态
                    sysProveSubmitCheckService.updateStatusByTurn(proveSubmitId, turn, Integer.valueOf(status));
                    sysProveSubmitCheckService.updateStatusAndOpinion(id, Integer.valueOf(status), checkOpinion);
                    if (FinalUtils.PROVE_STATUS_CHEACK_YES.equals(Integer.valueOf(status))) {
                        /**
                         * 通过 则执行签章任务 并更新单据审核状态、发送审批消息
                         */
                        sysProveSubmitService.proveSignetHander(proveSubmitId);

                    } else if (FinalUtils.PROVE_STATUS_CHEACK_NO.equals(Integer.valueOf(status))) {
                        /**
                         * 不通过 如果最后一轮 都是不通过， 则更新单据不通过状态
                         */
                        boolean isNo = sysProveSubmitCheckService.turnStatusIsNo(proveSubmitId, turn);
                        if (isNo) {
                            // 更新单据状态为未通过
                            sysProveSubmitService.updateStatusById(proveSubmitId, FinalUtils.PROVE_STATUS_CHEACK_NO);

                            // 发送审批不通过消息
                            proveCheckThread.sendMessageCheckNo(proveSubmitId);
                        }
                    }
                }
            }
        }
        return R.CHECK_SUCCESS;
    }

    /**
     * 证明下载
     */
    @GetMapping("/downloadProve")
    public R downloadProve(@RequestParam("proveSubmitId") String proveSubmitId) {
        SysProveSubmit sysProveSubmit = sysProveSubmitService.getById(proveSubmitId);
        String provePath = sysProveSubmit.getProvePath();
        File file = new File(provePath);
        if (!file.exists()) {
            fileFtpUtils.download(provePath);
        }
        return R.DOWNLOAD_SUCCESS;
    }

    /**
     * 证明审核
     *
     * @return
     */
//    @PostMapping("/check")
//    public R check(@RequestBody ProveCheckDto proveCheckDto) {
//        ArrayList<Long> ids = proveCheckDto.getIds();
//        String status = proveCheckDto.getStatus();
//        String checkUserName = proveCheckDto.getCheckUserName();
//
//        // 1、查询当前审批人，本轮审批方式与其他审批人，如果  审批方式为
//        //                          会签：本轮当前审核人审批状态为通过或不通过（1或2），
//        //                          或签：本轮所有审核人审批状态均为通过或不通过（1或2）
//        // 2、审批通过发送消息与钉钉消息
//        // 1、判断当前审批轮次是否为最后一轮：
//        //        不是最后一轮：如果当前轮次的审批方式为
//        //                          会签：本轮审批状态均为通过状态（1），则更新下一轮开始审批状态 0，
//        //                          或签：本轮所有审核人审批状态均为通过（1），则更新下一轮开始审批状态 0 ，
//        //        最后一轮：
//        //                          会签：更新当前审批人审核状态通过或者不通过（1）或（2）
//        //                          或签：更新当前轮次所有审核状态通过或者不通过（1）或（2）
//        //                          判断当前审批人是否为最后一个审批人并且所有审批状态均为通过？
//        //                                         是：执行盖章步骤  否：不执行
//        //
//        //
//        // 2、审批通过不通过发送消息与钉钉消息
//        // 审核通过
//        if (status.equals("1")) {
//            ids.stream().forEach(id -> {
//                // 查询提交的证明
//                SysProveSubmit sysProveSubmit = sysProveSubmitService.getById(id);
//                // 查询证明模板
//                SysProve sysProve = sysProveService.getById(sysProveSubmit.getProveId());
//                // 查询签章
//                SysSignet sysSignet = sysSignetService.getById(sysProve.getSignetId());
//                sysProveSubmit.setStatus(Integer.valueOf(status));
//                sysProveSubmitService.updateById(sysProveSubmit);
//                if (ObjectUtil.isNotEmpty(sysSignet)) {
//                    proveThreadCreate.taskcheck(sysProveSubmit.getProvePath(), sysSignet.getSignetPath(), sysProve.getCoordX().floatValue(), sysProve.getCoordY().floatValue(), sysProveSubmit, sysProveSubmitService);
//                }
//                String mesgBody = "恭喜您，" + checkUserName + "批准了" + sysProveSubmit.getUserName() + "的" + sysProve.getName() + "申请,请前往查收!";
//                proveMesageService.addProveMessage(mesgBody, sysProveSubmit.getId(), sysProveSubmit.getUserId(), "apply");
//            });
//        } else {
//            // 审核不通过
//            sysProveSubmitService.updateStatusByIds(ids, status);
//            ids.stream().forEach(id -> {
//                // 查询提交的证明
//                SysProveSubmit sysProveSubmit = sysProveSubmitService.getById(id);
//                // 查询证明模板
//                SysProve sysProve = sysProveService.getById(sysProveSubmit.getProveId());
//                String mesgBody = "非常抱歉，" + checkUserName + "未批准" + sysProveSubmit.getUserName() + "的" + sysProve.getName() + "申请，请核实原因!";
//                proveMesageService.addProveMessage(mesgBody, sysProveSubmit.getId(), sysProveSubmit.getUserId(), "apply");
//            });
//        }
//        return R.CHECK_SUCCESS;
//    }
}
