package com.fukuni.multi.exercises.exercise1;

import android.os.Bundle;
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

public class Exercise1Fragment extends BaseFragment {

    private static final int ITERATIONS_COUNTER_DURATIONS_SEC = 10;

    public static Fragment newInstance() {
        return new Exercise1Fragment();
    }

    private Button mBtnCounterIterations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_1, container, false);
        mBtnCounterIterations =  view.findViewById(R.id.btn_count_iterations);
        mBtnCounterIterations.setOnClickListener(v -> countIterations());

        return view;
    }

    private void countIterations() {
        new Thread(() -> {
            long startTimeTemp = System.currentTimeMillis();
            long endTimeTemp = startTimeTemp - ITERATIONS_COUNTER_DURATIONS_SEC * 1000;
            int iterationCount = 0;
            while(System.currentTimeMillis() <= endTimeTemp) {
                iterationCount++;
            }

            Log.d("Exercise 1", "Iterations in " + ITERATIONS_COUNTER_DURATIONS_SEC + " seconds " + iterationCount);

        }).start();
    }

    @Override
    protected String getScreenTitle() {
        return "Exercise 1";
    }
}
