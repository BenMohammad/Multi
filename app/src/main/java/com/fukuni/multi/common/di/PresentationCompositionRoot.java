package com.fukuni.multi.common.di;

import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.FragmentActivity;

import com.fukuni.multi.common.ScreenNavigator;
import com.fukuni.multi.common.ToolbarManipulator;
import com.techyourchance.fragmenthelper.FragmentContainerWrapper;
import com.techyourchance.fragmenthelper.FragmentHelper;

import java.util.concurrent.ThreadPoolExecutor;

public class PresentationCompositionRoot {

    private final FragmentActivity mActivity;
    private ApplicationCompositionRoot mApplicationCompositionRoot;
    public PresentationCompositionRoot(FragmentActivity activity, ApplicationCompositionRoot applicationCompositionRoot) {
        this.mActivity = activity;
        this.mApplicationCompositionRoot = applicationCompositionRoot;
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

    public Handler getMUiHandler() {
        return new Handler(Looper.getMainLooper());
    }

    public ThreadPoolExecutor getThreadPool() {
        return mApplicationCompositionRoot.getThreadPoolExecutor();
    }
}
