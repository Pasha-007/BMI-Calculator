package com.asg.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.asg.bmicalculator.ui.theme.BmiCalculatorTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BmiCalculatorTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BmiCalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun BmiCalculatorScreen(){
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var ageError by remember { mutableStateOf(false) }
    var heightError by remember { mutableStateOf(false) }
    var weightError by remember { mutableStateOf(false) }
    var bmiResult by remember { mutableStateOf<Double?>(null) }

    fun showError(field: String) {
        when (field) {
            "age" -> ageError = true
            "height" -> heightError = true
            "weight" -> weightError = true
        }
    }

    fun clearErrors() {
        ageError = false
        heightError = false
        weightError = false
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        TextField(
            value = age,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                    age = newValue
                }
            },
            isError = ageError,
            supportingText = {
                if (ageError) {
                    Text(
                        text = "Age is required (2-120)",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(bottom = 16.dp)

        )

        TextField(
            value = height,
            onValueChange = {newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                    height = newValue
                }
            },
            isError = heightError,
            supportingText = {
                if (heightError) {
                    Text(
                        text = "Height is required (50-300 cm)",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            label = { Text("Height") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = weight,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                    weight = newValue
                }
            },
            isError = weightError,
            supportingText = {
                if (weightError) {
                    Text(
                        text = "Weight is required (10-500 kg)",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            label = { Text("Weight") }
        )

        Button(
            onClick = {
                val ageInt = age.toIntOrNull()
                val heightDouble = height.toDoubleOrNull()
                val weightDouble = weight.toDoubleOrNull()

                clearErrors()

                when {
                    age.isEmpty() || ageInt == null || ageInt !in 2..120 -> {
                        showError("age")
                    }
                    height.isEmpty() || heightDouble == null || heightDouble !in 50.0..300.0 -> {
                        showError("height")
                    }
                    weight.isEmpty() || weightDouble == null || weightDouble !in 10.0..500.0 -> {
                        showError("weight")
                    }
                    else -> {
                        val heightInMeters = heightDouble / 100
                        val bmi = weightDouble / (heightInMeters * heightInMeters)
                        bmiResult = bmi
                    }
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Calculate")
        }
        if (bmiResult != null) {
            Text(
                text = "Your BMI is: %.2f".format(bmiResult),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BmiCalculatorTheme {
        BmiCalculatorScreen()
    }
}