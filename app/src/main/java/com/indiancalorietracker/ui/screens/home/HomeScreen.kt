package com.indiancalorietracker.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.indiancalorietracker.domain.model.DailySummary
import com.indiancalorietracker.domain.model.MealLog
import com.indiancalorietracker.domain.model.MealType
import com.indiancalorietracker.ui.screens.water.WaterViewModel
import com.indiancalorietracker.ui.theme.Blue40
import com.indiancalorietracker.ui.theme.Green40
import com.indiancalorietracker.ui.theme.Orange40
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    waterViewModel: WaterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val waterState by waterViewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Today's Progress",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            val dateText = remember {
                SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())
            }
            Text(
                text = dateText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        uiState.dailySummary?.let { summary ->
            item {
                CalorieProgressRing(
                    consumed = summary.totalCalories,
                    goal = summary.calorieGoal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }

            item {
                // Water Progress Card
                WaterProgressCard(
                    currentMl = waterState.currentMl,
                    goalMl = waterState.goalMl,
                    progress = waterState.progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            item {
                NutritionSummary(summary)
                Spacer(modifier = Modifier.height(24.dp))
            }

            MealType.entries.forEach { mealType ->
                val meals = viewModel.getMealsByType(mealType)
                item {
                    MealSection(
                        mealType = mealType,
                        meals = meals,
                        onDeleteMeal = { viewModel.deleteMeal(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun CalorieProgressRing(
    consumed: Int,
    goal: Int,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableFloatStateOf(0f) }
    val currentProgress by animateFloatAsState(
        targetValue = animationPlayed,
        animationSpec = tween(durationMillis = 1000),
        label = "progress"
    )

    LaunchedEffect(consumed, goal) {
        animationPlayed = (consumed.toFloat() / goal).coerceIn(0f, 1.2f)
    }

    val progressColor = if (consumed <= goal) Green40 else Orange40
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val strokeWidth = 20.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2
            val center = Offset(size.width / 2, size.height / 2)

            drawCircle(
                color = backgroundColor,
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth)
            )

            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * currentProgress.coerceAtMost(1f),
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$consumed",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = progressColor
            )
            Text(
                text = "of $goal cal",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = if (consumed <= goal) "remaining" else "over goal",
                style = MaterialTheme.typography.bodyMedium,
                color = if (consumed <= goal) Green40 else Orange40
            )
        }
    }
}

@Composable
fun NutritionSummary(summary: DailySummary) {
    val totalProtein = summary.meals.sumOf { (it.foodItem.protein * it.servings).toDouble() }.toFloat()
    val totalCarbs = summary.meals.sumOf { (it.foodItem.carbs * it.servings).toDouble() }.toFloat()
    val totalFat = summary.meals.sumOf { (it.foodItem.fat * it.servings).toDouble() }.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NutrientItem("Protein", "${totalProtein.toInt()}g")
            NutrientItem("Carbs", "${totalCarbs.toInt()}g")
            NutrientItem("Fat", "${totalFat.toInt()}g")
        }
    }
}

@Composable
fun WaterProgressCard(
    currentMl: Int,
    goalMl: Int,
    progress: Float,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableFloatStateOf(0f) }
    val currentProgress by animateFloatAsState(
        targetValue = animationPlayed,
        animationSpec = tween(durationMillis = 1000),
        label = "water_progress"
    )

    LaunchedEffect(progress) {
        animationPlayed = progress
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Blue40.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = null,
                    tint = Blue40,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Water Intake",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${currentMl}ml / ${goalMl}ml",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${(currentProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Blue40
                )
                LinearProgressIndicator(
                    progress = currentProgress,
                    modifier = Modifier
                        .width(80.dp)
                        .height(6.dp),
                    color = Blue40,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }
}

@Composable
fun NutrientItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MealSection(
    mealType: MealType,
    meals: List<MealLog>,
    onDeleteMeal: (Long) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = mealType.displayName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (meals.isEmpty()) {
            Text(
                text = "No items logged",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            meals.forEach { meal ->
                MealItem(meal = meal, onDelete = { onDeleteMeal(meal.id) })
            }
        }
    }
}

@Composable
fun MealItem(meal: MealLog, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meal.foodItem.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${meal.servings} serving${if (meal.servings > 1) "s" else ""} (${meal.foodItem.servingSize}${meal.foodItem.servingUnit})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${meal.totalCalories} cal",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Orange40
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
