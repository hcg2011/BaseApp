package com.chungo.base.widget.rv;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @Description 针对多类型的item，通过代理的形式来分发。代理管理类
 * @Author huangchangguo
 * @Created 2018/7/13 14:51
 */

public abstract class BaseItemDelegate<T extends MultiItemEntity, V extends BaseViewHolder> {
    /**
     * 布局形式的item，和 {@link #getItemView}每个代理只能使用其中一种
     */
    public abstract int getLayoutId();

    /**
     * 自定义view的item，和 {@link #getLayoutId}每个代理只能使用其中一种。
     * 用于动态判断，不使用请返回null
     */
    public abstract View getItemView(ViewGroup parent);

    public abstract int getViewType();

    //bindview的时候调用
    public abstract void convert(V holder, T t, int position);

    //用于判断当前的代理item和数据是否匹配
    public boolean isForViewType(T item) {
        return item.getItemType() == getViewType();
    }

    //子类若想实现条目点击事件则重写该方法
    //Subclasses override this method if you want to implement an item click event
    public void onClick(V helper, T data, int position) {

    }

    //子类若想实现条目长按事件则重写该方法，如果adapter里面已经设置了itemClick，则该方法自动失效
    //Subclasses override this method if you want to implement an item long press event
    public boolean onLongClick(V helper, T data, int position) {
        return false;
    }

    //子类若想实现条目触摸事件则重写该方法，如果adapter里面已经设置了itemClick，则该方法自动失效
    public boolean onTouch(V helper, T data, int position) {
        return false;
    }
}
