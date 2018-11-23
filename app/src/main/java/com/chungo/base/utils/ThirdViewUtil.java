package com.chungo.base.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * ================================================
 * Created by JessYan on 17/03/2016 13:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class ThirdViewUtil {
    private static int HAS_AUTO_LAYOUT_META = -1;//0 说明 AndroidManifest 里面没有使用 AutoLauout 的 Meta, 即不使用 AutoLayout, 1 为有 Meta, 即需要使用

    private ThirdViewUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static Unbinder bindTarget(Object target, Object source) {
        if (source instanceof Activity) {
            return ButterKnife.bind(target, (Activity) source);
        } else if (source instanceof View) {
            return ButterKnife.bind(target, (View) source);
        } else if (source instanceof Dialog) {
            return ButterKnife.bind(target, (Dialog) source);
        } else {
            return Unbinder.EMPTY;
        }
    }
}
