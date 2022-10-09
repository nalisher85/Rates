package com.example.rates.sort

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rates.sort.SortOptionsViewModel.Companion.SORT_OPTION_ARGUMENT
import com.example.rates.ui.theme.Surface300

@Composable
fun SortOptionScreen(viewModel: SortOptionsViewModel, navController: NavController) {

    navController.previousBackStackEntry?.savedStateHandle?.set(SORT_OPTION_ARGUMENT, viewModel.sortState.value)

    Content(
        option = viewModel.sortState.value,
        onOptionSelected = viewModel.onOptionSelected
    ) {
        navController.navigateUp()
    }

}

@Composable
private fun Content(
    option: SortOptionsViewModel.SortOption?,
    onOptionSelected: (SortOptionsViewModel.SortOption) -> Unit,
    onBtnClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Сортировка",
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(40.dp))
        Surface(
            elevation = 2.dp,
            shape = RoundedCornerShape(20.dp),
            color = if (option == SortOptionsViewModel.SortOption.NAME_AZ) Surface300 else MaterialTheme.colors.surface,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onOptionSelected(SortOptionsViewModel.SortOption.NAME_AZ) }

        ) {
            Text(
                text = "Название по возрастанию",
                modifier = Modifier.padding(10.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Surface(
            elevation = 2.dp,
            shape = RoundedCornerShape(20.dp),
            color = if (option == SortOptionsViewModel.SortOption.NAME_ZA) Surface300 else MaterialTheme.colors.surface,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onOptionSelected(SortOptionsViewModel.SortOption.NAME_ZA) }
        ) {
            Text(
                text = "Название по убыванию",
                modifier = Modifier.padding(10.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Surface(
            elevation = 2.dp,
            shape = RoundedCornerShape(20.dp),
            color = if (option == SortOptionsViewModel.SortOption.VALUE_ASCENDING) Surface300 else MaterialTheme.colors.surface,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onOptionSelected(SortOptionsViewModel.SortOption.VALUE_ASCENDING) }

        ) {
            Text(
                text = "Значение по возрастанию",
                modifier = Modifier.padding(10.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Surface(
            elevation = 2.dp,
            shape = RoundedCornerShape(20.dp),
            color = if (option == SortOptionsViewModel.SortOption.VALUER_DESCENDING) Surface300 else MaterialTheme.colors.surface,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onOptionSelected(SortOptionsViewModel.SortOption.VALUER_DESCENDING)
            }
        ) {
            Text(
                text = "Значение по убыванию",
                modifier = Modifier.padding(10.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onBtnClicked
        ) {
            Text(text = "Применить")
        }

    }
}


@Preview(showBackground = true)
@Composable
fun SortOptionPreview() {
    val state = remember {
        mutableStateOf(SortOptionsViewModel.SortOption.NAME_AZ)
    }

    Content(
        option = state.value,
        onOptionSelected = { state.value = it }
    ) {
    }
}
