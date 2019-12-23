package com.fukuni.multi.exercises.exercise9;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fukuni.multi.R;
import com.fukuni.multi.common.BaseFragment;

import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Exercise9Fragment extends BaseFragment {

    public static Fragment newInstance() {
        return new Exercise9Fragment();
    }

    private final static int MAX_TIMEOUT_MS = 1000;

    private EditText mEdtArgument;
    private EditText mEdtTimeout;
    private TextView mTxtResult;
    private Button mBtnStartWork;

    private ComputeFactorialUseCase mComputeFactorialUseCase;

    private @Nullable
    Disposable mDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComputeFactorialUseCase = new ComputeFactorialUseCase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_9, container, false);
        mEdtArgument = view.findViewById(R.id.edt_argument);
        mEdtTimeout = view.findViewById(R.id.edt_timeout);
        mTxtResult = view.findViewById(R.id.txt_result);
        mBtnStartWork =  view.findViewById(R.id.btn_compute);

        mBtnStartWork.setOnClickListener(v -> {
            if(mEdtArgument.getText().toString().isEmpty()) {
                return;
            }

            mBtnStartWork.setEnabled(false);
            mTxtResult.setText("");
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mBtnStartWork.getWindowToken(), 0);
            int argument = Integer.valueOf(mEdtArgument.getText().toString());


            mDisposable = mComputeFactorialUseCase.computeFactorial(argument, getTimeout())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        if(result.isAborted()) {
                            onFactorialAborted();
                        } else if(result.isTimedOut()) {
                            onFactorialTimeout();
                        } else {
                            onFactorialCompute(result.getResult());
                        }
                    });

        });
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void onFactorialCompute(BigInteger result) {
        mTxtResult.setText("Result: " + result.toString());
        mBtnStartWork.setEnabled(true);
    }

    private void onFactorialTimeout() {
        mTxtResult.setText("Timeout");
        mBtnStartWork.setEnabled(true);
    }

    private void onFactorialAborted() {
        mTxtResult.setText("Aborted");
        mBtnStartWork.setEnabled(true);
    }

    private int getTimeout() {
        int timeout;
        if(mEdtTimeout.getText().toString().isEmpty()) {
            timeout = MAX_TIMEOUT_MS;
        } else {
            timeout = Integer.valueOf(mEdtTimeout.getText().toString());
            if(timeout > MAX_TIMEOUT_MS) {
                timeout = MAX_TIMEOUT_MS;
            }
        }
        return timeout;

    }

    @Override
    protected String getScreenTitle() {
        return "Exercise 9";
    }
}
