package com.marijannovak.autismhelper.modules.child

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.modules.child.adapters.AACAdapter
import com.marijannovak.autismhelper.modules.child.mvvm.AACViewModel
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.toSentence
import kotlinx.android.synthetic.main.activity_aac.*
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList

class AACActivity : ViewModelActivity<AACViewModel, AacPhrase>() {

    private lateinit var tts: TextToSpeech
    private var ttsWords: ArrayList<String> = ArrayList()
    private var aacSelectorAdapter: AACAdapter? = null
    private var aacDisplayAdapter: AACAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aac)

        tts = TextToSpeech(this, {
            if(it == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
            } else {
                btnSpeak.isEnabled = false
                toast("Feature not supported!")
            }
        })

        btnSpeak.setOnClickListener {
            if(ttsWords.isNotEmpty()) {
                tts.speak(ttsWords.toSentence(), TextToSpeech.QUEUE_FLUSH, Bundle(), null)
            } else {
                tts.speak("Construct a sentence!", TextToSpeech.QUEUE_FLUSH, Bundle(), null)
            }

        }

        viewModel.loadPhrases()
    }

    override fun handleResource(resource: Resource<List<AacPhrase>>?) {
        resource?.let {
            showLoading(it.status)
            when(it.status) {
                Status.SUCCESS -> {
                    setUpAacData(it.data)
                }

                else -> {

                }
            }
        }
    }

    private fun setUpAacData(phrases: List<AacPhrase>?) {
        if(aacDisplayAdapter == null) {
            aacDisplayAdapter = AACAdapter(emptyList(), {
                aacPhrase, position ->
                    aacDisplayAdapter?.deleteItem(aacPhrase)
                    ttsWords.removeAt(position)
            })
            rvAacDisplay.adapter = aacDisplayAdapter
            rvAacDisplay.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }

        phrases?.let {
            if(aacSelectorAdapter == null) {
                aacSelectorAdapter = AACAdapter(emptyList(), {
                    phrase, _ ->
                        aacDisplayAdapter?.addItem(phrase)
                        ttsWords.add(phrase.name)
                })
                rvAacSelector.adapter = aacSelectorAdapter
                rvAacSelector.layoutManager = GridLayoutManager(this, 3)
            }

            aacSelectorAdapter!!.update(it)
        }
    }

    override fun subscribeToData() {
        viewModel.resourceLiveData.observe(this, Observer{ handleResource(it) })
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}
