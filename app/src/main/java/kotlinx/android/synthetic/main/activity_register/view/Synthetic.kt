package kotlinx.android.synthetic.main.activity_register.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.Button
import android.widget.ImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

val View.btnRegister: Button get() = findViewById(R.id.btnRegister)
val View.ivBack: ImageView get() = findViewById(R.id.ivBack)
val View.tietAccount: TextInputEditText get() = findViewById(R.id.tietAccount)
val View.tietConfirmPssword: TextInputEditText get() = findViewById(R.id.tietConfirmPssword)
val View.tietPassword: TextInputEditText get() = findViewById(R.id.tietPassword)
val View.tilAccount: TextInputLayout get() = findViewById(R.id.tilAccount)
val View.tilConfirmPssword: TextInputLayout get() = findViewById(R.id.tilConfirmPssword)
val View.tilPassword: TextInputLayout get() = findViewById(R.id.tilPassword)
