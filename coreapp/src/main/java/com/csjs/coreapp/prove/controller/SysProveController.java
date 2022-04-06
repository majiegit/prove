package com.csjs.coreapp.prove.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csjs.coreapp.commen.FinalUtils;
import com.csjs.coreapp.commen.R;
import com.csjs.coreapp.prove.entity.*;
import com.csjs.coreapp.prove.service.*;
import com.csjs.coreapp.prove.utils.DocmentOperationUtils;
import com.csjs.coreapp.prove.utils.FileFtpUtils;
import com.csjs.coreapp.prove.utils.proveTask.ProveAddThread;
import com.csjs.coreapp.prove.vo.SignetParamVo;
import com.csjs.coreapp.prove.vo.SysProveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/sysProve")
@CrossOrigin
public class SysProveController {
    @Autowired
    private ISysVarService sysVarService;
    @Autowired
    private ISysTpService sysTpService;
    @Autowired
    private ISysProveService sysProveService;
    @Autowired
    private ISysProveCheckService sysProveCheckService;
    @Autowired
    private ISysTpVarService sysTpVarService;
    @Autowired
    private ISysDataService sysDataService;
    @Autowired
    private ISysProveSubmitService sysProveSubmitService;
    @Autowired
    private ISysSignetService sysSignetService;
    @Autowired
    private ProveAddThread proveAddThread;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysProveFieldService sysProveFieldService;
    @Autowired
    private FileFtpUtils fileFtpUtils;
    @Autowired
    private DocmentOperationUtils docmentOperationUtils;

    /**
     * 证明新增
     *
     * @param name
     * @param file
     * @return
     */
    @PostMapping("/add")
    @Transactional
    public R add(@RequestParam("name") String name,
                 MultipartFile file,
                 @RequestParam("checkParam") String checkParam,
                 @RequestParam("signetParam") String signetParam) {
        // 上传 保存证明
        String fileName = file.getOriginalFilename();
        String tpName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
        String tpPath = fileFtpUtils.upload(FinalUtils.PROVE_TP_PATH, tpName, file);
        // 保存模板
        SysTp sysTp = new SysTp();
        sysTp.setTpName(tpName);
        sysTp.setTpPath(tpPath);
        sysTp.setCreateTime(new Date());
        sysTpService.save(sysTp);

        // 签章参数
        JSONObject signet = JSONUtil.parseObj(signetParam);
        String signetMode = signet.get("signetMode").toString();
        Integer coordX = Integer.valueOf(signet.get("coordX").toString());
        Integer coordY = Integer.valueOf(signet.get("coordY").toString());

        // 保存证明
        SysProve sysProve = new SysProve();
        sysProve.setName(name);
        sysProve.setTpId(sysTp.getId());
        sysProve.setSignetMode(signetMode);
        sysProve.setCreateTime(new Date());
        // 签章方式为本地 才有签章Id
        if (FinalUtils.signetMode_2.equals(signetMode)) {
            String signetId = signet.get("signetId").toString();
            sysProve.setSignetId(Long.valueOf(signetId));
        } else if (FinalUtils.signetMode_1.equals(signetMode)) {
            sysProve.setSignetId(null);
        }
        sysProve.setCoordX(coordX);
        sysProve.setCoordY(coordY);
        sysProveService.save(sysProve);

        // 审批参数保存
        sysProveCheckService.saveProveCheck(checkParam, sysProve.getId());

        // 解析SQL ${} 变量
        String regex = "\\$\\{[^}]+\\}";
        List<String> params = docmentOperationUtils.readWordParams(tpPath, regex);
        params.stream().forEach(item -> {
            SysTpVar sysTpVar = new SysTpVar();
            sysTpVar.setTpId(sysTp.getId());
            sysTpVar.setTpVarName(item);
            sysTpVar.setCreateTime(new Date());
            sysTpVarService.save(sysTpVar);
        });

        // 解析系统变量 @{}
        String regex2 = "\\@\\{[^}]+\\}";
        List<String> systems = docmentOperationUtils.readWordParams(tpPath,regex2);
        sysProveFieldService.saveField(systems, sysProve.getId(),FinalUtils.FIELD_TYPE_SYSTEM);

        // 解析模板单据变量 &{}
        String rege3 = "\\&\\{[^}]+\\}";
        List<String> temps = docmentOperationUtils.readWordParams(tpPath,rege3);
        sysProveFieldService.saveField(temps, sysProve.getId(),FinalUtils.FIELD_TYPE_TEMP);

        // 文档转图片预览
        proveAddThread.proveTpToImageTask(tpPath, sysTp, sysTpService);
        return R.SAVE_SUCCESS;
    }

