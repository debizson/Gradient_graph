package com.example.myapplication

import kotlin.math.absoluteValue

fun derivative(x: Float, functionType: Int): Float {
    return when (functionType) {
        0 -> 2 * x
        1 -> 2 * (x - 1)
        2 -> x
        3 -> 4 * x
        else -> 2 * x
    }
}

fun gradientDescent(startX: Float, learningRate: Float, functionType: Int, stopThreshold: Float): List<Float> {
    var x = startX
    val points = mutableListOf<Float>()

    while (derivative(x, functionType).absoluteValue > stopThreshold) {
        points.add(x)
        x -= learningRate * derivative(x, functionType)
    }
    points.add(x)
    return points
}
