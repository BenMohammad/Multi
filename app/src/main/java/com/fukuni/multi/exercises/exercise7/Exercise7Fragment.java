package com.fukuni.multi.exercises.exercise7;

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

public class Exercise7Fragment extends BaseFragment implements ComputeFactorialUseCase.Listener{

    public static Fragment newInstance() {
        return new Exercise7Fragment();
    }

    private static final int MAX_TIMEOUT_MS = 1000;
    private EditText mEdtArgument;
    private EditText mEdtTimeout;
    private Button mBtnStartWork;
    private TextView mTxtResults;

    private ComputeFactorialUseCase mComputeFactorialUseCase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComputeFactorialUseCase = new ComputeFactorialUseCase(
                getCompositionRoot().getMUiHandler(),
                getCompositionRoot().getThreadPool()
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_7, container, false);
        mEdtArgument = view.findViewById(R.id.edt_argument);
        mEdtTimeout = view.findViewById(R.id.edt_timeout);
        mTxtResults = view.findViewById(R.id.txt_result);
        mBtnStartWork = view.findViewById(R.id.btn_compute);
        mBtnStartWork.setOnClickListener(v ->  {
            if(mEdtArgument.getText().toString().isEmpty()) {
                return;
            }
            mTxtResults.setText("");
            mBtnStartWork.setEnabled(false);
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mBtnStartWork.getWindowToken(), 0);
            int argument =  Integer.valueOf(mEdtArgument.getText().toString());
            mComputeFactorialUseCase.computeFactorialAndNotify(argument, getTimeout());
        });
        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
        mComputeFactorialUseCase.unregisterListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mComputeFactorialUseCase.registerListener(this);
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
        return "Exercise 7";
    }


    @Override
    public void onFactorialComputed(BigInteger result) {
        mTxtResults.setText(result.toString());
        mBtnStartWork.setEnabled(true);
    }

    @Override
    public void onFactorialComputationTimeout() {
        mTxtResults.setText("Timeout");
        mBtnStartWork.setEnabled(true);
    }

    @Override
    public void onFactorialComputationAborted() {
        mTxtResults.setText("Aborted");
        mBtnStartWork.setEnabled(true);
    }
}
