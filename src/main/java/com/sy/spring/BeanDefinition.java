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

/**
 * <p>
 *
 * </p>
 *
 * @className BeanDefinition
 * @author Sue
 * @create 2022/10/9 
 **/
public class BeanDefinition {

    private Class aClass;
    private String scope;
    private Boolean lazy;

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Boolean getLazy() {
        return lazy;
    }

    public void setLazy(Boolean lazy) {
        this.lazy = lazy;
    }
}
