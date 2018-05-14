package com.marijannovak.autismhelper.modules.child

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.config.Constants.Companion.EXTRA_CATEGORY_ID
import com.marijannovak.autismhelper.config.Constants.Companion.EXTRA_CHILD
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.models.QuestionAnswersJoin
import com.marijannovak.autismhelper.modules.child.adapters.QuizPagerAdapter
import com.marijannovak.autismhelper.modules.child.mvvm.QuizViewModel
import com.marijannovak.autismhelper.utils.DialogHelper
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.activity_quiz.*
import org.jetbrains.anko.toast

class QuizActivity : ViewModelActivity<QuizViewModel, Any>() {
    private var quizAdapter: QuizPagerAdapter? = null
    private var child: Child? = null
    private var startTime: Long = System.currentTimeMillis()
    private var mistakes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        if (intent.hasExtra(EXTRA_CATEGORY_ID)) {
            val categoryId = intent.getIntExtra(EXTRA_CATEGORY_ID, -1)
            if (categoryId != -1) {
                viewModel.loadCategoryData(categoryId)
            }
        } else {
            showError(R.string.category_load_fail, null)
            finish()
        }

        if (intent.hasExtra(EXTRA_CHILD)) {
            child = intent.getSerializableExtra(EXTRA_CHILD) as Child
        } else {
            showError(R.string.children_load_fail, null)
            finish()
        }
    }

    override fun handleResource(categories: Resource<List<Any>>?) {
        categories?.let {
            showLoading(it.status)
            when (it.status) {
                Status.SUCCESS -> {
                    val questions = (it.data!![0] as CategoryQuestionsAnswersJoin).questionsAnswers
                    setUpQuestionsPager(questions)
                }

                Status.MESSAGE -> {
                    showError(0, it.message)
                }

                Status.SAVED -> {
                    val intent = Intent(this, PickCategoryActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                else -> {
                    //NOOP
                }
            }
        }
    }

    private fun setUpQuestionsPager(questionsWithAnswers: List<QuestionAnswersJoin>) {
        if (quizAdapter == null) {
            supportActionBar?.title = "${getString(R.string.question)} 1"
            startTime = System.currentTimeMillis()
            quizAdapter = QuizPagerAdapter(this, emptyList(),
                    onItemClick = {
                        if (it) {
                            val currentPos = vpQuestions.currentItem
                            val max = quizAdapter!!.dataSetSize()

                            if (max - 1 > currentPos) {
                                vpQuestions.currentItem = currentPos + 1
                                supportActionBar?.title = "${getString(R.string.question)} ${vpQuestions.currentItem}"
                            } else {
                                saveScore()
                            }
                        } else {
                            mistakes += 1
                            toast("False")
                        }
                    }
            )

            vpQuestions.adapter = quizAdapter
        }

        quizAdapter!!.updateDataSet(questionsWithAnswers)
    }

    override fun onBackPressed() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_quiz, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(it.itemId) {
                R.id.action_exit_quiz -> {
                    DialogHelper.showPromptDialog(this, getString(R.string.close_quiz), {
                        saveScore()
                    })
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveScore() {
        toast("Quiz finished")
        val timestamp = System.currentTimeMillis()
        val score = ChildScore(0, child!!.id, child!!.parentId, timestamp, timestamp - startTime, mistakes)
        viewModel.saveChildScore(score)
    }

    override fun subscribeToData() {
        viewModel.resourceLiveData.observe(this, Observer { categories -> handleResource(categories) })
    }
}
