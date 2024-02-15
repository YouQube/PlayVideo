package com.brandon.playvideo_app.ui.main

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.databinding.MainActivityBinding
import com.brandon.playvideo_app.ui.category.CategoryFragment
import com.brandon.playvideo_app.ui.library.LibraryFragment
import com.brandon.playvideo_app.ui.search.SearchFragment
import com.brandon.playvideo_app.ui.subscription.SubscriptionFragment
import com.brandon.playvideo_app.ui.trend.TrendFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: MainActivityBinding by lazy { MainActivityBinding.inflate(layoutInflater) }

    private var fragments: List<MainTabHolder> = listOf(
        MainTabHolder(
            fragment = TrendFragment.newInstance(), title = R.string.main_tab_title_trend
        ),
        MainTabHolder(
            fragment = CategoryFragment.newInstance(), title = R.string.main_tab_title_category
        ),
        MainTabHolder(
            fragment = SubscriptionFragment.newInstance(), title = R.string.main_tab_title_subscription
        ),
        MainTabHolder(
            fragment = LibraryFragment.newInstance(), title = R.string.main_tab_title_library
        ),
        MainTabHolder(
            fragment = SearchFragment.newInstance(), title = R.string.main_tab_title_search
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        setSplash()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        Timber.plant(Timber.DebugTree())
        initView()

    }

    private fun setSplash() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                false
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }
    }


    private fun initView() = with(binding) {
        // FragmentContainerView x BottomNavigationView 구현
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_activity_main
        ) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

    }
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        Timber.d("현재 fragment: " + currentFragment?.javaClass?.simpleName)
        if (currentFragment is NavHostFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}