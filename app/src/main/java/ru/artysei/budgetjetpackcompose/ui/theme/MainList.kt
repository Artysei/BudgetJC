package ru.artysei.budgetjetpackcompose.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.artysei.budgetjetpackcompose.Budget
import ru.artysei.budgetjetpackcompose.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainList(
    budgets: List<Budget>,
    modifier: Modifier = Modifier,
    onEditBudgetRequest: (Budget) -> Unit = {},
    onRemoveBudgetRequest: (Budget) -> Unit = {},
) {
    val sortedBudgets = budgets.sortedByDescending { it.time }
    val currentMonthExpenses = sortedBudgets.filter { it.money < 0 && it.time.toLocalDate().month == LocalDate.now().month }
    val currentMonthIncomes = sortedBudgets.filter { it.money >= 0 && it.time.toLocalDate().month == LocalDate.now().month }
    val expenses = sortedBudgets.filter { it.money < 0 && it.time.toLocalDate() == LocalDate.now() }
    val incomes = sortedBudgets.filter { it.money >= 0 && it.time.toLocalDate() == LocalDate.now() }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Доходы за месяц: ${currentMonthIncomes.sumOf { it.money }}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green
            )
            Text(
                text = "Расходы за месяц: ${currentMonthExpenses.sumOf { it.money }}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )

            Divider(modifier = Modifier.padding(8.dp))
            Text(
                text = "Доходы за день: ${incomes.sumOf { it.money }}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green
            )
            Text(
                text = "Расходы за день: ${expenses.sumOf { it.money }}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(8.dp))



        }
        val groupedBudgets = sortedBudgets.groupBy { it.time.toLocalDate() }

        LazyColumn(
            content = {
                groupedBudgets.forEach { (date, budgets) ->
                    stickyHeader {
                        Text(
                            text = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray)
                                .padding(8.dp),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    items(budgets) { budget ->
                        BudgetCard(
                            budget,
                            onEditClick = onEditBudgetRequest,
                            onRemoveClick = onRemoveBudgetRequest,
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                    }
                }
            }

        )

    }
}






@Preview
@Composable
fun MainListPreview(){
    BudgetJetpackComposeTheme {
        MainList(
            listOf(
                Budget(id = 1,  money = 212),
                Budget(id = 2, name = "xAFS", money = 1542),
                Budget(id = 3, name = "FSDFS",money = -672,time = LocalDateTime.of(2023,12,22,7,10)),
                Budget(id = 4, name = "FSD",money = -672, time = LocalDateTime.of(2023,12,16,7,10)),
                Budget(id = 4, name = "gdfg",money = -672, time = LocalDateTime.of(2023,12,16,7,10)),
                Budget(id = 4, name = "раз",money = -672, time = LocalDateTime.of(2023,12,16,7,10)),
                Budget(id = 4, name = "два",money = -672, time = LocalDateTime.of(2023,12,16,7,10)),
                Budget(id = 4, name = "три",money = -672, time = LocalDateTime.of(2023,12,16,7,10)),
                Budget(id = 5, name = "Приятно",money = -672)



            ),
            Modifier
                .padding(6.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetCard(
    budget: Budget,
    modifier: Modifier = Modifier,
    onEditClick: (Budget) -> Unit,
    onRemoveClick: (Budget) -> Unit
) {
    val textColor = if (budget.money >= 0) Color.Green else Color.Red

    ElevatedCard(
        onClick = {
            onEditClick(budget)
        },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (budget.name.isNotBlank()) {
                Text(
                    text = budget.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(3f)

                )
            }
            else {
                Text(
                    text = "Не указано",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(3f)
                )
            }
            Text(
                text = budget.money.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = textColor,
                modifier = Modifier.weight(2f)

            )
            IconButton(
                onClick = { onRemoveClick(budget) },
                modifier = Modifier.weight(1f)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_forever_24),
                    contentDescription = null,
                    tint = colorResource(id = R.color.remove),
                )
            }

        }
    }
}

@Composable
fun DeleteRequest(
    showRequest: Boolean = false,
    onDismiss: ()->Unit = {},
    onConfirm: ()->Unit = {},
){
    if (showRequest) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(onClick = { onConfirm() }) {
                    Text(text = stringResource(id = R.string.yes))
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text(text = stringResource(id = R.string.no))
                }
            },
            icon = {
                Icon(
                    painterResource(id = R.drawable.baseline_delete_forever_24),
                    contentDescription = null,
                    tint = colorResource(id = R.color.remove)
                )
            },
            title = { Text(stringResource(id = R.string.remove_dlg_title)) },
            text = { Text(stringResource(id = R.string.remove_dlg_text)) },
        )
    }
}

@Preview
@Composable
fun DeleteRequestPreview(){
    DeleteRequest(showRequest = true)
}


