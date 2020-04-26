package com.example.fueled_lunch

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import test.fueled.R
import test.fueled.base.BaseActivity
import test.fueled.custom_dialog.CustomSignUpDialog
import test.fueled.custom_dialog.SignUpListener
import test.fueled.databinding.ActivityLoginBinding
import test.fueled.ui.HomeActivity
import test.fueled.utils.AppUtils
import test.fueled.viewmodel.LoginViewModel


open class LoginActivity : BaseActivity(), OnCompleteListener<AuthResult> {
    lateinit var dialog: CustomSignUpDialog

    private lateinit var auth: FirebaseAuth
    private lateinit var mViewModel: LoginViewModel
    private lateinit var mBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.makeActivityFullScreen(window)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        initView();
        initFireBaseAuth
        setupViewModel()
    }

    private fun initView() {
        // This listener is used for handling done button click from keyboard
        mBinding.etPassword.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mViewModel.loginClicked()
            }
            false
        })
    }

    //This function is used to setup viewmodel
    private fun setupViewModel() {
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(LoginViewModel::class.java)
        registerObservers(mViewModel)
        mBinding.loginViewModel = mViewModel

        mViewModel.getLoginResponse()?.observe(this, Observer {
            performLoginSignUpAction(it)
        })
    }

    //This function is used to call login or signup methode
    private fun performLoginSignUpAction(it: Boolean?) {
        if (it!!) {
            loginUser(mViewModel.email, mViewModel.password)
        } else {
            openSignUpDialog()
        }
    }

    //This function is used to initilize firebaseauth
    private val initFireBaseAuth by lazy {
        auth = FirebaseAuth.getInstance()
    }

    //This Function is used for login using email and password (FirebaseAuth)
    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this)
    }

    //This Function is used for create user using email and password (FirebaseAuth)
    fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this)
    }


    /**
     * This is the callback function of firebase auth when task is completed, Received call in onComplete methode
     * **/
    override fun onComplete(task: Task<AuthResult>) {
        mProgressDialogObserver.onProgressChanged(false)
        if (task.isSuccessful) {
            auth.currentUser?.let { mViewModel.saveToPrefence(it) }
            writeNewUser()
            navigateToHome()
        } else {
            task.exception?.message?.let { AppUtils.showToast(it, this) }
        }
    }

    /**
     * This function is used to navigate to Home Screen
     * **/
    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish()
    }

    /**
     * This function is used to open signup dialog
     * **/
    fun openSignUpDialog() {
        dialog = CustomSignUpDialog(this, object : SignUpListener {
            override fun onSuccess(email: String, password: String) {
                if (mViewModel.validate(email, password)) {
                    mProgressDialogObserver.onProgressChanged(true)
                    createUser(email, password)
                    dialog.dismiss()
                }
            }
        });
        dialog.show()
    }
}

