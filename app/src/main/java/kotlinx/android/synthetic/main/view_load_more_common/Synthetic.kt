package kotlinx.android.synthetic.main.view_load_more_common

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

val Activity.load_more_load_complete_view: FrameLayout get() = findViewById(R.id.load_more_load_complete_view)
val Fragment.load_more_load_complete_view: FrameLayout get() = requireView().findViewById(R.id.load_more_load_complete_view)
val View.load_more_load_complete_view: FrameLayout get() = findViewById(R.id.load_more_load_complete_view)

val Activity.load_more_load_end_view: FrameLayout get() = findViewById(R.id.load_more_load_end_view)
val Fragment.load_more_load_end_view: FrameLayout get() = requireView().findViewById(R.id.load_more_load_end_view)
val View.load_more_load_end_view: FrameLayout get() = findViewById(R.id.load_more_load_end_view)

val Activity.load_more_load_fail_view: FrameLayout get() = findViewById(R.id.load_more_load_fail_view)
val Fragment.load_more_load_fail_view: FrameLayout get() = requireView().findViewById(R.id.load_more_load_fail_view)
val View.load_more_load_fail_view: FrameLayout get() = findViewById(R.id.load_more_load_fail_view)

val Activity.load_more_loading_view: LinearLayout get() = findViewById(R.id.load_more_loading_view)
val Fragment.load_more_loading_view: LinearLayout get() = requireView().findViewById(R.id.load_more_loading_view)
val View.load_more_loading_view: LinearLayout get() = findViewById(R.id.load_more_loading_view)

val Activity.loading_progress: ProgressBar get() = findViewById(R.id.loading_progress)
val Fragment.loading_progress: ProgressBar get() = requireView().findViewById(R.id.loading_progress)
val View.loading_progress: ProgressBar get() = findViewById(R.id.loading_progress)

val Activity.loading_text: TextView get() = findViewById(R.id.loading_text)
val Fragment.loading_text: TextView get() = requireView().findViewById(R.id.loading_text)
val View.loading_text: TextView get() = findViewById(R.id.loading_text)

val Activity.tv_prompt: TextView get() = findViewById(R.id.tv_prompt)
val Fragment.tv_prompt: TextView get() = requireView().findViewById(R.id.tv_prompt)
val View.tv_prompt: TextView get() = findViewById(R.id.tv_prompt)
