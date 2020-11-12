package com.styl.materialmessenger.modules.home

interface HomeContract {
    interface View {

    }

    interface Presenter {
        fun navigateToChat()
        fun navigateToPeople()
        fun navigateToSetting()

    }

    interface Router {
        fun navigateToChat()
        fun navigationToPeople()
        fun navigationToSetting()
    }
}