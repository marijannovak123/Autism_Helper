package com.marijannovak.autismhelper.modules.child.fragments

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.view.*
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.config.Constants.Companion.EXTRA_QUESTION_NO
import com.marijannovak.autismhelper.modules.child.QuizActivity
import com.marijannovak.autismhelper.modules.child.adapters.MathShapeAdapter
import kotlinx.android.synthetic.main.fragment_math.*
import org.jetbrains.anko.support.v4.toast

class MathFragment : BaseFragment() {

    private var questionNo = 1
    private var mathShapeAdapter: MathShapeAdapter? = null
    private var act: QuizActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val extraInt = it.getInt(EXTRA_QUESTION_NO)
            if(extraInt > 0) {
                questionNo = extraInt
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_math, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.act = activity as QuizActivity
    }

    override fun onStart() {
        super.onStart()
        mathShapeAdapter = MathShapeAdapter()
        rvMath.adapter = mathShapeAdapter
        rvMath.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 6)

        btnSubmitCount.setOnClickListener {
            val answer = etCount.text.toString().toInt()
            val rightAnswer = mathShapeAdapter!!.pickedElements
            if(answer == rightAnswer) {
                nextQuestion()
            } else {
                act?.playSound(false)
                act!!.mistakes++
                toast(R.string.fail)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        tvMathQuestion.text = String.format(getString(R.string.math_question_format), getString(mathShapeAdapter!!.pickedElementStringRes()))
        act?.supportActionBar?.title = "${getString(R.string.question)} $questionNo"
    }

    private fun nextQuestion() {
        etCount.text.clear()
        act?.playSound(true)
        mathShapeAdapter = MathShapeAdapter()
        rvMath.adapter = mathShapeAdapter
        tvMathQuestion.text = String.format(getString(R.string.math_question_format), getString(mathShapeAdapter!!.pickedElementStringRes()))
        questionNo++
        act?.supportActionBar?.title = "${getString(R.string.question)} $questionNo"
    }

    companion object {
        @JvmStatic
        fun newInstance(questionNo: Int) =
                MathFragment().apply {
                    arguments = Bundle().apply {
                        putInt(EXTRA_QUESTION_NO, questionNo)
                    }
                }
    }
}
