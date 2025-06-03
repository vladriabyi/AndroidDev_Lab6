package com.example.myapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.Exercise
import com.example.myapplication.data.ExerciseCategory
import com.example.myapplication.data.ExerciseType
import com.example.myapplication.data.ExerciseTypes
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExerciseTrackerScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseTrackerScreen(
    viewModel: ExerciseViewModel = viewModel()
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val exercises by viewModel.exercises.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fitness Tracker") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Exercise")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Calendar
            Calendar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                selectedDate = selectedDate,
                onDateSelected = { viewModel.updateSelectedDate(it) }
            )

            // Exercise List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(exercises) { exercise ->
                    ExerciseItem(
                        exercise = exercise,
                        onDelete = { viewModel.deleteExercise(exercise) }
                    )
                }
            }
        }

        if (showAddDialog) {
            AddExerciseDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, reps, sets, duration ->
                    viewModel.addExercise(
                        Exercise(
                            name = name,
                            repetitions = reps,
                            sets = sets,
                            duration = duration,
                            date = selectedDate
                        )
                    )
                    showAddDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseItem(
    exercise: Exercise,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${exercise.sets} sets × ${exercise.repetitions} reps",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Duration: ${exercise.duration} min",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, reps: Int, sets: Int, duration: Int) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(ExerciseCategory.STRENGTH) }
    var selectedExercise by remember { mutableStateOf<ExerciseType?>(null) }
    var customName by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var isCustomExercise by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Exercise") },
        text = {
            Column {
                // Category Selection
                Text("Category", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExerciseCategory.values().forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.name) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Exercise Selection
                Text("Exercise", style = MaterialTheme.typography.titleMedium)
                LazyColumn(
                    modifier = Modifier.height(200.dp)
                ) {
                    items(ExerciseTypes.getExercisesByCategory(selectedCategory)) { exercise ->
                        ListItem(
                            headlineContent = { Text(exercise.name) },
                            supportingContent = {
                                Text(
                                    when {
                                        exercise.defaultReps > 0 -> "${exercise.defaultSets} sets × ${exercise.defaultReps} reps"
                                        exercise.defaultDuration > 0 -> "${exercise.defaultDuration} min"
                                        else -> ""
                                    }
                                )
                            },
                            modifier = Modifier.clickable {
                                selectedExercise = exercise
                                isCustomExercise = false
                                reps = exercise.defaultReps.toString()
                                sets = exercise.defaultSets.toString()
                                duration = exercise.defaultDuration.toString()
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Custom Exercise Option
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        isCustomExercise = true
                        selectedExercise = null
                    }
                ) {
                    Checkbox(
                        checked = isCustomExercise,
                        onCheckedChange = { isCustomExercise = it })
                    Text("Custom Exercise")
                }
                
                if (isCustomExercise) {
                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = { Text("Exercise Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Exercise Details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = reps,
                        onValueChange = { reps = it },
                        label = { Text("Reps") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = sets,
                        onValueChange = { sets = it },
                        label = { Text("Sets") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Min") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val name = if (isCustomExercise) customName else selectedExercise?.name ?: ""
                    if (name.isNotBlank()) {
                        onConfirm(
                            name,
                            reps.toIntOrNull() ?: 0,
                            sets.toIntOrNull() ?: 0,
                            duration.toIntOrNull() ?: 0
                        )
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    selectedDate: Long,
    onDateSelected: (Long) -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = selectedDate
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = dateFormat.format(calendar.time),
            style = MaterialTheme.typography.titleLarge
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                onDateSelected(calendar.timeInMillis)
            }) {
                Text("Previous")
            }
            Button(onClick = {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                onDateSelected(calendar.timeInMillis)
            }) {
                Text("Next")
            }
        }
    }
} 