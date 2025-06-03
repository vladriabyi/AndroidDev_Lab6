package com.example.myapplication.data

data class ExerciseType(
    val name: String,
    val defaultReps: Int,
    val defaultSets: Int,
    val defaultDuration: Int,
    val category: ExerciseCategory
)

enum class ExerciseCategory {
    CARDIO,
    STRENGTH,
    FLEXIBILITY,
    BALANCE
}

object ExerciseTypes {
    val exercises = listOf(
        // Cardio exercises
        ExerciseType("Running", 0, 1, 30, ExerciseCategory.CARDIO),
        ExerciseType("Cycling", 0, 1, 30, ExerciseCategory.CARDIO),
        ExerciseType("Jumping Rope", 0, 1, 15, ExerciseCategory.CARDIO),
        ExerciseType("Swimming", 0, 1, 30, ExerciseCategory.CARDIO),
        
        // Strength exercises
        ExerciseType("Push-ups", 15, 3, 0, ExerciseCategory.STRENGTH),
        ExerciseType("Pull-ups", 10, 3, 0, ExerciseCategory.STRENGTH),
        ExerciseType("Squats", 20, 3, 0, ExerciseCategory.STRENGTH),
        ExerciseType("Lunges", 12, 3, 0, ExerciseCategory.STRENGTH),
        ExerciseType("Plank", 0, 3, 1, ExerciseCategory.STRENGTH),
        ExerciseType("Dumbbell Curls", 12, 3, 0, ExerciseCategory.STRENGTH),
        ExerciseType("Bench Press", 10, 3, 0, ExerciseCategory.STRENGTH),
        
        // Flexibility exercises
        ExerciseType("Stretching", 0, 1, 15, ExerciseCategory.FLEXIBILITY),
        ExerciseType("Yoga", 0, 1, 30, ExerciseCategory.FLEXIBILITY),
        ExerciseType("Pilates", 0, 1, 30, ExerciseCategory.FLEXIBILITY),
        
        // Balance exercises
        ExerciseType("Single Leg Stand", 0, 3, 1, ExerciseCategory.BALANCE),
        ExerciseType("Tree Pose", 0, 3, 1, ExerciseCategory.BALANCE),
        ExerciseType("Heel-to-Toe Walk", 0, 3, 1, ExerciseCategory.BALANCE)
    )

    fun getExercisesByCategory(category: ExerciseCategory): List<ExerciseType> {
        return exercises.filter { it.category == category }
    }
} 