package com.csjs.coreapp.prove.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csjs.coreapp.commen.R;
import com.csjs.coreapp.commen.exception.BaseException;
import com.csjs.coreapp.prove.dto.ProveFieldSaveDto;
import com.csjs.coreapp.prove.entity.SysProveField;
import com.csjs.coreapp.prove.entity.SysProveSubmit;
import com.csjs.coreapp.prove.entity.SysProveSubmitField;
import com.csjs.coreapp.prove.service.ISysProveFieldService;
import com.csjs.coreapp.prove.service.ISysProveSubmitFieldService;
import com.csjs.coreapp.prove.vo.SysProveFieldVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sysProveSubmitField")
@CrossOrigin
public class SysProveSubmitFieldController {
    @Autowired
    private ISysProveSubmitFieldService sysProveSubmitFieldService;

    /**
     * 证明提交字段列表
     *
     * @return
     */
    @GetMapping("/list")
    public R list(@RequestParam("proveSubmitId") String proveSubmitId, String type) {
        Long proveSubmitIdLong = Long.valueOf(proveSubmitId);
        SysProveSubmitField sysProveSubmitField = new SysProveSubmitField();
        sysProveSubmitField.setProveSubmitId(proveSubmitIdLong);
        if (ObjectUtil.isNotEmpty(type)) {
            sysProveSubmitField.setFieldType(type);
        }
        QueryWrapper<SysProveSubmitField> sysProveSubmitFieldQueryWrapper = new QueryWrapper<>(sysProveSubmitField);
        List<SysProveSubmitField> list = sysProveSubmitFieldService.list(sysProveSubmitFieldQueryWrapper);
        return R.SELECT_SUCCESS.data(list);
    }
}
