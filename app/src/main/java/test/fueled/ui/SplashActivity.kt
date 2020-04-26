package test.fueled.ui

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.fueled_lunch.LoginActivity
import test.fueled.R
import test.fueled.databinding.ActivitySplashBinding
import test.fueled.preference.AppPreferencesHelper
import test.fueled.utils.AppUtils


class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 1500
    private var animationDrawable: AnimationDrawable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.makeActivityFullScreen(window)
        val mBinding: ActivitySplashBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_splash)
        initAnimation(mBinding)
    }

    private fun initAnimation(mBinding: ActivitySplashBinding) {
        animationDrawable = mBinding.ivContainer.getBackground() as AnimationDrawable?
        animationDrawable?.start();
        checkAnimationStatus(50, animationDrawable);
    }

    private fun checkAnimationStatus(time: Int, animationDrawable: AnimationDrawable?) {
        val handler = Handler()
        handler.postDelayed({
            if (animationDrawable!!.getCurrent() != animationDrawable.getFrame(animationDrawable.getNumberOfFrames() - 1))
                checkAnimationStatus(time, animationDrawable);
            else navigateToNextScreen()
        }, SPLASH_TIME_OUT)
    }

    private fun navigateToNextScreen() {
        if (AppPreferencesHelper.getInstance(this).getUserId().length > 0) {
            startActivity(Intent(this, HomeActivity::class.java))
        } else
            startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish()
    }

}