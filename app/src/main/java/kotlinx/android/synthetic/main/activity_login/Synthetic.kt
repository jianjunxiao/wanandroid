package kotlinx.android.synthetic.main.activity_login

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

val Activity.btnLogin: Button get() = findViewById(R.id.btnLogin)
val Fragment.btnLogin: Button get() = requireView().findViewById(R.id.btnLogin)
val View.btnLogin: Button get() = findViewById(R.id.btnLogin)

val Activity.ivClose: ImageView get() = findViewById(R.id.ivClose)
val Fragment.ivClose: ImageView get() = requireView().findViewById(R.id.ivClose)
val View.ivClose: ImageView get() = findViewById(R.id.ivClose)

val Activity.tietAccount: TextInputEditText get() = findViewById(R.id.tietAccount)
val Fragment.tietAccount: TextInputEditText get() = requireView().findViewById(R.id.tietAccount)
val View.tietAccount: TextInputEditText get() = findViewById(R.id.tietAccount)

val Activity.tietPassword: TextInputEditText get() = findViewById(R.id.tietPassword)
val Fragment.tietPassword: TextInputEditText get() = requireView().findViewById(R.id.tietPassword)
val View.tietPassword: TextInputEditText get() = findViewById(R.id.tietPassword)

val Activity.tilAccount: TextInputLayout get() = findViewById(R.id.tilAccount)
val Fragment.tilAccount: TextInputLayout get() = requireView().findViewById(R.id.tilAccount)
val View.tilAccount: TextInputLayout get() = findViewById(R.id.tilAccount)

val Activity.tilPassword: TextInputLayout get() = findViewById(R.id.tilPassword)
val Fragment.tilPassword: TextInputLayout get() = requireView().findViewById(R.id.tilPassword)
val View.tilPassword: TextInputLayout get() = findViewById(R.id.tilPassword)

val Activity.tvGoRegister: TextView get() = findViewById(R.id.tvGoRegister)
val Fragment.tvGoRegister: TextView get() = requireView().findViewById(R.id.tvGoRegister)
val View.tvGoRegister: TextView get() = findViewById(R.id.tvGoRegister)
