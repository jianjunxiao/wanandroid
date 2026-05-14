package kotlinx.android.synthetic.main.view_change_text_zoom

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import androidx.appcompat.widget.AppCompatSeekBar

val Activity.seekBar: AppCompatSeekBar get() = findViewById(R.id.seekBar)
val Fragment.seekBar: AppCompatSeekBar get() = requireView().findViewById(R.id.seekBar)
val View.seekBar: AppCompatSeekBar get() = findViewById(R.id.seekBar)
