package ru.artysei.budgetjetpackcompose

import java.time.LocalDateTime

data class Budget(
    var id: Int = 0,
    var money: Int = 0,
    var name: String = "",
    var time: LocalDateTime = LocalDateTime.now()
)