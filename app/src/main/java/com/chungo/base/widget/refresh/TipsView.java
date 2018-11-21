package com.chungo.base.widget.refresh;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prize.dagger2_demo.R;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/5/22 15:29
 */
public class TipsView extends RelativeLayout {
	private final String TAG = "hcg_" + this.getClass().getSimpleName();
	private Context mCtx;

	private int mTipsBackgroundColor;
	private int mTipsTextColor;
	private int mTipsTextSize;
	private String mTipsText;

	private TextView mTextView;
	private TextView mButton;

	private boolean isTVShown = false;
	private boolean isTipsBtnShown = false;

	private static final String SCALE_X = "scaleX";
	private static final String SCALE_Y = "scaleY";
	private TranslateAnimation mHiddenAction = null;
	private TranslateAnimation mShowAction = null;

	private int mTvStartAnimDuration = 800;// 提示条展示的动画间隔时间
	private int mBtnStartAnimDuration = 500;// 按钮展示的动画间隔时间
	private int mShowAnimDuration = 1 * 1000;// 提示条展示的时间

	private int mEndAnimDuration = 200;// 提示条结束的动画间隔时间

	private AnimatorSet mAnimatorSet;

	public TipsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	public TipsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TipsView(Context context) {
		this(context, null);
	}

	@SuppressWarnings("deprecation")
	private void init(Context context, AttributeSet attrs) {
		this.mCtx = context;

		TypedArray tyArray = context.obtainStyledAttributes(attrs,
				R.styleable.TipsView);
		mTipsBackgroundColor = tyArray.getColor(
				R.styleable.TipsView_TipsBackgroundColor,
				0X33000000);
		mTipsTextColor = tyArray.getColor(
				R.styleable.TipsView_TipsTextColor,
				Color.parseColor("#B2ffffff"));
		mTipsTextSize = (int) tyArray.getDimension(
				R.styleable.TipsView_TipsTextSize, 12);
		mTipsText = tyArray
				.getString(R.styleable.TipsView_TipsText);
		tyArray.recycle();

		setGravity(Gravity.CENTER);

		mTextView = new TextView(mCtx);
		mTextView.setText(mTipsText);
		mTextView.setTextColor(mTipsTextColor);
		//mTextView.setTextColor(0xffffffff);
		mTextView.setTextSize(mTipsTextSize);

		LayoutParams textViewParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textViewParams.addRule(CENTER_IN_PARENT);
	
		mButton = new TextView(mCtx);
		// mButton.setHeight(LayoutParams.WRAP_CONTENT);
		// mButton.setWidth(LayoutParams.WRAP_CONTENT);
		mButton.setBackground(mCtx.getDrawable(R.drawable.bg_tips_selector));
		mButton.setTextColor(Color.parseColor("#3da8f2"));
		mButton.setTextSize(mTipsTextSize);
		mButton.setGravity(Gravity.CENTER);
		mButton.setShadowLayer(0, 1, 1, Color.parseColor("#e8e8e8"));
		// int padding = DensityUtil.dp2px(10);
		int padding = DensityUtil.dp2px(10);
		mButton.setPadding(padding, 0, padding, 0);
		LayoutParams layoutParams = new LayoutParams(WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		layoutParams.topMargin = 15;
		layoutParams.bottomMargin = 0;

		addView(mTextView,textViewParams);
		addView(mButton, layoutParams);
		
//		View lines = new View(mCtx);
//		RelativeLayout.LayoutParams linesParams = new RelativeLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, 1);
//		linesParams.setMarginStart((int) getResources().getDimension(
//				R.dimen.dp_15));
//		linesParams.setMarginEnd((int) getResources().getDimension(
//				R.dimen.dp_15));
//		lines.setBackgroundColor(getResources().getColor(R.color.lines_color));
//		linesParams.addRule(ALIGN_PARENT_BOTTOM);		
//		addView(lines, linesParams);
	}

	public void show(String text) {
		show(text, true);
	}

	/**
	 * 展示提示按钮
	 * 
	 * @param text
	 */
	public void showButton(String text) {
		show(text, false);
	}

	private void show(String text, Boolean isDefultTips) {
		if (TextUtils.isEmpty(text))
			return;

		CheackView(isDefultTips, text);

		startShowAnim(isDefultTips);
	}

	/**
	 * 
	 * @param isDefult
	 *            textView|button
	 */
	private void startShowAnim(Boolean isDefult) {

		if (isDefult) {// 缩放动画
			if (mAnimatorSet==null){ 			
				mAnimatorSet = new AnimatorSet();
				mAnimatorSet.setDuration(mTvStartAnimDuration);
				mAnimatorSet.setInterpolator(new OvershootInterpolator());
			}
				ObjectAnimator x = ObjectAnimator.ofFloat(mTextView, SCALE_X, 0.6f, 1f);
				ObjectAnimator y = ObjectAnimator.ofFloat(mTextView, SCALE_Y, 0.6f, 1f);
				ObjectAnimator z = ObjectAnimator.ofFloat(mTextView, SCALE_X, 0.6f, 1f);
				
				mAnimatorSet.play(x).with(y).with(z);
				mAnimatorSet.start();	
				isTVShown = true;
				setVisibility(true);
			//addAimatorListener(animatorSet, true);
		} else {// 平移动画		
			if (mShowAction == null) {
				mShowAction = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, -1.0f,
						Animation.RELATIVE_TO_SELF, 0.0f);
				mShowAction.setDuration(mBtnStartAnimDuration);
			}
			startAnimation(mShowAction);
			addAimationListener(mShowAction, true);
		}
	}

