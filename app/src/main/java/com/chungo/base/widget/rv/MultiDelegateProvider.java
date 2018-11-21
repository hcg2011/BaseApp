package com.chungo.base.widget.rv;

import android.util.SparseArray;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.util.MultiTypeDelegate;

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/7/12 17:59
 */
public class MultiDelegateProvider<T extends MultiItemEntity, K extends BaseItemDelegate> extends MultiTypeDelegate<T> {
    protected static final int DEFAULT_VIEW_TYPE = -0xff;
    protected SparseArray<K> delegates = new SparseArray<>();

    @Override
    protected int getItemType(T t) {
        if (!useItemDelegate())
            return getDefItemType();
        return getItemViewType(t);
    }

    private boolean useItemDelegate() {
        return getDelegateCount() > 0;
    }

    public MultiDelegateProvider registerDelegate(K delegate) {
        cheakRegister(delegate);
        int viewType = delegate.getViewType();
        int layoutId = delegate.getLayoutId();
        delegates.put(viewType, delegate);
        registerItemType(viewType,layoutId);
        return this;
    }

    private void cheakRegister(K delegate) {
        cheackDelegate(delegate);
        int viewType = delegate.getViewType();
        if (delegates.get(viewType) != null) {
            throw new IllegalArgumentException(
                    "An ItemViewDelegate is already registered for the viewType = "
                            + viewType
                            + ". Already registered ItemViewDelegate is "
                            + delegates.get(viewType));
        }
    }

    private void cheackDelegate(K delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("ItemProvider can not be null");
        }
    }

    public SparseArray<K> getItemDelegates() {
        return delegates;
    }

    public int getDelegateCount() {
        return delegates.size();
    }

    public MultiDelegateProvider<T, K> unRegisterDelegate(K delegate) {
        cheackDelegate(delegate);
        int indexToRemove = delegates.indexOfValue(delegate);
        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    public MultiDelegateProvider<T, K> unRegisterDelegate(int itemType) {
        int indexToRemove = delegates.indexOfKey(itemType);
        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    /**
     * viewType
     *
     * @param item
     * @return
     */
    public int getItemViewType(T item) {
        int delegatesCount = delegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            K delegate = delegates.valueAt(i);
            if (delegate.isForViewType(item)) {
                return delegates.keyAt(i);
            }
        }
        return getDefItemType();
    }

    public int getDefItemType() {
        return DEFAULT_VIEW_TYPE;
    }

    public Boolean isDefItemType(int type) {
        return type == DEFAULT_VIEW_TYPE;
    }

    public K convert(BaseViewHolder holder, T item, int position) {
        int viewType = holder.getItemViewType();
        K delegate = delegates.get(viewType);
        if (delegate != null)
            delegate.convert(holder, item, position);
        return delegate;
    }


    public K getItemViewDelegate(int viewType) {
        return delegates.get(viewType);
    }

    public int getItemLayoutId(int viewType) {
        return getItemViewDelegate(viewType).getLayoutId();
    }

    public int getItemViewType(K delegate) {
        return delegates.indexOfValue(delegate);
    }

}
