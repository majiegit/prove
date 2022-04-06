package com.csjs.coreapp.prove.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csjs.coreapp.commen.R;
import com.csjs.coreapp.prove.entity.SysProveMessage;
import com.csjs.coreapp.prove.entity.SysProveSubmit;
import com.csjs.coreapp.prove.service.ISysProveMesageService;
import com.csjs.coreapp.prove.service.ISysProveSubmitService;
import com.csjs.coreapp.prove.vo.SysProveMessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sysProveMessage")
@CrossOrigin
public class SysProveMessageController {
    @Autowired
    private ISysProveMesageService proveMesageService;
    @Autowired
    private ISysProveSubmitService proveSubmitService;

    @GetMapping("listByUser")
    public R listByUser(@RequestParam("userId") String userId) {
        SysProveMessage sysProveMessage = new SysProveMessage();
        sysProveMessage.setNoticeUser(userId);
        QueryWrapper<SysProveMessage> sysProveMessageQueryWrapper = new QueryWrapper<>(sysProveMessage);
        ArrayList<SysProveMessageVo> sysProveMessageVos = new ArrayList<>();
        List<SysProveMessage> list = proveMesageService.list(sysProveMessageQueryWrapper.orderByDesc("CREATE_TIME"));
        list.stream().forEach(item -> {
            SysProveSubmit byId = proveSubmitService.getById(item.getProveSubmitId());
            SysProveMessageVo sysProveMessageVo = BeanUtil.toBean(item, SysProveMessageVo.class);
            sysProveMessageVo.setSysProveSubmit(byId);
            sysProveMessageVos.add(sysProveMessageVo);
        });
        return R.SELECT_SUCCESS.data(sysProveMessageVos);
    }

    @GetMapping("getById")
    public R get(@RequestParam("id") String id) {
        SysProveMessage byId = proveMesageService.getById(id);
        return R.SELECT_SUCCESS.data(byId);
    }
}