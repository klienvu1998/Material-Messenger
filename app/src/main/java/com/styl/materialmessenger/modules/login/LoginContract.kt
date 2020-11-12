package com.styl.materialmessenger.modules.login

interface LoginContract {
    interface View {

    }

    interface Presenter {
        fun navigateToRegister()
    }

    interface Router {
        fun navigateToRegister()
    }
}