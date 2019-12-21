package com.fukuni.multi.exercises.exercise7;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.WorkerThread;

import com.fukuni.multi.common.BaseObservable;

import java.math.BigInteger;
import java.util.concurrent.ThreadPoolExecutor;

public class ComputeFactorialUseCase extends BaseObservable<ComputeFactorialUseCase.Listener> {

    public interface Listener {
        void onFactorialComputed(BigInteger result);
        void onFactorialComputationTimeout();
        void onFactorialComputationAborted();
    }

    private  final Object LOCK = new Object();
    private final Handler mUiHandler;
    private final ThreadPoolExecutor mThreadPoolExecutor;
    private int mNumOfThreads = 0;
    private ComputationRange[] mThreadsComputationRanges;
    private volatile BigInteger[] mThreadsComputationResults;
    private int mNumOfFinishedThreads;
    private long mComputationTimeoutTime;
    private boolean mAbortComputation;


    public ComputeFactorialUseCase(Handler mUiHandler, ThreadPoolExecutor threadPoolExecutor) {
        this.mUiHandler = mUiHandler;
        this.mThreadPoolExecutor = threadPoolExecutor;
    }

    @Override
    protected void onLastListenerUnregistered() {
        super.onLastListenerUnregistered();
        synchronized (LOCK) {
            mAbortComputation = true;
            LOCK.notifyAll();
        }
    }

    public void computeFactorialAndNotify(final int argument, final int timeout) {
        mThreadPoolExecutor.execute(() -> {
            initComputationParams(argument, timeout);
            startComputation();
            waitForThreadResultsOrTimeoutOrAbort();
            processComputationResult();
        });

    }
    @WorkerThread
    private void processComputationResult() {
        if(mAbortComputation) {
            notifyAbort();
            return;
        }

        BigInteger result = computeFinalResult();

        if(isTimeout()) {
            notifyTimeout();
            return;
        }

        notifySuccess(result);

    }

    private void notifySuccess(final BigInteger result) {
        mUiHandler.post(() -> {
            for(Listener listener : getListener()) {
                listener.onFactorialComputed(result);
            }
        });
    }

    private void notifyTimeout() {
        mUiHandler.post(() -> {
            for(Listener listener : getListener()) {
                listener.onFactorialComputationTimeout();
            }
        });
    }

    @WorkerThread
    private BigInteger computeFinalResult() {
        BigInteger result = new BigInteger("1");
        for(int i = 0; i < mNumOfThreads; i++) {
            if(isTimeout()) {
                break;
            }
            result = result.multiply(mThreadsComputationResults[i]);
        }

        return result;
    }

    private void notifyAbort() {
        mUiHandler.post(() -> {
            for(Listener listener : getListener()) {
                listener.onFactorialComputationAborted();
            }
        });
    }

    private void waitForThreadResultsOrTimeoutOrAbort() {
        synchronized (LOCK) {
            while(mNumOfFinishedThreads != mNumOfThreads && !mAbortComputation && !isTimeout()) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    @WorkerThread
    private void startComputation() {
        for(int i = 0; i < mNumOfThreads; i++) {
            final int threadIndex = i;

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

    private void initComputationParams(int argument, int timeout) {
        mNumOfThreads = argument < 20 ? 1 : Runtime.getRuntime().availableProcessors();
        synchronized (LOCK) {
            mNumOfFinishedThreads = 0;
            mAbortComputation = false;
        }

        mThreadsComputationResults = new BigInteger[mNumOfThreads];
        mThreadsComputationRanges = new ComputationRange[mNumOfThreads];
        initThreadsComputationRanges(argument);
        mComputationTimeoutTime = System.currentTimeMillis() + timeout;

    }

    private void initThreadsComputationRanges(int argument) {
        int computationRangeSize = argument / mNumOfThreads;

        long nextComputationRangeEnd = argument;
        for(int i = mNumOfThreads - 1; i >= 0; i--) {
            mThreadsComputationRanges[i] = new ComputationRange(
                    nextComputationRangeEnd - computationRangeSize + 1,
                    nextComputationRangeEnd
            );

            nextComputationRangeEnd = mThreadsComputationRanges[i].start;
        }

        mThreadsComputationRanges[0].start = 1;

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
