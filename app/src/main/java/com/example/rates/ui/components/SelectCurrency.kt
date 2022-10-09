package com.example.rates.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.rates.R


@Composable
fun SelectCurrency(
    currency: String,
    data: List<String>,
    onSortClicked: () -> Unit,
    onValuerChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrencyDropDown(
            currency = currency,
            data = data,
            onValuerChanged = onValuerChanged,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_sort),
            contentDescription = "sort",
            modifier = Modifier.padding(start = 10.dp).clickable { onSortClicked() }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrencyDropDown(
    currency: String,
    data: List<String>,
    modifier: Modifier = Modifier,
    onValuerChanged: (String) -> Unit,
) {

    var expanded by remember {
        mutableStateOf(false)
    }


    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            value = currency,
            onValueChange = {},
            enabled = false,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(0.dp, 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            data.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onValuerChanged(item)
                        expanded = false
                    }
                ) {
                    Text(
                        text = item,
                        color = Color.Black,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectCurrencyComponentPreview() {
    val list = listOf(
        "USD",
        "EUR",
        "RUB",
        "TJS",
        "UZB",
        "GBR"
    )
    var selected by remember {
        mutableStateOf("USD")
    }
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            SelectCurrency(currency = selected, data = list, onSortClicked = {}, onValuerChanged = {})
        }

    }
}

@Preview (showBackground = true)
@Composable
fun CurrencyDropDownPreview() {
    val list = listOf(
        "USD",
        "EUR",
        "RUB",
        "TJS",
        "UZB",
        "GBR"
    )
    var selected by remember {
        mutableStateOf("USD")
    }

    MaterialTheme {
        CurrencyDropDown(
            currency = selected,
            data = list,
            onValuerChanged = { selected = it }
        )
    }
}