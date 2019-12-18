package com.fukuni.multi.demonstrattion.threadwait;

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

public class ThreadWaitDemonstrationFragment extends BaseFragment {

    public  static Fragment newInstance() {
        return new ThreadWaitDemonstrationFragment();
    }

    private final static int MAX_TIMEOUT_MS = 1000;
    private final Object LOCK = new Object();
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private EditText mEdtArgument, mEdtTimeout;
    private Button mBtnStartWork;
    private TextView mTxtResult;

    private int numberOfThreads;
    private ComputationRange[] mThreadsComputationRanges;
    private volatile BigInteger[] mThreadsComputationResults;
    private int mNumOfFinishedThreads;
    private long mComputationTimeoutTime;
    private volatile boolean mAbortComputation;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thread_wait_demo, container, false);
        mEdtArgument = view.findViewById(R.id.edt_argument);
        mEdtTimeout = view.findViewById(R.id.edt_timeout);
        mBtnStartWork = view.findViewById(R.id.btn_compute);
        mTxtResult = view.findViewById(R.id.txt_result);

        mBtnStartWork.setOnClickListener(v -> {
            if(mEdtArgument.getText().toString().isEmpty()) {
                return;
            }

            mTxtResult.setText("");
            mBtnStartWork.setEnabled(false);

            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(mBtnStartWork.getWindowToken(), 0);
            int argument = Integer.valueOf(mEdtArgument.getText().toString());

            computeFactorial(argument, getTimeout());

        });

        return view;

    }

    private void computeFactorial(final int argument, final int timeout) {
        new Thread(() -> {
            initComputation(argument, timeout);
            startComputation();
            waitFOrThreadsResultsOrTimeoutOrAbort();
            processComputationResults();
        }).start();
    }

    private void processComputationResults() {
        String result;
        if(mAbortComputation) {
            result = "Computation Anorted";
        }else {
            result = computeFinalResult().toString();
        }

        if(isTimeout()) {
            result = "Timeout ....";
        }

        final String finalResultString = result;

        mUiHandler.post(() -> {
            if(!ThreadWaitDemonstrationFragment.this.isStateSaved()) {
                mTxtResult.setText(finalResultString);
                mBtnStartWork.setEnabled(true);
            }
        });

    }

    private BigInteger computeFinalResult() {
        BigInteger result = new BigInteger("1");
        for (int i = 0; i < numberOfThreads; i++) {
                if (isTimeout()) {
                    break;
                }

                result = result.multiply(mThreadsComputationResults[i]);

            }


            return result;

    }
    @WorkerThread
    private void waitFOrThreadsResultsOrTimeoutOrAbort() {
        synchronized (LOCK) {
            while(mNumOfFinishedThreads != numberOfThreads && !isTimeout() && !mAbortComputation) {
                try {
                    LOCK.wait(getRemainingMillisSeconds());
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
    private long getRemainingMillisSeconds() {
        return mComputationTimeoutTime - System.currentTimeMillis();
    }

    @WorkerThread
    private void startComputation() {
        for(int i =0; i < numberOfThreads; i++) {

            final int threadIndex = 0;
            new Thread(() -> {
                long rangeStart = mThreadsComputationRanges[threadIndex].start;
                long rangeEnd = mThreadsComputationRanges[threadIndex].end;

                BigInteger product = new BigInteger("1");
                for(long num = rangeStart; num <= rangeEnd; num++) {
                    if(isTimeout()) {
                        break;
                    }

                    product = product.multiply(new BigInteger(String.valueOf(num)));

                }

                mThreadsComputationResults[threadIndex] = product;
                synchronized (LOCK) {
                        mNumOfFinishedThreads++;
                        LOCK.notifyAll();
                }

            }).start();
        }
    }

    private boolean isTimeout() {
        return System.currentTimeMillis() >= mComputationTimeoutTime;
    }

    private void initComputation(int argument, int timeout) {
        numberOfThreads = argument < 20 ? 1 : Runtime.getRuntime().availableProcessors();
        synchronized (LOCK) {
            mNumOfFinishedThreads = 0;
        }

        mAbortComputation = false;
        mThreadsComputationResults =  new BigInteger[numberOfThreads];
        mThreadsComputationRanges = new ComputationRange[numberOfThreads];
        initThreadComputationRange(argument);
        mComputationTimeoutTime = System.currentTimeMillis() + timeout;


    }

    private void initThreadComputationRange(int argument) {
        int computationRangeSize = argument / numberOfThreads;

        long nextComputationRangeEnd = argument;
        for(int i = numberOfThreads - 1; i >= 0; i--) {
            mThreadsComputationRanges[i] = new ComputationRange(
                    nextComputationRangeEnd - computationRangeSize,
                    nextComputationRangeEnd
            );

            nextComputationRangeEnd = mThreadsComputationRanges[i].start - 1;

        }

        mThreadsComputationRanges[0].start = 1;

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
        return "Thread Wait Demo";
    }

    private static class ComputationRange {
        private long start;
        private long end;

        public ComputationRange(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }

}
