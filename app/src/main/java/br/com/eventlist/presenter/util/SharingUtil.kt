package br.com.eventlist.presenter.util

import android.app.Activity
import android.content.Intent

object SharingUtil {

    fun sharing(activity: Activity, title: String, shareBody: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TITLE, title)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        activity.startActivity(Intent.createChooser(sharingIntent, title))
    }
}