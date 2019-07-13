package kr.oklabs.meal.Splash

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler

import kr.oklabs.meal.MealActivity
import kr.oklabs.meal.R
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class SplashActivity : AppCompatActivity() {

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_splash)

        val SPLASH_DISPLAY_LENGTH = 1000
        Handler().postDelayed({
            val mainIntent = Intent(this@SplashActivity, MealActivity::class.java)
            this@SplashActivity.startActivity(mainIntent)
            this@SplashActivity.finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}
