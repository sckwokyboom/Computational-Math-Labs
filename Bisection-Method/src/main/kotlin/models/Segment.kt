package models

open class Segment(var leftBorder: Double, var rightBorder: Double) {
    fun leftBorderStep(step: Double): Segment = Segment(leftBorder + step, rightBorder)
    fun rightBorderStep(step: Double): Segment = Segment(leftBorder, rightBorder + step)
    fun middle(): Double = (leftBorder + rightBorder) / 2
}

class PositiveInfiniteSegment(start: Double) : Segment(start, Double.POSITIVE_INFINITY)

class NegativeInfiniteSegment(end: Double) : Segment(Double.NEGATIVE_INFINITY, end)