package com.fukuni.multi.exercises.exercise11;

import androidx.annotation.WorkerThread;

import com.fukuni.multi.common.BaseObservable;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import java.math.BigInteger;

public class ComputeFactorialUseCase extends BaseObservable<ComputeFactorialUseCase.Listener> {

    public interface Listener {
        void onFactorialComputed(BigInteger result);
        void onComputationTimeout();
    }

    private final Object LOCK = new Object();
    private final BackgroundThreadPoster backgroundThreadPoster;
    private final UiThreadPoster uiThreadPoster;
    private BigInteger[] rangeComputationResults;
    private int numOfFinishedThreads = 1;
    private long computationTimeout;
    private boolean abortComputation;

    public ComputeFactorialUseCase(UiThreadPoster uiThreadPoster, BackgroundThreadPoster backgroundThreadPoster) {
        this.backgroundThreadPoster = backgroundThreadPoster;
        this.uiThreadPoster = uiThreadPoster;
    }

    @Override
    protected void onLastListenerUnregistered() {
        super.onLastListenerUnregistered();
        synchronized (LOCK) {
            abortComputation = true;
            LOCK.notifyAll();
        }
    }

    public void computeFactorialAndNotify(final int argument, final int timeout) {
        backgroundThreadPoster.post(() -> {
            synchronized (LOCK) {
                numOfFinishedThreads = 0;
                abortComputation = false;
                computationTimeout = System.currentTimeMillis() + timeout;
            }


            ComputationRange[] computationRanges = getComputationRanges(argument);
            startComputation(computationRanges);
            waitForResultOrTimeoutOrAbort();
            processComputationResults();
        });
    }

    @WorkerThread
    private void processComputationResults() {
        if(isAborted()) {
            return;
        }

        BigInteger result = computeFinalResult();

        if(isTimeout()) {
            notifyTimeout();
            return;
        }

        notifySuccess(result);

    }

    private void notifySuccess(BigInteger result) {
        uiThreadPoster.post(() -> {
            for(Listener listener : getListener()) {
                listener.onFactorialComputed(result);
            }
        });
    }

    private void notifyTimeout() {
        uiThreadPoster.post(()-> {
            for(Listener listener : getListener()) {
                listener.onComputationTimeout();
            }
        });
    }

    @WorkerThread
    private BigInteger computeFinalResult() {
        synchronized (LOCK) {
            BigInteger result = new BigInteger("1");
            for(BigInteger partialResult : rangeComputationResults) {
                if(isTimeout()) {
                    break;
                }

                result = result.multiply(partialResult);

            }

            return result;

        }
    }

    private boolean isAborted() {
        synchronized (LOCK) {
            return abortComputation;
        }
    }

    @WorkerThread
    private void waitForResultOrTimeoutOrAbort() {
        synchronized (LOCK) {
            while(!isCompleted() && !abortComputation && !isTimeout()) {
                try {
                    LOCK.wait(getRemainingMillis());
                } catch (InterruptedException e) {
                    return;
                }

            }

        }
    }

    private long getRemainingMillis() {
        return computationTimeout - System.currentTimeMillis();
    }

    private boolean isCompleted() {
        synchronized (LOCK) {
            return numOfFinishedThreads == rangeComputationResults.length;
        }
    }

    @WorkerThread
    private void startComputation(ComputationRange[] computationRanges) {
        rangeComputationResults = new BigInteger[computationRanges.length];

        for(int i = 0; i < computationRanges.length; i++) {
            startRangeComputation(computationRanges[i], i);
        }

    }

    private void startRangeComputation(ComputationRange computationRange, int index) {
        backgroundThreadPoster.post(() -> {
            long rangeStart = computationRange.start;
            long rangeEnd = computationRange.end;

            BigInteger product = new BigInteger("1");
            for(long num = rangeStart; num <= rangeEnd; num++)  {
                if(isTimeout()) {
                    break;
                }
                product = product.multiply(new BigInteger(String.valueOf(num)));
            }

            synchronized (LOCK) {
                rangeComputationResults[index] = product;
                numOfFinishedThreads++;
                LOCK.notifyAll();
            }

        });
    }

    private ComputationRange[] getComputationRanges(int argument) {
        int numberOfThreads = argument < 20 ? 1 : Runtime.getRuntime().availableProcessors();

        ComputationRange[] computationRanges = new ComputationRange[numberOfThreads];
        int computationRangeSize = argument / numberOfThreads;

        long nextComputationRangeEnd = argument;
        for(int i = numberOfThreads - 1; i >= 0; i--) {
            computationRanges[i] = new ComputationRange(
                    nextComputationRangeEnd - computationRangeSize +1,
                    nextComputationRangeEnd
            );

            nextComputationRangeEnd = computationRanges[i].start - 1;
        }

        computationRanges[0] = new ComputationRange(1, computationRanges[0].end);

        return computationRanges;
    }

    private boolean isTimeout() {
        return System.currentTimeMillis() >= computationTimeout;
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
