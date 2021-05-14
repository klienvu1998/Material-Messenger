package com.styl.materialmessenger.activity.home.view

import android.os.Bundle
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.styl.materialmessenger.activity.BaseActivity
import com.styl.materialmessenger.R
import com.styl.materialmessenger.modules.unreadMessage.view.UnreadMessageFragment
import com.styl.materialmessenger.activity.home.HomeContract
import com.styl.materialmessenger.activity.home.presenter.HomePresenter
import com.styl.materialmessenger.modules.people.view.PeopleFragment
import com.styl.materialmessenger.modules.setting.view.SettingsFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), HomeContract.View {

    private val presenter =
        HomePresenter(
            this,
            this
        )
    private var chatFragment: UnreadMessageFragment? = null
    private var peopleFragment: PeopleFragment? = null
    private var settingsFragment: SettingsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        chatFragment = UnreadMessageFragment()
        peopleFragment = PeopleFragment()
        settingsFragment = SettingsFragment()
        chipNavBar.setOnItemSelectedListener(bottomNavBarListener)
        chipNavBar.setItemSelected(R.id.chat, true)
    }

    private val bottomNavBarListener = object : ChipNavigationBar.OnItemSelectedListener {
        override fun onItemSelected(id: Int) {
            when (id) {
                R.id.chat -> {
                    presenter.navigateToChat(chatFragment)
                }

                R.id.people -> {
                    presenter.navigateToPeople(peopleFragment)
                }

                R.id.setting -> {
                    presenter.navigateToSetting(settingsFragment)
                }
            }
        }

    }
}