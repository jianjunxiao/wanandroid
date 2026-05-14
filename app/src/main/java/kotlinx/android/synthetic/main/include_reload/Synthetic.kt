package kotlinx.android.synthetic.main.include_reload

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.Button
import android.widget.TextView

val Activity.btnReload: Button get() = findViewById(R.id.btnReload)
val Fragment.btnReload: Button get() = requireView().findViewById(R.id.btnReload)
val View.btnReload: Button get() = findViewById(R.id.btnReload)

val Activity.tvReload: TextView get() = findViewById(R.id.tvReload)
val Fragment.tvReload: TextView get() = requireView().findViewById(R.id.tvReload)
val View.tvReload: TextView get() = findViewById(R.id.tvReload)
