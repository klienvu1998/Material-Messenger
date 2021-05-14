package com.styl.materialmessenger.activity.home.router

import android.content.Context
import com.styl.materialmessenger.R
import com.styl.materialmessenger.modules.unreadMessage.view.UnreadMessageFragment
import com.styl.materialmessenger.activity.home.HomeContract
import com.styl.materialmessenger.activity.home.view.HomeActivity
import com.styl.materialmessenger.modules.people.view.PeopleFragment
import com.styl.materialmessenger.modules.setting.view.SettingsFragment

class HomeRouter(private val context: Context?): HomeContract.Router {
    override fun navigateToChat(chatFragment: UnreadMessageFragment?) {
        (context as HomeActivity).supportFragmentManager.beginTransaction().replace(R.id.homeContainer, UnreadMessageFragment()).commit()
    }

    override fun navigationToPeople(peopleFragment: PeopleFragment?) {
        (context as HomeActivity).supportFragmentManager.beginTransaction().replace(R.id.homeContainer, PeopleFragment()).commit()
    }

    override fun navigationToSetting(settingsFragment: SettingsFragment?) {
        settingsFragment?.let {
            (context as HomeActivity).supportFragmentManager.beginTransaction().replace(R.id.homeContainer,
                it
            ).commit()
        }
    }
}