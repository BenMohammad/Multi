package com.fukuni.multi.demonstrattion;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fukuni.multi.R;
import com.fukuni.multi.common.BaseFragment;

public class UiHandlerDemonstrationFragment extends BaseFragment {

    private static final int ITERATIONS_COUNTER_DURATION_SEC = 10;

    public static Fragment newInstance() {
        return new UiHandlerDemonstrationFragment();
    }

    private Button mBtnCountIterations;
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ui_handler_demo, container, false);

        mBtnCountIterations = view.findViewById(R.id.btn_count_iterations);
        mBtnCountIterations.setOnClickListener(v -> countIterations());

        return view;
    }

    private void countIterations() {
        new Thread(() -> {
            long startTimestamp = System.currentTimeMillis();
            long endTimestamp = startTimestamp + ITERATIONS_COUNTER_DURATION_SEC * 1000;
            int iterationsCount = 0;
            while(System.currentTimeMillis() <= endTimestamp) {
                iterationsCount++;
            }

            final int iterationsCountFinal = iterationsCount;
            mUiHandler.post(() -> {
                Log.d("UIHandler", "Current thread " + Thread.currentThread().getName());
                mBtnCountIterations.setText("Iterations: " + iterationsCountFinal);
            });

        }).start();
    }

    @Override
    protected String getScreenTitle() {
        return "UIHandler";
    }
}
