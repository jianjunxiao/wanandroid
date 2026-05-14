package kotlinx.android.synthetic.main.view_load_more_common.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

val View.load_more_load_complete_view: FrameLayout get() = findViewById(R.id.load_more_load_complete_view)
val View.load_more_load_end_view: FrameLayout get() = findViewById(R.id.load_more_load_end_view)
val View.load_more_load_fail_view: FrameLayout get() = findViewById(R.id.load_more_load_fail_view)
val View.load_more_loading_view: LinearLayout get() = findViewById(R.id.load_more_loading_view)
val View.loading_progress: ProgressBar get() = findViewById(R.id.loading_progress)
val View.loading_text: TextView get() = findViewById(R.id.loading_text)
val View.tv_prompt: TextView get() = findViewById(R.id.tv_prompt)
