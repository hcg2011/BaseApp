package com.chungo.base.widget.refresh;


import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/8/22 14:56
 */
public abstract class OnRefreshStatusListener implements OnMultiPurposeListener {

    public abstract void onHeaderPulling();

    public abstract void onHeaderFinish();

    @Override
    public void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
        onHeaderPulling();
    }

    @Override
    public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight) {

    }

    @Override
    public void onHeaderFinish(RefreshHeader header, boolean success) {
        onHeaderFinish();
    }

    @Override
    public void onFooterPulling(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onFooterReleasing(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int extendHeight) {

    }

    @Override
    public void onFooterFinish(RefreshFooter footer, boolean success) {

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }
}
