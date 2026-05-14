package kotlinx.android.synthetic.main.activity_login.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

val View.btnLogin: Button get() = findViewById(R.id.btnLogin)
val View.ivClose: ImageView get() = findViewById(R.id.ivClose)
val View.tietAccount: TextInputEditText get() = findViewById(R.id.tietAccount)
val View.tietPassword: TextInputEditText get() = findViewById(R.id.tietPassword)
val View.tilAccount: TextInputLayout get() = findViewById(R.id.tilAccount)
val View.tilPassword: TextInputLayout get() = findViewById(R.id.tilPassword)
val View.tvGoRegister: TextView get() = findViewById(R.id.tvGoRegister)
