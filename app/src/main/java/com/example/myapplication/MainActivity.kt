package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : AppCompatActivity() {
    private lateinit var gradientView: GradientView
    private var functionType = 0
    private var learningRate = 0.1f
    private var startX = 3.0f // Fix kezdő érték

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gradientView = findViewById(R.id.gradientView)
        val functionSelector = findViewById<RadioGroup>(R.id.functionSelector)
        val currentXView = findViewById<TextView>(R.id.currentXValue)
        val endLabel = findViewById<TextView>(R.id.endLabel)

        functionSelector.setOnCheckedChangeListener { _, checkedId ->
            functionType = when (checkedId) {
                R.id.fun1 -> 0
                R.id.fun2 -> 1
                R.id.fun3 -> 2
                R.id.fun4 -> 3
                else -> 0
            }
            gradientView.setFunctionType(functionType)
        }

        findViewById<Button>(R.id.startButton).setOnClickListener {
            endLabel.visibility = View.GONE // Elrejtjük az "End" jelzést indításkor

            val steps = gradientDescent(startX, learningRate, functionType, 0.01f)
            gradientView.animateSteps(steps)

            // Folyamatosan frissítjük az aktuális x értéket
            Thread {
                for (x in steps) {
                    runOnUiThread {
                        currentXView.text = "Aktuális x érték: ${"%.2f".format(x)}"
                    }
                    Thread.sleep(1000)
                }
                // Eljárás végén megjelenítjük az "End" feliratot
                runOnUiThread {
                    endLabel.visibility = View.VISIBLE
                }
            }.start()
        }
    }
}