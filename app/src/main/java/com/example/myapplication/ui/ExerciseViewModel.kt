package com.example.myapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Exercise
import com.example.myapplication.data.ExerciseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import com.example.myapplication.widget.ExerciseWidgetProvider

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ExerciseDatabase.getDatabase(application)
    private val exerciseDao = database.exerciseDao()

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    private val _selectedDate = MutableStateFlow(System.currentTimeMillis())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    init {
        viewModelScope.launch {
            exerciseDao.getAllExercises().collect { exerciseList ->
                _exercises.value = exerciseList
            }
        }
    }

    fun addExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseDao.insertExercise(exercise)

            // Trigger widget update
            val context = getApplication<Application>().applicationContext
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, ExerciseWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

            val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            }
            context.sendBroadcast(updateIntent)
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseDao.deleteExercise(exercise)
            // Optionally trigger widget update after deletion as well
            val context = getApplication<Application>().applicationContext
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, ExerciseWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

            val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            }
            context.sendBroadcast(updateIntent)
        }
    }

    fun updateSelectedDate(date: Long) {
        _selectedDate.value = date
        viewModelScope.launch {
            exerciseDao.getExercisesForDate(date).collect { exerciseList ->
                _exercises.value = exerciseList
            }
        }
    }
} 