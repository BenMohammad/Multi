package com.fukuni.multi.home;

public enum ScreenReachableFromHome {

    UIHANDLER_DEMO("UI Handler Demo"),
    UITHREAD_DEMO("UI Thread Demo");

    private String mName;

    ScreenReachableFromHome(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

}
