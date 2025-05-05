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

        val paintTicks = Paint().apply {
            color = Color.BLACK
            strokeWidth = 2f
        }

        val paintFunction = Paint().apply {
            color = Color.BLUE
            strokeWidth = 3f
            style = Paint.Style.STROKE
        }

        val paintPoint = Paint().apply {
            color = Color.RED
            strokeWidth = 10f
        }

        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2
        val centerY = height / 2
        val tickSize = 10f
        val unitSize = 40f // A beosztások közötti távolság

        // Tengelyek kirajzolása
        canvas.drawLine(50f, centerY, width - 50f, centerY, paintAxes) // X tengely
        canvas.drawLine(centerX, 50f, centerX, height - 50f, paintAxes) // Y tengely

        // X és Y tengely beosztásai (1-től 10-ig)
        for (i in -10..10) {
            val xPos = centerX + i * unitSize
            val yPos = centerY - i * unitSize

            // X tengely beosztásai (függőleges kis vonalak)
            canvas.drawLine(xPos, centerY - tickSize, xPos, centerY + tickSize, paintTicks)

            // Y tengely beosztásai (vízszintes kis vonalak)
            canvas.drawLine(centerX - tickSize, yPos, centerX + tickSize, yPos, paintTicks)
        }

        // Függvény kirajzolása
        val path = Path()
        for (x in -10..10) {
            val fx = getFunctionValue(x.toFloat(), functionType)
            if (x == -10) {
                path.moveTo(centerX + x * unitSize, centerY - fx * unitSize)
            } else {
                path.lineTo(centerX + x * unitSize, centerY - fx * unitSize)
            }
        }
        canvas.drawPath(path, paintFunction)

        // Gradiens pontok kirajzolása
        if (currentStep < points.size) {
            val x = points[currentStep]
            val fx = getFunctionValue(x, functionType)
            canvas.drawCircle(centerX + x * unitSize, centerY - fx * unitSize, 10f, paintPoint)
        }
    }


    private fun getFunctionValue(x: Float, functionType: Int): Float {
        return when (functionType) {
            0 -> x * x
            1 -> (x + 1) * (x + 1) + 2
            2 -> 0.5f * (x * x)
            3 -> 2 * (x * x) + 2
            else -> x * x
        }
    }
}