    /**
     * 证明修改
     *
     * @param id
     * @param name
     * @param file
     * @return
     */

    @PostMapping("/update")
    @Transactional
    public R update(@RequestParam("id") String id, MultipartFile file, @RequestParam("name") String name,
                    @RequestParam("checkParam") String checkParam,
                    @RequestParam("signetParam") String signetParam) {
        Long proveId = Long.valueOf(id);
        // 无模板文件修改  只修改证明表
        if (file != null) {
            String fileName = file.getOriginalFilename();
            String tpName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
            String tpPath = fileFtpUtils.upload(FinalUtils.PROVE_TP_PATH, tpName, file);
            String base64Src = docmentOperationUtils.wordToImage(tpPath);
            Long tpId = sysProveService.getById(Long.valueOf(id)).getTpId();
            String tpPathBeafo = sysTpService.getById(tpId).getTpPath();
            // 更新模板
            SysTp sysTp = new SysTp();
            sysTp.setId(tpId);
            sysTp.setTpName(tpName);
            sysTp.setTpPath(tpPath);
            sysTp.setTpLook(base64Src);
            sysTp.setUpdateTime(new Date());
            sysTpService.updateById(sysTp);
            // 解析SQL ${} 变量
            String regex = "\\$\\{[^}]+\\}";
            List<String> params = docmentOperationUtils.readWordParams(tpPath,regex);
            // 删除原模板变量
            sysTpVarService.removeByTpId(tpId);
            // 保存模板变量 #{}
            params.stream().forEach(item -> {
                SysTpVar sysTpVar = new SysTpVar();
                sysTpVar.setTpId(sysTp.getId());
                sysTpVar.setTpVarName(item);
                sysTpVar.setCreateTime(new Date());
                sysTpVarService.save(sysTpVar);
            });
            Long sysProveId = Long.valueOf(id);

            // 删除原来变量
            QueryWrapper<SysProveField> sysProveFieldQueryWrapper = new QueryWrapper<>();
            sysProveFieldService.remove(sysProveFieldQueryWrapper.eq("prove_id",sysProveId).eq("field_type",FinalUtils.FIELD_TYPE_SYSTEM));
            // 解析系统变量 @{}
            String regex2 = "\\@\\{[^}]+\\}";
            List<String> systems = docmentOperationUtils.readWordParams(tpPath,regex2);
            sysProveFieldService.saveField(systems, sysProveId,FinalUtils.FIELD_TYPE_SYSTEM);

            QueryWrapper<SysProveField> sysProveFieldQueryWrapper2 = new QueryWrapper<>();
            sysProveFieldService.remove(sysProveFieldQueryWrapper2.eq("prove_id",sysProveId).eq("field_type",FinalUtils.FIELD_TYPE_TEMP));
            // 解析模板单据变量 &{}
            String rege3 = "\\&\\{[^}]+\\}";
            List<String> temps = docmentOperationUtils.readWordParams(tpPath,rege3);
            sysProveFieldService.saveField(temps, sysProveId,FinalUtils.FIELD_TYPE_TEMP);
        }
        // 修改证明
        // 签章参数
        JSONObject signet = JSONUtil.parseObj(signetParam);
        String signetMode = signet.get("signetMode").toString();
        Integer coordX = Integer.valueOf(signet.get("coordX").toString());
        Integer coordY = Integer.valueOf(signet.get("coordY").toString());

        // 保存证明
        SysProve sysProve = new SysProve();
        sysProve.setId(proveId);
        sysProve.setName(name);
        sysProve.setSignetMode(signetMode);
        sysProve.setUpdateTime(new Date());
        // 签章方式为本地 才有签章Id
        if (FinalUtils.signetMode_2.equals(signetMode)) {
            String signetId = signet.get("signetId").toString();
            sysProve.setSignetId(Long.valueOf(signetId));
        } else if (FinalUtils.signetMode_1.equals(signetMode)) {
            sysProve.setSignetId(null);
        }
        sysProve.setCoordX(coordX);
        sysProve.setCoordY(coordY);
        sysProveService.updateById(sysProve);
        // 删除原来审批流
        sysProveCheckService.deleteProveCheck(proveId);
        // 添加新的审批流
        sysProveCheckService.saveProveCheck(checkParam, proveId);
        return R.SAVE_SUCCESS;
    }

