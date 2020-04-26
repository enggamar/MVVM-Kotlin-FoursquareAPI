package test.fueled.custom_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import test.fueled.databinding.DialogSignupBinding

/**
 * This Class is used to create Signup dialog
 * **/
class CustomSignUpDialog(private val mContext: Context, var signUpListener: SignUpListener) :
    Dialog(mContext) {
    private lateinit var mDialogBinding: DialogSignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDialogBinding = DialogSignupBinding.inflate(LayoutInflater.from(mContext))
        setContentView(mDialogBinding.getRoot())
        window!!.setGravity(Gravity.CENTER)
        setCancelable(false)
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        initView()
    }

    fun initView() {
        mDialogBinding.dialog = this
        mDialogBinding.etPassword.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signUpListener.onSuccess(
                    mDialogBinding.etEmail.text.toString(),
                    mDialogBinding.etPassword.text.toString()
                )
            }
            false
        })
    }

    /**
     * This function is used handle the click of signup button
     * **/
    fun signUpClicked() {
        signUpListener.onSuccess(
            mDialogBinding.etEmail.text.toString(),
            mDialogBinding.etPassword.text.toString()
        )
    }
}