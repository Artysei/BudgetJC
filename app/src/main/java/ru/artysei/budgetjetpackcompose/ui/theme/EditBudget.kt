package ru.artysei.budgetjetpackcompose.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import ru.artysei.budgetjetpackcompose.Budget
import ru.artysei.budgetjetpackcompose.BudgetData
import ru.artysei.budgetjetpackcompose.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetEditor(
    budget: BudgetData,
    modifier: Modifier = Modifier,
) {
    val calendarState = rememberSheetState()
    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date {date -> budget.time = date.atStartOfDay() }
    )
    Surface(modifier = Modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        )  {
            OutlinedTextField(
                value = budget.name,
                onValueChange = { budget.name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.e_title)) },
                singleLine = true,
            )
            OutlinedTextField(
                value = budget.money.toString(),
                onValueChange = { it ->
                    it.toIntOrNull().also {
                    if (it != null) {
                        budget.money = it
                    }
                } },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.e_money)) },
                singleLine = true,
            )
            Row{
                OutlinedTextField(
                    value = budget.time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                    onValueChange ={},
                    modifier = Modifier.weight(3f),
                    label = { Text(stringResource(R.string.e_time)) },
                    singleLine = true,
                )
                IconButton(
                    onClick = { calendarState.show() },
                    modifier = Modifier.padding(10.dp)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_apps_24),
                        contentDescription = null,
                        tint = colorResource(id = R.color.remove),
                    )
                }

            }





        }
    }
}


@Preview
@Composable
fun NoteEditorPreview(){
    BudgetJetpackComposeTheme {
        BudgetEditor(
            BudgetData(),
            Modifier.fillMaxSize()
        )
    }
}