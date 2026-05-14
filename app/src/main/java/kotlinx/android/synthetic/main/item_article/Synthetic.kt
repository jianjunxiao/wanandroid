package kotlinx.android.synthetic.main.item_article

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import android.widget.TextView

val Activity.iv_collect: ImageView get() = findViewById(R.id.iv_collect)
val Fragment.iv_collect: ImageView get() = requireView().findViewById(R.id.iv_collect)
val View.iv_collect: ImageView get() = findViewById(R.id.iv_collect)

val Activity.iv_pic: ImageView get() = findViewById(R.id.iv_pic)
val Fragment.iv_pic: ImageView get() = requireView().findViewById(R.id.iv_pic)
val View.iv_pic: ImageView get() = findViewById(R.id.iv_pic)

val Activity.tv_author: TextView get() = findViewById(R.id.tv_author)
val Fragment.tv_author: TextView get() = requireView().findViewById(R.id.tv_author)
val View.tv_author: TextView get() = findViewById(R.id.tv_author)

val Activity.tv_chapter: TextView get() = findViewById(R.id.tv_chapter)
val Fragment.tv_chapter: TextView get() = requireView().findViewById(R.id.tv_chapter)
val View.tv_chapter: TextView get() = findViewById(R.id.tv_chapter)

val Activity.tv_desc: TextView get() = findViewById(R.id.tv_desc)
val Fragment.tv_desc: TextView get() = requireView().findViewById(R.id.tv_desc)
val View.tv_desc: TextView get() = findViewById(R.id.tv_desc)

val Activity.tv_fresh: TextView get() = findViewById(R.id.tv_fresh)
val Fragment.tv_fresh: TextView get() = requireView().findViewById(R.id.tv_fresh)
val View.tv_fresh: TextView get() = findViewById(R.id.tv_fresh)

val Activity.tv_tag: TextView get() = findViewById(R.id.tv_tag)
val Fragment.tv_tag: TextView get() = requireView().findViewById(R.id.tv_tag)
val View.tv_tag: TextView get() = findViewById(R.id.tv_tag)

val Activity.tv_time: TextView get() = findViewById(R.id.tv_time)
val Fragment.tv_time: TextView get() = requireView().findViewById(R.id.tv_time)
val View.tv_time: TextView get() = findViewById(R.id.tv_time)

val Activity.tv_title: TextView get() = findViewById(R.id.tv_title)
val Fragment.tv_title: TextView get() = requireView().findViewById(R.id.tv_title)
val View.tv_title: TextView get() = findViewById(R.id.tv_title)

val Activity.tv_top: TextView get() = findViewById(R.id.tv_top)
val Fragment.tv_top: TextView get() = requireView().findViewById(R.id.tv_top)
val View.tv_top: TextView get() = findViewById(R.id.tv_top)