	/**
	 * 选择显示默认还是显示按钮
	 * 
	 * @param isDefult
	 * @param text
	 */
	private void CheackView(Boolean isDefult, String text) {

		if (isDefult) {// 显示默认提示		
				setBackgroundColor(mTipsBackgroundColor);
				setContentVisibility(true);
				mTextView.setText(text);
		} else {// 显示按钮提示			
				setBackgroundColor(0x00ffffff);
				setContentVisibility(false);
				//mButton.setBackgroundColor(mPropterBackgroundColor);
				mButton.setText(text);			
		}
	}

	public TextView getBtnView() {
		return this.mButton;
	}

	/**
	 * 关闭提示按钮，如果打开了
	 */
	public void hideIfNeeded() {
		if (this.getVisibility()!=View.VISIBLE)
			return;
		if (isTipsBtnShown) {
			isTVShown = false;
			isTipsBtnShown = false;
			TranslateAnim();
		}
	}

	public void hide() {
		if (this.getVisibility()!=View.VISIBLE)
			return;
		isTVShown = false;
		isTipsBtnShown = false;
		setVisibility(View.GONE);
	}
	
	/**
	 * 执行关闭的动画
	 */
	public void hide(int duration) {
		if (this.getVisibility()!=View.VISIBLE) {//正在执行动画或则已经关闭则返回
			Log.d(TAG, "hide-Error: tips gone or is animing!");
			return;
		}
		isTVShown = false;
		isTipsBtnShown = false;
		if (duration<=0) 
			TranslateAnim();
		else 
			TranslateAnim(duration);
	}

	
	private void TranslateAnim() {
		TranslateAnim(mEndAnimDuration);
	}
	
	/**
	 * 执行关闭的动画
	 * 
	 * @param duration
	 */
	private void TranslateAnim(int duration) {

		if (mHiddenAction == null) {
			mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
					0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f);
			mHiddenAction.setDuration(duration);			
		}
		startAnimation(mHiddenAction);
		addAimationListener(mHiddenAction, false);
	}

	/**
	 * 
	 * @param animator
	 * @param isShow
	 *            展示|关闭
	 */
	private void addAimatorListener(Animator animator, Boolean isShow) {
		addAnimListener(null, animator, isShow);
	}

	/**
	 * 
	 * @param animation
	 * @param isShow
	 *            展示|关闭
	 */
	private void addAimationListener(Animation animation, Boolean isShow) {
		addAnimListener(animation, null, isShow);
	}

	/**
	 * 动画监听器
	 * 
	 * @param animation
	 * @param animator
	 * @param isShow
	 */
	private void addAnimListener(Animation animation, Animator animator,
                                 final Boolean isShow) {
		if (animation != null)
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation arg0) {

				}

				@Override
				public void onAnimationRepeat(Animation arg0) {

				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					isTipsBtnShown = true;
					setVisibility(isShow);
				}
			});
		if (animator != null)
			animator.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator arg0) {
					// setVisibility(isShow);
				}

				@Override
				public void onAnimationRepeat(Animator arg0) {

				}

				@Override
				public void onAnimationEnd(Animator arg0) {
					isTVShown = true;
					setVisibility(isShow);
				}

				@Override
				public void onAnimationCancel(Animator arg0) {
					isTVShown = true;
					setVisibility(isShow);
				}
			});

	}

	private void setContentVisibility(Boolean isDefView) {
		if (isDefView) {
			if (mButton.getVisibility() != GONE)
				mButton.setVisibility(View.GONE);
			if (mTextView.getVisibility() != VISIBLE)
				mTextView.setVisibility(View.VISIBLE);
		} else {
			if (mTextView.getVisibility() != GONE)
				mTextView.setVisibility(View.GONE);
			if (mButton.getVisibility() != VISIBLE)
				mButton.setVisibility(View.VISIBLE);
		}
	}

	private void setVisibility(Boolean isShow) {
		if (isShow) {
			if (getVisibility() != VISIBLE)
				setVisibility(VISIBLE);
		} else {
			if (getVisibility() != GONE)
				setVisibility(GONE);
		}
	}
}
