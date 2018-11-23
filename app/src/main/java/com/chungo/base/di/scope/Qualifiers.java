package com.chungo.base.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/11/22 15:41
 */
public @interface Qualifiers {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Lifecycle {

    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface RxLifecycle {

    }
}
