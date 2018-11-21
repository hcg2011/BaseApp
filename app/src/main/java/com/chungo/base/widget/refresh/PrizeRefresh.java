package com.chungo.base.widget.refresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/5/22 15:29
 */
public class PrizeRefresh extends SmartRefreshLayout {
    private int mTipsSpinner = DensityUtil.dp2px(32);
    private TipsView mTips;


    public PrizeRefresh(Context context) {
        super(context);
    }

    public PrizeRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PrizeRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        ViewParent parent = this.getParent();
        if (parent instanceof FrameLayout) {
            for (int i = 0; i < ((FrameLayout) parent).getChildCount(); i++) {
                View child = ((FrameLayout) parent).getChildAt(i);
                if (child instanceof TipsView) {
                    mTipsSpinner = DensityUtil.dp2px(32);
                    mTips = (TipsView) child;
                    mTips.getBtnView().setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            autoRefresh();
                            mTips.hide();
                        }
                    });
                }
            }
        }
    }

    public SmartRefreshLayout finishRefresh(String tips) {
        showTips(tips);
        return finishRefresh(1000);
    }

    @Override
    public void resetStatus() {
        super.resetStatus();
    }

    private void showTips(String tips) {
        CheackNull();
        Log.d("hcg", "isActivated()=" + isActivated());
        //moveSpinnerInfinitely(mTipsSpinner);
        mTips.show(tips);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mTips.hide(250);
            }
        }, 1000);
    }

    /**
     * 关闭提示
     */
    public void hideTips() {
        CheackNull();
        animSpinner(0, 100, mReboundInterpolator, mReboundDuration);
        mTips.hide();
    }

    @Nullable
    @Override
    public RefreshHeader getRefreshHeader() {
        return super.getRefreshHeader();
    }

    /**
     * 展示提示按钮
     *
     * @param text
     */
    public void showTipsButton(String text) {
        CheackNull();
        getRefreshHeader().getView().setVisibility(View.INVISIBLE);
        animSpinner(mTipsSpinner, 100, mReboundInterpolator, mReboundDuration);
        mTips.showButton(text);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mTips.hide(500);
                animSpinner(0, 0, mReboundInterpolator, mReboundDuration);
            }
        }, 3000);
    }

    private void CheackNull() {
        if (mTips == null)
            initView();
        int height = mTips.getHeight();
        if (mTipsSpinner != height)
            mTipsSpinner = mTips.getHeight();
    }
}
