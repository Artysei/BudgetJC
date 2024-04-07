package ru.artysei.budgetjetpackcompose.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import ru.artysei.budgetjetpackcompose.Budget
import java.time.LocalDateTime
import java.time.ZoneOffset


class BudgetDao(context: Context):
    SQLiteOpenHelper(context, BUDGET_DB_NAME,null,1){

    companion object{
        const val BUDGET_DB_NAME = "Budget"
        const val BUDGET_TABLE_NAME = "Budget"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $BUDGET_TABLE_NAME( " +
                "id integer primary key, " +
                "money integer, " +
                "name text, " +
                "time integer) "
        db?.apply{
            try {
                beginTransaction()
                execSQL(query)
                setTransactionSuccessful()
            } catch (e: Exception){
                Log.e("DB", e.message.toString())
            } finally {
                endTransaction()
            }
        }
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun addBudget(budget: Budget){
        val values = createValuesFor(budget)
        writableDatabase.apply {
            try {
                beginTransaction()
                insert(BUDGET_TABLE_NAME, "", values)
                setTransactionSuccessful()
            } catch (e: Exception){
                Log.e("DB", e.message.toString())
            } finally {
                endTransaction()
            }
        }
    }

    fun editBudget(budget: Budget){
        val values = createValuesFor(budget)
        writableDatabase.apply {
            try{
                beginTransaction()
                if (update(BUDGET_TABLE_NAME, values, "id=?", arrayOf(budget.id.toString())) > 0)
                    setTransactionSuccessful()
                else throw Exception("Не найдена запись для редактирования")
            } catch (e: Exception){
                Log.e("DB", e.message.toString())
            } finally {
                endTransaction()
            }
        }
    }

    fun deleteBudget(budget: Budget){
        writableDatabase.apply {
            try {
                beginTransaction()
                if (delete(BUDGET_TABLE_NAME, "id = ?", arrayOf(budget.id.toString())) > 0)
                    setTransactionSuccessful()
                else throw Exception("Не найдена запись для удаления")
            } catch (e: Exception){
                Log.e("DB", e.message.toString())
            } finally {
                endTransaction()
            }
        }
    }

    fun getAllBudgets(): List<Budget>{
        val budgets: MutableList<Budget> = mutableListOf()
        readableDatabase.run {
            try {
                beginTransaction()
                with(
                    query(
                        BUDGET_TABLE_NAME,
                        arrayOf("id", "money", "name", "time"),
                        null,
                        null,
                        null,
                        null,
                        "time desc"
                    )
                ) {
                    while (moveToNext()) {
                        budgets.add(
                            Budget(
                                id = getInt(0),
                                money = getInt(1),
                                name = getString(2),
                                time = LocalDateTime.ofEpochSecond(
                                    getInt(3).toLong(),
                                    0,
                                    ZoneOffset.UTC
                                )
                            )
                        )
                    }
                    close()
                }
                setTransactionSuccessful()
            } catch (e: Exception) {
                Log.e("DB", e.message.toString())
            } finally {
                endTransaction()
            }
        }
        return budgets
    }

    private fun createValuesFor(budget: Budget) = ContentValues().apply{
        put("money",budget.money)
        put("name",budget.name)
        put("time",budget.time.toEpochSecond(ZoneOffset.UTC))

    }




}