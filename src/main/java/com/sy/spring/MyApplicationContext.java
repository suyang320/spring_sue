/*
 *  CMB Confidential
 *
 *  Copyright (C) 2022 China Merchants Bank Co., Ltd. All rights reserved.
 *
 *  No part of this file may be reproduced or transmitted in any form or by any
 *  means, electronic, mechanical, photocopying, recording, or otherwise, without
 *  prior written permission of China Merchants Bank Co., Ltd.
 */

package com.sy.spring;

import com.sy.test.BeanNameAware;
import com.sy.test.MyAppConfig;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @className MyApplicationContext
 * @author Sue
 * @create 2022/10/9
 **/
public class MyApplicationContext {

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    //单例池
    private Map<String, Object> singletonObjects = new HashMap<>();

    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public MyApplicationContext(Class<MyAppConfig> myAppConfigClass) throws ClassNotFoundException, NoSuchMethodException {

        //扫描 构建Bean定义，放在beanDefinitionMap中
        scan(myAppConfigClass);
        //扫描完，遍历beanDefinitionMap，找出所有的单例bean，创建单例bean并放在单例池中
        //单例模式先创建bean再放在单例池中
        Set<Map.Entry<String, BeanDefinition>> entries = beanDefinitionMap.entrySet();
        for (Map.Entry<String, BeanDefinition> entry : entries) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            String scope = beanDefinition.getScope();
            if ("singleton".equals(scope)) {
                Object singletonBean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonBean);
            }
        }


    }

    private void scan(Class<?> myAppConfigClass) throws ClassNotFoundException {
        if (myAppConfigClass.isAnnotationPresent(MyComponentScan.class)) {
            MyComponentScan annotation = myAppConfigClass.getAnnotation(MyComponentScan.class);
            //扫描路径
            String path = annotation.value();
            System.out.println("扫描路径：" + path);
            path = path.replace(".", "/");

            ClassLoader classLoader = MyApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File fileDirectory = new File(resource.getFile());
            if (fileDirectory.isDirectory()) {
                File[] files = fileDirectory.listFiles();
                //遍历class文件
                for (File file : files) {
                    String absolutePath = file.getAbsolutePath();
                    //System.out.println("absolutePath->" + absolutePath);
                    //加载类
                    //转换路径格式
                    absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.lastIndexOf(".class"));
                    absolutePath = absolutePath.replace("/", ".");
                    //System.out.println("absolutePath->" + absolutePath);
                    //class文件加载为class对象
                    //Class<?> aClass = Class.forName(absolutePath);
                    Class<?> aClass = classLoader.loadClass(absolutePath);
                    //找到有Component注解的类
                    if (aClass.isAnnotationPresent(MyComponent.class)) {
                        System.out.println(absolutePath + "有component注解");

                        //如果一个类有component注解，再判断是否实现了BeanPostProcessor接口
                        if (BeanPostProcessor.class.isAssignableFrom(aClass)) {
                            try {
                                BeanPostProcessor instance = (BeanPostProcessor) aClass.getConstructor().newInstance();
                                //保存BeanPostProcessor，等到创建bean的时候再使用
                                beanPostProcessorList.add(instance);

                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                            //不用获取BeanPostProcessor的BeanDefinition
                        } else {
                            MyComponent myComponent = aClass.getAnnotation(MyComponent.class);
                            //注解中配置的beanName
                            String beanName = myComponent.value();
                            //如果未指定beanName
                            if ("".equals(beanName)) {
                                //根据类型生成名字
                                beanName = Introspector.decapitalize(aClass.getSimpleName());
                            }
                            //如果有类有Component这个注解，那么需要创建一个BeanDefinition描述该bean
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setaClass(aClass);
                            //再判断是单例还是原型
                            if (aClass.isAnnotationPresent(MyScope.class)) {
                                MyScope myScope = aClass.getAnnotation(MyScope.class);
                                String scope = myScope.value();
                                beanDefinition.setScope(scope);
                            } else {
                                //没有注解，默认是单例，如果是单例
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }

                    }
                }
            }
        }
    }


    public Object getBean(String beanName) throws NoSuchMethodException {
        //如果没有则说明没有定义这个bean
        if (!beanDefinitionMap.containsKey(beanName)) {
            System.out.println("找不到该bean");
            return null;
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        String scope = beanDefinition.getScope();
        //如果是单例，直接从单例池中获取
        if ("singleton".equals(scope)) {
            Object singletonBean = singletonObjects.get(beanName);
            //可能因为扫描顺序问题，还没有初始化成功
            if (singletonBean == null) {
                singletonBean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonBean);
            }
            return singletonBean;
        } else {
            //原型模式每次都要重新创建
            Object prototypeBean = createBean(beanName, beanDefinition);
            return prototypeBean;
        }
    }

    //创建bean
    public Object createBean(String beanName, BeanDefinition beanDefinition) throws NoSuchMethodException {
        Class aClass = beanDefinition.getaClass();
        //调用构造方法创建对象
        Object instance = null;
        try {
            instance = aClass.getConstructor().newInstance();
            //创建bean的过程中进行依赖注入
            //遍历类的属性
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                //判断属性上是否有autowired注解
                if (field.isAnnotationPresent(MyAutowired.class)) {
                    field.setAccessible(true);
                    //赋值 -> 先根据类型再根据名字去找，我们这里只使用名字去找 会有循环依赖问题
                    field.set(instance, getBean(field.getName()));
                }
            }

            //依赖注入完成后,beanNameAware
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            //初始化前
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            //依赖注入完成之后进行初始化操作
            //判断是否实现了InitializingBean接口
            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }

            //初始化后
            //new MyBeanPostProcessor().postProcessAfterInitialization(instance, beanName);
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
               instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return instance;
    }

}
