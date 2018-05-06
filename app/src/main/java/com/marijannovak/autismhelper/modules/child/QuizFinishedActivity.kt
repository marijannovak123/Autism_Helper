package com.marijannovak.autismhelper.modules.child

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.config.Constants.Companion.EXTRA_SCORE
import com.marijannovak.autismhelper.data.models.ChildScore
import kotlinx.android.synthetic.main.activity_quiz_finished.*

class QuizFinishedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_finished)

        if(intent.hasExtra(EXTRA_SCORE)) {
            val score = intent.getSerializableExtra(EXTRA_SCORE) as ChildScore
            tvScore.text = "${score.duration} milisekundi, ${score.mistakes} pogresaka"
        }
    }
}
