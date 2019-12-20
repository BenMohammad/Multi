package com.fukuni.multi.demonstrattion.designthreadpool;

import java.util.LinkedList;
import java.util.Queue;

class MyBlockingQueue {

    private final Object QUEUE_LOCK = new Object();

    private final int mCapacity;
    private final Queue<Integer> mQueue = new LinkedList<>();

    private int mCurrent = 0;

    public MyBlockingQueue(int capacity) {
        this.mCapacity = capacity;
    }

    public void put(int number) {
        synchronized (QUEUE_LOCK) {
            while (mCurrent >= mCapacity) {
                try {
                    QUEUE_LOCK.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }

            mQueue.offer(number);
            mCurrent++;
            QUEUE_LOCK.notifyAll();

        }
    }

    public int task() {
        synchronized (QUEUE_LOCK) {
            while (mCurrent <= 0) {
                try {
                    QUEUE_LOCK.wait();
                } catch (InterruptedException e) {
                    return 0;
                }

            }



        mCurrent--;
        QUEUE_LOCK.notifyAll();
        return mQueue.poll();


        }
    }
}
