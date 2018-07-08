package com.marijannovak.autismhelper.modules.child

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.PhrasesSavedSentencesJoin
import com.marijannovak.autismhelper.data.models.SavedSentence
import com.marijannovak.autismhelper.modules.child.adapters.AACAdapter
import com.marijannovak.autismhelper.modules.child.fragments.PhraseCategoryFragment
import com.marijannovak.autismhelper.modules.child.mvvm.AACViewModel
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

        loadFragment(PhraseCategoryFragment())
    }

    private fun loadFragment(fragment: BaseFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun handleResource(phrasesAndSentences: Resource<PhrasesSavedSentencesJoin>?) {
        phrasesAndSentences?.let {
            showLoading(it.status, it.message)
            when (it.status) {
                Status.SUCCESS -> {
                    setUpAacData(it.data!!.phrases)
                }

                else -> {

                }
            }
        }
    }

    private fun setUpAacData(phrases: List<AacPhrase>?) {
        if (aacDisplayAdapter == null) {
            aacDisplayAdapter = AACAdapter(emptyList(), { _, position ->
                aacDisplayAdapter?.deleteItem(position)
                ttsWords.removeAt(position)
            }, { _, _ -> /*noop*/})
            rvAacDisplay.adapter = aacDisplayAdapter
            rvAacDisplay.layoutManager = GridLayoutManager(this, 5)
        }

        /*phrases?.let {
            if (aacSelectorAdapter == null) {
                aacSelectorAdapter = AACAdapter(emptyList(), { phrase, _ ->
                    if(aacDisplayAdapter!!.datasetCount() < 10) {
                        addItemToDisplay(phrase)
                    } else {
                        showMessage(R.string.max_phrases, null)
                    }
                }, { phrase, _ ->
                    speak(phrase.name)
                })
                rvAacSelector.adapter = aacSelectorAdapter
                rvAacSelector.layoutManager = GridLayoutManager(this, 5)
            }

            aacSelectorAdapter!!.update(it)
        }*/
    }

    private fun addItemToDisplay(phrase: AacPhrase) {
        aacDisplayAdapter?.addItem(phrase)
        ttsWords.add(phrase.text)
    }

    private fun speak(toSpeak: String) {
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
        viewModel.resourceLiveData.observe(this, Observer { handleResource(it) })
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
}
