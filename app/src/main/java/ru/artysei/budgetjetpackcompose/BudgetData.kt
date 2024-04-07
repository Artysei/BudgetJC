package ru.artysei.budgetjetpackcompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.LocalDateTime

class BudgetData {
    var id:Int by mutableStateOf(0)
    var money: Int  by mutableStateOf(0)
    var name: String by mutableStateOf("")
    var time: LocalDateTime by mutableStateOf(LocalDateTime.now())

    fun fillFromBudget(budget: Budget)
    {
        id = budget.id
        money = budget.money
        name = budget.name
        time = budget.time
    }

    fun toBudget() = Budget(id,money,name,time)

}