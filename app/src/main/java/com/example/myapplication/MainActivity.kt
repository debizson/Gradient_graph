package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    private var startX = 3.0f // Fix kezdőérték
    private var isRunning = false // Eljárás futásának állapota
    private var thread: Thread? = null // Eljárás vezérlése



    fun startBlinkingEffect(gradientTitle: TextView) {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed(object : Runnable {
            override fun run() {
                gradientTitle.visibility = View.INVISIBLE // Elrejtjük a feliratot

                handler.postDelayed({
                    gradientTitle.visibility = View.VISIBLE // 1 másodperc múlva visszaállítjuk
                }, 300)

                handler.postDelayed(this, 5000) // 5 másodperc múlva újra elkezdődik a ciklus
            }
        }, 5000)
    }


// Hívjuk meg az animációt, amikor az Activity elindul


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gradientView = findViewById(R.id.gradientView)
        val functionSelector = findViewById<RadioGroup>(R.id.functionSelector)
        val learningRateSelector = findViewById<RadioGroup>(R.id.learningRateSelector)
        val currentXView = findViewById<TextView>(R.id.currentXValue)
        val endLabel = findViewById<TextView>(R.id.endLabel)
        val startButton = findViewById<Button>(R.id.startButton)
        val gradientTitle = findViewById<TextView>(R.id.appTitle)
        val handler = Handler(Looper.getMainLooper())

        // Függvény kiválasztása
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

        // Tanulási ráta kiválasztása
        learningRateSelector.setOnCheckedChangeListener { _, checkedId ->
            learningRate = when (checkedId) {
                R.id.rate1 -> 0.1f
                R.id.rate2 -> 0.2f
                R.id.rate3 -> 0.3f
                R.id.rate4 -> 0.4f
                R.id.rate5 -> 0.5f
                else -> 0.1f
            }
        }

        val currentXLabel = findViewById<TextView>(R.id.currentXLabel)
        val currentXValue = findViewById<TextView>(R.id.currentXValue)
        // Start / Stop gomb kezelése
        startButton.setOnClickListener {
            if (!isRunning) {
                // Indítás
                isRunning = true
                startButton.text = "Stop"
                endLabel.visibility = View.GONE

                val steps = gradientDescent(startX, learningRate, functionType, 0.01f)
                gradientView.animateSteps(steps)

                thread = Thread {
                    for (x in steps) {
                        if (!isRunning) return@Thread // Ha leállították, kilép
                        runOnUiThread {
                            currentXValue.text = "%.2f".format(x) // Csak a számot frissítjük, hogy nagyobb legyen
                        }
                        Thread.sleep(1000)
                    }
                    runOnUiThread {
                        endLabel.visibility = View.VISIBLE
                        startButton.text = "Start"
                        isRunning = false
                    }
                }
                thread?.start()
            } else {
                // Leállítás
                isRunning = false
                startButton.text = "Start"
                endLabel.visibility = View.GONE

                // Az animáció megállítása
                gradientView.animateSteps(listOf(startX)) // Visszaállítja az eredeti pozíciót
                currentXView.text = "Aktuális x érték: ${"%.2f".format(startX)}"

                // Alapértelmezett kijelölés visszaállítása a rádiógomboknál
                functionSelector.check(R.id.fun1)
                learningRateSelector.check(R.id.rate1)

                // Nem használunk `interrupt()`, így nem lép ki a program!
            }
        }
        startBlinkingEffect(gradientTitle) // Elindítjuk az animációt

    }
}
