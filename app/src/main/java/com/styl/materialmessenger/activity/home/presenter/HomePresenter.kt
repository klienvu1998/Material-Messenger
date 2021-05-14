package com.styl.materialmessenger.activity.home.presenter

import android.content.Context
import com.styl.materialmessenger.modules.unreadMessage.view.UnreadMessageFragment
import com.styl.materialmessenger.activity.home.HomeContract
import com.styl.materialmessenger.activity.home.router.HomeRouter
import com.styl.materialmessenger.modules.people.view.PeopleFragment
import com.styl.materialmessenger.modules.setting.view.SettingsFragment

class HomePresenter(val homeView: HomeContract.View, private val context: Context?): HomeContract.Presenter {

    private val router = HomeRouter(context)

    override fun navigateToChat(chatFragment: UnreadMessageFragment?) {
        router.navigateToChat(chatFragment)
    }

    override fun navigateToPeople(peopleFragment: PeopleFragment?) {
        router.navigationToPeople(peopleFragment)
    }

    override fun navigateToSetting(settingsFragment: SettingsFragment?) {
        router.navigationToSetting(settingsFragment)
    }

}