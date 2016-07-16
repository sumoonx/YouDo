package cn.edu.seu.udo.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.IdRes;

import java.util.Stack;

import cn.edu.seu.udo.ui.fragment.HomeFragment;
import cn.edu.seu.udo.ui.fragment.RiseCoolFragment;
import cn.edu.seu.udo.ui.fragment.RiseFragment;
import cn.edu.seu.udo.ui.fragment.ScreenFragment;
import cn.edu.seu.udo.ui.fragment.StudyDetailFragment;
import cn.edu.seu.udo.ui.fragment.StudyFragment;
import cn.edu.seu.udo.utils.LogUtil;

/**
 * Author: Jeremy Xu on 2016/7/15 21:31
 * E-mail: jeremy_xm@163.com
 */
public class ScreenManager {

    private static final String HOME = HomeFragment.START;

    MainActivity mActivity;
    @IdRes int mContainer;

    ScreenFragment mHomeScreen;
    Stack<ScreenFragment> mScreens;

    public ScreenManager(MainActivity activity, @IdRes int container) {
        mActivity = activity;
        mContainer = container;
        mScreens = new Stack<>();
        setupHome();
    }

    public void toHomeScreen() {
        startScreen(HOME);
    }

    public void startScreen(String start) {
        ScreenFragment screen = (ScreenFragment) mActivity
                .getFragmentManager().findFragmentByTag(start);
        if (screen == null) {
            screen = newScreen(start);
        }
        if (screen == null) return;
        ScreenFragment top = mScreens.peek();
        switchScreen(top, screen);
    }

    public void stopCurrentScreen() {
        if (peakScreen().getName().equals(mHomeScreen.getName())) {
            mActivity.finish();
        } else {
            popScreen();
        }
    }

    public ScreenFragment topScreen() {
        return peakScreen();
    }

    private void switchScreen(ScreenFragment from, ScreenFragment to) {
        if (to != from && !popAbove(to)) {
                pushScreen(to);
        }
    }

    private ScreenFragment newScreen(String intent) {
        ScreenFragment ret = null;
        switch (intent) {
            case HomeFragment.START:
                ret = new HomeFragment();
                break;
            case StudyFragment.START:
                ret = new StudyFragment();
                break;
            case StudyDetailFragment.START:
                ret = new StudyDetailFragment();
                break;
            case RiseFragment.START:
                ret = new RiseFragment();
                break;
            case RiseCoolFragment.START:
                ret = new RiseCoolFragment();
                break;
        }
        return ret;
    }

    /**
     * Atomic operation, add the screen and push to the stack
     */
    private void setupHome() {
        mHomeScreen = newScreen(HOME);
        mActivity.getFragmentManager()
                .beginTransaction()
                .add(mContainer, mHomeScreen, mHomeScreen.getIntent())
                .commit();
        mActivity.setTitle(mHomeScreen.getTitle());
        mScreens.push(mHomeScreen);
        printSelf();
    }

    /**
     * Atomic operation, add the screen and push to the stack
     * @param screen screen to be pushed
     */
    private void pushScreen(ScreenFragment screen) {
        mActivity.getFragmentManager()
                .beginTransaction()
                .hide(mScreens.peek())
                .add(mContainer, screen, screen.getName())
                .commit();
        mActivity.setTitle(screen.getTitle());
        mScreens.push(screen);
        printSelf();
    }

    /**
     * Atomic operation, remove top screen and show the next top screen
     * @return next top screen
     */
    private ScreenFragment popScreen() {
        ScreenFragment ret = mScreens.pop();
        FragmentManager fragmentManager = mActivity.getFragmentManager();
        fragmentManager.beginTransaction()
                .remove(ret)
                .show(mScreens.peek())
                .commit();
        mActivity.setTitle(mScreens.peek().getTitle());
        printSelf();
        return ret;
    }

    /**
     * Atomic operation, peak the top screen
     * @return
     */
    private ScreenFragment peakScreen() {
        return mScreens.peek();
    }

    /**
     * Atomic operation,pop all screens above the given screen
     * @param screen target screen
     * @return false if not existed
     */
    private boolean popAbove(ScreenFragment screen) {
        if (!screen.isAdded())   return false;
        while (peakScreen() != screen) {
                popScreen();
        }
        printSelf();
        return true;
    }

    private void printSelf() {
        LogUtil.ai("Stack is " + mScreens);
    }
}
