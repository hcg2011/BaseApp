package com.chungo.base.widget.rv;

import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.HashMap;
import java.util.List;

/**
 * @Description 当有多种条目的时候，避免在convert()中做太多的业务逻辑，把逻辑放在对应的ItemProvider中
 * @Author huangchangguo
 * @Created 2018/7/13 14:51
 */

public abstract class BaseMultiItemAdapter<T extends MultiItemEntity, V extends BaseViewHolder> extends BaseQuickAdapter<T, V> {
    public static final int MAX_CACHE = 30; //item的最大缓存数，超过则清除。用于控制无限下拉导致数据过多的情况
    protected MultiDelegateProvider mProvider;
    protected HashMap<Integer, Boolean> mClickTypes;

    public BaseMultiItemAdapter(@Nullable List<T> data) {
        super(data);
        initialize();
    }

    public BaseMultiItemAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        initialize();
    }

    public BaseMultiItemAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
        initialize();
    }

    /**
     * 注册多item的代理类
     */
    public abstract void registerDelegate();

    protected void initialize() {
        if (mProvider == null)
            mProvider = new MultiDelegateProvider();
        registerDelegate();
        setMultiTypeDelegate(mProvider);
    }
    /**
     * 添加item代理
     *
     * @param delegate
     * @return
     */
    public BaseMultiItemAdapter addItemDelegate(BaseItemDelegate delegate) {
        if (mProvider != null)
            mProvider.registerDelegate(delegate);
        return this;
    }

    @Override
    protected V onCreateDefViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mLayoutResId;
        BaseItemDelegate delegate = mProvider.getItemViewDelegate(viewType);
        if (delegate == null) {
            return createBaseViewHolder(parent, layoutId);
        }
        View itemView = delegate.getItemView(parent);
        //是否使用的layout形式创建item。通过delegate.getItemView() 是否为空来判断
        if (itemView == null) {//layout形式
            if (!mProvider.isDefItemType(viewType))
                if (getMultiTypeDelegate() != null) {
                    layoutId = getMultiTypeDelegate().getLayoutId(viewType);
                }
            return createBaseViewHolder(parent, layoutId);
        } else {//view形式
            return createBaseViewHolder(itemView);
        }
    }

    @Override
    protected void convert(V helper, T item) {
        int position = helper.getLayoutPosition() - getHeaderLayoutCount();

        BaseItemDelegate delegate = mProvider.convert(helper, item, position);

        bindClick(helper, item, position, delegate);
    }

    private void bindClick(final V helper, final T item, final int position, final BaseItemDelegate delegate) {
        if (delegate == null || !isEnabled(delegate.getViewType()))
            return;
        OnItemClickListener clickListener = getOnItemClickListener();
        OnItemLongClickListener longClickListener = getOnItemLongClickListener();
        View itemView = helper.itemView;
        //设置touch
        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return delegate.onTouch(helper, item, position);
            }
        });

        //如果已经设置了子条目点击监听和子条目长按监听
        if (clickListener != null && longClickListener != null)
            return;
        //如果没有设置长按监听，则回调给itemDelegate
        if (clickListener == null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delegate.onClick(helper, item, position);
                }
            });
        }
        //如果没有设置长按监听，则回调给itemDelegate
        if (longClickListener == null) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return delegate.onLongClick(helper, item, position);
                }
            });
        }
    }

    /**
     * 指定type的点击事件是否可用
     *
     * @param isEnable def true
     * @param viewType
     */
    public void setClickEnableForViewType(Boolean isEnable, int viewType) {
        if (mClickTypes == null && mClickTypes.size() <= 0)
            mClickTypes = new HashMap<>();
        mClickTypes.put(viewType, isEnable);
    }

    /**
     * 判断item的类型是否可用
     *
     * @param viewType
     * @return
     */
    protected boolean isEnabled(int viewType) {
        return mClickTypes == null
                || mClickTypes.size() <= 0
                || !mClickTypes.containsKey(viewType)
                || mClickTypes.get(viewType);
    }

    @Override
    public void remove(@IntRange(from = 0L) int position) {
        if (mData == null
                || position < 0
                || position >= mData.size())
            return;

        T entity = mData.get(position);
        if (entity instanceof IExpandable) {
            removeAllChild((IExpandable) entity, position);
        }
        removeDataFromParent(entity);
        super.remove(position);
    }

    /**
     * 移除子控件时，移除父控件实体类中相关子控件数据，避免关闭后再次展开数据重现
     *
     * @param child 子控件实体
     */
    protected void removeDataFromParent(T child) {
        int position = getParentPosition(child);
        if (position >= 0) {
            IExpandable parent = (IExpandable) mData.get(position);
            parent.getSubItems().remove(child);
        }
    }

    /**
     * 移除父控件时，若父控件处于展开状态，则先移除其所有的子控件
     *
     * @param parent         父控件实体
     * @param parentPosition 父控件位置
     */
    protected void removeAllChild(IExpandable parent, int parentPosition) {
        if (parent.isExpanded()) {
            List<MultiItemEntity> chidChilds = parent.getSubItems();
            if (chidChilds == null || chidChilds.size() == 0)
                return;

            int childSize = chidChilds.size();
            for (int i = 0; i < childSize; i++) {
                remove(parentPosition + 1);
            }
        }
    }

    public void addAll(List<T> data) {
        mData.addAll(data);
        notifyItemRangeInserted(getItemCount(), getItemCount() + data.size());
    }

    /**
     * 伴随清除缓存的添加数据。只能用于LinearLayoutManager
     *
     * @param rv
     * @param data
     */
    public void addAllAndClearCache(RecyclerView rv, List<T> data) {
        if (mData.size() > MAX_CACHE) {
            int scrollNum = dealItem(rv, data);
            notifyDataSetChanged();
            try {
                if (scrollNum != 0)
                    rv.scrollBy(0, scrollNum);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.gc();
        } else {
            addAll(data);
        }
    }

    private void InsertedData(RecyclerView rv, final List<T> data) {
        if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE
                && (rv.isComputingLayout() == false)) {
            mData.addAll(data);
            notifyItemRangeInserted(getItemCount(),
                    getItemCount() + data.size());
        }
    }

    private int dealItem(RecyclerView rv, List<T> data) {
        RecyclerView.LayoutManager managers = rv.getLayoutManager();
        if (!(managers instanceof RecyclerView.LayoutManager)) {
            mData.addAll(data);
            return 0;
        }
        try {
            LinearLayoutManager manager = (LinearLayoutManager) managers;
            int position = manager.findFirstVisibleItemPosition();
            for (int i = 1; i <= mData.size() - position; i++) {
                T t = mData.get(mData.size() - i);
                data.add(0, t);
            }
            int itemHeight = Math.abs(manager.findViewByPosition(position)
                    .getTop());
            mData.clear();
            mData.addAll(data);
            return itemHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
