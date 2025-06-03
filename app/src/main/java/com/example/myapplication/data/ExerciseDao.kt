package com.example.myapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY date DESC")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE date = :date")
    fun getExercisesForDate(date: Long): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE date BETWEEN :startTime AND :endTime")
    suspend fun getExercisesForDateRange(startTime: Long, endTime: Long): List<Exercise>

    @Insert
    suspend fun insertExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Update
    suspend fun updateExercise(exercise: Exercise)
} 