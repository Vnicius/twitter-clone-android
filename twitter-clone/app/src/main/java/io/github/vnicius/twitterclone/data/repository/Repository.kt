package io.github.vnicius.twitterclone.data.repository

data class Repository<T>(val local: T, val remote: T)