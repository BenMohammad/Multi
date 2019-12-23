package com.fukuni.multi.demonstrattion.designrxjava;

import android.os.Bundle;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DesignWithRxJavaDemonstrationFragment extends BaseFragment {

    public static Fragment newInstance() {
        return new DesignWithRxJavaDemonstrationFragment();
    }

    private Button mBtnStart;
    private ProgressBar mProgress;
    private TextView mTxtReceivedMessages;
    private TextView mTxtExecutionTime;

    private ProducerConsumerBenchmarkUseCase mProducerConsumerBenchmarkUseCase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProducerConsumerBenchmarkUseCase = new ProducerConsumerBenchmarkUseCase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_design_with_thread_demonstration, container, false);
        mTxtExecutionTime = view.findViewById(R.id.txt_execution_time);
        mTxtReceivedMessages = view.findViewById(R.id.txt_received_message_count);
        mProgress = view.findViewById(R.id.progress);
        mBtnStart = view.findViewById(R.id.btn_start);

        mBtnStart.setOnClickListener(v -> {
            mBtnStart.setEnabled(false);
            mTxtReceivedMessages.setText("");
            mTxtExecutionTime.setText("");
            mProgress.setVisibility(View.VISIBLE);

            mProducerConsumerBenchmarkUseCase.startBenchMark()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onBenchmarkCompleted);

        });

        return view;
    }

    @Override
    protected String getScreenTitle() {
        return "RxJava Design";
    }


    private void onBenchmarkCompleted(ProducerConsumerBenchmarkUseCase.Result result){
        mProgress.setVisibility(View.INVISIBLE);
        mTxtReceivedMessages.setText("Received Messages: " + result.getNumOfReceivedMessages());
        mTxtExecutionTime.setText("Execution Time: " + result.getExecutionTime());
        mBtnStart.setEnabled(true);
    }

}
