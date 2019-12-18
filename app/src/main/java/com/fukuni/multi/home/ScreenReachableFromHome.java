package com.fukuni.multi.home;

public enum ScreenReachableFromHome {

    UIHANDLER_DEMO("UI Handler Demo"),
    UITHREAD_DEMO("UI Thread Demo"),
    EXERCISE_1("Exercise 1"),
    EXERCISE_2("Exercise 2"),
    CUSTOM_HANDLER("Custom Handler"),
    ATOMICITY("Atomicity"),
    EXERCISE_3("Exercise 3"),
    EXERCISE_4("Exercise 4"),
    THREAD_WAIT("Thread Wait "),
    EXERCISE_5("Exercise 5");

    private String mName;

    ScreenReachableFromHome(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

}
