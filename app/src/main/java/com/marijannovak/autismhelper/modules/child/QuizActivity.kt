package com.marijannovak.autismhelper.modules.child

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.config.Constants.Companion.KEY_CATEGORY_ID
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import com.marijannovak.autismhelper.modules.child.mvvm.QuizViewModel
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.logTag

class QuizActivity : ViewModelActivity<QuizViewModel, CategoryQuestionsAnswersJoin>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        if(intent.hasExtra(KEY_CATEGORY_ID)) {
            val categoryId = intent.getIntExtra(KEY_CATEGORY_ID, -1)

            if(categoryId != -1) {
                viewModel.loadCategoryData(categoryId)
            }
        } else {
            showError(R.string.category_load_fail, null)
        }
    }

    override fun handleResource(categories: Resource<List<CategoryQuestionsAnswersJoin>>?) {
        categories?.let {
            when(it.status) {
                Status.SUCCESS -> {
                    val category = it.data!![0]
                    Log.e(logTag(), "${category.category} ${category.questionsAnswers}")
                }

                Status.MESSAGE -> {
                    showError(0, it.message)
                }

                else -> {

                }
            }
        }
    }

    override fun subscribeToData() {
        viewModel.resourceLiveData.observe(this, Observer { categories -> handleResource(categories) })
    }
}
