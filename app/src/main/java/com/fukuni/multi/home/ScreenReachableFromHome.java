package com.fukuni.multi.home;

public enum ScreenReachableFromHome {

    UIHANDLER_DEMO("UI Handler Demo"),
    UITHREAD_DEMO("UI Thread Demo"),
    EXERCISE_1("Exercise 1"),
    EXERCISE_2("Exercise 2"),
    CUSTOM_HANDLER("Custom Handler");

    private String mName;

    ScreenReachableFromHome(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

}
