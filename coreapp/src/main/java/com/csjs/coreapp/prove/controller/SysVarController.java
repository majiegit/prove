package com.csjs.coreapp.prove.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csjs.coreapp.commen.R;
import com.csjs.coreapp.prove.entity.SysVar;
import com.csjs.coreapp.prove.service.ISysVarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysVar")
@CrossOrigin
public class SysVarController {
    @Autowired
    private ISysVarService sysVarService;

    /**
     * 查询变量
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public R get(@RequestParam("id") Long id) {
        SysVar sysTpVar = new SysVar();
        sysTpVar.setId(id);
        QueryWrapper<SysVar> sysTpVarQueryWrapper = new QueryWrapper<>(sysTpVar);
        List<SysVar> list = sysVarService.list(sysTpVarQueryWrapper);
        return R.SELECT_SUCCESS.data(list);
    }

    /**
     * 变量保存
     *
     * @param sysVar
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestBody SysVar sysVar) {
        if (sysVar.getId() == null) {
            sysVar.setCreateTime(new Date());
        } else {
            sysVar.setUpdateTime(new Date());
        }
        if (ObjectUtil.isNotEmpty(sysVar.getName())) {
            QueryWrapper<SysVar> sysVarQueryWrapper = new QueryWrapper<>();
            List<SysVar> sysVars = sysVarService.list(sysVarQueryWrapper.eq("name", sysVar.getName()));
            if (sysVars.size() != 0) {
                return R.data(500, "该名称已被其他字段使用,请保证字段与名称唯一性");
            }
        }
        boolean save = sysVarService.saveOrUpdate(sysVar);
        if (save) {
            return R.SAVE_SUCCESS;
        } else {
            return R.SAVE_ERROR;
        }
    }

    /**
     * 变量列表
     *
     * @return
     */
    @GetMapping("/list")
    public R list() {
        List<SysVar> list = sysVarService.list();
        return R.SELECT_SUCCESS.data(list);
    }

    /**
     * 变量删除
     *
     * @return
     */
    @PostMapping("/delete")
    public R delete(@RequestBody List<Long> ids) {
        boolean b = sysVarService.removeByIds(ids);
        if (b) {
            return R.DELETE_SUCCESS;
        } else {
            return R.DELETE_ERROR;
        }
    }
}