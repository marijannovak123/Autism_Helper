package com.marijannovak.autismhelper.modules.child

import android.arch.lifecycle.Observer
import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.marijannovak.autismhelper.modules.child.adapters.QuizPagerAdapter
import com.marijannovak.autismhelper.modules.child.mvvm.QuizViewModel
import com.marijannovak.autismhelper.utils.DialogHelper
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.activity_quiz.*
import org.jetbrains.anko.toast

class QuizActivity : ViewModelActivity<QuizViewModel, Any>() {

    private var quizAdapter: QuizPagerAdapter? = null
    //todo: to viewmodel
    private var child: Child? = null
    private var startTime: Long
    private var mistakes = 0
    private val soundPool: SoundPool
    private var soundPoolLoaded = false
    private var sounds: Map<String, Int>? = null

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
            val categoryId = intent.getIntExtra(EXTRA_CATEGORY_ID, -1)
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

    override fun handleResource(categories: Resource<List<Any>>?) {
        categories?.let {
            showLoading(it.status, it.message)
            when (it.status) {
                Status.SUCCESS -> {
                    val questions = (it.data!![0] as CategoryQuestionsAnswersJoin).questionsAnswers
                    setUpQuestionsPager(questions)
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

    private fun setUpQuestionsPager(questionsWithAnswers: List<QuestionAnswersJoin>) {
        if (quizAdapter == null) {
            supportActionBar?.title = "${getString(R.string.question)} 1"
            startTime = System.currentTimeMillis()
            quizAdapter = QuizPagerAdapter(this, emptyList(),
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

    private fun playSound(isCorrect: Boolean) {
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
                    DialogHelper.showPromptDialog(this, getString(R.string.close_quiz), {
                        saveScore()
                    })
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
