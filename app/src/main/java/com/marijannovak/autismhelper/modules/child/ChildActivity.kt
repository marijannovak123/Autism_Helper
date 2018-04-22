package com.marijannovak.autismhelper.modules.child

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.data.models.Answer
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import com.marijannovak.autismhelper.data.models.QuestionAnswersJoin
import com.marijannovak.autismhelper.modules.child.mvvm.ChildViewModel
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.logTag

class ChildActivity : ViewModelActivity<ChildViewModel, CategoryQuestionsAnswersJoin>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child)
    }

    override fun subscribeToData() {
        viewModel.resourceLiveData.observe(this, Observer { handleResource(it)})
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCategories()
    }

    override fun handleResource(resource: Resource<List<CategoryQuestionsAnswersJoin>>?) {
        resource?.let {
            when(it.status) {
                Status.SUCCESS -> {
                    showLoading(false)
                    setCategoriesData(it.data)
                }

                Status.HOME -> {
                    startActivity(Intent(this@ChildActivity, LoginActivity::class.java))
                    finish()
                }

                Status.LOADING -> {
                    showLoading(true)
                }

                Status.MESSAGE -> {
                    showLoading(false)
                    showError(0, it.message)
                }

                else -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun setCategoriesData(categoryQuestionAnswersJoins: List<CategoryQuestionsAnswersJoin>?) {
        categoryQuestionAnswersJoins?.let {
            Log.e(logTag(), "\n\n-------------------------------------\n\n")
            for(cqa: CategoryQuestionsAnswersJoin in categoryQuestionAnswersJoins) {
                Log.e(logTag(), "\nCategory: ${cqa.category}\n")
                for(questionAnswers: QuestionAnswersJoin in cqa.questionsAnswers) {
                    Log.e(logTag(), "\tQuestion: ${questionAnswers.question}")
                    for (answer: Answer in questionAnswers.answers) {
                        Log.e(logTag(), "\t\tAnswer: $answer")
                    }
                }
            }
            Log.e(logTag(), "\n\n----------------------------------\n\n")
        }
    }
}
