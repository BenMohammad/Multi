package com.fukuni.multi.exercises.exercise4;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;

import com.fukuni.multi.R;
import com.fukuni.multi.common.BaseFragment;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

public class Exercise4Fragment extends BaseFragment {

    public static Fragment newInstance() {
        return new Exercise4Fragment();
    }

    private static int MAX_TIMEOUT_MS = 1000;

    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private EditText mEdtArgument;
    private EditText mEdtTimeout;
    private Button mBtnStart;
    private TextView mTxtResult;

    private int mNumberOfThreads;
    private ComputationRange[] mThreadComputationRanges;
    private volatile BigInteger[] mThreadComputationResults;
    private final AtomicInteger mNumOfFinishedThreads = new AtomicInteger(0);

    private long mComputationTimeoutTime;
    private volatile boolean mAbortComputation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_4, container, false);
        mEdtArgument = view.findViewById(R.id.edt_argument);
        mEdtTimeout = view.findViewById(R.id.edt_timeout);
        mBtnStart = view.findViewById(R.id.btn_compute);
        mTxtResult = view.findViewById(R.id.txt_result);

        mBtnStart.setOnClickListener(v -> {
            if(mEdtArgument.getText().toString().isEmpty()) {
                return;
            }

            mTxtResult.setText("");
            mBtnStart.setEnabled(false);

            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mBtnStart.getWindowToken(), 0);

            int argument =  Integer.valueOf(mEdtArgument.getText().toString());

            computeFactorial(argument, getTimeout());

        });

        return view;

    }

    private void computeFactorial(final int argument,final int timeout) {
        new Thread(() -> {
            initComputationParams(argument, timeout);
            startComputation();
            waitForThreadResultsORTimeoutOrAbort();
            processComputationResults();
        }).start();
    }

    @WorkerThread
    private void processComputationResults() {
        String resultString;

        if(mAbortComputation) {
            resultString = "Computation aborted";
        } else {
            resultString = computeFinalResult().toString();
        }
        if(isTimeout()) {
            resultString = "Computation Timeout";
        }

        final String finalResultString = resultString;
        mUiHandler.post(() -> {
            if(!Exercise4Fragment.this.isStateSaved()) {
                mTxtResult.setText(finalResultString);
                mBtnStart.setEnabled(true);
            }
        });
    }

    private BigInteger computeFinalResult() {
        BigInteger result = new BigInteger("1");
        for(int i = 0; i < mNumberOfThreads; i++) {
            if(isTimeout()) {
                break;
            }
            result = result.multiply(mThreadComputationResults[i]);
        }

        return result;
    }

    private void waitForThreadResultsORTimeoutOrAbort() {
        while(true) {
            if(mNumOfFinishedThreads.get() == mNumberOfThreads) {
                break;
            } else if (mAbortComputation) {
                break;
            } else if(isTimeout()) {
                break;
            } else {
                try {
                    Thread.sleep(100);
                } catch(InterruptedException e) {

                }
            }

        }
    }

    @WorkerThread
    private void startComputation() {
        for(int i = 0; i < mNumberOfThreads; i++) {
            final int threadIndex = i;
            new Thread(() -> {
                long rangeStart = mThreadComputationRanges[threadIndex].start;
                long rangeEnd = mThreadComputationRanges[threadIndex].end;

                BigInteger product = new BigInteger("1");
                for(long num = rangeStart; num <= rangeEnd; num++) {
                    if(isTimeout()) {
                        break;
                    }
                    product = product.multiply(new BigInteger(String.valueOf(num)));
                }

                mThreadComputationResults[threadIndex] = product;
                mNumOfFinishedThreads.incrementAndGet();
            }).start();
        }
    }

    private boolean isTimeout() {
        return System.currentTimeMillis() >= mComputationTimeoutTime;
    }

    private void initComputationParams(int argument, int timeout) {
        mNumberOfThreads = argument < 20 ? 1 : Runtime.getRuntime().availableProcessors();
        mNumOfFinishedThreads.set(0);
        mAbortComputation = false;
        mThreadComputationResults = new BigInteger[mNumberOfThreads];
        mThreadComputationRanges = new ComputationRange[mNumberOfThreads];
        initThreadComputationRanges(argument);

        mComputationTimeoutTime = System.currentTimeMillis() + timeout;
    }

    private void initThreadComputationRanges(int argument) {
        int computationRangeSize = argument / mNumberOfThreads;

        long nextComputationRangeEnd = argument;

        for(int i = mNumberOfThreads - 1; i >= 0; i--) {
            mThreadComputationRanges[i] = new ComputationRange(
                    nextComputationRangeEnd - computationRangeSize + 1,
                    nextComputationRangeEnd
            );
            nextComputationRangeEnd = mThreadComputationRanges[i].start - 1;
        }

        mThreadComputationRanges[0].start = 1;
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
        return "Exercise 4";
    }


    private static class ComputationRange{
        private long start;
        private long end;

        public ComputationRange(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }


}
