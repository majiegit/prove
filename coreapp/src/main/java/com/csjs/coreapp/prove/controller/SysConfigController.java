package com.csjs.coreapp.prove.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csjs.coreapp.commen.FinalUtils;
import com.csjs.coreapp.commen.R;
import com.csjs.coreapp.commen.exception.BaseException;
import com.csjs.coreapp.prove.entity.SysConfig;
import com.csjs.coreapp.prove.entity.SysVar;
import com.csjs.coreapp.prove.service.ISysConfigService;
import com.csjs.coreapp.prove.service.ISysDataService;
import com.csjs.coreapp.prove.service.ISysVarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysConfig")
@CrossOrigin
public class SysConfigController {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDataService sysDataService;
    @Autowired
    private ISysVarService sysVarService;

    /**
     * 配置列表
     *
     * @return
     */
    @GetMapping("/list")
    public R configList() {
        QueryWrapper<SysConfig> sysConfigQueryWrapper = new QueryWrapper<>();
        List<SysConfig> list = sysConfigService.list(sysConfigQueryWrapper.orderByAsc("create_date"));
        return R.SELECT_SUCCESS.data(list);
    }

    /**
     * 保存配置
     */
    @PostMapping("/save")
    public R add(@RequestBody SysConfig sysConfig) {
        sysConfig.setCreateDate(new Date());
        if (sysConfig.getKeyName().equals(FinalUtils.proveTpSql)) {
            boolean b = saveSqlVar(sysConfig);
            if (b) {
                sysConfigService.save(sysConfig);
            } else {
                return new R(500, "SQL解析错误");
            }
        } else {
            sysConfigService.save(sysConfig);
        }
        return R.SAVE_SUCCESS;
    }

    /**
     * 更新配置
     */
    @PostMapping("/update")
    public R update(@RequestBody SysConfig sysConfig) {
        sysConfig.setUpdateDate(new Date());
        if (sysConfig.getKeyName().equals(FinalUtils.proveTpSql)) {
            boolean b = saveSqlVar(sysConfig);
            if (b) {
                sysConfigService.updateByKeyName(sysConfig.getKeyName(), sysConfig);
            } else {
                return new R(500, "SQL解析错误");
            }
        } else {
            sysConfigService.updateByKeyName(sysConfig.getKeyName(), sysConfig);
        }
        return R.UPDATE_SUCCESS;
    }

    /**
     * 删除配置
     */
    @PostMapping("/delete")
    public R delete(@RequestBody SysConfig sysConfig) {
        boolean save = sysConfigService.deleteByKeyName(sysConfig.getKeyName());
        return R.DELETE_SUCCESS;
    }


    private boolean saveSqlVar(SysConfig sysConfig) {
        String sql = sysConfig.getValueName();
        List<String> sqlField = sysDataService.getSqlField(sql);
        if (sqlField.size() != 0) {
            sqlField.stream().forEach(field -> {
                SysVar sysVar = new SysVar();
                sysVar.setField(field);
                QueryWrapper<SysVar> sysVarQueryWrapper = new QueryWrapper<>(sysVar);
                SysVar one = sysVarService.getOne(sysVarQueryWrapper);
                if (one == null) {
                    sysVar.setCreateTime(new Date());
                    sysVarService.save(sysVar);
                }
            });
            return true;
        } else {
            return false;
        }
    }
}
