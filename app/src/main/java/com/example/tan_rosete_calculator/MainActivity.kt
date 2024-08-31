package com.example.tan_rosete_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    // Declaring UI components
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

    // Variables for calculator logic
    private var input = "" // Stores the current input
    private var operation = "" // Stores the current operation (+, -, *, /)
    private var firstNum = 0.0 // Stores the first operand
    private var waitingForSecondOperand = false // Flag to check if waiting for the second operand
    private var clearism = false // Flag to determine when to clear the display

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

        // Set up button listeners for numbers
        // Checks if 'clearism' is true, clears display, and resets 'clearism' before appending the number
        button0.setOnClickListener {
            if(clearism) {
                clear()
                clearism = false
            }
            appendNumber("0")
        }

        button1.setOnClickListener {
            if(clearism) {
                clear()
                clearism = false
            }
            appendNumber("1")
        }

        button2.setOnClickListener {
            if(clearism) {
                clear()
                clearism = false
            }
            appendNumber("2")
        }

        button3.setOnClickListener {
            if(clearism) {
                clear()
                clearism = false
            }
            appendNumber("3")
        }

        button4.setOnClickListener {
            if(clearism) {
                clear()
                clearism = false
            }
            appendNumber("4")
        }

        button5.setOnClickListener {
            if(clearism) {
                clear()
                clearism = false
            }
            appendNumber("5")
        }

        button6.setOnClickListener {
            if(clearism) {
                clear()
                clearism = false
            }
            appendNumber("6")
        }

        button7.setOnClickListener {
            if(clearism) {
                clear()
                clearism = false
            }
            appendNumber("7")
        }

        button8.setOnClickListener {
            if(clearism) {
                clear()
                clearism = false
            }
            appendNumber("8")
        }

        button9.setOnClickListener {
            if(clearism) {
                clear()
                clearism = false
            }
            appendNumber("9")
        }

        buttonDot.setOnClickListener { appendDot() } // Handles the decimal point

        // Set up button listeners for operations
        // Resets 'clearism' after setting the operation
        buttonPlus.setOnClickListener {
            setOperation("+")
            clearism = false
        }

        buttonMinus.setOnClickListener {
            setOperation("-")
            clearism = false
        }

        buttonMultiply.setOnClickListener {
            setOperation("*")
            clearism = false
        }

        buttonDivide.setOnClickListener {
            setOperation("/")
            clearism = false
        }

        buttonEquals.setOnClickListener { calculateResult() } // Calculates the result when equals is pressed
        buttonC.setOnClickListener { clear() } // Clears the display
        buttonPlusMinus.setOnClickListener { toggleSign() } // Toggles the sign of the current input
        buttonPercent.setOnClickListener { applyPercentage() } // Converts the current input to a percentage
    }

    // Appends a number to the current input
    private fun appendNumber(number: String) {
        if (waitingForSecondOperand) {
            // If waiting for the second operand, replace the current input with the new number
            input = number
            waitingForSecondOperand = false
            if (solutionTv.text.isEmpty()) {
                solutionTv.text = ""
            }
        } else {
            input += number
        }
        resultTv.text = formatOutput(input.toDoubleOrNull() ?: 0.0) // Update the result display
        solutionTv.text = solutionTv.text.toString() + number // Update the solution display
    }

    // Appends a decimal point to the current input
    private fun appendDot() {
        if (!input.contains(".")) {
            input += "."
            solutionTv.text = solutionTv.text.toString() + "."
            resultTv.text = input
        }
    }

    // Sets the operation (+, -, *, /)
    private fun setOperation(op: String) {
        if (input.isNotEmpty()) {
            val currentNum = input.toDoubleOrNull() ?: 0.0

            if (operation.isNotEmpty()) {
                // If there's an existing operation, calculate the result so far
                firstNum = performOperation(firstNum, currentNum, operation)
            } else {
                // Otherwise, set the first number
                firstNum = currentNum
            }

            operation = op // Store the operation
            waitingForSecondOperand = true // Flag that we're waiting for the second operand
            solutionTv.text = solutionTv.text.toString() + op // Update the solution display
            input = "" // Reset the input
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

    // Performs the arithmetic operation based on the passed operator
    private fun performOperation(firstNum: Double, secondNum: Double, operation: String): Double {
        return when (operation) {
            "+" -> firstNum + secondNum
            "-" -> firstNum - secondNum
            "*" -> firstNum * secondNum
            "/" -> firstNum / secondNum
            else -> secondNum
        }
    }


    // Calculates the final result
    private fun calculateResult() {
        if (input.isEmpty() && operation.isNotEmpty()) {
            // If the input is empty and there's an operation, don't allow equals to be pressed
            return
        }

        if (input.isEmpty() && operation.isEmpty()) return

        val currentNum = input.toDoubleOrNull() ?: 0.0
        val finalResult = performOperation(firstNum, currentNum, operation) // Calculate the result

        val formattedResult = formatOutput(finalResult) // Format the result
        resultTv.text = formattedResult // Display the result
        solutionTv.text = formattedResult // Update the solution display

        // Reset for a new calculation
        input = ""
        operation = ""
        firstNum = finalResult
        waitingForSecondOperand = true
        clearism = true // Set the flag to clear the display on the next input
    }


    // Clears the display and resets all variables
    private fun clear() {
        input = ""
        firstNum = 0.0
        resultTv.text = "0"
        solutionTv.text = ""
        operation = ""
        waitingForSecondOperand = false
    }

    // Toggles the sign of the current input
    private fun toggleSign() {
        if (input.isNotEmpty()) {
            input = if (input.startsWith("-")) {
                input.substring(1) // Remove the minus sign if it exists
            } else {
                "-$input" // Add a minus sign if it doesn't exist
            }

            // Update the result and solution displays
            resultTv.text = input
            val currentSolutionText = solutionTv.text.toString()
            val updatedSolutionText = currentSolutionText.dropLastWhile { it.isDigit() || it == '.' || it == '-' }
            solutionTv.text = updatedSolutionText + input
        }
    }

    // Converts the current input to a percentage
    private fun applyPercentage() {
        if (input.isNotEmpty()) {
            val num = input.toDoubleOrNull()
            if (num != null) {
                input = (num / 100).toString()
                resultTv.text = input
            }
        }
    }

    // Formats the output for display (e.g., with commas and rounding)
    private fun formatOutput(number: Double): String {
        return if (number % 1 == 0.0) {
            DecimalFormat("#,###").format(number) // No decimal places if the number is whole
        } else {
            DecimalFormat("#,###.##").format(number) // Two decimal places if the number has a fraction
        }
    }
}
