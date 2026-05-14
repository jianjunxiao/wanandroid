package kotlinx.android.synthetic.main.activity_register

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.Button
import android.widget.ImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

val Activity.btnRegister: Button get() = findViewById(R.id.btnRegister)
val Fragment.btnRegister: Button get() = requireView().findViewById(R.id.btnRegister)
val View.btnRegister: Button get() = findViewById(R.id.btnRegister)

val Activity.ivBack: ImageView get() = findViewById(R.id.ivBack)
val Fragment.ivBack: ImageView get() = requireView().findViewById(R.id.ivBack)
val View.ivBack: ImageView get() = findViewById(R.id.ivBack)

val Activity.tietAccount: TextInputEditText get() = findViewById(R.id.tietAccount)
val Fragment.tietAccount: TextInputEditText get() = requireView().findViewById(R.id.tietAccount)
val View.tietAccount: TextInputEditText get() = findViewById(R.id.tietAccount)

val Activity.tietConfirmPssword: TextInputEditText get() = findViewById(R.id.tietConfirmPssword)
val Fragment.tietConfirmPssword: TextInputEditText get() = requireView().findViewById(R.id.tietConfirmPssword)
val View.tietConfirmPssword: TextInputEditText get() = findViewById(R.id.tietConfirmPssword)

val Activity.tietPassword: TextInputEditText get() = findViewById(R.id.tietPassword)
val Fragment.tietPassword: TextInputEditText get() = requireView().findViewById(R.id.tietPassword)
val View.tietPassword: TextInputEditText get() = findViewById(R.id.tietPassword)

val Activity.tilAccount: TextInputLayout get() = findViewById(R.id.tilAccount)
val Fragment.tilAccount: TextInputLayout get() = requireView().findViewById(R.id.tilAccount)
val View.tilAccount: TextInputLayout get() = findViewById(R.id.tilAccount)

val Activity.tilConfirmPssword: TextInputLayout get() = findViewById(R.id.tilConfirmPssword)
val Fragment.tilConfirmPssword: TextInputLayout get() = requireView().findViewById(R.id.tilConfirmPssword)
val View.tilConfirmPssword: TextInputLayout get() = findViewById(R.id.tilConfirmPssword)

val Activity.tilPassword: TextInputLayout get() = findViewById(R.id.tilPassword)
val Fragment.tilPassword: TextInputLayout get() = requireView().findViewById(R.id.tilPassword)
val View.tilPassword: TextInputLayout get() = findViewById(R.id.tilPassword)
