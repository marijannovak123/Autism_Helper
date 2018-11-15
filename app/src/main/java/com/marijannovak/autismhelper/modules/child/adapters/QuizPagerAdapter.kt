package com.marijannovak.autismhelper.modules.child.adapters

import android.content.Context
import android.graphics.Color
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.data.models.Answer
import com.marijannovak.autismhelper.data.models.QuestionAnswersJoin
import kotlinx.android.synthetic.main.list_item_question_color.view.*
import kotlinx.android.synthetic.main.list_item_question_emotion.view.*

class QuizPagerAdapter(
        private val context: Context,
        private val categoryId: Int,
        private var questionsWithAnswers: List<QuestionAnswersJoin>,
        private val onItemClick: (Boolean) -> Unit
) : androidx.viewpager.widget.PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return when(categoryId) {
            1 -> instantiateColorQuestion(container, questionsWithAnswers[position])
            else -> instantiateEmotionQuestion(container, questionsWithAnswers[position])
        }
    }

    private fun instantiateEmotionQuestion(container: ViewGroup, questionAnswerJoin: QuestionAnswersJoin): View {
        val view: View =  LayoutInflater.from(context).inflate(R.layout.list_item_question_emotion, container, false)

        with(view) {
            with(questionAnswerJoin) {
                tvEmotionQuestion.text = question.text

                Glide.with(context)
                        .load(getEmotion(answers[0]))
                        .into(ivAnswer1)

                Glide.with(context)
                        .load(getEmotion(answers[1]))
                        .into(ivAnswer2)

                Glide.with(context)
                        .load(getEmotion(answers[2]))
                        .into(ivAnswer3)

                Glide.with(context)
                        .load(getEmotion(answers[3]))
                        .into(ivAnswer4)

                Glide.with(context)
                        .load(question.imgPath)
                        .into(ivEmotionImage)

                ivAnswer1.setOnClickListener { onItemClick(answers[0].isCorrect) }
                ivAnswer2.setOnClickListener { onItemClick(answers[1].isCorrect) }
                ivAnswer3.setOnClickListener { onItemClick(answers[2].isCorrect) }
                ivAnswer4.setOnClickListener { onItemClick(answers[3].isCorrect) }
            }
        }

        container.addView(view)
        return view
    }

    private fun getEmotion(answer: Answer): Int {
        return when(answer.text) {
            "Sad" -> R.drawable.sad
            "Scared", "Sleepy" -> R.drawable.scare
            "Surprised" -> R.drawable.shocked
            "Happy" -> R.drawable.happy
            "Excited" -> R.drawable.excited
            "Angry" -> R.drawable.angry
            else -> R.drawable.happy
        }
    }

    private fun instantiateColorQuestion(container: ViewGroup, questionWithAnswers: QuestionAnswersJoin): View {
        val view: View =  LayoutInflater.from(context).inflate(R.layout.list_item_question_color, container, false)

        with(view) {
            with(questionWithAnswers) {
                tvColorQuestion.text = question.text
                tvAnswer1.text = answers[0].text
                tvAnswer2.text = answers[1].text
                tvAnswer3.text = answers[2].text
                tvAnswer4.text = answers[3].text

                ivColorImage.setBackgroundColor(Color.parseColor(question.extraData))

                tvAnswer1.setOnClickListener { onItemClick(answers[0].isCorrect) }
                tvAnswer2.setOnClickListener { onItemClick(answers[1].isCorrect) }
                tvAnswer3.setOnClickListener { onItemClick(answers[2].isCorrect) }
                tvAnswer4.setOnClickListener { onItemClick(answers[3].isCorrect) }
            }
        }

        container.addView(view)
        return view
    }

    override fun getCount() = questionsWithAnswers.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    fun updateDataSet(questionsWithAnswers: List<QuestionAnswersJoin>) {
        this.questionsWithAnswers = questionsWithAnswers
        notifyDataSetChanged()
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        (container as ViewGroup).removeView(`object` as View)
    }

    fun dataSetSize() = count
}