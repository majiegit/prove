package com.csjs.coreapp.prove.controller;


import com.csjs.coreapp.prove.service.ISysTpService;
import com.csjs.coreapp.prove.service.ISysTpVarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysTp")
@CrossOrigin
public class SysTpController {
    @Autowired
    private ISysTpService sysTpService;
    @Autowired
    private ISysTpVarService sysTpVarService;


}