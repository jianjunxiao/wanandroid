package kotlinx.android.synthetic.main.activity_share

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText

val Activity.acetSharePeople: AppCompatEditText get() = findViewById(R.id.acetSharePeople)
val Fragment.acetSharePeople: AppCompatEditText get() = requireView().findViewById(R.id.acetSharePeople)
val View.acetSharePeople: AppCompatEditText get() = findViewById(R.id.acetSharePeople)

val Activity.acetTitle: AppCompatEditText get() = findViewById(R.id.acetTitle)
val Fragment.acetTitle: AppCompatEditText get() = requireView().findViewById(R.id.acetTitle)
val View.acetTitle: AppCompatEditText get() = findViewById(R.id.acetTitle)

val Activity.acetlink: AppCompatEditText get() = findViewById(R.id.acetlink)
val Fragment.acetlink: AppCompatEditText get() = requireView().findViewById(R.id.acetlink)
val View.acetlink: AppCompatEditText get() = findViewById(R.id.acetlink)

val Activity.ivBack: ImageView get() = findViewById(R.id.ivBack)
val Fragment.ivBack: ImageView get() = requireView().findViewById(R.id.ivBack)
val View.ivBack: ImageView get() = findViewById(R.id.ivBack)

val Activity.tvSubmit: TextView get() = findViewById(R.id.tvSubmit)
val Fragment.tvSubmit: TextView get() = requireView().findViewById(R.id.tvSubmit)
val View.tvSubmit: TextView get() = findViewById(R.id.tvSubmit)
