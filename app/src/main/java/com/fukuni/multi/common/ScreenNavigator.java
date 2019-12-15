package com.fukuni.multi.common;

import com.fukuni.multi.demonstrattion.UiHandlerDemonstrationFragment;
import com.fukuni.multi.demonstrattion.UiThreadDemonstrationFragment;
import com.fukuni.multi.exercises.Exercise1Fragment;
import com.fukuni.multi.home.HomeFragment;
import com.techyourchance.fragmenthelper.FragmentHelper;

public class ScreenNavigator {

    private final FragmentHelper mFragmentHelper;

    public ScreenNavigator(FragmentHelper fragmentHelper) {
        this.mFragmentHelper = fragmentHelper;
    }

    public void toHomeScreen() {
        mFragmentHelper.replaceFragmentAndClearHistory(HomeFragment.newInstance());
    }

    public void naigateBack() {
        mFragmentHelper.navigateBack();
    }

    public void navigateUp() {
        mFragmentHelper.navigateUp();
    }

    public void toUiHandlerDemo() {
        mFragmentHelper.replaceFragment(UiHandlerDemonstrationFragment.newInstance());
    }

    public void toUiThreadDemo() {
        mFragmentHelper.replaceFragment(UiThreadDemonstrationFragment.newInstance());
    }

    public void toExercise1() {
        mFragmentHelper.replaceFragment(Exercise1Fragment.newInstance());
    }
}
