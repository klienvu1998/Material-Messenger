package com.styl.materialmessenger.modules.home.router

import android.content.Context
import com.styl.materialmessenger.R
import com.styl.materialmessenger.modules.chat.view.ChatFragment
import com.styl.materialmessenger.modules.home.HomeContract
import com.styl.materialmessenger.modules.home.view.HomeActivity
import com.styl.materialmessenger.modules.people.view.PeopleFragment
import com.styl.materialmessenger.modules.setting.view.SettingsFragment

class HomeRouter(private val context: Context?): HomeContract.Router {
    override fun navigateToChat() {
        (context as HomeActivity).supportFragmentManager.beginTransaction().replace(R.id.homeContainer, ChatFragment()).commit()
    }

    override fun navigationToPeople() {
        (context as HomeActivity).supportFragmentManager.beginTransaction().replace(R.id.homeContainer, PeopleFragment()).commit()
    }

    override fun navigationToSetting() {
        (context as HomeActivity).supportFragmentManager.beginTransaction().replace(R.id.homeContainer, SettingsFragment()).commit()
    }
}