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

import java.lang.reflect.Field;
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
public class MyValueBeanPostProcessor implements BeanPostProcessor {

    /**
     * 测试通过自定义注解给属性赋值
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(MyValue.class)) {
                field.setAccessible(true);
                MyValue annotation = field.getAnnotation(MyValue.class);
                String value = annotation.value();
                try {
                    field.set(bean,value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

}
