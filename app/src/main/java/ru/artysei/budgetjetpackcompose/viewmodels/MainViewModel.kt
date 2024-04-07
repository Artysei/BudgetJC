package ru.artysei.budgetjetpackcompose.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import ru.artysei.budgetjetpackcompose.Budget
import ru.artysei.budgetjetpackcompose.BudgetData
import ru.artysei.budgetjetpackcompose.R
import ru.artysei.budgetjetpackcompose.database.BudgetDao
import ru.artysei.budgetjetpackcompose.ui.theme.ViewMode

class MainViewModel(app: Application): AndroidViewModel(app) {
    var viewMode by mutableStateOf(ViewMode.Main)
    val dao: BudgetDao by lazy {
        BudgetDao(getApplication<Application>().applicationContext)
    }
    private var currentBudget: Budget? = null
    var budgetData: BudgetData = BudgetData()
    var showRemoveRequest by mutableStateOf(false)

    val titleId: Int
        get() = when(viewMode){
            ViewMode.Main -> R.string.app_name
            ViewMode.Single -> R.string.edit_budget
        }

    val budgets: List<Budget>
        get() = dao.getAllBudgets()

    fun toMainView(){
        viewMode = ViewMode.Main

    }
    fun toSingleView(budget: Budget) {
        currentBudget = budget
        budgetData.fillFromBudget(currentBudget ?: Budget())
        viewMode = ViewMode.Single
    }

    fun saveBudget(){
        currentBudget = budgetData.toBudget().also {
            if(it.id == 0)
                dao.addBudget(it)
            else dao.editBudget(it)
        }
        toMainView()
    }

    fun removeBudgetRequest(budget: Budget){
        currentBudget = budget
        showRemoveRequest = true
    }

    fun removeCurrentBudget() = currentBudget?.let {
        showRemoveRequest = false
        dao.deleteBudget(it)
    }


}