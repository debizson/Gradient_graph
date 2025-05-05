package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
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
    private var learningRate = 1.0f
    private var startX = 5.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gradientView = findViewById(R.id.gradientView)

        val functionSelector = findViewById<RadioGroup>(R.id.functionSelector)

        // Rádiógomb eseményfigyelő
        functionSelector.setOnCheckedChangeListener { _, checkedId ->
            functionType = when (checkedId) {
                R.id.fun1 -> 0
                R.id.fun2 -> 1
                R.id.fun3 -> 2
                R.id.fun4 -> 3
                else -> 0
            }
            gradientView.setFunctionType(functionType) // Frissítjük a grafikát
        }

        findViewById<Button>(R.id.startButton).setOnClickListener {
            val steps = gradientDescent(startX, learningRate, functionType, 0.01f)
            gradientView.animateSteps(steps)
        }
    }
}

