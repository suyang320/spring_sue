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

import com.sy.spring.BeanPostProcessor;
import com.sy.spring.MyComponent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p>
 *
 * </p>
 *
 * @className MyBeanPostProcessor
 * @author Sue
 * @create 2022/10/9 
 **/
@MyComponent
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        //创建bean的过程中，每一个bean都会调用此方法，也可以单独判断
        if ("orderService".equals(beanName)) {
            System.out.println("生成orderService的代理对象");
            Object proxyInstance = Proxy.newProxyInstance(MyBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    //切面逻辑
                    System.out.println(beanName + "执行切面逻辑");
                    return method.invoke(bean, args);
                }
            });
            return proxyInstance;
        } else {
            System.out.println(beanName + "执行bean的后置处理器");
            return bean;
        }
    }
}
