package com.example.fixit

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTestLogin {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.fixit", appContext.packageName)
    }

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(Login::class.java)

    private lateinit var activityScenario : ActivityScenario<Login>

    @Before
    fun prepare(){
        activityScenario = activityScenarioRule.scenario
    }

    @Test
    fun loginButton(){
        onView(withId(R.id.loginButton)).perform(click())
    }

    @Test
    fun cancelButton(){
        onView(withId(R.id.btnCancel)).perform(click())
    }

    @Test
    fun forgetButton(){
        onView(withId(R.id.forgetPassword)).perform(click())
    }

    @Test
    fun redirectButton(){
        onView(withId(R.id.signupRedirectText)).perform(click())
    }

    @After
    fun clear(){
        activityScenario.close()
    }

}