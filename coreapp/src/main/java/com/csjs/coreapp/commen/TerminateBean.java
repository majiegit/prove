package com.csjs.coreapp.commen;

import javax.annotation.PreDestroy;

/**
 * @author MJ
 * @Date 2019-08-17
 */
public class TerminateBean {

    @PreDestroy
    public void preDestroy() {
        System.out.println("TerminalBean is destroyed");
    }

}