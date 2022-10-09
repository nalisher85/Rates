package com.example.rates.navigation

import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.example.rates.R

@Keep
sealed class Screen(val rout: String, @StringRes val resourceId: Int) {
    object Popular: Screen(Screens.POPULAR.name, R.string.popular)
    object Favorite: Screen(Screens.FAVORITE.name, R.string.favorite)

}

enum class Screens {
    MAIN, POPULAR, FAVORITE, SORT_OPTION
}