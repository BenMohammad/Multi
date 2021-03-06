package com.fukuni.multi.exercises.exercise5;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fukuni.multi.R;
import com.fukuni.multi.common.BaseFragment;

import java.util.LinkedList;
import java.util.Queue;

public class Exercise5Fragment extends BaseFragment {

    public static Fragment newInstance() {
        return new Exercise5Fragment();
    }

    private static final int NUM_OF_MESSAGES = 1000;
    private static final int BLOCKING_QUEUE_CAPACITY = 5;

    private final Object LOCK = new Object();
    private Button mBtnStart;
    private ProgressBar mProgress;
    private TextView mTxtReceivedMessages;
    private TextView mTxtExecutionTime;

    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private final MyBlockingQueue mBlockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);
    private int mNumOfFinishedConsumers;
    private int mNumOfReceivedMessages;
    private long mStartTimestamp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_5, container, false);
        mBtnStart = view.findViewById(R.id.btn_start);
        mProgress = view.findViewById(R.id.progress);
        mTxtReceivedMessages = view.findViewById(R.id.txt_received_message_count);
        mTxtExecutionTime = view.findViewById(R.id.txt_execution_time);
        mBtnStart.setOnClickListener(v -> {
            mBtnStart.setEnabled(false);
            mTxtReceivedMessages.setText("");
            mTxtExecutionTime.setText("");
            mProgress.setVisibility(View.VISIBLE);

            mNumOfReceivedMessages = 0;
            mNumOfFinishedConsumers = 0;

            startComputation();
        });

        return view;


    }

    private void startComputation() {
        mStartTimestamp = System.currentTimeMillis();

        new Thread(() -> {
            synchronized (LOCK) {
                while(mNumOfFinishedConsumers < NUM_OF_MESSAGES) {
                    try {
                        LOCK.wait();
                    } catch(InterruptedException e) {
                        return;
                    }
                }
            }

            showResults();

        }).start();

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

    }

    private void startNewConsumer() {
        new Thread(() -> {
            int message = mBlockingQueue.take();
            synchronized (LOCK) {
                if(message != -1) {
                    mNumOfReceivedMessages++;
                }
                mNumOfFinishedConsumers++;
                LOCK.notify();

            }
        }).start();
    }

    private void startNewProducer(final int index) {

        new Thread(() -> mBlockingQueue.put(index)).start();

    }




    private void showResults() {
        mUiHandler.post(() -> {
            mProgress.setVisibility(View.GONE);
            mBtnStart.setEnabled(true);
            synchronized (LOCK) {
                mTxtReceivedMessages.setText("Received messages: " + mNumOfReceivedMessages);
            }

            long executionTimeMs = System.currentTimeMillis() - mStartTimestamp;
            mTxtExecutionTime.setText("ExecutionTime: " + executionTimeMs + "ms");

        });
    }

    @Override
    protected String getScreenTitle() {
        return "Exercise 5";
    }


    private static class MyBlockingQueue {
        private final Object QUEUE_LOCK = new Object();
        private final int mCapacity;
        private final Queue<Integer> mQueue = new LinkedList<>();
        private int mCurrentSize = 0;

        public MyBlockingQueue(int capacity) {
            this.mCapacity = capacity;
        }

        public void put(int number) {
            synchronized (QUEUE_LOCK) {
                while(mCurrentSize >= mCapacity) {
                    try {
                        QUEUE_LOCK.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                mQueue.offer(number);
                mCurrentSize++;
                QUEUE_LOCK.notifyAll();

            }
        }

        public int take() {
            synchronized (QUEUE_LOCK) {
                while(mCurrentSize <= 0) {
                    try {
                        QUEUE_LOCK.wait();
                    } catch(InterruptedException e) {
                        return 0;
                    }
                }

                mCurrentSize--;
                QUEUE_LOCK.notifyAll();
                return mQueue.poll();
            }
        }

    }

}
