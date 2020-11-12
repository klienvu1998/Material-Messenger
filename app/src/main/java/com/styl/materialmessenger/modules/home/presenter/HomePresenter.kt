package com.styl.materialmessenger.modules.home.presenter

import android.content.Context
import com.styl.materialmessenger.modules.home.HomeContract
import com.styl.materialmessenger.modules.home.router.HomeRouter

class HomePresenter(val homeView: HomeContract.View, private val context: Context?): HomeContract.Presenter {

    private val router = HomeRouter(context)

    override fun navigateToChat() {
        router.navigateToChat()
    }

    override fun navigateToPeople() {
        router.navigationToPeople()
    }

    override fun navigateToSetting() {
        router.navigationToSetting()
    }

}