package com.chungo.base.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description 作用域，用于限定生命周期
 * @Author huangchangguo
 * @Created 2018/11/22 15:16
 */
public @interface Scopes {

    //Activity 的生命周期，对于activity单例
    @Scope
    @Documented
    @Retention(RUNTIME)
    @interface Activity {
    }

    //Fragment 的生命周期，对于fragment单例
    @Scope
    @Documented
    @Retention(RUNTIME)
    @interface Fragment {
    }
}
