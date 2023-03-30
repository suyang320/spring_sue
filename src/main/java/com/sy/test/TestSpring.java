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

import com.sy.spring.MyApplicationContext;
import org.junit.Test;

/**
 * <p>
 *
 * </p>
 *
 * @className TestSpring
 * @author Sue
 * @create 2022/9/15 
 **/
public class TestSpring {


    @Test
    public void testCreateBean() throws ClassNotFoundException, NoSuchMethodException {

        System.out.println(System.getProperty("java.classpath"));
        System.out.println(System.getProperty("java.class.path"));
        MyApplicationContext applicationContent = new MyApplicationContext(MyAppConfig.class);

        UserService userService = (UserService) applicationContent.getBean("userService");
        userService.test();

        System.out.println("userService->" + applicationContent.getBean("userService"));
        System.out.println("userService->" + applicationContent.getBean("userService"));

        OrderInterface orderService = (OrderInterface) applicationContent.getBean("orderService");
        orderService.test();

        System.out.println("orderService->" + applicationContent.getBean("orderService"));
        System.out.println("orderService->" + applicationContent.getBean("orderService"));


    }

}
