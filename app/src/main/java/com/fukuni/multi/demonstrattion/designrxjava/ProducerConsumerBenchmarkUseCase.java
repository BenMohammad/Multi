package com.fukuni.multi.demonstrattion.designrxjava;

import io.reactivex.Observable;

public class ProducerConsumerBenchmarkUseCase {

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
    private final MyBlockingQueue mBlockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);
    private int mNumOfFinishedConsumers;
    private int mNumOfReceivedMessages;
    private long mStartTimestamp;

    public Observable<Result> startBenchMark() {
        return Observable.fromCallable(
                () -> {
                    synchronized (LOCK) {
                        mNumOfReceivedMessages = 0;
                        mNumOfFinishedConsumers = 0;
                        mStartTimestamp = System.currentTimeMillis();
                    }

                    new Thread(() -> {
                        for(int i = 0; i < NUM_OF_MESSAGES; i++) {
                            startNewProducer(i);
                        }
                    }).start();


                    new Thread(() -> {

                        for(int i = 0; i < NUM_OF_MESSAGES; i++) {
                            startNewConsumer();
                        }

                    }).start();

                    synchronized (LOCK) {
                        while(mNumOfFinishedConsumers < NUM_OF_MESSAGES) {
                            try {
                                LOCK.wait();
                            } catch (InterruptedException e) {
                                return null;
                            }
                        }
                    }

                    return new Result(
                            System.currentTimeMillis() - mStartTimestamp,
                            mNumOfReceivedMessages
                    );
                }
            );
        }

    private void  startNewProducer(final int index) {
        new Thread(() -> mBlockingQueue.put(index)).start();
    }

    private void startNewConsumer() {
        new Thread(() -> {
            int message = mBlockingQueue.take();
            synchronized (LOCK) {
                if(message != -1) {
                    mNumOfReceivedMessages++;
                }
                mNumOfFinishedConsumers++;
                LOCK.notifyAll();

            }
        }).start();
    }

}
