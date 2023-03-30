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
import com.sy.spring.MyComponent;
import com.sy.spring.MyScope;

/**
 * <p>
 *
 * </p>
 *
 * @className UserService
 * @author Sue
 * @create 2022/10/9 
 **/
@MyScope("singleton")
@MyComponent("userService")
public class UserService implements InitializingBean,BeanNameAware {

    private String beanName;

    public void test() {
        System.out.println("myUserService.test..............");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("myUserService.afterPropertiesSet...");
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("userService.setBeanName...");
        this.beanName = beanName;
    }
}
