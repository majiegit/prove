package com.csjs.coreapp.prove.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csjs.coreapp.commen.R;
import com.csjs.coreapp.prove.entity.SysTpVar;
import com.csjs.coreapp.prove.service.ISysTpVarService;
import com.csjs.coreapp.prove.service.ISysVarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sysTpVar")
@CrossOrigin
public class SysTpVarController {
    @Autowired
    private ISysTpVarService sysTpVarService;
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
        SysTpVar sysTpVar = new SysTpVar();
        sysTpVar.setTpId(id);
        QueryWrapper<SysTpVar> sysTpVarQueryWrapper = new QueryWrapper<>(sysTpVar);
        List<SysTpVar> list = sysTpVarService.list(sysTpVarQueryWrapper);
        return R.SELECT_SUCCESS.data(list);
    }

    /**
     * 保存变量
     *
     * @param list
     * @return
     */
    @PostMapping("/save")
    public R get(@RequestBody List<SysTpVar> list) {
        sysTpVarService.saveOrUpdateBatch(list);
        return R.SAVE_SUCCESS;
    }


    /**
     * 查询所有去重模板变量
     */
    @GetMapping("/list")
    public R list() {
        List<SysTpVar> list = sysTpVarService.list();
        ArrayList<SysTpVar> sysTpVars = new ArrayList<>();
        list.stream().forEach(item -> {
            if (sysTpVars.toString().indexOf(item.getTpVarName()) == -1) {
                sysTpVars.add(item);
            }

        });
        return R.SELECT_SUCCESS.data(sysTpVars);
    }

}