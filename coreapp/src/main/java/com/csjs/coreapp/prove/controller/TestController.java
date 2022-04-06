package com.csjs.coreapp.prove.controller;

import com.csjs.coreapp.commen.R;
import com.csjs.coreapp.prove.service.ISysDataService;
import com.csjs.coreapp.prove.service.ISysProveSubmitCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private ISysProveSubmitCheckService sysProveSubmitCheckService;
    @Autowired
    private ISysDataService sysDataService;

    @GetMapping("/test")
    public R test() {
        Map<String, Object> shouldSalary = sysDataService.getShouldSalary("1001A7100000000007QU");
        return R.SUCCESS.data(shouldSalary);
    }
}
