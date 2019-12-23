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
    EXERCISE_5("Exercise 5"),
    DESIGN_WITH_THREADS("Design with Threads"),
    EXERCISE_6("Exercise 6"),
    DESIGN_THREAD_POOL("Design with ThreadPool"),
    EXERCISE_7("Exercise 7"),
    DESIGN_WITH_ASYNC("Design with Async"),
    DESIGN_WITH_THREADPOSTER("Design with ThreadPoster"),
    EXERCISE_8("Exercise 8"),
    DESIGN_RXJAVA("Design with RxJava"),
    EXERCISE_9("Exercise 9"),
    DESIGN_COROUTINES("Design with Coroutines");

    private String mName;

    ScreenReachableFromHome(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

}
