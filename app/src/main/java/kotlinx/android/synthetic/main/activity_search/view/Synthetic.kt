package kotlinx.android.synthetic.main.activity_search.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout

val View.acetInput: AppCompatEditText get() = findViewById(R.id.acetInput)
val View.clTitle: ConstraintLayout get() = findViewById(R.id.clTitle)
val View.flContainer: FrameLayout get() = findViewById(R.id.flContainer)
val View.ivBack: ImageView get() = findViewById(R.id.ivBack)
val View.ivClearSearch: ImageView get() = findViewById(R.id.ivClearSearch)
val View.ivDone: ImageView get() = findViewById(R.id.ivDone)
