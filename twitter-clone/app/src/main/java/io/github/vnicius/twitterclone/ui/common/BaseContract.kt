package io.github.vnicius.twitterclone.ui.common

/** Base contract for View and Presenter */
interface BaseContract {

    interface View {

        /**
         * Show a loader to the user
         */
        fun showLoader()

        /**
         * Show a error message to the user
         * @param message with the error
         */
        fun showError(message: String)
    }

    interface Presenter {

        /**
         * Dispose presenter coroutines jobs
         */
        fun dispose()
    }
}
