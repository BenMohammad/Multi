package com.fukuni.multi.common.di;

import androidx.fragment.app.FragmentActivity;

import com.fukuni.multi.common.ScreenNavigator;
import com.fukuni.multi.common.ToolbarManipulator;
import com.techyourchance.fragmenthelper.FragmentContainerWrapper;
import com.techyourchance.fragmenthelper.FragmentHelper;

public class PresentationCompositionRoot {

    private final FragmentActivity mActivity;

    public PresentationCompositionRoot(FragmentActivity activity) {
        this.mActivity = activity;
    }

    public ScreenNavigator getScreenNavigator() {
        return new ScreenNavigator(getFragmentHelper());
    }

    private FragmentHelper getFragmentHelper() {
        return new FragmentHelper(mActivity, getFragmentWrapper(), mActivity.getSupportFragmentManager());
    }
    private FragmentContainerWrapper getFragmentWrapper() {
        return (FragmentContainerWrapper)mActivity;
    }

    public ToolbarManipulator getToolbarManipulator() {
        return (ToolbarManipulator) mActivity;
    }
}
