package cn.edu.seu.udo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import butterknife.BindView;
import cn.edu.seu.udo.R;
import cn.edu.seu.udo.ui.view.CreditsRollView;

/**
 * Author: Jeremy Xu on 2016/6/25 22:20
 * E-mail: jeremy_xm@163.com
 */
public class RiseCoolFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener {

    public static final String TAG = "rise_cool";

    public static final String START = "rise_cool_start";

    private static final float SCROLL_ANIM_DURATION = 30000;    // [ms] = 30 s

    @BindView(R.id.creditsroll)
    CreditsRollView creditsroll;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    private boolean isScrolling;
    private ValueAnimator mScrollAnimator;


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (isScrolling) {
            stopScrollAnimation();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Don't care
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        creditsroll.setScrollPosition(progress / 100000f); // We have increments of 1/100000 %
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_rise_cool;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        seekbar.setOnSeekBarChangeListener(this);
        creditsroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isScrolling) {
                    animateScroll();
                } else {
                    stopScrollAnimation();
                }
            }
        });
        return rootView;
    }

    private void animateScroll() {
        isScrolling = true;
        mScrollAnimator = ObjectAnimator.ofInt(seekbar, "progress", seekbar.getProgress(), seekbar.getMax());
        mScrollAnimator.setDuration(
                (long) (SCROLL_ANIM_DURATION * (1 - (float) seekbar.getProgress() / seekbar.getMax())));
        mScrollAnimator.setInterpolator(new LinearInterpolator());
        mScrollAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Don't care
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isScrolling = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Don't care
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Don't care
            }
        });
        mScrollAnimator.start();
    }

    private void stopScrollAnimation() {
        if (mScrollAnimator != null) {
            mScrollAnimator.cancel();
            mScrollAnimator = null;
        }
    }
}
