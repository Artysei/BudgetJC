package ru.artysei.budgetjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import ru.artysei.budgetjetpackcompose.ui.theme.BudgetEditor
import ru.artysei.budgetjetpackcompose.ui.theme.BudgetJetpackComposeTheme
import ru.artysei.budgetjetpackcompose.ui.theme.DeleteRequest
import ru.artysei.budgetjetpackcompose.ui.theme.MainList
import ru.artysei.budgetjetpackcompose.ui.theme.ViewMode
import ru.artysei.budgetjetpackcompose.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    private val mvm: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudgetJetpackComposeTheme {
                MainUI(
                    modifier = Modifier.fillMaxSize(),
                    title = stringResource(id = mvm.titleId),
                    viewMode = mvm.viewMode,
                    onAddBudget = { mvm.toSingleView(Budget()) },
                    onBack = mvm::toMainView,
                    onSaveCurrentBudget = mvm::saveBudget

                ) {
                    if (mvm.viewMode == ViewMode.Main){
                        DeleteRequest(
                            showRequest = mvm.showRemoveRequest,
                            onDismiss = {
                                mvm.showRemoveRequest = false
                            }
                        ){
                            mvm.removeCurrentBudget()
                        }
                        MainList(
                            mvm.budgets,
                            onEditBudgetRequest = mvm::toSingleView,
                            onRemoveBudgetRequest =mvm::removeBudgetRequest
                            )
                    }
                    else {
                        BudgetEditor(mvm.budgetData)

                    }


                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUI(
    modifier: Modifier = Modifier,
    title: String = "",
    viewMode: ViewMode = ViewMode.Main,
    onAddBudget: ()->Unit = {},
    onBack: ()->Unit = {},
    onSaveCurrentBudget: ()->Unit = {},
    content: @Composable ()->Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text =  title)
                },
                navigationIcon ={ if (viewMode == ViewMode.Single){
                    FilledTonalIconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = null
                        )

                    }
                } },
                actions ={if (viewMode == ViewMode.Single){
                    FilledTonalIconButton(onClick = {
                        onSaveCurrentBudget()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_check_24),
                            contentDescription = null
                        )
                    }
                } },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                )

            )
        },
        floatingActionButton = {
            if (viewMode == ViewMode.Main) {
                OutlinedIconButton(
                    modifier = Modifier.defaultMinSize(48.dp, 48.dp),
                    onClick = {
                        onAddBudget()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_blur_circular_24),
                        contentDescription = null,
                        tint = colorResource(id = R.color.add)
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,


    ) {
        Column(Modifier.padding(it)) {
            content()
        }
    }

}
@Preview
@Composable
fun MainUIPreview(){
    BudgetJetpackComposeTheme {
        MainUI(
            Modifier.fillMaxSize(),
            title = "Заголовок активности",
            viewMode = ViewMode.Main,
        ){

        }
    }
}