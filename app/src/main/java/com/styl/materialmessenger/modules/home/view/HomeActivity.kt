package com.styl.materialmessenger.modules.home.view

import android.os.Bundle
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.styl.materialmessenger.BaseActivity
import com.styl.materialmessenger.R
import com.styl.materialmessenger.modules.home.HomeContract
import com.styl.materialmessenger.modules.home.presenter.HomePresenter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), HomeContract.View {

    private val presenter = HomePresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        chipNavBar.setOnItemSelectedListener(bottomNavBarListener)
        chipNavBar.setItemSelected(R.id.chat, true)
    }

    private val bottomNavBarListener = object : ChipNavigationBar.OnItemSelectedListener {
        override fun onItemSelected(id: Int) {
            when (id) {
                R.id.chat -> {
                    presenter.navigateToChat()
                }

                R.id.people -> {
                    presenter.navigateToPeople()
                }

                R.id.setting -> {
                    presenter.navigateToSetting()
                }
            }
        }

    }
}