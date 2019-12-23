package com.fukuni.multi.demonstrattion.designcoroutines

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fukuni.multi.R
import com.fukuni.multi.common.BaseFragment

class DesignWithCoroutinesDemoFragment : BaseFragment(), ProducerConsumerBenchmarkUseCase.Listener {


    private lateinit var btnStart : Button
    private lateinit var progress : ProgressBar
    private lateinit var txtReceivedMessagesCount : TextView
    private lateinit var txtExecutionTime : TextView

    private lateinit var producerConsumerBenchmarkUseCase: ProducerConsumerBenchmarkUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        producerConsumerBenchmarkUseCase = ProducerConsumerBenchmarkUseCase()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_design_with_coroutines, container, false)

        progress = view.findViewById(R.id.progress)
        txtReceivedMessagesCount = view.findViewById(R.id.txt_received_message_count)
        txtExecutionTime = view.findViewById(R.id.txt_execution_time)
        btnStart = view.findViewById(R.id.btn_start)

        btnStart.setOnClickListener {
            btnStart.isEnabled = false
            txtReceivedMessagesCount.text = ""
            txtExecutionTime.text = ""
            progress.visibility = View.VISIBLE

            producerConsumerBenchmarkUseCase.startBenchmarkAndNotify()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        producerConsumerBenchmarkUseCase.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        producerConsumerBenchmarkUseCase.unregisterListener(this)
    }


    override fun getScreenTitle(): String {
        return "Coroutine Demo"
    }


    override fun onBenchmarkCompleted(result: ProducerConsumerBenchmarkUseCase.Result) {
        progress.visibility = View.INVISIBLE
        txtReceivedMessagesCount.text = "Messages: ${result.numOfReceivedMessages}"
        txtExecutionTime.text = "ExecutionTime: ${result.executionTime}"
        btnStart.isEnabled = true

    }


    companion object {
        fun newInstance() : Fragment {
            return DesignWithCoroutinesDemoFragment()
        }
    }

}