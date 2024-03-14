package com.example.shoppinglist

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast

class ShoppingAppWidget : AppWidgetProvider() {

    companion object {
        val WIDGET_CLICK = "com.example.shoppinglist.WidgetClick"
        val IMAGE_INTENT = "com.example.shoppinglist.ImageIntent"
        val START_INTENT = "com.example.shoppinglist.StartIntent"
        val PAUSE_INTENT = "com.example.shoppinglist.PauseIntent"
        val STOP_INTENT = "com.example.shoppinglist.StopIntent"
        val NEXT_INTENT = "com.example.shoppinglist.NextIntent"

        var i = 0
        val images = listOf(
            R.drawable.summer,
            R.drawable.spring,
            R.drawable.winter,
            R.drawable.autumn
        )

        var j = 0
        val sounds = listOf(
            R.raw.biolumin177658,
            R.raw.black_scorpion_music_highway_124115,
            R.raw.los_destellos_otro_ano_que_pasa_demo_180361
        )
        val soundDescriptions = listOf(
            "Track 1: Bio Lumin",
            "Track 2: Black Scorpoion Music Highway",
            "Track 3: Los Destellos Otro Ano Que Pasa Demo"
        )

        var mediaPlayer: MediaPlayer? = null
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.i("tag", "onReceive")
        if (intent?.action.equals(WIDGET_CLICK)) {
            Log.i("tag", "widget click")
            Toast.makeText(context, "widget click", Toast.LENGTH_SHORT).show()
        } else if (intent?.action.equals(IMAGE_INTENT)) {
            i = (i + 1) % 4
            Log.i("tag", "image intent $i")
            val views = RemoteViews(context!!.packageName, R.layout.shopping_app_widget)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            views.setImageViewResource(R.id.imageView, images[i])
            appWidgetManager.updateAppWidget(
                ComponentName(context, ShoppingAppWidget::class.java),
                views
            )
        } else if (intent?.action.equals(START_INTENT)) {
            startTrack(context)
        } else if (intent?.action.equals(PAUSE_INTENT)) {
            pauseTrack()
        } else if (intent?.action.equals(STOP_INTENT)) {
            stopTrack()
        } else if (intent?.action.equals(NEXT_INTENT)) {
            nextTrack(context)
        }
    }
}

@SuppressLint("RemoteViewLayout")
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
) {
    var requestCode = 0
    val views = RemoteViews(context.packageName, R.layout.shopping_app_widget)
    views.setImageViewResource(R.id.imageView, ShoppingAppWidget.images[0])
    requestCode++

    val intentWWW = Intent(Intent.ACTION_VIEW)
    intentWWW.data = Uri.parse("https://www.pja.edu.pl")
    val pendingIntentWWW = PendingIntent.getActivity(
        context,
        requestCode,
        intentWWW,
        PendingIntent.FLAG_IMMUTABLE
    )
    views.setOnClickPendingIntent(R.id.buttonOpenBrowser, pendingIntentWWW)

    setPendingIntent(
        ShoppingAppWidget.WIDGET_CLICK,
        context,
        requestCode,
        R.id.buttonMakeToast,
        views
    )
    setPendingIntent(
        ShoppingAppWidget.IMAGE_INTENT,
        context,
        requestCode,
        R.id.buttonChangeImage,
        views
    )

    setPendingIntent(ShoppingAppWidget.START_INTENT, context, requestCode, R.id.buttonStart, views)
    setPendingIntent(ShoppingAppWidget.PAUSE_INTENT, context, requestCode, R.id.buttonPause, views)
    setPendingIntent(ShoppingAppWidget.STOP_INTENT, context, requestCode, R.id.buttonStop, views)
    setPendingIntent(ShoppingAppWidget.NEXT_INTENT, context, requestCode, R.id.buttonNext, views)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

internal fun setPendingIntent(
    flag: String,
    context: Context,
    requestCode: Int,
    button: Int,
    views: RemoteViews,
) {
    val intent = Intent(flag)
    intent.component = ComponentName(context, ShoppingAppWidget::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )
    views.setOnClickPendingIntent(button, pendingIntent)
}

private fun startTrack(context: Context?) {
    Log.i("tag", "start")
    if (ShoppingAppWidget.mediaPlayer == null) {
        ShoppingAppWidget.mediaPlayer = MediaPlayer.create(context, ShoppingAppWidget.sounds[ShoppingAppWidget.j])
        ShoppingAppWidget.mediaPlayer!!.isLooping = true
        ShoppingAppWidget.mediaPlayer!!.start()
        val views = RemoteViews(context!!.packageName, R.layout.shopping_app_widget)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        views.setTextViewText(R.id.songName, ShoppingAppWidget.soundDescriptions[ShoppingAppWidget.j])
        appWidgetManager.updateAppWidget(
            ComponentName(context, ShoppingAppWidget::class.java),
            views
        )
    } else ShoppingAppWidget.mediaPlayer!!.start()
}

private fun pauseTrack() {
    Log.i("tag", "pause")
    if (ShoppingAppWidget.mediaPlayer?.isPlaying == true)
        ShoppingAppWidget.mediaPlayer?.pause()
}

private fun stopTrack() {
    Log.i("tag", "stop")
    if (ShoppingAppWidget.mediaPlayer != null) {
        ShoppingAppWidget.mediaPlayer!!.stop()
        ShoppingAppWidget.mediaPlayer!!.release()
        ShoppingAppWidget.mediaPlayer = null
    }
}

private fun nextTrack(context: Context?) {
    Log.i("tag", "next")
    stopTrack()
    ShoppingAppWidget.j = (ShoppingAppWidget.j + 1) % (ShoppingAppWidget.sounds.size)
    startTrack(context)
}