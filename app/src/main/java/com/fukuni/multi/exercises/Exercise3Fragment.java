package com.fukuni.multi.exercises;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fukuni.multi.R;
import com.fukuni.multi.common.BaseFragment;

public class Exercise3Fragment extends BaseFragment {

    private static final int SECONDS_TO_COUNT = 3;

    public static Fragment newInstance() {
        return new Exercise3Fragment();
    }

    private Button mBtnCountSeconds;
    private TextView mTxtCount;

    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_3, container, false);
        mBtnCountSeconds = view.findViewById(R.id.btn_count_seconds);
        mTxtCount = view.findViewById(R.id.txt_count);
        mBtnCountSeconds.setOnClickListener(v -> countIterations());


        return view;
    }

    private void countIterations() {
        mBtnCountSeconds.setEnabled(false);
        new Thread(() -> {
            for(int i =1; i <= SECONDS_TO_COUNT; i++) {
                final int count = i;
                mUiHandler.post(() -> mTxtCount.setText(String.valueOf(count)));
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }

            mUiHandler.post(() -> {
                mTxtCount.setText("Done!!");
                mBtnCountSeconds.setEnabled(true);
            });

        }).start();
    }

    @Override
    protected String getScreenTitle() {
        return "Exercise 3";
    }
}
