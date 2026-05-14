package kotlinx.android.synthetic.main.activity_search

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout

val Activity.acetInput: AppCompatEditText get() = findViewById(R.id.acetInput)
val Fragment.acetInput: AppCompatEditText get() = requireView().findViewById(R.id.acetInput)
val View.acetInput: AppCompatEditText get() = findViewById(R.id.acetInput)

val Activity.clTitle: ConstraintLayout get() = findViewById(R.id.clTitle)
val Fragment.clTitle: ConstraintLayout get() = requireView().findViewById(R.id.clTitle)
val View.clTitle: ConstraintLayout get() = findViewById(R.id.clTitle)

val Activity.flContainer: FrameLayout get() = findViewById(R.id.flContainer)
val Fragment.flContainer: FrameLayout get() = requireView().findViewById(R.id.flContainer)
val View.flContainer: FrameLayout get() = findViewById(R.id.flContainer)

val Activity.ivBack: ImageView get() = findViewById(R.id.ivBack)
val Fragment.ivBack: ImageView get() = requireView().findViewById(R.id.ivBack)
val View.ivBack: ImageView get() = findViewById(R.id.ivBack)

val Activity.ivClearSearch: ImageView get() = findViewById(R.id.ivClearSearch)
val Fragment.ivClearSearch: ImageView get() = requireView().findViewById(R.id.ivClearSearch)
val View.ivClearSearch: ImageView get() = findViewById(R.id.ivClearSearch)

val Activity.ivDone: ImageView get() = findViewById(R.id.ivDone)
val Fragment.ivDone: ImageView get() = requireView().findViewById(R.id.ivDone)
val View.ivDone: ImageView get() = findViewById(R.id.ivDone)
