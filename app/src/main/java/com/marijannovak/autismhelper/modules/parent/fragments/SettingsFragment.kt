package com.marijannovak.autismhelper.modules.parent.fragments


import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.SeekBar
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.modules.parent.ParentActivity
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class SettingsFragment : InjectableFragment<ParentViewModel>(), SeekBar.OnSeekBarChangeListener {

    private lateinit var tts: TextToSpeech
    private var ttsSupported = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tts = TextToSpeech(activity) {
            if (it == TextToSpeech.SUCCESS) {
                ttsSupported = true
                tts.language = Locale.US
                tts.setSpeechRate(viewModel.getTtsSpeed())
                tts.setPitch(viewModel.getTtsPitch())
            }
        }

        sbPitch.progress = (viewModel.getTtsPitch() * 10).toInt()
        sbSpeed.progress = (viewModel.getTtsSpeed() * 10).toInt()
        swSound.isChecked = viewModel.isSoundOn()

        sbPitch.setOnSeekBarChangeListener(this)
        sbSpeed.setOnSeekBarChangeListener(this)

        tvSpeedValue.text = sbSpeed.progress.toString()
        tvPitchValue.text = sbPitch.progress.toString()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if(seekBar == sbSpeed){
            tvSpeedValue.text = sbSpeed.progress.toString()
            tts.setSpeechRate(sbSpeed.progress.toFloat() / 10)
        }

        if(seekBar == sbPitch) {
            tvPitchValue.text = sbPitch.progress.toString()
            tts.setPitch(sbPitch.progress.toFloat() / 10)
      }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //NOOP
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
       if(ttsSupported) {
           tts.speak(getString(R.string.this_is_my_voice), TextToSpeech.QUEUE_FLUSH, Bundle(), null)
       }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(item.itemId) {
                R.id.action_settings_save -> {
                    viewModel.saveSettings(Settings(swSound.isChecked, sbSpeed.progress.toFloat() / 10, sbPitch.progress.toFloat() / 10))
                    val parentActivity = activity as ParentActivity
                    parentActivity.showMessage(R.string.settings_saved, null)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

     data class Settings(var soundOn: Boolean, var ttsSpeed: Float, var ttsPitch: Float)
}
