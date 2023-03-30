/*
 *  CMB Confidential
 *
 *  Copyright (C) 2022 China Merchants Bank Co., Ltd. All rights reserved.
 *
 *  No part of this file may be reproduced or transmitted in any form or by any
 *  means, electronic, mechanical, photocopying, recording, or otherwise, without
 *  prior written permission of China Merchants Bank Co., Ltd.
 */

package com.sy.test;


import com.sy.spring.InitializingBean;
import com.sy.spring.MyAutowired;
import com.sy.spring.MyComponent;
import com.sy.spring.MyScope;

/**
 * <p>
 *
 * </p>
 *
 * @className MyOrderService
 * @author Sue
 * @create 2022/10/9 
 **/
@MyComponent
@MyScope("prototype")
public class OrderService implements OrderInterface, InitializingBean {

    @MyAutowired
    private UserService userService;

    @MyValue("xxxxxx")
    private String testStr;

    @Override
    public void test() {
        System.out.println("myOrderService.test..........");
        System.out.println("myOrderService.myUserService->" + userService);
        System.out.println("myOrderService.testStr->" + testStr);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("myOrderService.afterPropertiesSet...");
    }
}
