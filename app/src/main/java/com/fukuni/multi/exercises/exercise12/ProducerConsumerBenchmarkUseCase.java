package com.fukuni.multi.exercises.exercise12;

import com.fukuni.multi.common.BaseObservable;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

public class ProducerConsumerBenchmarkUseCase extends BaseObservable<ProducerConsumerBenchmarkUseCase.Listener> {

    public interface Listener {
        void onBenchmarkCompleted(Result result);

    }

    public static class Result {
        private final long mExecutionTime;
        private final int mNumOfReceivedMessages;

        public Result(long executionTime, int numOfReceivedMessages) {
            this.mExecutionTime = executionTime;
            this.mNumOfReceivedMessages = numOfReceivedMessages;
        }

        public long getExecutionTime() {
            return mExecutionTime;
        }

        public int getNumOfReceivedMessages() {
            return mNumOfReceivedMessages;
        }
    }

    private static final int NUM_OF_MESSAGES = 1000;
    private static final int BLOCKING_QUEUE_CAPACITY = 5;
    private final Object LOCK = new Object();
    private final UiThreadPoster uiThreadPoster = new UiThreadPoster();
    private final BackgroundThreadPoster backgroundThreadPoster = new BackgroundThreadPoster();
    private final MyBlockingQueue mBlockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);
    private int mNumOfFinishedConsumers;
    private int mNUmOfReceivedMessages;

    public void startBenchmarkAndNotify() {
        backgroundThreadPoster.post(() -> {
            mNUmOfReceivedMessages = 0;
            mNumOfFinishedConsumers = 0;

            long startTimesatmp = System.currentTimeMillis();

            backgroundThreadPoster.post(() -> {
                for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                    startNewProducer(i);
                }
            });


            backgroundThreadPoster.post(() -> {
                for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                    startNewConsumer();
                }
            });

            waitForAllConsumersToFinish();

            Result result;
            synchronized (LOCK) {

                result = new Result(
                        System.currentTimeMillis() - startTimesatmp,
                        mNUmOfReceivedMessages
                );

            }

            notifySuccess(result);

        });

    }


    private void waitForAllConsumersToFinish() {
        synchronized (LOCK) {
            while(mNumOfFinishedConsumers < NUM_OF_MESSAGES) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private void startNewProducer(final int index) {
        backgroundThreadPoster.post(() -> mBlockingQueue.put(index));
    }

    private void startNewConsumer() {
        backgroundThreadPoster.post(() -> {
            int message = mBlockingQueue.take();
            synchronized (LOCK) {
                if(message != -1) {
                    mNUmOfReceivedMessages++;
                }

                mNumOfFinishedConsumers++;
                LOCK.notifyAll();

            }


        });
    }


    private void notifySuccess(Result result) {
        uiThreadPoster.post(() -> {
            for(Listener listener : getListener()) {
                listener.onBenchmarkCompleted(result);
            }
        });
    }


}
