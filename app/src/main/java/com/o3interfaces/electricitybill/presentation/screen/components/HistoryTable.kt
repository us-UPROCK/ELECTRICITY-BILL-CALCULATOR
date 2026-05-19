package com.o3interfaces.electricitybill.presentation.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.o3interfaces.electricitybill.domain.model.ReadingRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Composable that shows the last 3 saved readings for the current customer
 * in an animated card table with columns for index, reading value, cost, and date.
 * Hidden automatically when there are no historical records.
 * */
@Composable
fun HistoryTable(
    readings: List<ReadingRecord>,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = readings.isNotEmpty(), modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Recent Readings",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TableRow(index = "#", reading = "Reading", cost = "Cost", date = "Date", isHeader = true)
                HorizontalDivider()
                readings.forEachIndexed { i, record ->
                    TableRow(
                        index = "${i + 1}",
                        reading = record.reading.toString(),
                        cost = "$${"%.2f".format(record.cost)}",
                        date = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                            .format(Date(record.timestamp)),
                        isHeader = false
                    )
                }
            }
        }
    }
}

@Composable
private fun TableRow(
    index: String,
    reading: String,
    cost: String,
    date: String,
    isHeader: Boolean
) {
    val weight = if (isHeader) FontWeight.Bold else FontWeight.Normal
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(index, modifier = Modifier.weight(0.5f), fontWeight = weight, style = MaterialTheme.typography.bodySmall)
        Text(reading, modifier = Modifier.weight(1.2f), fontWeight = weight, style = MaterialTheme.typography.bodySmall)
        Text(cost, modifier = Modifier.weight(1.3f), fontWeight = weight, style = MaterialTheme.typography.bodySmall)
        Text(date, modifier = Modifier.weight(1.3f), fontWeight = weight, style = MaterialTheme.typography.bodySmall)
    }
}
