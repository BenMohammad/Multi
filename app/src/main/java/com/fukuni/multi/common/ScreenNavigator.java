package com.fukuni.multi.common;

import com.fukuni.multi.demonstrattion.AtomicityDemonstrationFragment;
import com.fukuni.multi.demonstrattion.CustomHandlerDemonstrationFragment;
import com.fukuni.multi.demonstrattion.UiHandlerDemonstrationFragment;
import com.fukuni.multi.demonstrattion.UiThreadDemonstrationFragment;
import com.fukuni.multi.demonstrattion.designasynctask.DesignWithAsyncTaskDemonstrationFragment;
import com.fukuni.multi.demonstrattion.designthread.DesignWithThreadsDemonstrationFragment;
import com.fukuni.multi.demonstrattion.designthreadpool.DesignWithThreadPoolDemonstrationFragment;
import com.fukuni.multi.demonstrattion.designthreadposter.DesignWithThreadPosterDemonstrationFragment;
import com.fukuni.multi.demonstrattion.threadwait.ThreadWaitDemonstrationFragment;
import com.fukuni.multi.exercises.exercise1.Exercise1Fragment;
import com.fukuni.multi.exercises.exercise2.Exercise2Fragment;
import com.fukuni.multi.exercises.exercise3.Exercise3Fragment;
import com.fukuni.multi.exercises.exercise4.Exercise4Fragment;
import com.fukuni.multi.exercises.exercise5.Exercise5Fragment;
import com.fukuni.multi.exercises.exercise6.Exercise6Fragment;
import com.fukuni.multi.exercises.exercise7.Exercise7Fragment;
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

    public void toExercise2() {
        mFragmentHelper.replaceFragment(Exercise2Fragment.newInstance());
    }

    public void toCustomHandlerDemo() {
        mFragmentHelper.replaceFragment(CustomHandlerDemonstrationFragment.newInstance());
    }

    public void toAtomicityDemo() {
        mFragmentHelper.replaceFragment(AtomicityDemonstrationFragment.newInstance());
    }

    public void toExercise3() {
        mFragmentHelper.replaceFragment(Exercise3Fragment.newInstance());
    }

    public void toExercise4() {
        mFragmentHelper.replaceFragment(Exercise4Fragment.newInstance());
    }

    public void toThreadWaitDemo() {
        mFragmentHelper.replaceFragment(ThreadWaitDemonstrationFragment.newInstance());
    }

    public void toExercise5() {
        mFragmentHelper.replaceFragment(Exercise5Fragment.newInstance());
    }

    public void toDesignWithThreads() {
        mFragmentHelper.replaceFragment(DesignWithThreadsDemonstrationFragment.newInstance());
    }

    public void toExercise6() {
        mFragmentHelper.replaceFragment(Exercise6Fragment.newInstance());
    }

    public void toDesignWithThreadPool() {
        mFragmentHelper.replaceFragment(DesignWithThreadPoolDemonstrationFragment.newInstance());
    }

    public void toExercise7() {
        mFragmentHelper.replaceFragment(Exercise7Fragment.newInstance());
    }

    public void toDesignWithAsync() {
        mFragmentHelper.replaceFragment(DesignWithAsyncTaskDemonstrationFragment.newInstance());
    }

    public void toDesignWithThreadPoster() {
        mFragmentHelper.replaceFragment(DesignWithThreadPosterDemonstrationFragment.newInstance() );
    }
}
