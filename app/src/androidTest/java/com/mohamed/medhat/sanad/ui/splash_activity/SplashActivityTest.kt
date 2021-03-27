package com.mohamed.medhat.sanad.ui.splash_activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mohamed.medhat.sanad.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * A test class for [SplashActivity]. It doesn't actually test anything but making sure that the
 * testing environment is working properly.
 */
@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @get: Rule
    val activityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun helloWorldTest() {
        onView(withId(R.id.iv_app_logo)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_app_slogan)).check(matches(isDisplayed()))
    }
}