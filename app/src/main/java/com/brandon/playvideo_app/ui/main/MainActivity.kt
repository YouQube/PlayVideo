package com.brandon.playvideo_app.ui.main

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.databinding.MainActivityBinding
import com.brandon.playvideo_app.ui.category.CategoryFragment
import com.brandon.playvideo_app.ui.library.LibraryFragment
import com.brandon.playvideo_app.ui.search.SearchFragment
import com.brandon.playvideo_app.ui.subscription.SubscriptionFragment
import com.brandon.playvideo_app.ui.trend.TrendFragment
import timber.log.Timber


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
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        Timber.plant(Timber.DebugTree())
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            statusBarColor = Color.BLACK
            WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars = true
        }

        binding.clContainer.setPadding(0, getStatusBarHeight(this)-10, 0, getNaviBarHeight(this))


        initView()
    }


    private fun initView() = with(binding) {
        // FragmentContainerView x BottomNavigationView 구현
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_activity_main
        ) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")

        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    fun getNaviBarHeight(context:Context) : Int {
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")

        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

}