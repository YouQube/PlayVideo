package com.brandon.playvideo_app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
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

}