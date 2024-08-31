package com.example.tan_rosete_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var resultTv: TextView
    private lateinit var solutionTv: TextView
    private lateinit var buttonC: MaterialButton
    private lateinit var buttonPlusMinus: MaterialButton
    private lateinit var buttonPercent: MaterialButton
    private lateinit var buttonDivide: MaterialButton
    private lateinit var buttonMultiply: MaterialButton
    private lateinit var buttonMinus: MaterialButton
    private lateinit var buttonPlus: MaterialButton
    private lateinit var buttonEquals: MaterialButton
    private lateinit var buttonDot: MaterialButton
    private lateinit var button0: MaterialButton
    private lateinit var button1: MaterialButton
    private lateinit var button2: MaterialButton
    private lateinit var button3: MaterialButton
    private lateinit var button4: MaterialButton
    private lateinit var button5: MaterialButton
    private lateinit var button6: MaterialButton
    private lateinit var button7: MaterialButton
    private lateinit var button8: MaterialButton
    private lateinit var button9: MaterialButton

    private var input = ""
    private var operation = ""
    private var firstNum = 0.0
    private var waitingForSecondOperand = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize TextViews
        resultTv = findViewById(R.id.result_tv)
        solutionTv = findViewById(R.id.solution_tv)

        // Initialize Buttons
        buttonC = findViewById(R.id.button_c)
        buttonPlusMinus = findViewById(R.id.positive_negative)
        buttonPercent = findViewById(R.id.percent)
        buttonDivide = findViewById(R.id.button_divide)
        buttonMultiply = findViewById(R.id.button_multiply)
        buttonMinus = findViewById(R.id.button_minus)
        buttonPlus = findViewById(R.id.button_plus)
        buttonEquals = findViewById(R.id.button_equals)
        buttonDot = findViewById(R.id.button_dot)
        button0 = findViewById(R.id.button_0)
        button1 = findViewById(R.id.button_1)
        button2 = findViewById(R.id.button_2)
        button3 = findViewById(R.id.button_3)
        button4 = findViewById(R.id.button_4)
        button5 = findViewById(R.id.button_5)
        button6 = findViewById(R.id.button_6)
        button7 = findViewById(R.id.button_7)
        button8 = findViewById(R.id.button_8)
        button9 = findViewById(R.id.button_9)

        // Set up button listeners
        button0.setOnClickListener { appendNumber("0") }
        button1.setOnClickListener { appendNumber("1") }
        button2.setOnClickListener { appendNumber("2") }
        button3.setOnClickListener { appendNumber("3") }
        button4.setOnClickListener { appendNumber("4") }
        button5.setOnClickListener { appendNumber("5") }
        button6.setOnClickListener { appendNumber("6") }
        button7.setOnClickListener { appendNumber("7") }
        button8.setOnClickListener { appendNumber("8") }
        button9.setOnClickListener { appendNumber("9") }
        buttonDot.setOnClickListener { appendDot() }

        buttonPlus.setOnClickListener { setOperation("+") }
        buttonMinus.setOnClickListener { setOperation("-") }
        buttonMultiply.setOnClickListener { setOperation("*") }
        buttonDivide.setOnClickListener { setOperation("/") }

        buttonEquals.setOnClickListener { calculateResult() }
        buttonC.setOnClickListener { clear() }
        buttonPlusMinus.setOnClickListener { toggleSign() }
        buttonPercent.setOnClickListener { applyPercentage() }
    }

    private fun appendNumber(number: String) {
        if (waitingForSecondOperand) {
            input = number
            waitingForSecondOperand = false
            // Clear solutionTv only if starting a completely new equation
            if (solutionTv.text.isEmpty()) {
                solutionTv.text = ""
            }
        } else {
            input += number
        }
        resultTv.text = formatOutput(input.toDoubleOrNull() ?: 0.0)
        solutionTv.text = solutionTv.text.toString() + number
    }

    private fun appendDot() {
        if (!input.contains(".")) {
            input += "."
            solutionTv.text = solutionTv.text.toString() + "."
            resultTv.text = input
        }
    }

    private fun setOperation(op: String) {
        if (input.isNotEmpty()) {
            firstNum = input.toDoubleOrNull() ?: 0.0
            operation = op
            waitingForSecondOperand = true
            solutionTv.text = solutionTv.text.toString() + op
            input = ""
        } else if (operation.isEmpty()) {
            // No input but an operation is set, reuse firstNum
            operation = op
            solutionTv.text = resultTv.text.toString() + op
            waitingForSecondOperand = true
        } else if (operation.isNotEmpty()) {
            // Replace the last operator with the new one
            solutionTv.text = solutionTv.text.toString().dropLast(1) + op
            operation = op
        }
    }


    private fun calculateResult() {
        if (input.isEmpty() && operation.isEmpty()) return

        // If there's no input but there's an ongoing operation, treat it as using the firstNum again
        if (input.isEmpty() && operation.isNotEmpty()) {
            input = firstNum.toString()
        }

        val secondNum = input.toDoubleOrNull() ?: return
        val finalResult = when (operation) {
            "+" -> firstNum + secondNum
            "-" -> firstNum - secondNum
            "*" -> firstNum * secondNum
            "/" -> firstNum / secondNum
            else -> secondNum
        }

        val formattedResult = formatOutput(finalResult)
        resultTv.text = formattedResult
        solutionTv.text = formattedResult

        // Reset the input and operation for a new calculation
        input = ""
        operation = ""
        firstNum = finalResult
        waitingForSecondOperand = true
    }




    private fun calculateIntermediateResult() {
        if (input.isNotEmpty()) {
            val secondNum = input.toDoubleOrNull() ?: return
            firstNum = when (operation) {
                "+" -> firstNum + secondNum
                "-" -> firstNum - secondNum
                "*" -> firstNum * secondNum
                "/" -> firstNum / secondNum
                else -> secondNum
            }
            input = ""
            resultTv.text = formatOutput(firstNum)
        }
    }


    private fun clear() {
        input = ""
        firstNum = 0.0
        resultTv.text = "0"
        solutionTv.text = ""
        operation = ""
        waitingForSecondOperand = false
    }

    private fun toggleSign() {
        if (input.isNotEmpty()) {
            // Toggle the sign of the input
            input = if (input.startsWith("-")) {
                input.substring(1) // Remove the minus sign if it exists
            } else {
                "-$input" // Add a minus sign if it doesn't exist
            }

            // Update the result display
            resultTv.text = input

            // Update the solution display
            val currentSolutionText = solutionTv.text.toString()

            // Remove the previous number from solutionTv to avoid multiple signs
            val updatedSolutionText = currentSolutionText.dropLastWhile { it.isDigit() || it == '.' || it == '-' }

            // Add the toggled number to the solutionTv
            solutionTv.text = updatedSolutionText + input
        }
    }

    private fun applyPercentage() {
        if (input.isNotEmpty()) {
            val num = input.toDoubleOrNull()
            if (num != null) {
                input = (num / 100).toString()
                resultTv.text = input
            }
        }
    }

    private fun formatOutput(number: Double): String {
        return if (number % 1 == 0.0) {
            DecimalFormat("#,###").format(number)
        } else {
            DecimalFormat("#,###.##").format(number)
        }
    }

}