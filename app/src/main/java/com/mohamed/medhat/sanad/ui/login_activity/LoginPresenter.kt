package com.mohamed.medhat.sanad.ui.login_activity

import android.os.Bundle
import android.view.Gravity
import androidx.core.widget.addTextChangedListener
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.model.LoginUser
import com.mohamed.medhat.sanad.ui.CommonNavViewModel
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.registration_activity.RegistrationActivity
import com.mohamed.medhat.sanad.utils.handleLoadingState
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * An mvp presenter for [LoginActivity].
 */
@ActivityScope
class LoginPresenter @Inject constructor(val sharedPrefs: SharedPrefs) :
    AdvancedPresenter<LoginView, LoginViewModel>() {

    private lateinit var loginView: LoginView
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var commonNavViewModel: CommonNavViewModel
    private lateinit var activity: LoginActivity

    override fun setView(view: LoginView) {
        loginView = view
        activity = (loginView as LoginActivity)
    }

    fun setNavigationViewModel(commonNavViewModel: CommonNavViewModel) {
        this.commonNavViewModel = commonNavViewModel
    }

    override fun start(savedInstanceState: Bundle?) {
        fixPasswordGravity()
        sharedPrefs.clearAll()
        loginViewModel.token.observe(activity) {
            loginView.apply {
                displayToast(activity.getString(R.string.successfully_logged_in))
                commonNavViewModel.calculateDestination()
            }
        }
        loginViewModel.state.observe(activity) {
            val textErrorViewer = TextErrorViewer(loginViewModel.appError, activity.tv_login_error)
            loginView.setAppErrorViewer(textErrorViewer)
            handleLoadingState(loginView, it)
        }
        commonNavViewModel.state.observe(activity) {
            val textErrorViewer = TextErrorViewer(loginViewModel.appError, activity.tv_login_error)
            loginView.setAppErrorViewer(textErrorViewer)
            handleLoadingState(loginView, it)
        }
        commonNavViewModel.destination.observe(activity) {
            if (it != null) {
                loginView.startActivityAsRoot(it)
            }
        }
    }

    override fun setViewModel(viewModel: LoginViewModel) {
        loginViewModel = viewModel
    }

    fun onLoginClicked() {
        loginView.apply {
            hideError()
            hideLoadingIndicator()
            resetInputError(activity.et_login_email)
            resetInputError(activity.et_login_password)
        }
        val emptyError = activity.getString(R.string.empty_field_warning)
        var somethingWrong = false
        if (loginView.getEmail().isEmpty()) {
            loginView.showInputError(
                activity.et_login_email,
                emptyError
            )
            somethingWrong = true
        }
        if (loginView.getPassword().isEmpty()) {
            loginView.showInputError(
                activity.et_login_password,
                emptyError
            )
            somethingWrong = true
        }
        if (!somethingWrong) {
            val loginUser = LoginUser(
                email = loginView.getEmail(),
                password = loginView.getPassword()
            )
            loginViewModel.login(loginUser)
        }
    }

    fun onRegisterClicked() {
        loginView.navigateTo(RegistrationActivity::class.java)
    }

    private fun fixPasswordGravity() {
        activity.et_login_password.addTextChangedListener {
            if (it.toString().isEmpty()) {
                activity.et_login_password.gravity = Gravity.END or Gravity.CENTER
            } else {
                activity.et_login_password.gravity = Gravity.START or Gravity.CENTER
            }
        }
    }

    fun onFacebookClicked() {
        inDevelopment()
    }

    fun onGoogleClicked() {
        inDevelopment()
    }

    fun onForgetPasswordClicked() {
        inDevelopment()
    }

    private fun inDevelopment() {
        loginView.displayToast(activity.getString(R.string.in_development))
    }
}