package com.marijannovak.autismhelper.ui.activities

import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.PhrasesSavedSentencesJoin
import com.marijannovak.autismhelper.data.models.SavedSentence
import com.marijannovak.autismhelper.adapter.AACAdapter
import com.marijannovak.autismhelper.ui.fragments.PhraseCategoryFragment
import com.marijannovak.autismhelper.viewmodels.AACViewModel
import com.marijannovak.autismhelper.utils.DialogHelper
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.toSentence
import kotlinx.android.synthetic.main.activity_aac.*
import java.util.*
import kotlin.collections.ArrayList

class AACActivity : ViewModelActivity<AACViewModel, PhrasesSavedSentencesJoin>() {

    private lateinit var tts: TextToSpeech
    private var ttsWords: ArrayList<String> = ArrayList()
    private var ttsSupported = false
    private var aacDisplayAdapter: AACAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aac)

        tts = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                ttsSupported = true
                tts.language = Locale.US
                tts.setSpeechRate(viewModel.getTtsSpeed())
                tts.setPitch(viewModel.getTtsPitch())
            }
        }

        setupAacDisplay()
        loadFragment(PhraseCategoryFragment())
    }

    fun loadFragment(fragment: BaseFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flContainer, fragment, fragment.javaClass.simpleName)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun handleResource(resource: Resource<PhrasesSavedSentencesJoin>?) {

    }

    private fun setupAacDisplay() {
        if (aacDisplayAdapter == null) {
            aacDisplayAdapter = AACAdapter(emptyList(), { _, position ->
                aacDisplayAdapter?.deleteItem(position)
                ttsWords.removeAt(position)
                if (aacDisplayAdapter!!.datasetCount() == 0) {
                    tvNoPhrasesAdded.visibility = View.VISIBLE
                }
            }, { _, _ -> /*noop*/ })
            rvAacDisplay.adapter = aacDisplayAdapter
            rvAacDisplay.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 5)
        }
    }

    fun addItemToDisplay(phrase: AacPhrase) {
        tvNoPhrasesAdded.visibility = View.GONE
        aacDisplayAdapter?.addItem(phrase)
        ttsWords.add(phrase.text)
    }

    fun addMultipleItemsToDisplay(phrases: List<AacPhrase>) {
        tvNoPhrasesAdded.visibility = View.GONE
        val phraseStrings = ArrayList<String>()
        phrases.forEach {
            phraseStrings.add(it.text)
        }
        aacDisplayAdapter?.addItems(phrases)
        ttsWords.addAll(phraseStrings)
    }

    fun speak(toSpeak: String) {
        if (ttsSupported) {
            if(viewModel.isSoundOn()) {
                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, Bundle(), null)
            } else {
                showMessage(R.string.audio_disabled, null)
            }
        } else {
            showMessage(R.string.feature_not_supported, null)
        }
    }

    override fun subscribeToData() {
        viewModel.resource.observe(this, Observer { handleResource(it) })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_aac, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                R.id.action_speak -> {
                    if (ttsWords.isNotEmpty()) {
                        speak(ttsWords.toSentence())
                    } else {
                        speak(getString(R.string.construct_sentence))
                    }
                }

                R.id.action_enter_text -> {
                   DialogHelper.showEnterPhraseTextDialog(this@AACActivity) {
                       addItemToDisplay(it)
                   }
                }

                R.id.action_save_sentence -> {
                    aacDisplayAdapter?.let {
                        val sentence = it.getDataset()
                        if(sentence.isNotEmpty()) {
                            viewModel.saveSentence(SavedSentence(0, sentence))
                        }
                    }
                }

                else -> {}

            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }

    override fun onBackPressed() {
        val phraseCategoryFragment = supportFragmentManager.findFragmentByTag(PhraseCategoryFragment::class.java.simpleName)
        if(phraseCategoryFragment != null && phraseCategoryFragment.isVisible) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    fun getDisplayedPhrasesNo(): Int {
        return aacDisplayAdapter!!.datasetCount()
    }
}
