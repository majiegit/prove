package com.csjs.coreapp.prove.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csjs.coreapp.commen.R;
import com.csjs.coreapp.commen.exception.BaseException;
import com.csjs.coreapp.prove.dto.ProveFieldSaveDto;
import com.csjs.coreapp.prove.entity.SysProveField;
import com.csjs.coreapp.prove.service.ISysProveFieldService;
import com.csjs.coreapp.prove.vo.SysProveFieldVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/sysProveField")
@CrossOrigin
public class SysProveFieldController {
    @Autowired
    private ISysProveFieldService sysProveFieldService;


    @PostMapping("/save")
    @Transactional
    public R save(@RequestBody ProveFieldSaveDto fieldSaveDto) {
        String proveId = fieldSaveDto.getProveId();
        sysProveFieldService.remove(new QueryWrapper<SysProveField>().eq("prove_id", proveId));
        ArrayList<SysProveField> sysProveFieldExpands = fieldSaveDto.getSysProveFieldExpands();
        for (int i = 0; i < sysProveFieldExpands.size(); i++) {
            SysProveField item = sysProveFieldExpands.get(i);
            if (ObjectUtil.isEmpty(item.getId())) {
                if (ObjectUtil.isEmpty(item.getProveId())) {
                    throw new BaseException("证明ID 不能为空");
                }
                item.setFieldKey(item.getProveId().toString() + "_" + i);
            }
            sysProveFieldService.saveOrUpdate(item);
        }
        return R.SAVE_SUCCESS;
    }

    /**
     * 证明字段列表
     *
     * @return
     */
    @GetMapping("/list")
    public R list(@RequestParam("proveId") String proveId, String type) {
        Long proveIdLong = Long.valueOf(proveId);
        ArrayList<SysProveField> sysProveFieldExpands;
        if (StrUtil.isNotEmpty(type)) {
            sysProveFieldExpands = sysProveFieldService.listProveFields(proveIdLong, type);
        } else {
            sysProveFieldExpands = sysProveFieldService.listProveFields(proveIdLong);
        }
        ArrayList<SysProveFieldVo> sysProveFieldVos = new ArrayList<>();
        sysProveFieldExpands.stream().forEach(item -> {
            SysProveFieldVo sysProveFieldVo = new SysProveFieldVo();
            sysProveFieldVo.setFieldId(item.getId());
            sysProveFieldVo.setFieldKey(item.getFieldKey());
            sysProveFieldVo.setFieldName(item.getFieldName());
            sysProveFieldVo.setFieldType(item.getFieldType());
            sysProveFieldVo.setFieldValue("");
            sysProveFieldVos.add(sysProveFieldVo);
        });

        return R.SELECT_SUCCESS.data(sysProveFieldVos);
    }

    @PostMapping("/delete")
    @Transactional
    public R delete(@RequestParam("id") String id) {
        sysProveFieldService.removeById(id);
        return R.SAVE_SUCCESS;
    }
}
