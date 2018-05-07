package com.marijannovak.autismhelper.modules.child.adapters

import android.content.Context
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.data.models.QuestionAnswersJoin
import kotlinx.android.synthetic.main.list_item_question.view.*

class QuizPagerAdapter(
        private val context: Context,
        private var questionsWithAnswers: List<QuestionAnswersJoin>,
        private val onItemClick: (Boolean) -> Unit
)
    : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_question, container, false)

        val question = questionsWithAnswers[position].question
        val answers = questionsWithAnswers[position].answers

        with(view){
            tvQuestion.text = question.text
            tvAnswer1.text = answers[0].text
            tvAnswer2.text = answers[1].text
            tvAnswer3.text = answers[2].text
            tvAnswer4.text = answers[3].text

            when(question.categoryId) {
                1 -> {
                    ivExtraImage.setBackgroundColor(Color.parseColor(question.extraData))
                    ivExtraImage.visibility = View.VISIBLE
                }

                2 -> {
                    Glide.with(view)
                            .load(question.imgPath)
                            .into(ivExtraImage)

                    ivExtraImage.visibility = View.VISIBLE
                }

                else -> ivExtraImage.visibility = View.GONE
            }

            tvAnswer1.setOnClickListener { onItemClick(answers[0].isCorrect) }
            tvAnswer2.setOnClickListener { onItemClick(answers[1].isCorrect) }
            tvAnswer3.setOnClickListener { onItemClick(answers[2].isCorrect) }
            tvAnswer4.setOnClickListener { onItemClick(answers[3].isCorrect) }
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