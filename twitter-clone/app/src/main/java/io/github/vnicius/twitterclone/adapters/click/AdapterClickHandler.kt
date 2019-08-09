package io.github.vnicius.twitterclone.adapters.click

import android.view.View

interface AdapterClickHandler<T> {
    fun onClick(view: View, item: T)
}