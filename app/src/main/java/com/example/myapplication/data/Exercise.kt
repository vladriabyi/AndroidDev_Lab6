package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val repetitions: Int,
    val sets: Int,
    val duration: Int, // in minutes
    val date: Long // timestamp in milliseconds
) 