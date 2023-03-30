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

public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 初始化后，创建bean的过程中，每一个bean都会调用此方法，也可以单独判断，可以用来实现aop
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}