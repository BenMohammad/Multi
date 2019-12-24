package com.fukuni.multi.exercises.exercise10

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fukuni.multi.R
import com.fukuni.multi.common.BaseFragment
import kotlinx.coroutines.*
import java.math.BigInteger

class Exercise10Fragment : BaseFragment() {

    private lateinit var edtArgument : EditText
    private lateinit var edtTimeout : EditText
    private lateinit var txtResult : TextView
    private lateinit var btnStartWork : Button
    private lateinit var computeFactorialUseCase: ComputeFactorialUseCase

    private var job : Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        computeFactorialUseCase = ComputeFactorialUseCase()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_exercise_10, container, false)

        view.apply {
            edtArgument = findViewById(R.id.edt_argument)
            edtTimeout = findViewById(R.id.edt_timeout)
            txtResult = findViewById(R.id.txt_result)
            btnStartWork = findViewById(R.id.btn_compute)

        }

        btnStartWork.setOnClickListener {
            if(edtArgument.text.toString().isEmpty()) {
                return@setOnClickListener
            }

            txtResult.text = ""
            btnStartWork.isEnabled = false

            val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(btnStartWork.windowToken, 0)
            val argument = Integer.valueOf(edtArgument.text.toString())

            job = CoroutineScope(Dispatchers.Main).launch {
                when(val result = computeFactorialUseCase.computeFactorial(argument, getTimeout())) {
                    is ComputeFactorialUseCase.Result.Success -> onFactorialComputed(result.result)
                    is ComputeFactorialUseCase.Result.Timeout -> onFactorialComputedTimeout();
                }
            }

        }

        return view;
    }

    override fun onStop() {
        super.onStop()
        job?.apply { cancel() }
    }

    fun onFactorialComputedTimeout() {
        txtResult.text = "Timeout"
        btnStartWork.isEnabled = true
    }

    fun onFactorialComputed(result: BigInteger)  {
        txtResult.text = result.toString()
        btnStartWork.isEnabled = true
    }


    private fun getTimeout(): Int {
        var timeout : Int
        if(edtTimeout.text.toString().isEmpty()) {
            timeout = MAX_TIMEOUT_MS
        } else {
            timeout = Integer.valueOf(edtTimeout.text.toString())
            if(timeout > MAX_TIMEOUT_MS) {
                timeout = MAX_TIMEOUT_MS
            }
        }
        return timeout

    }

    override fun getScreenTitle(): String {
        return "Exercise 10"
    }

    companion object {
        private const val MAX_TIMEOUT_MS = 1000

        fun newInstance() : Fragment {
            return Exercise10Fragment()
        }
    }

}