    /**
     * 证明列表
     *
     * @return
     */
    @GetMapping("/list")
    public R list() {
        QueryWrapper<SysProve> sysProveQueryWrapper = new QueryWrapper<>();
        List<SysProve> list = sysProveService.list(sysProveQueryWrapper.orderByDesc("CREATE_TIME"));
        LinkedList<SysProveVo> sysProveVos = new LinkedList<>();
        list.stream().forEach(item -> {
            SysTp sysTp = sysTpService.getById(item.getTpId());
            SysProveVo sysProveVo = new SysProveVo();
            sysProveVo.setId(item.getId());
            sysProveVo.setName(item.getName());
            sysProveVo.setTpId(sysTp.getId());
            sysProveVo.setTpName(sysTp.getTpName());
            sysProveVo.setTpPath(sysTp.getTpPath());
            sysProveVo.setCreateTime(sysTp.getCreateTime());
            sysProveVo.setTpLook(sysTp.getTpLook());
            sysProveVo.setUpdateTime(sysTp.getUpdateTime());
            // 签章参数
            SignetParamVo signetParamVo = new SignetParamVo();
            signetParamVo.setCoordX(item.getCoordX());
            signetParamVo.setCoordY(item.getCoordY());
            signetParamVo.setSignetMode(item.getSignetMode());
            signetParamVo.setSignetId(item.getSignetId());
            if (item.getSignetMode().equals(FinalUtils.signetMode_2)) {
                // 本地签章
                SysSignet sysSignet = sysSignetService.getById(item.getSignetId());
                signetParamVo.setSignetName(sysSignet.getSignetName());
                signetParamVo.setSignetPath(sysSignet.getSignetPath());
                signetParamVo.setSignetLook(sysSignet.getSignetLook());
            } else {
                // e签宝
            }
            sysProveVo.setSignetParam(signetParamVo);
//             审批参数
            sysProveVo.setCheckParam(sysProveCheckService.listByProveIdGroupTurn(item.getId()));
            sysProveVos.add(sysProveVo);
        });
        return R.SELECT_SUCCESS.data(sysProveVos);
    }

    /**
     * 证明删除
     *
     * @param proveId
     * @return
     */
    @PostMapping("/delete")
    @Transactional
    public R deleteBlog(@RequestParam("proveId") String proveId) {
        List<SysProveSubmit> list = sysProveSubmitService.listByProveId(proveId);
        if (list.size() != 0) {
            return R.data(500, "证明已有人员申请，无法删除证明模板");
        }
        SysProve byId = sysProveService.getById(proveId);
        Long tpId = byId.getTpId();
        // 删除证明模板变量
        sysTpVarService.removeByTpId(tpId);
        // 删除证明模板
        SysTp sysTp = sysTpService.getById(tpId);
        sysTpService.removeById(tpId);
        String tpPath = sysTp.getTpPath();
        // 删除证明
        sysProveService.removeById(proveId);
        // 删除证明审核流
        sysProveCheckService.deleteProveCheck(Long.valueOf(proveId));
        // 删除证明模板文件
        FileUtil.del(tpPath);
        return R.DELETE_SUCCESS;
    }

    /**
     * 证明预览
     */
    @GetMapping("/view")
    public R view(@RequestParam("proveId") String proveId, @RequestParam("userId") String userId) {
        // 查询证明模板
        SysTp sysTp = sysTpService.getById(sysProveService.getById(proveId).getTpId());
        String tpPath = sysTp.getTpPath();
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
        String image = docmentOperationUtils.wordContentReplaceBase64Image(tpPath, hashMaps, userInfo);
        HashMap<String, Object> map = new HashMap<>();
        map.put("image", image);
        return R.SELECT_SUCCESS.data(map);
    }

    /**
     * 证明审核人查询
     *
     * @return
     */
    @GetMapping("checkUser")
    public R getCheckUserList() {
        List<Map<String, Object>> smUserList = sysDataService.getSmUserList();
        return R.SELECT_SUCCESS.data(smUserList);
    }

    @GetMapping("checkUserList/111")
    public String getCheckUser() {

        return "这是一个地址url";
    }
}
