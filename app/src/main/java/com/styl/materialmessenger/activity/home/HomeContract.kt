package com.styl.materialmessenger.activity.home

import com.styl.materialmessenger.modules.unreadMessage.view.UnreadMessageFragment
import com.styl.materialmessenger.modules.people.view.PeopleFragment
import com.styl.materialmessenger.modules.setting.view.SettingsFragment

interface HomeContract {
    interface View {

    }

    interface Presenter {
        fun navigateToChat(chatFragment: UnreadMessageFragment?)
        fun navigateToPeople(peopleFragment: PeopleFragment?)
        fun navigateToSetting(settingsFragment: SettingsFragment?)

    }

    interface Router {
        fun navigateToChat(chatFragment: UnreadMessageFragment?)
        fun navigationToPeople(peopleFragment: PeopleFragment?)
        fun navigationToSetting(settingsFragment: SettingsFragment?)
    }
}