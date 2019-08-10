package io.github.vnicius.twitterclone.adapters.click

import android.view.View

/**
 * Interface to help in the handle of click on adapter's items
 *
 * @param T the type of the model object
 */

interface AdapterClickHandler<T> {

    /**
     * Handle the click in the [view] for the [item] fo type [T]
     */
    fun onClick(view: View, item: T)
}