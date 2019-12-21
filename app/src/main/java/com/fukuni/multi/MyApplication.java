package com.fukuni.multi;

import android.app.Application;

import com.fukuni.multi.common.di.ApplicationCompositionRoot;

public class MyApplication extends Application {

    private final ApplicationCompositionRoot applicationCompositionRoot =
            new ApplicationCompositionRoot();

    public ApplicationCompositionRoot getApplicationCompositionRoot() {
        return new ApplicationCompositionRoot();
    }
}
