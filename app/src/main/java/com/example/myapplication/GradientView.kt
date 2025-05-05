package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View




class GradientView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var points: List<Float> = emptyList()
    private var currentStep = 0
    private var functionType = 0

    fun setFunctionType(type: Int) {
        functionType = type
        invalidate()
    }

    fun animateSteps(steps: List<Float>) {
        points = steps
        currentStep = 0
        Handler(Looper.getMainLooper()).postDelayed(::updateStep, 1000)
    }

    private fun updateStep() {
        if (currentStep < points.size) {
            invalidate()
            currentStep++
            Handler(Looper.getMainLooper()).postDelayed(::updateStep, 1000)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paintAxes = Paint().apply {
            color = Color.BLACK
            strokeWidth = 5f
        }

        val paintFunction = Paint().apply {
            color = Color.BLUE
            strokeWidth = 3f
        }

        val paintPoint = Paint().apply {
            color = Color.RED
            strokeWidth = 10f
        }

        val width = width.toFloat()
        val height = height.toFloat()

        // Tengelyek kirajzolása
        canvas.drawLine(50f, height / 2, width - 50f, height / 2, paintAxes)
        canvas.drawLine(width / 2, 50f, width / 2, height - 50f, paintAxes)

        // Függvény kirajzolása
        val path = Path()
        for (x in -10..10) {
            val fx = getFunctionValue(x.toFloat(), functionType)
            if (x == -10) {
                path.moveTo(width / 2 + x * 20, height / 2 - fx * 20)
            } else {
                path.lineTo(width / 2 + x * 20, height / 2 - fx * 20)
            }
        }
        canvas.drawPath(path, paintFunction)

        // Gradiens pontok kirajzolása
        if (currentStep < points.size) {
            val x = points[currentStep]
            val fx = getFunctionValue(x, functionType)
            canvas.drawCircle(width / 2 + x * 20, height / 2 - fx * 20, 10f, paintPoint)
        }
    }

    private fun getFunctionValue(x: Float, functionType: Int): Float {
        return when (functionType) {
            0 -> x * x
            1 -> x * x + 2
            2 -> 0.5f * (x * x)
            3 -> 2 * (x * x) + 2
            else -> x * x
        }
    }
}
