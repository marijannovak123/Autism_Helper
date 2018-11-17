package com.marijannovak.autismhelper.ui.activities

import androidx.lifecycle.Observer
import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.config.Constants.Companion.CORRECT
import com.marijannovak.autismhelper.config.Constants.Companion.EXTRA_CATEGORY_ID
import com.marijannovak.autismhelper.config.Constants.Companion.EXTRA_CHILD
import com.marijannovak.autismhelper.config.Constants.Companion.FALSE
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.models.QuestionAnswersJoin
import com.marijannovak.autismhelper.adapter.QuizPagerAdapter
import com.marijannovak.autismhelper.ui.fragments.MathFragment
import com.marijannovak.autismhelper.viewmodels.QuizViewModel
import com.marijannovak.autismhelper.utils.DialogHelper
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.logTag
import kotlinx.android.synthetic.main.activity_quiz.*
import org.jetbrains.anko.toast

class QuizActivity : ViewModelActivity<QuizViewModel, CategoryQuestionsAnswersJoin>() {

    private var quizAdapter: QuizPagerAdapter? = null
    private var child: Child? = null
    private val soundPool: SoundPool
    private var soundPoolLoaded = false
    private var sounds: Map<String, Int>? = null
    private var categoryId = -1

    private var startTime: Long
    var mistakes = 0

    init {
        startTime = System.currentTimeMillis()
        soundPool = SoundPool.Builder().setMaxStreams(1).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        soundPool.setOnLoadCompleteListener { _, _, _  -> soundPoolLoaded = true }
        sounds = mapOf(
                Pair(CORRECT, soundPool.load(this, R.raw.sound_correct, 1)),
                Pair(FALSE, soundPool.load(this, R.raw.sound_fail, 2))
        )

        if (intent.hasExtra(EXTRA_CATEGORY_ID)) {
            categoryId = intent.getIntExtra(EXTRA_CATEGORY_ID, -1)
            if (categoryId != -1) {
                viewModel.loadCategoryData(categoryId)
            }
        } else {
            showMessage(R.string.category_load_fail, null)
            finish()
        }

        if (intent.hasExtra(EXTRA_CHILD)) {
            child = intent.getSerializableExtra(EXTRA_CHILD) as Child
        } else {
            showMessage(R.string.children_load_fail, null)
            finish()
        }
    }

    override fun handleResource(categories: Resource<CategoryQuestionsAnswersJoin>?) {
        categories?.let {
            handleLoading(it.status, it.message)
            when (it.status) {
                Status.SUCCESS -> {
                    startTime = System.currentTimeMillis()
                    if(it.data!!.category.id == 0) {
                        setUpMathFragment()
                    } else {
                        val questions = it.data.questionsAnswers
                        setUpQuestionsPager(questions)
                    }
                }

                Status.MESSAGE -> {
                    showMessage(0, it.message)
                }

                Status.SAVED -> {
                    val intent = Intent(this, PickCategoryActivity::class.java)
                    intent.putExtra(EXTRA_CHILD, child)
                    startActivity(intent)
                    finish()
                }

                else -> {
                    //NOOP
                }
            }
        }
    }

    private fun setUpMathFragment() {
        vpQuestions.visibility = View.GONE
        llContainer.visibility = View.VISIBLE

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.llContainer, MathFragment.newInstance(1))
        transaction.addToBackStack(MathFragment.logTag())
        transaction.commit()
    }
    private fun setUpQuestionsPager(questionsWithAnswers: List<QuestionAnswersJoin>) {
        vpQuestions.visibility = View.VISIBLE
        llContainer.visibility = View.GONE
        if (quizAdapter == null) {
            supportActionBar?.title = "${getString(R.string.question)} 1"
            quizAdapter = QuizPagerAdapter(this, categoryId, emptyList(),
                    onItemClick = {
                        if (it) {
                            playSound(true)
                            val currentPos = vpQuestions.currentItem
                            val max = quizAdapter!!.dataSetSize()

                            if (max - 1 > currentPos) {
                                vpQuestions.currentItem = currentPos + 1
                                supportActionBar?.title = "${getString(R.string.question)} ${vpQuestions.currentItem}"
                            } else {
                                saveScore()
                            }
                        } else {
                            playSound(false)
                            mistakes += 1
                            toast(R.string.fail)
                        }
                    }
            )

            vpQuestions.adapter = quizAdapter
        }

        quizAdapter!!.updateDataSet(questionsWithAnswers)
    }

     fun playSound(isCorrect: Boolean) {
        if(viewModel.isSoundOn()) {
            sounds?.let {
                val toPlay = if(isCorrect) it[CORRECT] else it[FALSE]
                soundPool.play(toPlay!!, 1f, 1f, 1, 0, 1f)
            }
        }
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
                    DialogHelper.showPromptDialog(this, getString(R.string.close_quiz)) {
                        saveScore()
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveScore() {
        toast(R.string.quiz_finished)
        val timestamp = System.currentTimeMillis()
        val score = ChildScore(0, child!!.id, child!!.parentId, timestamp, timestamp - startTime, mistakes)
        viewModel.saveChildScore(score)
    }

    override fun subscribeToData() {
        viewModel.resourceLiveData.observe(this, Observer { categories -> handleResource(categories) })
    }
}
