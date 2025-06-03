package com.example.myapplication.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.myapplication.R
import com.example.myapplication.data.ExerciseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Implementation of App Widget functionality.
 */
class ExerciseWidgetProvider : AppWidgetProvider() {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views = RemoteViews(context.packageName, R.layout.exercise_widget_layout)

    // Отримуємо початок сьогоднішнього дня як Long timestamp
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val startOfTodayMillis = calendar.timeInMillis

    // Отримуємо кінець сьогоднішнього дня
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    val startOfTomorrowMillis = calendar.timeInMillis

    // Наразі просто встановлюємо базовий текст, поки отримуємо дані асинхронно
    views.setTextViewText(R.id.widget_text, "Завантаження...")
    appWidgetManager.updateAppWidget(appWidgetId, views)

    // Запускаємо корутину для отримання даних з бази даних
    CoroutineScope(Dispatchers.IO).launch {
        val database = ExerciseDatabase.getDatabase(context)
        val exerciseDao = database.exerciseDao()

        // Отримуємо тренування за сьогоднішній день
        val exercisesToday = exerciseDao.getExercisesForDateRange(startOfTodayMillis, startOfTomorrowMillis)

        // Формуємо текст для віджета
        val widgetText = if (exercisesToday.isNotEmpty()) {
            val lastExercise = exercisesToday.last() // Отримуємо останню вправу
            "Тренувань сьогодні: ${exercisesToday.size}\nОстання вправа: ${lastExercise.name}"
        } else {
            "Тренувань сьогодні: 0"
        }

        // Оновлюємо текст віджета в UI потоці
        val updatedViews = RemoteViews(context.packageName, R.layout.exercise_widget_layout)
        updatedViews.setTextViewText(R.id.widget_text, widgetText)

        // Оновлюємо віджет
        appWidgetManager.updateAppWidget(appWidgetId, updatedViews)
    }
} 