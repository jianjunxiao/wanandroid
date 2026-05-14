package kotlinx.android.synthetic.main.fragment_progress_dialog

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.TextView

val Activity.tvMessage: TextView get() = findViewById(R.id.tvMessage)
val Fragment.tvMessage: TextView get() = requireView().findViewById(R.id.tvMessage)
val View.tvMessage: TextView get() = findViewById(R.id.tvMessage)
